package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;

import com.villagers.mod.Config;
import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.entity.BanditCampBlockEntity;
import com.villagers.mod.integration.CitadelAnchorSavedData;

import java.util.UUID;

public final class BanditSiteGenerator {
    public static final int MIN_SPAWN_DIST = 800;
    public static final int MAX_SPAWN_DIST = 4000;
    private static final int MIN_SITE_SEPARATION = 700;

    private BanditSiteGenerator() {
    }

    public static boolean needsGeneration(ServerLevel level) {
        if (!level.dimension().equals(Level.OVERWORLD)) {
            return false;
        }
        BanditWorldSavedData data = BanditWorldSavedData.get(level);
        if (data.isGenerated() && !data.sites().isEmpty()) {
            return false;
        }
        return true;
    }

    /** @deprecated Prefer {@link BanditGenerationScheduler#scheduleIfNeeded}; kept for tests and locate flush. */
    public static void ensureGenerated(ServerLevel level) {
        BanditGenerationScheduler.scheduleIfNeeded(level);
        BanditGenerationScheduler.runUntilDoneOrTimeout(level, 20_000);
    }

    public static void finishGeneration(ServerLevel level) {
        BanditWorldSavedData data = BanditWorldSavedData.get(level);
        if (data.sites().isEmpty()) {
            VillagersMod.LOGGER.error(
                    "Bandit site generation placed nothing (check terrain); will retry next load");
            return;
        }
        data.setGenerated();
        VillagersMod.LOGGER.info("Placed {} bandit sites in overworld", data.sites().size());
    }

    public static boolean tryOnePlacementAttempt(
            ServerLevel level,
            BanditWorldSavedData data,
            BlockPos spawn,
            RandomSource random,
            BanditWorldSavedData.SiteKind kind,
            int minDist,
            int maxDist,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        BlockPos surface = BanditSitePlacement.sampleSiteOrigin(level, data, spawn, random, kind, minDist, maxDist, chunkBudget);
        if (surface == null) {
            return false;
        }
        if (!farEnough(data, surface)) {
            return false;
        }
        if ((kind == BanditWorldSavedData.SiteKind.CORVIN || kind == BanditWorldSavedData.SiteKind.GARLAND)
                && !farEnoughFromOtherQuestCamp(data, surface, kind)) {
            return false;
        }
        placeSite(level, data, surface, kind);
        return true;
    }

    public static BlockPos samplePlacementPos(
            ServerLevel level,
            BlockPos spawn,
            RandomSource random,
            int minDist,
            int maxDist,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        double angle = random.nextDouble() * Math.PI * 2;
        int dist = minDist + random.nextInt(Math.max(1, maxDist - minDist));
        int x = spawn.getX() + (int) (Math.cos(angle) * dist);
        int z = spawn.getZ() + (int) (Math.sin(angle) * dist);
        return pickCampSurface(level, x, z, chunkBudget);
    }

    static void placeCitadelShadowCamps(
            ServerLevel level,
            BanditWorldSavedData data,
            BlockPos spawn,
            RandomSource random,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        var citadel = CitadelAnchorSavedData.citadelCenter(level);
        if (citadel.isEmpty()) {
            return;
        }
        BlockPos anchor = citadel.get();
        int extra = Config.CITADEL_SHADOW_EXTRA_CAMPS.get();
        int approach = Config.CITADEL_APPROACH_DISTANCE.get();
        int jitter = Config.CITADEL_SHADOW_RING_JITTER.get();
        for (int i = 0; i < extra; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            int dist = approach + random.nextInt(jitter * 2 + 1) - jitter;
            int x = anchor.getX() + (int) (Math.cos(angle) * dist);
            int z = anchor.getZ() + (int) (Math.sin(angle) * dist);
            BlockPos search = new BlockPos(x, 64, z);
            BlockPos surface = BanditSitePlacement.sampleShadowCampAtVillage(level, data, search, chunkBudget);
            if (surface != null && farEnough(data, surface)) {
                placeSite(level, data, surface, BanditWorldSavedData.SiteKind.CAMP);
            }
        }
    }

    static boolean farEnoughFromOtherQuestCamp(
            BanditWorldSavedData data, BlockPos pos, BanditWorldSavedData.SiteKind kind) {
        int minSep = Config.QUEST_CAMP_MIN_SEPARATION.get();
        long minSq = (long) minSep * minSep;
        for (var site : data.sites()) {
            if (site.kind() == BanditWorldSavedData.SiteKind.CORVIN || site.kind() == BanditWorldSavedData.SiteKind.GARLAND) {
                if (site.origin().distSqr(pos) < minSq) {
                    return false;
                }
            }
        }
        return true;
    }

    static int terrainVariance(ServerLevel level, BlockPos center) {
        int min = center.getY();
        int max = center.getY();
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, center.getX() + dx, center.getZ() + dz);
                min = Math.min(min, y);
                max = Math.max(max, y);
            }
        }
        return max - min;
    }

    private static BlockPos pickCampSurface(
            ServerLevel level, int x, int z, BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        if (!chunkBudget.ensureChunk(chunkX, chunkZ)) {
            return null;
        }
        BlockPos top = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
        if (isSupportedCampSurface(level, top)) {
            return top;
        }
        for (int dy = 1; dy <= 12; dy++) {
            BlockPos up = top.above(dy);
            if (isSupportedCampSurface(level, up)) {
                return up;
            }
            BlockPos down = top.below(dy);
            if (isSupportedCampSurface(level, down)) {
                return down;
            }
        }
        return null;
    }

    private static boolean isSupportedCampSurface(ServerLevel level, BlockPos surface) {
        if (!level.getFluidState(surface).isEmpty() || !level.getFluidState(surface.above()).isEmpty()) {
            return false;
        }
        var ground = level.getBlockState(surface.below());
        return !ground.isAir() && ground.isSolidRender(level, surface.below());
    }

    static boolean farEnough(BanditWorldSavedData data, BlockPos pos) {
        for (var site : data.sites()) {
            if (site.origin().distSqr(pos) < (long) MIN_SITE_SEPARATION * MIN_SITE_SEPARATION) {
                return false;
            }
        }
        return true;
    }

    public static void placeSite(
            ServerLevel level, BanditWorldSavedData data, BlockPos origin, BanditWorldSavedData.SiteKind kind) {
        UUID id = UUID.randomUUID();
        level.getChunk(origin.getX() >> 4, origin.getZ() >> 4);
        level.setBlock(origin, VillagersMod.BANDIT_CAMP.get().defaultBlockState(), 3);
        BlockEntity be = level.getBlockEntity(origin);
        if (be instanceof BanditCampBlockEntity camp) {
            camp.initSite(id, kind);
        }
        placeChestIfAir(level, origin.north());
        placeChestIfAir(level, origin.east());
        if (kind == BanditWorldSavedData.SiteKind.HIDEOUT) {
            BanditHideoutFeatures.dressHideout(level, origin, level.random);
        }
        if (kind == BanditWorldSavedData.SiteKind.CORVIN) {
            BanditCorvinFeatures.dressCorvinTower(level, origin);
        }
        BanditLoot.fillSiteChests(level, origin, kind, level.random);
        data.addSite(new BanditWorldSavedData.SiteRecord(id, kind, origin, true, 0));
        if (be instanceof BanditCampBlockEntity camp) {
            camp.onWorldDataLinked();
        }
    }

    private static void placeChestIfAir(ServerLevel level, BlockPos chestPos) {
        if (level.getBlockState(chestPos).isAir()) {
            level.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 3);
        }
    }
}
