package com.villagers.mod.combat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

/** Picks up ground arrows after shots miss (MVP arrow recovery). */
public final class ArrowRecovery {
    private static final double PICKUP_RANGE_SQR = 6.0 * 6.0;

    private ArrowRecovery() {
    }

    public static void tryPickup(Villager soldier) {
        if (soldier.level().isClientSide || !(soldier.level() instanceof ServerLevel level)) {
            return;
        }
        if (!soldier.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        ItemStack ranged = VillagerGearRules.get(soldier, VillagerGearSlot.RANGED);
        if (ranged.isEmpty() || (!ranged.is(Items.BOW) && !ranged.is(Items.CROSSBOW))) {
            return;
        }
        ItemStack arrows = VillagerGearRules.get(soldier, VillagerGearSlot.ARROWS);
        if (!arrows.isEmpty() && arrows.getCount() >= VillagerGearRules.MAX_ARROWS) {
            return;
        }
        for (ItemEntity drop : level.getEntitiesOfClass(ItemEntity.class,
                soldier.getBoundingBox().inflate(6.0))) {
            if (!drop.getItem().is(Items.ARROW)) {
                continue;
            }
            int have = arrows.isEmpty() ? 0 : arrows.getCount();
            int take = Math.min(VillagerGearRules.MAX_ARROWS - have, drop.getItem().getCount());
            if (take <= 0) {
                return;
            }
            ItemStack updated = arrows.isEmpty() ? new ItemStack(Items.ARROW, take) : arrows.copy();
            if (!arrows.isEmpty()) {
                updated.setCount(have + take);
            }
            VillagerGearRules.set(soldier, VillagerGearSlot.ARROWS, updated);
            drop.getItem().shrink(take);
            if (drop.getItem().isEmpty()) {
                drop.discard();
            }
            return;
        }
    }
}
