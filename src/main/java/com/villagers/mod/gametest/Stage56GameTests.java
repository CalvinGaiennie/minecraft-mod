package com.villagers.mod.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.threat.NewMoonRaidService;
import com.villagers.mod.threat.SwarmService;

import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(VillagersMod.MODID)
@PrefixGameTestTemplate(false)
public class Stage56GameTests {
    @GameTest(template = "empty")
    public static void stage5_swarmConstants(GameTestHelper helper) {
        helper.assertValueEqual(0.05, SwarmService.SPAWN_CHANCE, "Swarm nightly chance");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage5_newMoonWindow(GameTestHelper helper) {
        helper.assertTrue(NewMoonRaidService.isDarkMoonNight(0), "Day 0 is dark moon window");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage6_trainingDummyRegistered(GameTestHelper helper) {
        helper.setBlock(net.minecraft.core.BlockPos.ZERO, VillagersMod.TRAINING_DUMMY.get());
        helper.assertBlockPresent(VillagersMod.TRAINING_DUMMY.get(), net.minecraft.core.BlockPos.ZERO);
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void stage6_necromancerWandRegistered(GameTestHelper helper) {
        helper.assertTrue(VillagersMod.NECROMANCER_WAND.isBound(), "Necromancer wand item");
        helper.succeed();
    }
}
