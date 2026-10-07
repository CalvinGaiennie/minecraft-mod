package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
    private static final int MIN_SPAWN_DIST = 800;
    private static final int MAX_SPAWN_DIST = 4000;
    private static final int MIN_SITE_SEPARATION = 700;

    private BanditSiteGenerator() {
    }

    public static void ensureGenerated(ServerLevel level) {
        if (!level.dimension().equals(Level.OVERWORLD)) {
            return;
        }
        BanditWorldSavedData data = BanditWorldSavedData.get(level);
        if (data.isGenerated()) {
            return;
        }
        BlockPos spawn = level.getSharedSpawnPos();
        var random = level.getRandom();
        int camps = Config.BANDIT_CAMP_COUNT.get();
        int hideouts = Config.BANDIT_HIDEOUT_COUNT.get();
        for (int i = 0; i < camps; i++) {
            tryPlaceSite(level, data, spawn, random, BanditWorldSavedData.SiteKind.CAMP, MIN_SPAWN_DIST, MAX_SPAWN_DIST);
        }
        for (int i = 0; i < hideouts; i++) {
            tryPlaceSite(level, data, spawn, random, BanditWorldSavedData.SiteKind.HIDEOUT, MIN_SPAWN_DIST, MAX_SPAWN_DIST);
        }
        tryPlaceQuestCamp(level, data, spawn, random, BanditWorldSavedData.SiteKind.CORVIN,
                Config.CORVIN_CAMP_MIN_DISTANCE.get(), Config.CORVIN_CAMP_MAX_DISTANCE.get(), false);
        tryPlaceQuestCamp(level, data, spawn, random, BanditWorldSavedData.SiteKind.GARLAND,
                Config.GARLAND_CAMP_MIN_DISTANCE.get(), Config.GARLAND_CAMP_MAX_DISTANCE.get(), true);
        placeCitadelShadowCamps(level, data, spawn, random);
        data.setGenerated();
    }

    private static void placeCitadelShadowCamps(ServerLevel level, BanditWorldSavedData data, BlockPos spawn,
            net.minecraft.util.RandomSource random) {
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
            BlockPos surface = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(x, 0, z));
            if (farEnough(data, surface) && level.getBlockState(surface.below()).isSolidRender(level, surface.below())) {
                placeSite(level, data, surface, BanditWorldSavedData.SiteKind.CAMP);
            }
        }
    }

    private static void tryPlaceQuestCamp(ServerLevel level, BanditWorldSavedData data, BlockPos spawn,
            net.minecraft.util.RandomSource random, BanditWorldSavedData.SiteKind kind, int minDist, int maxDist,
            boolean preferRoughTerrain) {
        BlockPos best = null;
        int bestScore = Integer.MIN_VALUE;
        for (int attempt = 0; attempt < 100; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2;
            int dist = minDist + random.nextInt(Math.max(1, maxDist - minDist));
            int x = spawn.getX() + (int) (Math.cos(angle) * dist);
            int z = spawn.getZ() + (int) (Math.sin(angle) * dist);
            BlockPos surface = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(x, 0, z));
            if (!level.getBlockState(surface.below()).isSolidRender(level, surface.below())) {
                continue;
            }
            if (!farEnough(data, surface)) {
                continue;
            }
            if (!farEnoughFromOtherQuestCamp(data, surface, kind)) {
                continue;
            }
            int score = preferRoughTerrain ? terrainVariance(level, surface) : 0;
            if (score > bestScore || best == null) {
                bestScore = score;
                best = surface;
            }
            if (!preferRoughTerrain) {
                placeSite(level, data, surface, kind);
                return;
            }
        }
        if (best != null) {
            placeSite(level, data, best, kind);
        } else {
            VillagersMod.LOGGER.warn("Failed to place bandit site kind {}", kind);
        }
    }

    private static boolean farEnoughFromOtherQuestCamp(BanditWorldSavedData data, BlockPos pos, BanditWorldSavedData.SiteKind kind) {
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

    private static int terrainVariance(ServerLevel level, BlockPos center) {
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

    private static void tryPlaceSite(ServerLevel level, BanditWorldSavedData data, BlockPos spawn, net.minecraft.util.RandomSource random,
            BanditWorldSavedData.SiteKind kind, int minDist, int maxDist) {
        for (int attempt = 0; attempt < 80; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2;
            int dist = minDist + random.nextInt(Math.max(1, maxDist - minDist));
            int x = spawn.getX() + (int) (Math.cos(angle) * dist);
            int z = spawn.getZ() + (int) (Math.sin(angle) * dist);
            BlockPos surface = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(x, 0, z));
            if (!level.getBlockState(surface.below()).isSolidRender(level, surface.below())) {
                continue;
            }
            if (!farEnough(data, surface)) {
                continue;
            }
            placeSite(level, data, surface, kind);
            return;
        }
        VillagersMod.LOGGER.warn("Failed to place bandit site kind {}", kind);
    }

    private static boolean farEnough(BanditWorldSavedData data, BlockPos pos) {
        for (var site : data.sites()) {
            if (site.origin().distSqr(pos) < (long) MIN_SITE_SEPARATION * MIN_SITE_SEPARATION) {
                return false;
            }
        }
        return true;
    }

    public static void placeSite(ServerLevel level, BanditWorldSavedData data, BlockPos origin, BanditWorldSavedData.SiteKind kind) {
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
