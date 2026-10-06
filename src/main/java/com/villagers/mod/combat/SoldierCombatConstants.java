package com.villagers.mod.combat;

public final class SoldierCombatConstants {
    /** Hostiles within this range (with line of sight) wake soldiers into combat. */
    public static final double WAKE_HOSTILE_RANGE = 64.0;
    /**
     * Hold ground: only engage hostiles this close to the guard anchor (nearest rampart, else assigned bed).
     * Matches wake range so walls can cover the same ground players expect from “something’s near.”
     */
    public static final double HOLD_GROUND_ENGAGE_RADIUS = 48.0;
    /** Hold ground: drop chase and return toward anchor if the soldier runs farther than this from it. */
    public static final double HOLD_GROUND_MAX_CHASE = 64.0;
    /** Switch to bow at or beyond this distance (blocks) on open ground. */
    public static final double RANGED_PREFER_MIN_DISTANCE = 8.0;
    /** Minimum horizontal range when shooting from a rampart (wall-huggers are often closer). */
    public static final double RAMPART_RANGED_MIN_DISTANCE = 2.0;
    /** Switch to melee at or below this distance (blocks). */
    public static final double MELEE_PREFER_MAX_DISTANCE = 6.0;
    /** Max bow shot distance (blocks). */
    public static final double RANGED_ATTACK_MAX_DISTANCE = 32.0;
    /** Home post bed radius for home bonus and holding ground (no retreat). */
    public static final double HOME_BED_RADIUS = 128.0;
    /** Veterans with a veteran's sword defend threats within this range of home bed. */
    public static final double VETERAN_DEFEND_RADIUS = 128.0;
    /** Retreat to fallback below this health fraction when outside home radius. */
    public static final float RETREAT_HEALTH_FRACTION = 0.30f;
    /** Enemies within this range block post bed healing (Stage 3 combat rule). */
    public static final double OUT_OF_COMBAT_ENEMY_RANGE = 16.0;

    private SoldierCombatConstants() {
    }
}
