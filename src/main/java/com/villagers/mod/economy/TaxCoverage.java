package com.villagers.mod.economy;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.village.VillageZone;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TaxCoverage {
    public static final int VILLAGERS_PER_SOLDIER = 5;

    private TaxCoverage() {
    }

    public static int maxTaxableVillagers(int activeSoldierCount) {
        return Math.max(0, activeSoldierCount * VILLAGERS_PER_SOLDIER);
    }

    public static List<Villager> taxableVillagers(ServerLevel level, BlockPos taxBoxPos, int soldierCount) {
        int cap = maxTaxableVillagers(soldierCount);
        if (cap == 0) {
            return List.of();
        }

        List<VillageZone.Circle> zone = VillageZone.taxCircles(level, taxBoxPos);
        int queryRadius = VillageZone.queryHalfExtent(zone);

        List<Villager> candidates = new ArrayList<>();
        for (Villager villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(taxBoxPos).inflate(queryRadius))) {
            if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            if (!VillageZone.villagerInTaxZone(villager, zone)) {
                continue;
            }
            candidates.add(villager);
        }
        candidates.sort(Comparator.comparingDouble(v -> v.blockPosition().distSqr(taxBoxPos)));

        if (candidates.size() <= cap) {
            return candidates;
        }
        return new ArrayList<>(candidates.subList(0, cap));
    }
}
