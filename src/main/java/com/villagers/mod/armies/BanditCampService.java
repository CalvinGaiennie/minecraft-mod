package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import com.villagers.mod.Config;
import com.villagers.mod.block.entity.BanditCampBlockEntity;
import com.villagers.mod.entity.BanditData;
import com.villagers.mod.entity.VillagerAttachments;

import java.util.UUID;

public final class BanditCampService {
    private BanditCampService() {
    }

    public static void bootstrapSite(ServerLevel level, BanditCampBlockEntity camp) {
        BanditWorldSavedData data = BanditWorldSavedData.get(level);
        BanditWorldSavedData.SiteRecord record = data.findById(camp.getSiteId());
        if (record == null) {
            return;
        }
        if (!record.lootFilled()) {
            BlockPos chest = camp.getBlockPos().north();
            BanditLoot.fillSiteChest(level, chest, camp.getKind(), level.random);
            data.updateSite(new BanditWorldSavedData.SiteRecord(record.id(), record.kind(), record.origin(), true, record.nextRespawnGameTime()));
        }
        trySpawnGarrison(level, camp, record, true);
    }

    public static void tickSite(ServerLevel level, BanditCampBlockEntity camp) {
        BanditWorldSavedData data = BanditWorldSavedData.get(level);
        BanditWorldSavedData.SiteRecord record = data.findById(camp.getSiteId());
        if (record == null) {
            return;
        }
        if (!level.hasNearbyAlivePlayer(camp.getBlockPos().getX(), camp.getBlockPos().getY(), camp.getBlockPos().getZ(), 128)) {
            return;
        }
        int alive = countCampBandits(level, camp.getSiteId(), camp.getBlockPos());
        int target = garrisonSize(camp.getKind());
        if (alive < target && level.getGameTime() >= record.nextRespawnGameTime()) {
            trySpawnGarrison(level, camp, record, false);
        }
    }

    private static int garrisonSize(BanditWorldSavedData.SiteKind kind) {
        return switch (kind) {
            case CAMP -> 4;
            case HIDEOUT -> 8;
            case CORVIN -> 10;
            case GARLAND -> 12;
        };
    }

    private static int countCampBandits(ServerLevel level, UUID campId, BlockPos origin) {
        int count = 0;
        AABB box = new AABB(origin).inflate(48);
        for (Villager villager : level.getEntitiesOfClass(Villager.class, box)) {
            if (!villager.hasData(VillagerAttachments.BANDIT_DATA.get())) {
                continue;
            }
            BanditData bd = villager.getData(VillagerAttachments.BANDIT_DATA.get());
            if (bd != null && campId.equals(bd.getHomeCampId())) {
                count++;
            }
        }
        return count;
    }

    private static void trySpawnGarrison(ServerLevel level, BanditCampBlockEntity camp, BanditWorldSavedData.SiteRecord record, boolean initial) {
        int target = garrisonSize(camp.getKind());
        int alive = countCampBandits(level, camp.getSiteId(), camp.getBlockPos());
        int toSpawn = initial ? target : Math.min(2, target - alive);
        if (toSpawn <= 0) {
            return;
        }
        UUID questOwner = null;
        BanditData.BossRole boss = BanditData.BossRole.NONE;
        net.minecraft.network.chat.Component bossName = null;
        BanditWorldSavedData worldData = BanditWorldSavedData.get(level);
        if (camp.getKind() == BanditWorldSavedData.SiteKind.CORVIN && !worldData.isCorvinBossDefeated()) {
            boss = BanditData.BossRole.CORVIN;
            bossName = net.minecraft.network.chat.Component.literal("Corvin");
        } else if (camp.getKind() == BanditWorldSavedData.SiteKind.GARLAND && !worldData.isGarlandBossDefeated()) {
            boss = BanditData.BossRole.GARLAND;
            bossName = net.minecraft.network.chat.Component.literal("Garland");
        }
        boolean bossSpawned = alive > 0;
        for (int i = 0; i < toSpawn; i++) {
            BlockPos spawn = camp.getBlockPos().offset(level.random.nextInt(5) - 2, 0, level.random.nextInt(5) - 2);
            boolean spawnBoss = !bossSpawned && boss != BanditData.BossRole.NONE;
            BanditSpawnHelper.spawnBandit(level, spawn, camp.getSiteId(), spawnBoss || level.random.nextFloat() < 0.15f,
                    spawnBoss ? boss : BanditData.BossRole.NONE, questOwner, spawnBoss ? bossName : null);
            if (spawnBoss) {
                bossSpawned = true;
            }
        }
        if (!initial && alive == 0) {
            BanditWorldSavedData data = BanditWorldSavedData.get(level);
            data.updateSite(new BanditWorldSavedData.SiteRecord(record.id(), record.kind(), record.origin(), record.lootFilled(),
                    level.getGameTime() + Config.BANDIT_CAMP_RESPAWN_TICKS.get()));
        }
    }

    public static void markCampCleared(ServerLevel level, UUID campId) {
        BanditWorldSavedData data = BanditWorldSavedData.get(level);
        BanditWorldSavedData.SiteRecord record = data.findById(campId);
        if (record == null) {
            return;
        }
        data.updateSite(new BanditWorldSavedData.SiteRecord(record.id(), record.kind(), record.origin(), record.lootFilled(),
                level.getGameTime() + Config.BANDIT_CAMP_RESPAWN_TICKS.get()));
    }
}
