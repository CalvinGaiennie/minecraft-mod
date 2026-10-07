package com.villagers.mod.armies;

public enum ArmiesQuestStage {
    NOT_STARTED,
    /** First village hit soldier threshold; waiting for owner to enter claim. */
    D1_WAITING_ENTER,
    D1_ACTIVE,
    /** Halvek dead; ledger dropped, O1 not opened yet. */
    O1_LEDGER,
    /** Corvin camp is the goal. */
    O1_ACTIVE,
    /** Corvin dead; D2 will trigger on largest village enter. */
    D2_ARMED,
    D2_ACTIVE,
    /** Garland fled D2; need map + ledger combine. */
    O2_HUNT,
    O2_ACTIVE,
    COMPLETE
}
