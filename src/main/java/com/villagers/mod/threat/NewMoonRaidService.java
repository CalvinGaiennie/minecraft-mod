package com.villagers.mod.threat;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Pillager;
import com.villagers.mod.player.EnlistmentService;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

import java.util.UUID;

public final class NewMoonRaidService {
    private static final java.util.Map<UUID, Long> warnedRaids = new java.util.HashMap<>();

    private NewMoonRaidService() {
    }

    public static boolean isDarkMoonNight(long dayTime) {
        long day = dayTime / 24000L;
        return day % 28 == 0 || day % 28 == 27 || day % 28 == 1;
    }

    public static boolean isNewMoonNight(long dayTime) {
        return dayTime / 24000L % 28 == 0;
    }

    public static void tickForPlayer(ServerLevel level, ServerPlayer player) {
        if (!EnlistmentService.isEnlisted(player)) {
            return;
        }
        long time = level.getDayTime() % 24000L;
        if (time < 12000 || time > 18000) {
            return;
        }
        if (!isDarkMoonNight(level.getDayTime())) {
            return;
        }
        VillageData village = VillageManager.get(level).findVillageAt(
                player.getBlockX(), player.getBlockY(), player.getBlockZ());
        if (village == null) {
            return;
        }
        double chance = isNewMoonNight(level.getDayTime()) ? 0.40 : 0.15;
        UUID key = village.getVillageId();
        if (time < 12500 && !warnedRaids.containsKey(key)) {
            level.playSound(null, player.blockPosition(), SoundEvents.RAID_HORN.value(), SoundSource.HOSTILE, 1.2f, 0.7f);
            player.displayClientMessage(Component.translatable("message.villagers.raid_warning"), true);
            warnedRaids.put(key, level.getDayTime());
        }
        if (time != 13000 || level.random.nextDouble() > chance) {
            return;
        }
        spawnRaid(level, player, village);
        warnedRaids.remove(key);
    }

    private static void spawnRaid(ServerLevel level, ServerPlayer player, VillageData village) {
        int count = switch (level.random.nextInt(10)) {
            case 0, 1, 2, 3, 4, 5 -> 3 + level.random.nextInt(4);
            case 6, 7, 8 -> 7 + level.random.nextInt(4);
            default -> 11 + level.random.nextInt(4);
        };
        BlockPos center = new BlockPos(village.getMarkerX(), village.getMarkerY(), village.getMarkerZ());
        double angle = level.random.nextDouble() * Math.PI * 2;
        int dist = 64 + level.random.nextInt(32);
        for (int i = 0; i < count; i++) {
            double spread = level.random.nextDouble() * Math.PI * 2;
            int d = dist + level.random.nextInt(8);
            int x = center.getX() + (int) (Math.cos(angle) * d + Math.cos(spread) * 3);
            int z = center.getZ() + (int) (Math.sin(angle) * d + Math.sin(spread) * 3);
            BlockPos spawn = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, 0, z));
            Pillager pillager = EntityType.PILLAGER.create(level);
            if (pillager == null) {
                continue;
            }
            pillager.setPos(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);
            pillager.setPersistenceRequired();
            if (i == 0) {
                pillager.setPatrolLeader(true);
            }
            level.addFreshEntity(pillager);
        }
        player.displayClientMessage(Component.translatable("message.villagers.raid_started", count), true);
    }
}
