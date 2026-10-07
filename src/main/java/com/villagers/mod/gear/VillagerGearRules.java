package com.villagers.mod.gear;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.combat.MilitiaStats;
import com.villagers.mod.combat.SoldierRanks;
import com.villagers.mod.entity.MilitiaData;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.entity.VillagerKitData;
import com.villagers.mod.item.CampaignBootsItem;
import com.villagers.mod.threat.BanditService;
import com.villagers.mod.threat.WanderingNecromancerService;

public final class VillagerGearRules {
    public static final int MAX_ARROWS = 16;

    private VillagerGearRules() {
    }

    public static boolean accepts(VillagerGearSlot slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return switch (slot) {
            case HELMET -> isHelmet(stack);
            case CHESTPLATE -> isChestplate(stack);
            case LEGGINGS -> isLeggings(stack);
            case BOOTS -> isBoots(stack);
            case MELEE -> isMeleeWeapon(stack);
            case RANGED -> isRangedTool(stack);
            case ARROWS -> stack.is(Items.ARROW);
        };
    }

    public static boolean isMilitiaGear(ItemStack stack) {
        VillagerGearSlot slot = slotForItem(stack);
        return slot != null && slot != VillagerGearSlot.LEGGINGS;
    }

    public static VillagerGearSlot slotForItem(ItemStack stack) {
        for (VillagerGearSlot slot : VillagerGearSlot.values()) {
            if (accepts(slot, stack)) {
                return slot;
            }
        }
        return null;
    }

    public static ItemStack get(Villager villager, VillagerGearSlot slot) {
        ensureImportedFromEntity(villager, slot);
        return kit(villager).get(slot);
    }

    public static void set(Villager villager, VillagerGearSlot slot, ItemStack stack) {
        ItemStack copy = stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
        if (slot == VillagerGearSlot.ARROWS && !copy.isEmpty()) {
            copy.setCount(Math.min(copy.getCount(), MAX_ARROWS));
        } else if (!copy.isEmpty()) {
            copy.setCount(1);
        }
        kit(villager).set(slot, copy);
        applySlotToEntity(villager, slot, copy);
        syncSoldierCombatIfNeeded(villager);
    }

    /**
     * Drops every kit slot at current durability/count, then clears kit and vanilla equipment
     * so death does not duplicate items.
     */
    public static void dropEntireKit(Villager villager) {
        if (villager.level().isClientSide) {
            return;
        }
        mergeHandsIntoKitBeforeDrop(villager);
        for (VillagerGearSlot slot : VillagerGearSlot.values()) {
            ItemStack stack = kit(villager).get(slot);
            if (!stack.isEmpty()) {
                villager.spawnAtLocation(stack.copy());
            }
        }
        clearKit(villager);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            villager.setItemSlot(slot, ItemStack.EMPTY);
        }
    }

    /** Copy kit onto the villager model (armor + mainhand). Safe on client after kit attachment sync. */
    public static void applyToEntity(Villager villager) {
        for (VillagerGearSlot slot : VillagerGearSlot.values()) {
            applySlotToEntity(villager, slot, kit(villager).get(slot));
        }
        syncSoldierCombatIfNeeded(villager);
    }

    /** Re-push armor slots from kit (server authoritative; also used when kit syncs on client). */
    public static void refreshVisibleEquipment(Villager villager) {
        syncArmorEquipment(villager);
    }

    /** Wear kit armor on the entity so damage reduction matches a player (rank HP is separate). */
    public static void syncArmorEquipment(Villager villager) {
        for (VillagerGearSlot slot : new VillagerGearSlot[] {
                VillagerGearSlot.HELMET, VillagerGearSlot.CHESTPLATE, VillagerGearSlot.LEGGINGS, VillagerGearSlot.BOOTS }) {
            applySlotToEntity(villager, slot, kit(villager).get(slot));
        }
    }

    private static void syncSoldierCombatIfNeeded(Villager villager) {
        if (villager.level().isClientSide) {
            return;
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null) {
                SoldierRanks.syncRank(villager, data);
            }
            return;
        }
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            MilitiaData data = villager.getData(VillagerAttachments.MILITIA_DATA.get());
            if (data != null) {
                MilitiaStats.sync(villager, data);
            }
        }
    }

    private static void applySlotToEntity(Villager villager, VillagerGearSlot slot, ItemStack stack) {
        switch (slot) {
            case HELMET -> villager.setItemSlot(EquipmentSlot.HEAD, stack.copy());
            case CHESTPLATE -> villager.setItemSlot(EquipmentSlot.CHEST, stack.copy());
            case LEGGINGS -> villager.setItemSlot(EquipmentSlot.LEGS, stack.copy());
            case BOOTS -> villager.setItemSlot(EquipmentSlot.FEET, stack.copy());
            case MELEE -> villager.setItemSlot(EquipmentSlot.MAINHAND, stack.copy());
            case RANGED, ARROWS -> {
            }
        }
    }

    /** Threat mobs wear gear via vanilla slots only; kit import would treat them as militia/soldiers. */
    public static void clearKitForVanillaEquipmentMob(Villager villager) {
        villager.removeData(VillagerAttachments.VILLAGER_KIT.get());
    }

    /** Pull legacy equipment into kit once (GameTests / old saves). */
    private static void ensureImportedFromEntity(Villager villager, VillagerGearSlot slot) {
        if (BanditService.isBandit(villager) || WanderingNecromancerService.isWanderingNecromancer(villager)) {
            return;
        }
        VillagerKitData data = kit(villager);
        if (!data.get(slot).isEmpty()) {
            return;
        }
        ItemStack fromEntity = switch (slot) {
            case HELMET -> villager.getItemBySlot(EquipmentSlot.HEAD);
            case CHESTPLATE -> villager.getItemBySlot(EquipmentSlot.CHEST);
            case LEGGINGS -> villager.getItemBySlot(EquipmentSlot.LEGS);
            case BOOTS -> villager.getItemBySlot(EquipmentSlot.FEET);
            case MELEE -> meleeImportFromEntity(villager);
            case RANGED -> rangedImportFromEntity(villager);
            case ARROWS -> villager.getItemBySlot(EquipmentSlot.OFFHAND);
        };
        if (!fromEntity.isEmpty() && accepts(slot, fromEntity)) {
            data.set(slot, fromEntity);
        }
    }

    public static void loadIntoContainer(Villager villager, net.minecraft.world.Container container) {
        for (VillagerGearSlot slot : VillagerGearSlot.values()) {
            container.setItem(slot.ordinal(), get(villager, slot).copy());
        }
    }

    public static void saveFromContainer(Villager villager, net.minecraft.world.Container container) {
        for (VillagerGearSlot slot : VillagerGearSlot.values()) {
            set(villager, slot, container.getItem(slot.ordinal()));
        }
    }

    private static void mergeHandsIntoKitBeforeDrop(Villager villager) {
        for (VillagerGearSlot slot : VillagerGearSlot.values()) {
            ensureImportedFromEntity(villager, slot);
        }
        ItemStack main = villager.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!main.isEmpty()) {
            if (accepts(VillagerGearSlot.RANGED, main)) {
                kit(villager).set(VillagerGearSlot.RANGED, main.copy());
            } else if (accepts(VillagerGearSlot.MELEE, main)) {
                kit(villager).set(VillagerGearSlot.MELEE, main.copy());
            }
        }
        ItemStack off = villager.getItemBySlot(EquipmentSlot.OFFHAND);
        if (!off.isEmpty() && accepts(VillagerGearSlot.ARROWS, off)) {
            kit(villager).set(VillagerGearSlot.ARROWS, off.copy());
        }
    }

    private static void clearKit(Villager villager) {
        VillagerKitData data = kit(villager);
        for (VillagerGearSlot slot : VillagerGearSlot.values()) {
            data.set(slot, ItemStack.EMPTY);
        }
    }

    private static VillagerKitData kit(Villager villager) {
        if (!villager.hasData(VillagerAttachments.VILLAGER_KIT.get())) {
            villager.setData(VillagerAttachments.VILLAGER_KIT.get(), new VillagerKitData());
        }
        return villager.getData(VillagerAttachments.VILLAGER_KIT.get());
    }

    private static ItemStack meleeImportFromEntity(Villager villager) {
        ItemStack main = villager.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!main.isEmpty() && accepts(VillagerGearSlot.MELEE, main)) {
            return main;
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack rangedImportFromEntity(Villager villager) {
        ItemStack main = villager.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!main.isEmpty() && accepts(VillagerGearSlot.RANGED, main)) {
            return main;
        }
        ItemStack off = villager.getItemBySlot(EquipmentSlot.OFFHAND);
        if (!off.isEmpty() && accepts(VillagerGearSlot.RANGED, off)) {
            return off;
        }
        return ItemStack.EMPTY;
    }

    private static boolean isHelmet(ItemStack item) {
        return equipsInSlot(item, EquipmentSlot.HEAD)
                || item.is(VillagersMod.NIGHTWATCH_HELMET.get());
    }

    private static boolean isChestplate(ItemStack item) {
        return equipsInSlot(item, EquipmentSlot.CHEST)
                || item.is(VillagersMod.ROYAL_GUARD_CHESTPLATE.get());
    }

    private static boolean isLeggings(ItemStack item) {
        return equipsInSlot(item, EquipmentSlot.LEGS);
    }

    private static boolean isBoots(ItemStack item) {
        return equipsInSlot(item, EquipmentSlot.FEET) || CampaignBootsItem.isCampaignBoots(item);
    }

    /** Iron-tier or better sword/axe (wood and stone do not qualify). */
    private static boolean isMeleeWeapon(ItemStack item) {
        if (!(item.getItem() instanceof SwordItem || item.getItem() instanceof AxeItem)) {
            return false;
        }
        if (item.getItem() instanceof TieredItem tiered) {
            var tier = tiered.getTier();
            return tier != Tiers.WOOD && tier != Tiers.STONE;
        }
        return true;
    }

    private static boolean equipsInSlot(ItemStack stack, EquipmentSlot slot) {
        if (!(stack.getItem() instanceof ArmorItem armor)) {
            return false;
        }
        return armor.getEquipmentSlot() == slot;
    }

    private static boolean isRangedTool(ItemStack item) {
        return item.getItem() == Items.BOW || item.getItem() == Items.CROSSBOW
                || item.getItem() == Items.WOODEN_PICKAXE || item.getItem() == Items.STONE_PICKAXE
                || item.getItem() == Items.IRON_PICKAXE || item.getItem() == Items.DIAMOND_PICKAXE
                || item.getItem() == Items.NETHERITE_PICKAXE;
    }
}
