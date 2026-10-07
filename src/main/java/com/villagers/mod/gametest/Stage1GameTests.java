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
import com.villagers.mod.block.entity.MessStationBlockEntity;
import com.villagers.mod.bed.SoldierBedClaims;
import com.villagers.mod.bed.VanillaBedHelper;
import com.villagers.mod.util.SoldierStructureHelper;
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
    public static void soldierBed_freesWhenSoldierDies(GameTestHelper helper) {
        Stage1GameTestSupport.placeCoreStructures(helper);
        BlockPos bedRel = new BlockPos(0, 1, 1);
        BlockPos bedAbs = helper.absolutePos(bedRel);
        BlockPos foot = VanillaBedHelper.footPosition(helper.getLevel(), bedAbs);
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);
        helper.assertTrue(SoldierConversion.tryConvert(villager), "Conversion should succeed");
        helper.assertTrue(
                SoldierBedClaims.get(helper.getLevel()).getSoldierAt(foot) != null,
                "Vanilla bed should be claimed");

        villager.hurt(helper.getLevel().damageSources().genericKill(), Float.MAX_VALUE);
        helper.assertTrue(!villager.isAlive(), "Soldier should die");
        helper.assertTrue(
                SoldierBedClaims.get(helper.getLevel()).getSoldierAt(foot) == null,
                "Bed claim should clear after death");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void soldierConversion_noVanillaBedFails(GameTestHelper helper) {
        helper.setBlock(new BlockPos(1, 1, 0), VillagersMod.MESS_STATION.get());
        Stage1GameTestSupport.stockMessStation(helper, new BlockPos(1, 1, 0));
        helper.setBlock(new BlockPos(0, 1, 1), VillagersMod.POST_BLOCK.get());
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);

        var reason = SoldierConversion.blockReason(villager);
        helper.assertTrue(
                reason != null && reason.equals(net.minecraft.network.chat.Component.translatable(
                        "message.villagers.convert_need_bed")),
                "Post block alone does not count as a bed");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void soldierConversion_emptyMessFails(GameTestHelper helper) {
        helper.setBlock(new BlockPos(1, 1, 0), VillagersMod.MESS_STATION.get());
        Stage1GameTestSupport.placeRedBed(helper, new BlockPos(0, 1, 1));
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);

        helper.assertFalse(SoldierConversion.canBecomeSoldier(villager), "Empty mess should block conversion");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void messStation_hasBlockEntityWhenPlaced(GameTestHelper helper) {
        helper.setBlock(new BlockPos(0, 1, 0), VillagersMod.MESS_STATION.get());
        var be = helper.getLevel().getBlockEntity(helper.absolutePos(new BlockPos(0, 1, 0)));
        helper.assertTrue(be instanceof MessStationBlockEntity, "Placed mess station should have block entity");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void messStation_acceptsSoldierFood(GameTestHelper helper) {
        helper.setBlock(new BlockPos(0, 1, 0), VillagersMod.MESS_STATION.get());
        var be = helper.getLevel().getBlockEntity(helper.absolutePos(new BlockPos(0, 1, 0)));
        helper.assertTrue(be instanceof MessStationBlockEntity, "Mess block entity should exist");
        MessStationBlockEntity mess = (MessStationBlockEntity) be;
        helper.assertTrue(mess.tryInsertOne(new ItemStack(Items.BREAD)), "Bread should insert");
        helper.assertTrue(mess.hasSoldierFood(), "Mess should count as stocked");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void messStation_rejectsNonFood(GameTestHelper helper) {
        helper.setBlock(new BlockPos(0, 1, 0), VillagersMod.MESS_STATION.get());
        var be = helper.getLevel().getBlockEntity(helper.absolutePos(new BlockPos(0, 1, 0)));
        MessStationBlockEntity mess = (MessStationBlockEntity) be;
        helper.assertFalse(mess.tryInsertOne(new ItemStack(Items.IRON_INGOT)), "Non-food should not insert");
        helper.assertFalse(mess.hasSoldierFood(), "Mess should stay empty");
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
        VillageManager.get(helper.getLevel()).registerVillage(villageA, helper.getLevel());

        helper.assertFalse(
                VillageManager.get(helper.getLevel()).canClaim(ownerB, 10, 0, VillageManager.HAMLET_RADIUS),
                "Second player should not claim overlapping land");
        helper.assertTrue(
                VillageManager.get(helper.getLevel()).canClaim(ownerB, 100, 0, VillageManager.HAMLET_RADIUS),
                "Distant claim should be allowed");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void musterRoll_ownedVillageRollup(GameTestHelper helper) {
        VillageManager.clearForLevel(helper.getLevel());
        UUID owner = UUID.randomUUID();
        VillageData village = new VillageData("North Hold", owner, 0, 64, 0);
        village.setRadius(VillageManager.HAMLET_RADIUS);
        VillageManager.get(helper.getLevel()).registerVillage(village, helper.getLevel());

        helper.assertValueEqual(
                1,
                VillageManager.get(helper.getLevel()).getVillagesOwnedBy(owner).size(),
                "Owner should have one village");
        MusterRollHelper.Summary inVillage = MusterRollHelper.summarizeInVillage(helper.getLevel(), village);
        helper.assertValueEqual(0, inVillage.soldierCount(), "No soldiers until enlisted");
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
