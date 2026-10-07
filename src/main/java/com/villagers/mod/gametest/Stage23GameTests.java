package com.villagers.mod.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Items;
import com.villagers.mod.VillagersMod;

import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import com.villagers.mod.block.entity.ArrowBinBlockEntity;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.block.entity.SupplyDepotBlockEntity;
import com.villagers.mod.economy.SupplyRouting;
import com.villagers.mod.combat.SoldierCombat;
import com.villagers.mod.combat.SoldierGearStats;
import com.villagers.mod.combat.SoldierCombatConstants;
import com.villagers.mod.combat.SoldierRanks;
import com.villagers.mod.economy.TaxCoverage;
import com.villagers.mod.village.VillageManager;
import com.villagers.mod.village.VillageZone;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerPersonalNames;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.entity.VeteranBedSync;
import com.villagers.mod.entity.VeteranData;
import com.villagers.mod.entity.ZombieSoldierCure;

@GameTestHolder(VillagersMod.MODID)
@PrefixGameTestTemplate(false)
public class Stage23GameTests {
    @GameTest(template = "empty")
    public static void combat_homeBedRadius128(GameTestHelper helper) {
        BlockPos origin = helper.absolutePos(BlockPos.ZERO);
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);
        SoldierData data = new SoldierData(villager.getUUID());
        BlockPos bed = origin.offset(100, 0, 0);
        data.setAssignedPostBed(bed);
        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), data);
        villager.teleportTo(bed.getX() + 20, bed.getY(), bed.getZ());
        helper.assertTrue(SoldierCombat.isWithinHomeBedRadius(villager), "Within 128 blocks of post bed");
        villager.teleportTo(bed.getX() + 150, bed.getY(), bed.getZ());
        helper.assertFalse(SoldierCombat.isWithinHomeBedRadius(villager), "Outside 128 blocks of post bed");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void soldierLabels_skipDefaultVillagerName(GameTestHelper helper) {
        helper.assertValueEqual("Soldier", VillagerPersonalNames.soldierLabel("Soldier", "Villager", 0), "No kills");
        helper.assertValueEqual(
                "Soldier (2 kills)",
                VillagerPersonalNames.soldierLabel("Soldier", "Villager", 2),
                "Legacy Villager name omitted");
        helper.assertValueEqual(
                "Seasoned Steve (5 kills)",
                VillagerPersonalNames.soldierLabel("Seasoned", "Steve", 5),
                "Custom name kept");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void ranks_seasonedAtFiveKills(GameTestHelper helper) {
        helper.assertTrue(SoldierRanks.rankForKills(5) == SoldierRanks.Rank.SEASONED, "Five kills is seasoned");
        helper.assertTrue(SoldierRanks.rankForKills(12) == SoldierRanks.Rank.SERGEANT, "Twelve kills is sergeant");
        helper.assertTrue(SoldierRanks.rankForKills(20) == SoldierRanks.Rank.HERO, "Twenty kills is hero");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void taxCoverage_fivePerSoldier(GameTestHelper helper) {
        helper.assertValueEqual(15, TaxCoverage.maxTaxableVillagers(3), "Three soldiers cover fifteen villagers");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void taxZone_default128WithoutClaim(GameTestHelper helper) {
        BlockPos box = helper.absolutePos(BlockPos.ZERO);
        helper.assertValueEqual(
                VillageZone.DEFAULT_TAX_RADIUS,
                VillageZone.primaryTaxRadius(helper.getLevel(), box),
                "Wilderness tax box uses 128-block zone");
        helper.assertValueEqual(128, VillageManager.radiusForPopulation(40), "City tier is 128");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void supply_depotRoutesToRecruiterBox(GameTestHelper helper) {
        helper.setBlock(new BlockPos(2, 1, 0), VillagersMod.RECRUITER_BOX.get());
        helper.setBlock(new BlockPos(0, 1, 0), VillagersMod.SUPPLY_DEPOT.get());
        var depotBe = helper.getLevel().getBlockEntity(helper.absolutePos(new BlockPos(0, 1, 0)));
        var recruiterBe = helper.getLevel().getBlockEntity(helper.absolutePos(new BlockPos(2, 1, 0)));
        helper.assertTrue(depotBe instanceof SupplyDepotBlockEntity, "Supply depot block entity");
        helper.assertTrue(recruiterBe instanceof RecruiterBlockEntity, "Recruiter block entity");
        SupplyDepotBlockEntity depot = (SupplyDepotBlockEntity) depotBe;
        RecruiterBlockEntity recruiter = (RecruiterBlockEntity) recruiterBe;
        depot.tickWeeklySupply(1L);
        helper.assertTrue(recruiter.getItem(0).is(Items.IRON_SWORD), "Sword routed to recruiter");
        helper.assertTrue(recruiter.getItem(1).is(Items.BOW), "Bow routed to recruiter");
        helper.assertTrue(recruiter.getItem(2).is(Items.IRON_CHESTPLATE), "Chestplate routed to recruiter");
        helper.assertTrue(depot.getItem(0).isEmpty(), "Depot stays empty when recruiter present");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void gear_ironSwordAttackDamage(GameTestHelper helper) {
        double swordDamage = SoldierGearStats.attackDamageFromStack(Items.IRON_SWORD.getDefaultInstance());
        helper.assertTrue(swordDamage >= 5.0 && swordDamage <= 7.0, "Iron sword uses item attack damage");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void recruiter_capacityTen(GameTestHelper helper) {
        helper.assertValueEqual(10, RecruiterBlockEntity.MAX_RECRUITS, "Recruiter capacity");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void recruiter_villagerPullsEnlistGear(GameTestHelper helper) {
        helper.setBlock(new BlockPos(2, 1, 0), VillagersMod.RECRUITER_BOX.get());
        var recruiterBe = helper.getLevel().getBlockEntity(helper.absolutePos(new BlockPos(2, 1, 0)));
        helper.assertTrue(recruiterBe instanceof RecruiterBlockEntity, "Recruiter block entity");
        RecruiterBlockEntity recruiter = (RecruiterBlockEntity) recruiterBe;
        recruiter.setItem(0, new net.minecraft.world.item.ItemStack(Items.IRON_HELMET));
        recruiter.setItem(1, new net.minecraft.world.item.ItemStack(Items.IRON_CHESTPLATE));
        recruiter.setItem(2, new net.minecraft.world.item.ItemStack(Items.IRON_SWORD));
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);
        for (com.villagers.mod.gear.VillagerGearSlot slot : com.villagers.mod.gear.VillagerGearSlot.values()) {
            com.villagers.mod.gear.VillagerGearRules.set(villager, slot, net.minecraft.world.item.ItemStack.EMPTY);
        }
        com.villagers.mod.gear.GearReplacement.tryPullEnlistGear(villager, recruiter);
        helper.assertFalse(
                com.villagers.mod.gear.VillagerGearRules.get(villager, com.villagers.mod.gear.VillagerGearSlot.HELMET).isEmpty(),
                "Helmet pulled from recruiter");
        helper.assertFalse(
                com.villagers.mod.gear.VillagerGearRules.get(villager, com.villagers.mod.gear.VillagerGearSlot.CHESTPLATE).isEmpty(),
                "Chestplate pulled");
        helper.assertTrue(
                com.villagers.mod.gear.VillagerGearRules.get(villager, com.villagers.mod.gear.VillagerGearSlot.MELEE).is(Items.IRON_SWORD),
                "Sword pulled");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void arrowBin_acceptsArrowsOnly(GameTestHelper helper) {
        helper.assertTrue(ArrowBinBlockEntity.accepts(Items.ARROW.getDefaultInstance()), "Accepts arrows");
        helper.assertFalse(ArrowBinBlockEntity.accepts(Items.BREAD.getDefaultInstance()), "Rejects bread");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void veteran_defendUses128RadiusConstant(GameTestHelper helper) {
        helper.assertValueEqual(128.0, SoldierCombatConstants.VETERAN_DEFEND_RADIUS, "Veteran defend radius");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void combat_wakeUses64RadiusConstant(GameTestHelper helper) {
        helper.assertValueEqual(64.0, SoldierCombatConstants.WAKE_HOSTILE_RANGE, "Wake hostile range");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void combat_holdGroundConstants(GameTestHelper helper) {
        helper.assertValueEqual(48.0, SoldierCombatConstants.HOLD_GROUND_ENGAGE_RADIUS, "Engage radius");
        helper.assertValueEqual(64.0, SoldierCombatConstants.HOLD_GROUND_MAX_CHASE, "Max chase");
        helper.assertTrue(
                SoldierCombatConstants.RANGED_PREFER_MIN_DISTANCE > SoldierCombatConstants.MELEE_PREFER_MAX_DISTANCE,
                "Ranged band above melee band");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void zombieCure_oddsRoll(GameTestHelper helper) {
        int soldiers = 0;
        for (int i = 0; i < 1000; i++) {
            if (ZombieSoldierCure.roll(helper.getLevel().getRandom()) == ZombieSoldierCure.RETURN_SOLDIER) {
                soldiers++;
            }
        }
        helper.assertTrue(soldiers > 500 && soldiers < 700, "Cure roll roughly 60% soldier");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void fallback_blockRegistered(GameTestHelper helper) {
        helper.setBlock(BlockPos.ZERO, VillagersMod.FALLBACK_BLOCK.get());
        helper.assertBlockPresent(VillagersMod.FALLBACK_BLOCK.get(), BlockPos.ZERO);
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void veteran_engagesInsideHomeRadius(GameTestHelper helper) {
        helper.setBlock(BlockPos.ZERO, net.minecraft.world.level.block.Blocks.RED_BED);
        BlockPos origin = helper.absolutePos(BlockPos.ZERO);
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);
        VeteranData veteran = new VeteranData(3, "Soldier");
        villager.setData(VillagerAttachments.VETERAN_DATA.get(), veteran);
        villager.getBrain().setMemory(
                net.minecraft.world.entity.ai.memory.MemoryModuleType.HOME,
                net.minecraft.core.GlobalPos.of(helper.getLevel().dimension(), origin));
        VeteranBedSync.syncHomeBedFromVillager(villager);
        villager.setItemSlot(
                net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                VillagersMod.VETERAN_SWORD.get().getDefaultInstance());
        helper.assertTrue(SoldierCombat.shouldEngageHostiles(villager), "Veteran with sword at home should fight");
        villager.teleportTo(200, 1, 200);
        helper.assertFalse(SoldierCombat.shouldEngageHostiles(villager), "Veteran far from home bed is civilian");
        helper.succeed();
    }
}
