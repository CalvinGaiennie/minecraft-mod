package com.villagers.mod.performance;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.war.HornCommandService;

/** Who gets full-rate combat AI vs throttled background ticks. */
public final class CombatTickPolicy {
    /** Full combat + behavior every tick within this horizontal distance of any player. */
    public static final double FULL_RATE_PLAYER_RADIUS = 96.0;
    private static final long FULL_RATE_RADIUS_SQ = (long) FULL_RATE_PLAYER_RADIUS * (long) FULL_RATE_PLAYER_RADIUS;

    /** Idle scan interval (ticks) for combat units far from all players. */
    public static final int FAR_IDLE_COMBAT_INTERVAL = 10;

    private CombatTickPolicy() {
    }

    public static boolean nearAnyPlayer(Villager villager) {
        if (!(villager.level() instanceof ServerLevel level)) {
            return true;
        }
        double x = villager.getX();
        double z = villager.getZ();
        for (ServerPlayer player : level.players()) {
            if (!player.isAlive()) {
                continue;
            }
            double dx = player.getX() - x;
            double dz = player.getZ() - z;
            if (dx * dx + dz * dz <= FULL_RATE_RADIUS_SQ) {
                return true;
            }
        }
        return false;
    }

    public static boolean shouldRunHeavyCombat(Villager villager, long gameTime, boolean hasTarget) {
        if (HornCommandService.isRallyActive(villager, gameTime)) {
            return true;
        }
        if (hasTarget) {
            return true;
        }
        if (nearAnyPlayer(villager)) {
            return true;
        }
        return ((gameTime + villager.getId()) % FAR_IDLE_COMBAT_INTERVAL) == 0L;
    }

    public static boolean shouldRunBehavior(Villager villager, long gameTime, boolean heavyCombat) {
        if (HornCommandService.isRallyActive(villager, gameTime)) {
            return true;
        }
        if (heavyCombat || nearAnyPlayer(villager)) {
            return true;
        }
        return ((gameTime + villager.getId()) % 5) == 0L;
    }
}
