package com.villagers.mod.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.entity.MessStationBlockEntity;

final class Stage1GameTestSupport {
    private Stage1GameTestSupport() {
    }

    static void stockMessStation(GameTestHelper helper, BlockPos relativePos) {
        BlockPos pos = helper.absolutePos(relativePos);
        var be = helper.getLevel().getBlockEntity(pos);
        if (be instanceof MessStationBlockEntity mess) {
            mess.setFoodForTesting(new ItemStack(Items.BREAD));
        }
    }

    static Villager spawnEquippedVillager(GameTestHelper helper, BlockPos relativePos) {
        BlockPos pos = helper.absolutePos(relativePos);
        Villager villager = new Villager(EntityType.VILLAGER, helper.getLevel());
        villager.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        helper.getLevel().addFreshEntity(villager);
        return villager;
    }

    static void placeCoreStructures(GameTestHelper helper) {
        helper.setBlock(new BlockPos(1, 1, 0), VillagersMod.MESS_STATION.get());
        stockMessStation(helper, new BlockPos(1, 1, 0));
        helper.setBlock(new BlockPos(0, 1, 1), VillagersMod.POST_BED.get());
    }
}
