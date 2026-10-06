package com.villagers.mod.gear;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.entity.MilitiaConversion;
import com.villagers.mod.entity.VillagerAttachments;

public final class VillagerGearHandEquip {
    private VillagerGearHandEquip() {
    }

    public static boolean tryEquip(Villager villager, Player player, ItemStack held) {
        if (villager.level().isClientSide || held.isEmpty()) {
            return false;
        }
        if (villager.isBaby()) {
            return false;
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())
                || villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            return tryEquipKit(villager, player, held);
        }
        if (villager.getVillagerData().getProfession() != VillagerProfession.NONE
                && !VillagerGearRules.isMilitiaGear(held)) {
            return false;
        }
        if (!tryEquipKit(villager, player, held)) {
            return false;
        }
        MilitiaConversion.onGearUpdated(villager);
        return true;
    }

    private static boolean tryEquipKit(Villager villager, Player player, ItemStack held) {
        VillagerGearSlot slot = VillagerGearRules.slotForItem(held);
        if (slot == null) {
            return false;
        }
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get()) && slot == VillagerGearSlot.LEGGINGS) {
            return false;
        }
        ItemStack previous = VillagerGearRules.get(villager, slot).copy();
        int count = slot == VillagerGearSlot.ARROWS ? Math.min(held.getCount(), VillagerGearRules.MAX_ARROWS) : 1;
        ItemStack placed = held.copyWithCount(count);
        VillagerGearRules.set(villager, slot, placed);
        if (!player.getAbilities().instabuild) {
            held.shrink(count);
        }
        if (!previous.isEmpty()) {
            if (!player.getInventory().add(previous)) {
                player.drop(previous, false);
            }
        }
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            MilitiaConversion.onGearUpdated(villager);
        }
        return true;
    }
}
