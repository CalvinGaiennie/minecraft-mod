package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import com.villagers.mod.Config;
import com.villagers.mod.VillagersMod;

import java.util.HashMap;
import java.util.Map;

public final class BanditGenerationScheduler {
    /** Small budget: each attempt can force one distant chunk to generate. */
    private static final int PLACEMENT_ATTEMPTS_PER_TICK = 2;
    private static final int MAX_CHUNK_GENS_PER_TICK = 1;
    private static final int MAX_ATTEMPTS_PER_SITE = 80;

    private static final Map<ResourceKey<Level>, Job> JOBS = new HashMap<>();

    private BanditGenerationScheduler() {
    }

    public static void scheduleIfNeeded(ServerLevel level) {
        if (!BanditSiteGenerator.needsGeneration(level)) {
            return;
        }
        JOBS.computeIfAbsent(level.dimension(), key -> Job.start(level));
        VillagersMod.LOGGER.info("Scheduled bandit site generation for {} (background, after join)", level.dimension().location());
    }

    public static void tick(ServerLevel level) {
        if (level.players().isEmpty()) {
            return;
        }
        Job job = JOBS.get(level.dimension());
        if (job == null) {
            return;
        }
        job.advance(level, PLACEMENT_ATTEMPTS_PER_TICK, MAX_CHUNK_GENS_PER_TICK);
        if (job.finished) {
            JOBS.remove(level.dimension());
            BanditSiteGenerator.finishGeneration(level);
        }
    }

    public static void runUntilDoneOrTimeout(ServerLevel level, int maxTicks) {
        runUntilDoneOrTimeout(level, maxTicks, 8, 4);
    }

    /** Used by {@code /villagers locate} — higher budget than background ticks. */
    public static void runUntilDoneOrTimeout(
            ServerLevel level, int maxTicks, int attemptsPerTick, int chunkGensPerTick) {
        scheduleIfNeeded(level);
        for (int i = 0; i < maxTicks; i++) {
            if (!BanditSiteGenerator.needsGeneration(level) && !JOBS.containsKey(level.dimension())) {
                return;
            }
            Job job = JOBS.get(level.dimension());
            if (job != null) {
                job.advance(level, attemptsPerTick, chunkGensPerTick);
                if (job.finished) {
                    JOBS.remove(level.dimension());
                    BanditSiteGenerator.finishGeneration(level);
                }
            }
        }
    }

    public static boolean isRunning(ServerLevel level) {
        return JOBS.containsKey(level.dimension());
    }

    private static final class Job {
        private final BlockPos spawn;
        private int campsLeft;
        private int hideoutsLeft;
        private boolean corvinDone;
        private boolean garlandDone;
        private boolean shadowDone;

        private int attemptsOnStep;
        private boolean finished;

        private Job(BlockPos spawn, int campsLeft, int hideoutsLeft) {
            this.spawn = spawn;
            this.campsLeft = campsLeft;
            this.hideoutsLeft = hideoutsLeft;
        }

        static Job start(ServerLevel level) {
            BanditWorldSavedData data = BanditWorldSavedData.get(level);
            if (data.isGenerated()) {
                data.resetGenerationState();
            }
            return new Job(
                    level.getSharedSpawnPos(),
                    Config.BANDIT_CAMP_COUNT.get(),
                    Config.BANDIT_HIDEOUT_COUNT.get());
        }

        void advance(ServerLevel level, int attemptBudget, int chunkGenBudget) {
            BanditWorldSavedData data = BanditWorldSavedData.get(level);
            ChunkGenBudget chunks = new ChunkGenBudget(level, chunkGenBudget);
            while (attemptBudget > 0 && !finished) {
                if (campsLeft > 0) {
                    attemptBudget = placeRegular(level, data, chunks, BanditWorldSavedData.SiteKind.CAMP,
                            BanditSiteGenerator.MIN_SPAWN_DIST, BanditSiteGenerator.MAX_SPAWN_DIST, attemptBudget,
                            () -> campsLeft--);
                    continue;
                }
                if (hideoutsLeft > 0) {
                    attemptBudget = placeRegular(level, data, chunks, BanditWorldSavedData.SiteKind.HIDEOUT,
                            BanditSiteGenerator.MIN_SPAWN_DIST, BanditSiteGenerator.MAX_SPAWN_DIST, attemptBudget,
                            () -> hideoutsLeft--);
                    continue;
                }
                if (!corvinDone) {
                    attemptBudget = placeRegular(level, data, chunks, BanditWorldSavedData.SiteKind.CORVIN,
                            Config.CORVIN_CAMP_MIN_DISTANCE.get(), Config.CORVIN_CAMP_MAX_DISTANCE.get(), attemptBudget,
                            () -> corvinDone = true);
                    continue;
                }
                if (!garlandDone) {
                    attemptBudget = placeRegular(level, data, chunks, BanditWorldSavedData.SiteKind.GARLAND,
                            Config.GARLAND_CAMP_MIN_DISTANCE.get(), Config.GARLAND_CAMP_MAX_DISTANCE.get(), attemptBudget,
                            () -> garlandDone = true);
                    continue;
                }
                if (!shadowDone) {
                    BanditSiteGenerator.placeCitadelShadowCamps(level, data, spawn, level.getRandom(), chunks);
                    shadowDone = true;
                    continue;
                }
                finished = true;
            }
        }

        private int placeRegular(
                ServerLevel level,
                BanditWorldSavedData data,
                ChunkGenBudget chunks,
                BanditWorldSavedData.SiteKind kind,
                int minDist,
                int maxDist,
                int attemptBudget,
                Runnable onStepComplete) {
            while (attemptBudget > 0 && attemptsOnStep < MAX_ATTEMPTS_PER_SITE) {
                attemptBudget--;
                attemptsOnStep++;
                if (BanditSiteGenerator.tryOnePlacementAttempt(
                        level, data, spawn, level.getRandom(), kind, minDist, maxDist, chunks)) {
                    onStepComplete.run();
                    attemptsOnStep = 0;
                    return attemptBudget;
                }
            }
            if (attemptsOnStep >= MAX_ATTEMPTS_PER_SITE) {
                VillagersMod.LOGGER.warn("Failed to place bandit site kind {}", kind);
                onStepComplete.run();
                attemptsOnStep = 0;
            }
            return attemptBudget;
        }
    }

    static final class ChunkGenBudget {
        private final ServerLevel level;
        private int remaining;

        ChunkGenBudget(ServerLevel level, int remaining) {
            this.level = level;
            this.remaining = remaining;
        }

        boolean ensureChunk(int chunkX, int chunkZ) {
            if (level.getChunkSource().hasChunk(chunkX, chunkZ)) {
                return true;
            }
            if (remaining <= 0) {
                return false;
            }
            remaining--;
            level.getChunk(chunkX, chunkZ);
            return true;
        }
    }
}
