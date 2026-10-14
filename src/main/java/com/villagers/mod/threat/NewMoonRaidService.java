package com.villagers.mod.threat;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import com.villagers.mod.armies.BanditSpawnHelper;
import com.villagers.mod.entity.BanditData;
import com.villagers.mod.player.EnlistmentService;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

import java.util.UUID;

/** New-moon style raids: deserter **bandits**, not pillagers ({@code threats-and-mobs.md}). */
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
        int count = spawnRaid(level, player, village);
        player.displayClientMessage(Component.translatable("message.villagers.raid_started", count), true);
        warnedRaids.remove(key);
    }

    /**
     * Spawns a new-moon raid at the player's current village (or position if unclaimed).
     *
     * @return bandit count spawned
     */
    public static int triggerRaid(ServerLevel level, ServerPlayer player) {
        VillageData village = VillageManager.get(level).findVillageAt(
                player.getBlockX(), player.getBlockY(), player.getBlockZ());
        if (village == null) {
            player.displayClientMessage(Component.translatable("message.villagers.raid_no_village"), true);
            return 0;
        }
        level.playSound(null, player.blockPosition(), SoundEvents.RAID_HORN.value(), SoundSource.HOSTILE, 1.2f, 0.7f);
        int count = spawnRaid(level, player, village);
        player.displayClientMessage(Component.translatable("message.villagers.raid_started", count), true);
        return count;
    }

    private static int spawnRaid(ServerLevel level, ServerPlayer player, VillageData village) {
        int count = rollRaidSize(level);
        UUID raidCampId = UUID.randomUUID();
        BlockPos center = new BlockPos(village.getMarkerX(), village.getMarkerY(), village.getMarkerZ());
        double angle = level.random.nextDouble() * Math.PI * 2;
        int dist = 64 + level.random.nextInt(32);
        int spawned = 0;
        for (int i = 0; i < count; i++) {
            double spread = level.random.nextDouble() * Math.PI * 2;
            int d = dist + level.random.nextInt(8);
            int x = center.getX() + (int) (Math.cos(angle) * d + Math.cos(spread) * 3);
            int z = center.getZ() + (int) (Math.sin(angle) * d + Math.sin(spread) * 3);
            BlockPos spawn = level.getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, 0, z));
            boolean leader = i == 0;
            BanditSpawnHelper.spawnBandit(
                    level,
                    spawn,
                    raidCampId,
                    leader,
                    BanditData.BossRole.NONE,
                    null,
                    leader ? Component.translatable("entity.villagers.raid_captain") : null);
            spawned++;
        }
        if (spawned > 0) {
            BanditRaidService.beginRaid(level, village, raidCampId);
        }
        return spawned;
    }

    static int rollRaidSize(ServerLevel level) {
        return switch (level.random.nextInt(10)) {
            case 0, 1, 2, 3, 4, 5 -> 3 + level.random.nextInt(4);
            case 6, 7, 8 -> 7 + level.random.nextInt(4);
            default -> 11 + level.random.nextInt(4);
        };
    }
}
