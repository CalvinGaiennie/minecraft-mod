package com.villagers.mod.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;

import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.entity.VillagerAttachments;

@GameTestHolder("villagers")
public class SoldierConversionTest {

    @GameTest(template = "empty")
    public static void testVillagerBecomeSoldier(GameTestHelper helper) {
        var blockPos = helper.absolutePos(new net.minecraft.core.BlockPos(0, 1, 0));

        Villager villager = new Villager(net.minecraft.world.entity.EntityType.VILLAGER, helper.getLevel());
        villager.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, new net.minecraft.world.item.ItemStack(Items.IRON_HELMET));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, new net.minecraft.world.item.ItemStack(Items.IRON_CHESTPLATE));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new net.minecraft.world.item.ItemStack(Items.IRON_SWORD));

        helper.getLevel().addFreshEntity(villager);

        var messStationPos = helper.absolutePos(new net.minecraft.core.BlockPos(1, 1, 0));
        var bedPos = helper.absolutePos(new net.minecraft.core.BlockPos(0, 1, 1));

        helper.setBlock(messStationPos, com.villagers.mod.VillagersMod.MESS_STATION.get().defaultBlockState());
        helper.setBlock(bedPos, com.villagers.mod.VillagersMod.POST_BED.get().defaultBlockState());

        helper.succeedWhen(() -> {
            if (SoldierConversion.canBecomeSoldier(villager)) {
                SoldierConversion.convertToSoldier(villager);
                if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                    var data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
                    if (data != null && "Soldier".equals(data.getRank())) {
                        return;
                    }
                }
            }
            throw new AssertionError("Villager should have become a soldier");
        });
    }
}
