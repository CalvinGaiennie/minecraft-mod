package com.villagers.mod.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

import com.villagers.mod.armies.ArmiesQuestSavedData;
import com.villagers.mod.armies.ArmiesQuestStage;

public class ArmiesGameTests {
    @GameTest(template = "villagers:empty")
    public static void quest_saved_data_stage(GameTestHelper helper) {
        var level = helper.getLevel();
        var state = ArmiesQuestSavedData.get(level).getOrCreate(java.util.UUID.randomUUID());
        state.stage = ArmiesQuestStage.D1_WAITING_ENTER;
        ArmiesQuestSavedData.get(level).markDirtySelf();
        helper.assertValueEqual(ArmiesQuestStage.D1_WAITING_ENTER, state.stage, "Stage persisted in memory");
        helper.assertValueEqual(0.90, com.villagers.mod.Config.TITLE_ROOKBREAKER_DESERTION_MULTIPLIER.get(), "Rookbreaker default");
        helper.succeed();
    }
}
