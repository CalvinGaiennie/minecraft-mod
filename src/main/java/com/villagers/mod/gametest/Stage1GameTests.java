package com.villagers.mod.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.SoldierBehavior;
import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.SoldierDesertion;
import com.villagers.mod.entity.VeteranData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.util.MusterRollHelper;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

import java.util.UUID;

@GameTestHolder(VillagersMod.MODID)
@PrefixGameTestTemplate(false)
public class Stage1GameTests {

    @GameTest(template = "empty")
    public static void soldierConversion_requiresAllRequirements(GameTestHelper helper) {
        Stage1GameTestSupport.placeCoreStructures(helper);
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);

        helper.assertTrue(SoldierConversion.canBecomeSoldier(villager), "Fully equipped villager should qualify");
        helper.assertTrue(SoldierConversion.tryConvert(villager), "Conversion should succeed");
        helper.assertTrue(villager.hasData(VillagerAttachments.SOLDIER_DATA.get()), "Soldier attachment should exist");
        helper.assertTrue(villager.getCustomName() != null, "Soldier label should sync to clients via custom name");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void soldierConversion_missingGearFails(GameTestHelper helper) {
        Stage1GameTestSupport.placeCoreStructures(helper);
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, ItemStack.EMPTY);

        helper.assertFalse(SoldierConversion.canBecomeSoldier(villager), "Missing weapon should block conversion");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void desertion_homelessThreeNights(GameTestHelper helper) {
        SoldierData data = new SoldierData(UUID.randomUUID());
        data.setHomelessNights(2);
        helper.assertFalse(SoldierDesertion.wouldDesertFromHomelessness(data), "Two nights is not enough");
        data.setHomelessNights(3);
        helper.assertTrue(SoldierDesertion.wouldDesertFromHomelessness(data), "Three nights should desert");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void desertion_hungryDaySeven(GameTestHelper helper) {
        SoldierData data = new SoldierData(UUID.randomUUID());
        data.setLastFoodDay(0);
        helper.assertFalse(SoldierDesertion.wouldDesertFromHunger(data, 6, false), "Day six without food is not enough");
        helper.assertTrue(SoldierDesertion.wouldDesertFromHunger(data, 7, false), "Day seven without food should desert");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void discharge_keepsKills(GameTestHelper helper) {
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);
        SoldierData data = new SoldierData(villager.getUUID());
        data.setKills(12);
        data.setRank("Seasoned");
        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), data);

        SoldierConversion.dischargeHonorable(villager);

        helper.assertFalse(villager.hasData(VillagerAttachments.SOLDIER_DATA.get()), "Soldier data should be removed");
        helper.assertTrue(villager.hasData(VillagerAttachments.VETERAN_DATA.get()), "Veteran record should remain");
        VeteranData veteran = villager.getData(VillagerAttachments.VETERAN_DATA.get());
        helper.assertValueEqual(12, veteran.getKills(), "Kills should persist after discharge");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void village_noOverlappingClaims(GameTestHelper helper) {
        VillageManager.clearAllForTests();
        UUID ownerA = UUID.randomUUID();
        UUID ownerB = UUID.randomUUID();

        VillageData villageA = new VillageData("A", ownerA, 0, 0, 0);
        villageA.setRadius(VillageManager.HAMLET_RADIUS);
        VillageManager.get(helper.getLevel()).registerVillage(villageA);

        helper.assertFalse(
                VillageManager.get(helper.getLevel()).canClaim(ownerB, 10, 0, VillageManager.HAMLET_RADIUS),
                "Second player should not claim overlapping land");
        helper.assertTrue(
                VillageManager.get(helper.getLevel()).canClaim(ownerB, 100, 0, VillageManager.HAMLET_RADIUS),
                "Distant claim should be allowed");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void musterRoll_countsMatch(GameTestHelper helper) {
        Villager first = Stage1GameTestSupport.spawnEquippedVillager(helper, new BlockPos(1, 0, 0));
        Villager second = Stage1GameTestSupport.spawnEquippedVillager(helper, new BlockPos(-1, 0, 0));

        SoldierData firstData = new SoldierData(first.getUUID());
        firstData.addKill();
        firstData.addKill();
        first.setData(VillagerAttachments.SOLDIER_DATA.get(), firstData);

        SoldierData secondData = new SoldierData(second.getUUID());
        secondData.addKill();
        second.setData(VillagerAttachments.SOLDIER_DATA.get(), secondData);

        MusterRollHelper.Summary summary =
                MusterRollHelper.summarizeNear(helper.getLevel(), helper.absolutePos(BlockPos.ZERO), 64);
        helper.assertValueEqual(2, summary.soldierCount(), "Muster roll soldier count");
        helper.assertValueEqual(3, summary.totalKills(), "Muster roll kill total");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void awakenHorn_setsAwakenFlag(GameTestHelper helper) {
        Villager soldier = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);
        SoldierData data = new SoldierData(soldier.getUUID());
        soldier.setData(VillagerAttachments.SOLDIER_DATA.get(), data);

        SoldierBehavior.signalAwaken(soldier, helper.getLevel().getGameTime());
        helper.assertTrue(
                soldier.getData(VillagerAttachments.SOLDIER_DATA.get()).getAwakenUntilGameTime()
                        > helper.getLevel().getGameTime(),
                "Awaken horn should set awaken window");
        helper.succeed();
    }
}
