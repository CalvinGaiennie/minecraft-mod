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

    public static final ModConfigSpec.DoubleValue ARMIES_QUEST_D2_GARLAND_FLEE_BANDIT_LOSS_FRACTION = BUILDER
            .comment("Garland flees D2 when this fraction of his assault bandits (not counting him) are dead")
            .defineInRange("armiesQuestD2GarlandFleeBanditLossFraction", 0.65, 0.1, 1.0);

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

    public static final ModConfigSpec.IntValue CORVIN_CAMP_MIN_DISTANCE = BUILDER
            .defineInRange("corvinCampMinDistance", 800, 256, 8000);

    public static final ModConfigSpec.IntValue CORVIN_CAMP_MAX_DISTANCE = BUILDER
            .defineInRange("corvinCampMaxDistance", 2200, 512, 10000);

    public static final ModConfigSpec.IntValue GARLAND_CAMP_MIN_DISTANCE = BUILDER
            .defineInRange("garlandCampMinDistance", 2200, 512, 12000);

    public static final ModConfigSpec.IntValue GARLAND_CAMP_MAX_DISTANCE = BUILDER
            .defineInRange("garlandCampMaxDistance", 4000, 1024, 16000);

    public static final ModConfigSpec.IntValue QUEST_CAMP_MIN_SEPARATION = BUILDER
            .comment("Corvin and Garland camps must be at least this far apart")
            .defineInRange("questCampMinSeparation", 1200, 512, 8000);

    public static final ModConfigSpec.IntValue CITADEL_SHADOW_EXTRA_CAMPS = BUILDER
            .comment("Extra bandit camps placed on the citadel approach ring (Armies-only uses spawn-relative ring)")
            .defineInRange("citadelShadowExtraCamps", 3, 0, 12);

    public static final ModConfigSpec.IntValue CITADEL_APPROACH_DISTANCE = BUILDER
            .comment("Citadel shadow camp ring radius from citadel center — just inside max view distance so the citadel is barely visible")
            .defineInRange("citadelApproachDistance", 480, 128, 512);

    public static final ModConfigSpec.IntValue CITADEL_SHADOW_RING_JITTER = BUILDER
            .comment("Random +/- blocks added to citadelApproachDistance for each shadow camp")
            .defineInRange("citadelShadowRingJitter", 24, 0, 64);

    public static final ModConfigSpec.IntValue WANDERING_NECROMANCER_GRUDGE_DELAY_DAYS = BUILDER
            .comment("In-game days after desert before the first grudge strike can roll (~30 = one month)")
            .defineInRange("wanderingNecromancerGrudgeDelayDays", 30, 0, 120);

    public static final ModConfigSpec.IntValue WANDERING_NECROMANCER_STRIKE_INTERVAL_DAYS = BUILDER
            .comment("In-game days between grudge strikes from a living wandering necromancer")
            .defineInRange("wanderingNecromancerStrikeIntervalDays", 2, 1, 14);

    public static final ModConfigSpec.IntValue NATURAL_VILLAGE_BELL_RADIUS = BUILDER
            .comment("Horizontal radius around a bell for natural village militia")
            .defineInRange("naturalVillageBellRadius", 48, 16, 128);

    public static final ModConfigSpec.IntValue NATURAL_MILITIA_BASE_COUNT = BUILDER
            .defineInRange("naturalMilitiaBaseCount", 2, 0, 8);

    public static final ModConfigSpec.IntValue NATURAL_MILITIA_NEAR_SPAWN_COUNT = BUILDER
            .comment("Militia count when citadel anchor is set and bell is within nearSpawnDistance of world spawn")
            .defineInRange("naturalMilitiaNearSpawnCount", 4, 0, 12);

    public static final ModConfigSpec.IntValue NATURAL_MILITIA_MID_COUNT = BUILDER
            .defineInRange("naturalMilitiaMidCount", 3, 0, 12);

    public static final ModConfigSpec.IntValue NATURAL_MILITIA_NEAR_SPAWN_DISTANCE = BUILDER
            .defineInRange("naturalMilitiaNearSpawnDistance", 1000, 256, 8000);

    public static final ModConfigSpec.IntValue NATURAL_MILITIA_MID_DISTANCE = BUILDER
            .defineInRange("naturalMilitiaMidDistance", 3000, 512, 12000);

    public static final ModConfigSpec.BooleanValue NATURAL_MILITIA_USE_CITADEL_TIER_COUNTS = BUILDER
            .comment("When false, always use naturalMilitiaBaseCount regardless of spawn distance")
            .define("naturalMilitiaUseCitadelTierCounts", true);

    public static final ModConfigSpec.DoubleValue ORPHAN_SOLDIER_BONUS_HEALTH = BUILDER
            .defineInRange("orphanSoldierBonusHealth", 4.0, 0.0, 40.0);

    public static final ModConfigSpec.DoubleValue ORPHAN_SOLDIER_BONUS_DAMAGE = BUILDER
            .comment("Fraction added to melee/ranged damage (0.15 = +15%)")
            .defineInRange("orphanSoldierBonusDamage", 0.15, 0.0, 2.0);

    public static final ModConfigSpec.DoubleValue ORPHAN_DESERTION_MULTIPLIER = BUILDER
            .comment("Multiplies desertion likelihood for orphan-raised soldiers/militia (>1 = desert more often)")
            .defineInRange("orphanDesertionMultiplier", 1.5, 1.0, 4.0);

    static final ModConfigSpec SPEC = BUILDER.build();
}
