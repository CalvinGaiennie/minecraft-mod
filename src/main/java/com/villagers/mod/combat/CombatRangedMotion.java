package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

/** Hysteresis for archer spacing so navigation is not stop/move every tick at range edges. */
final class CombatRangedMotion {
    /** Blocks closer than this: enter retreat / hold. */
    private static final double CLOSE_ENTER_MARGIN = 0.25;
    /** Blocks farther than min before leaving retreat. */
    private static final double CLOSE_EXIT_MARGIN = 1.25;
    /** Blocks beyond max before chasing. */
    private static final double FAR_ENTER_MARGIN = 0.0;
    /** Blocks inside max before stopping chase. */
    private static final double FAR_EXIT_MARGIN = 3.0;

    private boolean tooClose;
    private boolean tooFar;

    void tickGround(
            PathfinderMob mob,
            LivingEntity target,
            double distBlocks,
            double minRange,
            double maxRange,
            boolean allowChase) {
        tickGround(mob, target, distBlocks, minRange, maxRange, allowChase, false);
    }

    void tickGround(
            PathfinderMob mob,
            LivingEntity target,
            double distBlocks,
            double minRange,
            double maxRange,
            boolean allowChase,
            boolean retreatWhenClose) {
        updateBands(distBlocks, minRange, maxRange);
        if (tooFar) {
            if (allowChase && mob.getNavigation().isDone()) {
                mob.getNavigation().moveTo(target, 0.9);
            }
            return;
        }
        if (tooClose) {
            if (retreatWhenClose) {
                retreatFrom(mob, target);
            } else if (mob.getNavigation().isInProgress()) {
                mob.getNavigation().stop();
            }
            return;
        }
        if (allowChase && mob.getNavigation().isDone()) {
            mob.getNavigation().moveTo(target, 0.65);
        }
    }

    private static void retreatFrom(PathfinderMob mob, LivingEntity target) {
        double dx = mob.getX() - target.getX();
        double dz = mob.getZ() - target.getZ();
        double len = Math.sqrt(dx * dx + dz * dz);
        if (len < 0.01) {
            dx = mob.getRandom().nextDouble() - 0.5;
            dz = mob.getRandom().nextDouble() - 0.5;
            len = Math.sqrt(dx * dx + dz * dz);
        }
        double step = 10.0;
        double gx = mob.getX() + dx / len * step;
        double gz = mob.getZ() + dz / len * step;
        mob.getNavigation().moveTo(gx, mob.getY(), gz, 1.15);
    }

    void tickRampartHold(PathfinderMob mob, LivingEntity target, double distBlocks, double minRange, double maxRange) {
        updateBands(distBlocks, minRange, maxRange);
        if (tooFar && mob.getNavigation().isDone()) {
            return;
        }
        if (tooClose && mob.getNavigation().isInProgress()) {
            mob.getNavigation().stop();
        }
    }

    private void updateBands(double dist, double minRange, double maxRange) {
        double closeEnter = Math.max(0.5, minRange - CLOSE_ENTER_MARGIN);
        double closeExit = minRange + CLOSE_EXIT_MARGIN;
        double farEnter = maxRange + FAR_ENTER_MARGIN;
        double farExit = Math.max(minRange + 1.0, maxRange - FAR_EXIT_MARGIN);

        if (dist < closeEnter) {
            tooClose = true;
        } else if (dist > closeExit) {
            tooClose = false;
        }
        if (dist > farEnter) {
            tooFar = true;
        } else if (dist < farExit) {
            tooFar = false;
        }
    }
}
