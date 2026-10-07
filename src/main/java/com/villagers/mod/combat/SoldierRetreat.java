package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.util.SoldierStructureHelper;
import com.villagers.mod.war.CampaignService;

public final class SoldierRetreat {
    private SoldierRetreat() {
    }

    public static void tick(Villager soldier) {
        if (!soldier.hasData(VillagerAttachments.SOLDIER_DATA.get()) || soldier.level().isClientSide) {
            return;
        }
        if (SoldierCombat.isWithinHomeBedRadius(soldier)) {
            return;
        }
        float max = soldier.getMaxHealth();
        if (max <= 0 || soldier.getHealth() / max > SoldierCombatConstants.RETREAT_HEALTH_FRACTION) {
            return;
        }
        SoldierData data = soldier.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data != null && data.isOnCampaign()) {
            CampaignService.pathRetreatToCamp(soldier, data);
            return;
        }
        SoldierStructureHelper.findNearestFallback(soldier.level(), soldier.blockPosition()).ifPresent(fallback -> {
            if (soldier.getNavigation().isDone()) {
                soldier.getNavigation().moveTo(fallback.getX() + 0.5, fallback.getY(), fallback.getZ() + 0.5, 1.0);
            }
        });
    }
}
