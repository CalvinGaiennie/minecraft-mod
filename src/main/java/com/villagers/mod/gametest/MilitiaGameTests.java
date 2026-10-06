package com.villagers.mod.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.combat.MilitiaFlee;
import com.villagers.mod.entity.MilitiaConversion;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

@GameTestHolder(VillagersMod.MODID)
@PrefixGameTestTemplate(false)
public class MilitiaGameTests {

    @GameTest(template = "empty")
    public static void militiaConversion_traderWithWeaponAndArmor(GameTestHelper helper) {
        Villager villager = spawnTrader(helper, BlockPos.ZERO);
        VillagerGearRules.set(villager, VillagerGearSlot.HELMET, new ItemStack(Items.IRON_HELMET));
        VillagerGearRules.set(villager, VillagerGearSlot.MELEE, new ItemStack(Items.IRON_SWORD));

        MilitiaConversion.onGearUpdated(villager);

        helper.assertTrue(villager.hasData(VillagerAttachments.MILITIA_DATA.get()), "Trader with kit should become militia");
        helper.assertFalse(villager.hasData(VillagerAttachments.SOLDIER_DATA.get()), "Traders cannot become soldiers");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void militiaConversion_unemployedWithFullSetupBecomesSoldier(GameTestHelper helper) {
        Stage1GameTestSupport.placeCoreStructures(helper);
        Villager villager = Stage1GameTestSupport.spawnEquippedVillager(helper, BlockPos.ZERO);

        MilitiaConversion.onGearUpdated(villager);

        helper.assertTrue(villager.hasData(VillagerAttachments.SOLDIER_DATA.get()), "Unemployed with full setup should soldier");
        helper.assertFalse(villager.hasData(VillagerAttachments.MILITIA_DATA.get()), "Should not stay militia");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void militiaFlee_disarmsWhenTriggered(GameTestHelper helper) {
        Villager villager = spawnTrader(helper, BlockPos.ZERO);
        VillagerGearRules.set(villager, VillagerGearSlot.HELMET, new ItemStack(Items.IRON_HELMET));
        VillagerGearRules.set(villager, VillagerGearSlot.MELEE, new ItemStack(Items.IRON_SWORD));
        MilitiaConversion.onGearUpdated(villager);
        villager.setHealth(1.0f);

        MilitiaFlee.tick(villager);

        helper.assertFalse(villager.hasData(VillagerAttachments.MILITIA_DATA.get()), "Fleeing militia should disarm");
        helper.assertTrue(VillagerGearRules.get(villager, VillagerGearSlot.MELEE).isEmpty(), "Weapon dropped on flee");
        helper.succeed();
    }

    private static Villager spawnTrader(GameTestHelper helper, BlockPos relativePos) {
        BlockPos pos = helper.absolutePos(relativePos);
        Villager villager = new Villager(EntityType.VILLAGER, helper.getLevel());
        villager.setVillagerData(
                villager.getVillagerData().setProfession(VillagerProfession.ARMORER).setType(VillagerType.PLAINS));
        villager.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
        helper.getLevel().addFreshEntity(villager);
        return villager;
    }
}
