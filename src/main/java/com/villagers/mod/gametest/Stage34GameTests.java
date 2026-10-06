package com.villagers.mod.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Items;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.defense.NightwatchGear;
import com.villagers.mod.economy.TaxRolls;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.item.CampaignBootsItem;
import com.villagers.mod.war.BlockadeService;
import com.villagers.mod.war.MutinyService;

import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(VillagersMod.MODID)
@PrefixGameTestTemplate(false)
public class Stage34GameTests {
    @GameTest(template = "empty")
    public static void stage3_havenRegistered(GameTestHelper helper) {
        helper.setBlock(BlockPos.ZERO, VillagersMod.HAVEN_BLOCK.get());
        helper.assertBlockPresent(VillagersMod.HAVEN_BLOCK.get(), BlockPos.ZERO);
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage3_nightwatchHelmetAccepted(GameTestHelper helper) {
        helper.assertTrue(
                VillagerGearRules.accepts(VillagerGearSlot.HELMET, VillagersMod.NIGHTWATCH_HELMET.get().getDefaultInstance()),
                "Nightwatch helmet is valid soldier helmet");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage4_campBlockRegistered(GameTestHelper helper) {
        helper.setBlock(BlockPos.ZERO, VillagersMod.CAMP_BLOCK.get());
        helper.assertBlockPresent(VillagersMod.CAMP_BLOCK.get(), BlockPos.ZERO);
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage4_campaignBootsFlag(GameTestHelper helper) {
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);
        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), new SoldierData(villager.getUUID()));
        VillagerGearRules.set(villager, VillagerGearSlot.BOOTS, VillagersMod.CAMPAIGN_BOOTS.get().getDefaultInstance());
        helper.assertTrue(CampaignBootsItem.isCampaignBoots(
                VillagerGearRules.get(villager, VillagerGearSlot.BOOTS)), "Campaign boots equipped");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage2_taxRollsDefaultBread(GameTestHelper helper) {
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);
        var rolled = TaxRolls.rollForVillager(villager, helper.getLevel().getRandom());
        helper.assertTrue(rolled.is(Items.BREAD), "Tax roll defaults to bread without trades");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage4_blockadeConstants(GameTestHelper helper) {
        helper.assertValueEqual(64, BlockadeService.BLOCKADE_RADIUS, "Blockade radius");
        helper.assertValueEqual(15, BlockadeService.MIN_SIEGE_SOLDIERS, "Siege soldier count");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage4_mutinyThreshold(GameTestHelper helper) {
        helper.assertValueEqual(5, MutinyService.MUTINY_THRESHOLD, "Mutiny threshold");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage3_royalGuardChest(GameTestHelper helper) {
        helper.assertTrue(NightwatchGear.isRoyalGuardChest(
                VillagersMod.ROYAL_GUARD_CHESTPLATE.get().getDefaultInstance()), "Royal guard chestplate");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage3_caltropRegistered(GameTestHelper helper) {
        helper.setBlock(BlockPos.ZERO, VillagersMod.CALTROP_BLOCK.get());
        helper.assertBlockPresent(VillagersMod.CALTROP_BLOCK.get(), BlockPos.ZERO);
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage3_villageProtectionSlowdown(GameTestHelper helper) {
        helper.assertValueEqual(10.0f, com.villagers.mod.village.VillageProtection.breakSlowdownFactor(25), "Max slowdown");
        helper.succeed();
    }
}
