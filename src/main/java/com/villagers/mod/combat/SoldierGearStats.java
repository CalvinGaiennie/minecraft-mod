package com.villagers.mod.combat;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

/** Reads kit gear into combat numbers (weapon damage). Armor protection comes from worn equipment. */
public final class SoldierGearStats {
    private static final double FIST_DAMAGE = 1.0;

    private SoldierGearStats() {
    }

    public static double meleeAttackDamage(Villager villager) {
        return attackDamageFromStack(VillagerGearRules.get(villager, VillagerGearSlot.MELEE));
    }

    public static double attackDamageFromStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return FIST_DAMAGE;
        }
        double total = 0;
        ItemAttributeModifiers modifiers = stack.getOrDefault(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (!entry.attribute().is(Attributes.ATTACK_DAMAGE)) {
                continue;
            }
            if (!entry.slot().test(EquipmentSlot.MAINHAND)) {
                continue;
            }
            total += entry.modifier().amount();
        }
        return total > 0 ? total : FIST_DAMAGE;
    }

}
