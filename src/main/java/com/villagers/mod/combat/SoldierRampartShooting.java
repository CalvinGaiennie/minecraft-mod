package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.RampartBlock;
import com.villagers.mod.util.SoldierStructureHelper;

import org.jetbrains.annotations.Nullable;

/** Finds rampart walk positions with line of sight instead of pathing down to ground-level targets. */
public final class SoldierRampartShooting {
    private SoldierRampartShooting() {
    }

    public static boolean isRampartAnchor(Level level, BlockPos anchor) {
        return level.getBlockState(anchor).getBlock() instanceof RampartBlock;
    }

    public static boolean prefersWallArchery(Villager soldier, ServerLevel level, @Nullable BlockPos anchor) {
        if (anchor != null && isRampartAnchor(level, anchor)) {
            return true;
        }
        return SoldierStructureHelper.findNearestRampart(level, soldier.blockPosition()).isPresent();
    }

    public static double horizontalDistanceSqr(LivingEntity from, LivingEntity to) {
        double dx = from.getX() - to.getX();
        double dz = from.getZ() - to.getZ();
        return dx * dx + dz * dz;
    }

    public static Vec3 standOnRampart(BlockPos rampartPos) {
        return new Vec3(rampartPos.getX() + 0.5, rampartPos.getY() + 1, rampartPos.getZ() + 0.5);
    }

    public static boolean shouldUseRampartArchery(Villager soldier, LivingEntity target) {
        if (!SoldierCombatArms.hasRangedReady(soldier)) {
            return false;
        }
        if (!(soldier.level() instanceof ServerLevel level)) {
            return false;
        }
        BlockPos anchor = SoldierCombatArms.resolveCombatAnchor(soldier);
        return prefersWallArchery(soldier, level, anchor);
    }

    public static boolean isOnRampart(Villager soldier) {
        BlockPos feet = BlockPos.containing(soldier.getX(), soldier.getY() - 0.1, soldier.getZ());
        return soldier.level().getBlockState(feet.below()).is(VillagersMod.RAMPART.get());
    }

    /** Projectile-style line of fire from the soldier's current position (stricter than mob LOS). */
    public static boolean hasRampartLineOfFire(Villager soldier, LivingEntity target) {
        if (!(soldier.level() instanceof ServerLevel level)) {
            return false;
        }
        return hasClearShot(level, soldier, soldier.getEyePosition(), target);
    }

    /**
     * Path onto the wall and toward a tile with line of fire. Returns true if navigation was started
     * (caller should not ground-chase the target).
     */
    public static boolean tryPathToRampartForArchery(Mob mob, Villager soldier, LivingEntity target) {
        if (!(soldier.level() instanceof ServerLevel level)) {
            return false;
        }
        BlockPos anchor = SoldierCombatArms.resolveCombatAnchor(soldier);
        if (pathToShootPosition(mob, soldier, target, anchor)) {
            return true;
        }
        if (!isOnRampart(soldier)) {
            RampartPathing.findBestRampartToStandOn(soldier, RampartPathing.RAMPART_SEEK_RADIUS)
                    .ifPresent(rampart -> RampartPathing.navigateToStand(
                            soldier, standOnRampart(rampart), 0.85));
            return true;
        }
        return false;
    }

    /** Path along the wall to a rampart tile that can see the target; returns true if a move was issued. */
    public static boolean pathToShootPosition(Mob mob, Villager soldier, LivingEntity target, BlockPos anchor) {
        if (!(soldier.level() instanceof ServerLevel level)) {
            return false;
        }
        BlockPos best = findBestShootPosition(level, soldier, target, anchor);
        if (best == null) {
            return false;
        }
        RampartClaims.get(level).claim(level, best, soldier.getUUID());
        RampartPathing.navigateToStand(soldier, standOnRampart(best), 0.75);
        return true;
    }

    @Nullable
    private static BlockPos findBestShootPosition(ServerLevel level, Villager soldier, LivingEntity target, BlockPos anchor) {
        int r = (int) SoldierCombatConstants.HOLD_GROUND_ENGAGE_RADIUS;
        BlockPos center = isRampartAnchor(level, anchor) ? anchor : soldier.blockPosition();
        BlockPos best = null;
        double bestScore = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-r, -8, -r), center.offset(r, 8, r))) {
            if (!level.getBlockState(pos).is(VillagersMod.RAMPART.get())) {
                continue;
            }
            if (!canStandOn(level, pos)) {
                continue;
            }
            if (!RampartClaims.get(level).isAvailableFor(level, pos, soldier.getUUID())) {
                continue;
            }
            Vec3 eye = standOnRampart(pos).add(0, soldier.getEyeHeight() - 1, 0);
            if (!hasClearShot(level, soldier, eye, target)) {
                continue;
            }
            double horizToTarget = horizontalDistanceSqrAt(pos, target);
            if (horizToTarget > SoldierCombatConstants.RANGED_ATTACK_MAX_DISTANCE * SoldierCombatConstants.RANGED_ATTACK_MAX_DISTANCE) {
                continue;
            }
            double score = soldier.blockPosition().distSqr(pos) + horizToTarget * 0.05;
            if (score < bestScore) {
                bestScore = score;
                best = pos.immutable();
            }
        }
        return best;
    }

    private static double horizontalDistanceSqrAt(BlockPos rampart, LivingEntity target) {
        double dx = (rampart.getX() + 0.5) - target.getX();
        double dz = (rampart.getZ() + 0.5) - target.getZ();
        return dx * dx + dz * dz;
    }

    private static boolean hasClearShot(ServerLevel level, Villager soldier, Vec3 from, LivingEntity target) {
        return level.clip(new net.minecraft.world.level.ClipContext(
                from, target.getEyePosition(), net.minecraft.world.level.ClipContext.Block.COLLIDER,
                net.minecraft.world.level.ClipContext.Fluid.NONE, soldier)).getType()
                == net.minecraft.world.phys.HitResult.Type.MISS;
    }

    private static boolean canStandOn(ServerLevel level, BlockPos rampartPos) {
        BlockPos feet = rampartPos.above();
        if (!level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()) {
            return false;
        }
        BlockPos head = feet.above();
        return level.getBlockState(head).getCollisionShape(level, head).isEmpty();
    }
}
