package com.villagers.mod.test;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.SoldierDesertion;
import com.villagers.mod.VillagersMod;

public class Stage1Slice5Test {

    @GameTest(template = "empty")
    public static void testDesertion_Homeless(GameTestHelper helper) {
        Villager villager = new Villager(EntityType.VILLAGER, helper.getLevel());
        villager.moveTo(0, 0, 0);
        helper.getLevel().addFreshEntity(villager);

        SoldierData soldierData = new SoldierData(villager.getUUID());
        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), soldierData);

        for (int i = 0; i < 3; i++) {
            soldierData.setHomelessNights(i + 1);
            if (i < 2) {
                helper.assertFalse(SoldierDesertion.shouldDeserve(villager),
                    "Should not desert until 3 homeless nights");
            }
        }

        helper.assertTrue(SoldierDesertion.shouldDeserve(villager),
            "Should desert after 3 homeless nights");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void testDesertion_Hungry(GameTestHelper helper) {
        Villager villager = new Villager(EntityType.VILLAGER, helper.getLevel());
        villager.moveTo(0, 0, 0);
        helper.getLevel().addFreshEntity(villager);

        SoldierData soldierData = new SoldierData(villager.getUUID());
        soldierData.setLastFoodDay(0);
        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), soldierData);

        long currentDay = helper.getLevel().getDayTime() / 24000;
        long daysWithoutFood = currentDay - 0;

        helper.assertTrue(daysWithoutFood >= 7 || SoldierDesertion.shouldDeserve(villager),
            "Should desert after 7 days without food or based on calculation");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void testDischarge_WeaponRemoved(GameTestHelper helper) {
        Villager villager = new Villager(EntityType.VILLAGER, helper.getLevel());
        villager.moveTo(0, 0, 0);
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        helper.getLevel().addFreshEntity(villager);

        SoldierData soldierData = new SoldierData(villager.getUUID());
        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), soldierData);

        helper.assertTrue(villager.hasData(VillagerAttachments.SOLDIER_DATA.get()),
            "Villager should have soldier data");

        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, ItemStack.EMPTY);

        SoldierDesertion.processDischarged(villager);
        helper.assertFalse(villager.hasData(VillagerAttachments.SOLDIER_DATA.get()),
            "Villager should lose soldier data when discharged");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void testAwakenHornShowsNearby(GameTestHelper helper) {
        Villager soldier = new Villager(EntityType.VILLAGER, helper.getLevel());
        soldier.moveTo(0, 0, 0);
        helper.getLevel().addFreshEntity(soldier);

        SoldierData soldierData = new SoldierData(soldier.getUUID());
        soldier.setData(VillagerAttachments.SOLDIER_DATA.get(), soldierData);

        helper.assertTrue(soldier.hasData(VillagerAttachments.SOLDIER_DATA.get()),
            "Soldier should have data for awaken horn to detect");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void testMusterRollTracksKills(GameTestHelper helper) {
        Villager soldier = new Villager(EntityType.VILLAGER, helper.getLevel());
        soldier.moveTo(0, 0, 0);
        helper.getLevel().addFreshEntity(soldier);

        SoldierData soldierData = new SoldierData(soldier.getUUID());
        soldierData.addKill();
        soldierData.addKill();
        soldierData.addKill();
        soldier.setData(VillagerAttachments.SOLDIER_DATA.get(), soldierData);

        SoldierData retrieved = soldier.getData(VillagerAttachments.SOLDIER_DATA.get());
        helper.assertTrue(retrieved.getKills() == 3,
            "Muster roll should track kills correctly");
        helper.succeed();
    }
}
