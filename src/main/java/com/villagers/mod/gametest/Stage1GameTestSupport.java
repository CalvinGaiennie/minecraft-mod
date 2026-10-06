package com.villagers.mod.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BedPart;

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
        villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.NONE).setType(VillagerType.PLAINS));
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
        placeRedBed(helper, new BlockPos(0, 1, 1));
    }

    static void placeRedBed(GameTestHelper helper, BlockPos footRelative) {
        BlockPos foot = helper.absolutePos(footRelative);
        var footState = Blocks.RED_BED.defaultBlockState()
                .setValue(BedBlock.FACING, Direction.SOUTH)
                .setValue(BedBlock.PART, BedPart.FOOT);
        helper.getLevel().setBlock(foot, footState, 3);
        BlockPos head = foot.relative(BedBlock.getConnectedDirection(footState));
        helper.getLevel().setBlock(head, footState.setValue(BedBlock.PART, BedPart.HEAD), 3);
    }
}
