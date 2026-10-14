package com.villagers.mod.economy;

import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

/** Collection interval from {@code soldiers-and-villages.md} tax table. */
public final class TaxSchedule {
    private TaxSchedule() {
    }

    public static int minDaysBetweenCollections(int memberCount) {
        if (memberCount <= 5) {
            return 3;
        }
        if (memberCount <= 10) {
            return 2;
        }
        if (memberCount <= 20) {
            return 2;
        }
        return 1;
    }

    public static int membersNearTaxBox(ServerLevel level, BlockPos taxBoxPos) {
        VillageData village = VillageManager.get(level).findVillageAt(taxBoxPos.getX(), taxBoxPos.getY(), taxBoxPos.getZ());
        if (village == null) {
            return 0;
        }
        return VillageManager.countMembersInVillage(level, village);
    }
}
