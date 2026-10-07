package com.villagers.mod;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue ARMIES_QUEST_D1_SOLDIER_THRESHOLD = BUILDER
            .comment("Living soldiers (not militia) in claim to qualify for D1")
            .defineInRange("armiesQuestD1SoldierThreshold", 30, 1, 500);

    public static final ModConfigSpec.BooleanValue ARMIES_QUEST_D1_IGNORE_MOON = BUILDER
            .define("armiesQuestD1IgnoreMoon", true);

    public static final ModConfigSpec.BooleanValue ARMIES_QUEST_BOSS_PLAYER_KILL_ONLY = BUILDER
            .define("armiesQuestBossPlayerKillOnly", true);

    public static final ModConfigSpec.DoubleValue TITLE_ROOKBREAKER_DESERTION_MULTIPLIER = BUILDER
            .comment("Multiplier on desertion rolls while owner has Rookbreaker title (0.90 = -10%)")
            .defineInRange("titleRookbreakerDesertionMultiplier", 0.90, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue DESERTER_OUTCOME_VILLAGER = BUILDER
            .defineInRange("deserterOutcomeVillager", 0.50, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue DESERTER_OUTCOME_BANDIT = BUILDER
            .defineInRange("deserterOutcomeBandit", 0.45, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue DESERTER_OUTCOME_NECROMANCER = BUILDER
            .defineInRange("deserterOutcomeNecromancer", 0.05, 0.0, 1.0);

    public static final ModConfigSpec.IntValue BANDIT_CAMP_COUNT = BUILDER
            .defineInRange("banditCampCount", 10, 0, 64);

    public static final ModConfigSpec.IntValue BANDIT_HIDEOUT_COUNT = BUILDER
            .defineInRange("banditHideoutCount", 3, 0, 32);

    public static final ModConfigSpec.IntValue BANDIT_CAMP_RESPAWN_TICKS = BUILDER
            .comment("Garrison respawn delay after clear (pillager-outpost class)")
            .defineInRange("banditCampRespawnTicks", 6000, 600, 72000);

    static final ModConfigSpec SPEC = BUILDER.build();
}
