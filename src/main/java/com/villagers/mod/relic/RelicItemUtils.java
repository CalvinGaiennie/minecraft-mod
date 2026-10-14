package com.villagers.mod.relic;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

import com.villagers.mod.item.NecromancerHoodItem;
import com.villagers.mod.item.NecromancerRobeItem;
import com.villagers.mod.item.NecromancerWandItem;
import com.villagers.mod.VillagersMod;

public final class RelicItemUtils {
    public static final float RELIC_DAMAGE_MULTIPLIER = 1.25f;
    /** Each relic armor piece multiplies incoming damage by this (stacked). */
    public static final float ARMOR_DAMAGE_FACTOR_PER_PIECE = 0.86f;
    public static final float OATHKEEPER_ALLY_DAMAGE_FACTOR = 0.72f;
    public static final float IRONROOT_UNDEAD_DAMAGE_FACTOR = 0.70f;
    public static final float TIDESPEAR_PULL_STRENGTH = 1.35f;
    public static final float KINGSMAUL_KNOCKBACK = 1.25f;
    public static final int KINGSMAUL_SLOW_TICKS = 60;

    private RelicItemUtils() {
    }

    public static Item.Properties relicProperties() {
        return new Item.Properties()
                .fireResistant()
                .stacksTo(1)
                .setNoRepair();
    }

    public static boolean isKingRelic(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IKingRelic;
    }

    public static RelicKind kind(ItemStack stack) {
        return stack.isEmpty() ? null : RelicKind.fromItem(stack.getItem());
    }

    public static boolean necromancerBlockedFromRelics(net.minecraft.world.entity.player.Player player) {
        if (player.getInventory().contains(new ItemStack(VillagersMod.NECROMANCER_WAND.get()))) {
            return true;
        }
        ItemStack head = player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD);
        ItemStack chest = player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST);
        return head.getItem() instanceof NecromancerHoodItem || chest.getItem() instanceof NecromancerRobeItem;
    }

    public static ItemStack createDefaultStack(Item item, HolderLookup.Provider registries) {
        ItemStack stack = new ItemStack(item);
        RelicKind kind = RelicKind.fromItem(item);
        if (registries != null) {
            var enchants = registries.lookupOrThrow(Registries.ENCHANTMENT);
            if (kind == RelicKind.TIDESPEAR) {
                stack.enchant(enchants.getOrThrow(Enchantments.LOYALTY), 3);
                stack.enchant(enchants.getOrThrow(Enchantments.CHANNELING), 1);
            }
            if (kind == RelicKind.LONG_HUNT) {
                stack.enchant(enchants.getOrThrow(Enchantments.INFINITY), 1);
                stack.enchant(enchants.getOrThrow(Enchantments.POWER), 5);
                stack.enchant(enchants.getOrThrow(Enchantments.PIERCING), 4);
            }
        }
        return stack;
    }
}
