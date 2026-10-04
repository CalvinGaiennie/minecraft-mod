package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import com.villagers.mod.util.SoldierStructureHelper;

public class SoldierConversion {
    public static boolean canBecomeSoldier(Villager villager) {
        if (villager == null || villager.getVillagerData().getProfession() != VillagerProfession.NONE) {
            return false;
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return false;
        }

        ItemStack helmet = villager.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD);
        ItemStack chestplate = villager.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST);
        ItemStack mainHand = villager.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND);

        if (helmet.isEmpty() || chestplate.isEmpty() || mainHand.isEmpty()) {
            return false;
        }
        if (!isHelmet(helmet) || !isChestplate(chestplate) || !isWeapon(mainHand)) {
            return false;
        }

        Level level = villager.level();
        BlockPos origin = villager.blockPosition();
        if (SoldierStructureHelper.findFreePostBed(level, origin).isEmpty()) {
            return false;
        }
        return SoldierStructureHelper.hasStockedMessStation(level, origin);
    }

    public static boolean tryConvert(Villager villager) {
        if (!canBecomeSoldier(villager)) {
            return false;
        }
        convertToSoldier(villager);
        return true;
    }

    private static boolean isHelmet(ItemStack item) {
        return item.getItem() == Items.LEATHER_HELMET || item.getItem() == Items.CHAINMAIL_HELMET
                || item.getItem() == Items.IRON_HELMET || item.getItem() == Items.DIAMOND_HELMET
                || item.getItem() == Items.NETHERITE_HELMET;
    }

    private static boolean isChestplate(ItemStack item) {
        return item.getItem() == Items.LEATHER_CHESTPLATE || item.getItem() == Items.CHAINMAIL_CHESTPLATE
                || item.getItem() == Items.IRON_CHESTPLATE || item.getItem() == Items.DIAMOND_CHESTPLATE
                || item.getItem() == Items.NETHERITE_CHESTPLATE;
    }

    private static boolean isWeapon(ItemStack item) {
        return item.getItem() == Items.WOODEN_SWORD || item.getItem() == Items.STONE_SWORD
                || item.getItem() == Items.IRON_SWORD || item.getItem() == Items.DIAMOND_SWORD
                || item.getItem() == Items.NETHERITE_SWORD || item.getItem() == Items.WOODEN_AXE
                || item.getItem() == Items.STONE_AXE || item.getItem() == Items.IRON_AXE
                || item.getItem() == Items.DIAMOND_AXE || item.getItem() == Items.NETHERITE_AXE;
    }

    public static void convertToSoldier(Villager villager) {
        if (villager.level().isClientSide) {
            return;
        }

        Level level = villager.level();
        BlockPos origin = villager.blockPosition();

        SoldierData data = new SoldierData(villager.getUUID());
        data.setDisplayName(villager.getName().getString());
        if (villager.hasData(VillagerAttachments.VETERAN_DATA.get())) {
            VeteranData veteran = villager.getData(VillagerAttachments.VETERAN_DATA.get());
            if (veteran.getKills() > 0) {
                data.setKills(veteran.getKills());
                data.setRank(veteran.getRank());
            }
        }
        long day = level.getDayTime() / 24000L;
        data.setLastFoodDay(day);

        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), data);
        SoldierStructureHelper.findFreePostBed(level, origin).ifPresent(bedPos ->
                SoldierStructureHelper.claimPostBed(level, bedPos, villager.getUUID()));
        SoldierLabels.apply(villager, data);
    }

    public static void dischargeHonorable(Villager villager) {
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null || villager.level().isClientSide) {
            return;
        }
        VeteranData veteran = new VeteranData(data.getKills(), data.getRank());
        villager.setData(VillagerAttachments.VETERAN_DATA.get(), veteran);
        villager.removeData(VillagerAttachments.SOLDIER_DATA.get());
        SoldierStructureHelper.releasePostBedForSoldier(villager.level(), villager);
        SoldierLabels.clear(villager);
    }
}
