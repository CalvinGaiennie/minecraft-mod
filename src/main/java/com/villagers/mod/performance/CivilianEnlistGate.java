package com.villagers.mod.performance;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.gear.GearReplacement;
import com.villagers.mod.village.VillageManager;

/** Limits civilian enlist / gear pull to owned claims or recruiter range. */
public final class CivilianEnlistGate {
    /** Once per five seconds, staggered per villager. */
    public static final int CIVILIAN_ENLIST_INTERVAL_TICKS = 100;

    private CivilianEnlistGate() {
    }

    public static boolean shouldRunCivilianEnlist(Villager villager, long gameTime) {
        if (((gameTime + villager.getId()) % CIVILIAN_ENLIST_INTERVAL_TICKS) != 0L) {
            return false;
        }
        if (!(villager.level() instanceof ServerLevel level)) {
            return false;
        }
        BlockPos pos = villager.blockPosition();
        if (VillageManager.get(level).findVillageAt(pos.getX(), pos.getY(), pos.getZ()) != null) {
            return true;
        }
        return GearReplacement.findNearestRecruiter(level, pos) != null;
    }
}
