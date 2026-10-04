package com.villagers.mod.entity;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class SoldierConversion {
    public static boolean canBecomeSoldier(Villager villager) {
        if (villager == null || villager.getVillagerData().getProfession() != VillagerProfession.NONE) {
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

        return hasRequiredStructures(villager);
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

    private static boolean hasRequiredStructures(Villager villager) {
        var level = villager.getCommandSenderWorld();
        if (level == null) return false;

        int x = villager.getBlockX();
        int y = villager.getBlockY();
        int z = villager.getBlockZ();

        for (int dx = -16; dx <= 16; dx++) {
            for (int dy = -5; dy <= 5; dy++) {
                for (int dz = -16; dz <= 16; dz++) {
                    var block = level.getBlockState(new net.minecraft.core.BlockPos(x + dx, y + dy, z + dz)).getBlock();
                    if (block instanceof com.villagers.mod.block.PostBedBlock) {
                        if (!hasStockedMessStation(villager, x, y, z)) return false;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean hasStockedMessStation(Villager villager, int villagerX, int villagerY, int villagerZ) {
        var level = villager.getCommandSenderWorld();
        if (level == null) return false;

        for (int dx = -16; dx <= 16; dx++) {
            for (int dy = -5; dy <= 5; dy++) {
                for (int dz = -16; dz <= 16; dz++) {
                    var block = level.getBlockState(new net.minecraft.core.BlockPos(villagerX + dx, villagerY + dy, villagerZ + dz)).getBlock();
                    if (block instanceof com.villagers.mod.block.MessStationBlock) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void convertToSoldier(Villager villager) {
        if (villager == null || villager.getCommandSenderWorld() == null || villager.getCommandSenderWorld().isClientSide) {
            return;
        }

        SoldierData data = new SoldierData(villager.getUUID());
        data.setDisplayName(villager.getName().getString());
        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), data);
    }
}
