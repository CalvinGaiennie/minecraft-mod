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
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.squad.SquadService;
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
    public static void stage3_squadNightwatchToggle(GameTestHelper helper) {
        BlockPos boxPos = BlockPos.ZERO;
        helper.setBlock(boxPos, VillagersMod.RECRUITER_BOX.get());
        var be = helper.getLevel().getBlockEntity(boxPos);
        helper.assertTrue(be instanceof RecruiterBlockEntity, "Squad box present");
        ((RecruiterBlockEntity) be).setNightwatchEnabled(true);
        helper.assertTrue(SquadService.squadNightwatch(helper.getLevel(), boxPos), "Squad nightwatch enabled");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage4_campBlockRegistered(GameTestHelper helper) {
        helper.setBlock(BlockPos.ZERO, VillagersMod.CAMP_BLOCK.get());
        helper.assertBlockPresent(VillagersMod.CAMP_BLOCK.get(), BlockPos.ZERO);
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage4_squadCampaignToggle(GameTestHelper helper) {
        BlockPos boxPos = BlockPos.ZERO;
        helper.setBlock(boxPos, VillagersMod.RECRUITER_BOX.get());
        var be = helper.getLevel().getBlockEntity(boxPos);
        helper.assertTrue(be instanceof RecruiterBlockEntity, "Squad box present");
        RecruiterBlockEntity squadBox = (RecruiterBlockEntity) be;
        squadBox.setSquadOnCampaign(true);
        helper.assertTrue(squadBox.isSquadOnCampaign(), "Squad campaign flag");
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

    @GameTest(template = "empty")
    public static void squadBox_assignRampartViaWritLogic(GameTestHelper helper) {
        BlockPos boxPos = new BlockPos(0, 1, 0);
        BlockPos rampartPos = new BlockPos(4, 1, 0);
        helper.setBlock(boxPos, VillagersMod.RECRUITER_BOX.get());
        helper.setBlock(rampartPos, VillagersMod.RAMPART.get());
        helper.assertTrue(
                SquadService.assignRampartToBox(helper.getLevel(), helper.absolutePos(boxPos), helper.absolutePos(rampartPos)),
                "Rampart links to squad box");
        var be = helper.getLevel().getBlockEntity(helper.absolutePos(boxPos));
        helper.assertTrue(be instanceof RecruiterBlockEntity, "Squad box block entity");
        helper.assertValueEqual(1, ((RecruiterBlockEntity) be).getSquadRamparts().size(), "One linked rampart");
        helper.assertFalse(
                SquadService.assignRampartToBox(helper.getLevel(), helper.absolutePos(boxPos), helper.absolutePos(rampartPos)),
                "Duplicate rampart rejected");
        helper.succeed();
    }
}
