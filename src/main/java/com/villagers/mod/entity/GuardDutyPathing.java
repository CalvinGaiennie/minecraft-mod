package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Items;

import com.villagers.mod.combat.ArrowResupply;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

public final class GuardDutyPathing {
    private GuardDutyPathing() {
    }

    public static void tick(Villager villager) {
        if (villager.level().isClientSide) {
            return;
        }
        if (needsArrows(villager)) {
            var resupply = ArrowResupply.nearestResupplyPos(
                    (net.minecraft.server.level.ServerLevel) villager.level(), villager.blockPosition());
            if (resupply.isPresent()) {
                BlockPos bin = resupply.get();
                if (villager.blockPosition().distSqr(bin) <= 16) {
                    ArrowResupply.tryResupply(villager);
                } else {
                    PostReturnPathing.navigateFast(villager, bin.getX() + 0.5, bin.getY(), bin.getZ() + 0.5);
                }
                return;
            }
        }
        PostReturnPathing.returnToRampart(villager);
    }

    private static boolean needsArrows(Villager villager) {
        var ranged = VillagerGearRules.get(villager, VillagerGearSlot.RANGED);
        if (ranged.isEmpty() || (!ranged.is(Items.BOW) && !ranged.is(Items.CROSSBOW))) {
            return false;
        }
        var arrows = VillagerGearRules.get(villager, VillagerGearSlot.ARROWS);
        return arrows.isEmpty() || arrows.getCount() < VillagerGearRules.MAX_ARROWS;
    }

}
