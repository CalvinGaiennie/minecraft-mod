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
            tryPlaceSite(level, data, spawn, random, BanditWorldSavedData.SiteKind.CAMP);
        }
        for (int i = 0; i < hideouts; i++) {
            tryPlaceSite(level, data, spawn, random, BanditWorldSavedData.SiteKind.HIDEOUT);
        }
        tryPlaceSite(level, data, spawn, random, BanditWorldSavedData.SiteKind.CORVIN);
        tryPlaceSite(level, data, spawn, random, BanditWorldSavedData.SiteKind.GARLAND);
        data.setGenerated();
    }

    private static void tryPlaceSite(ServerLevel level, BanditWorldSavedData data, BlockPos spawn, net.minecraft.util.RandomSource random,
            BanditWorldSavedData.SiteKind kind) {
        for (int attempt = 0; attempt < 80; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2;
            int dist = MIN_SPAWN_DIST + random.nextInt(MAX_SPAWN_DIST - MIN_SPAWN_DIST);
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
        BlockPos chestPos = origin.north();
        if (level.getBlockState(chestPos).isAir()) {
            level.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 3);
        }
        data.addSite(new BanditWorldSavedData.SiteRecord(id, kind, origin, false, 0));
        if (be instanceof BanditCampBlockEntity camp) {
            camp.onWorldDataLinked();
        }
    }
}
