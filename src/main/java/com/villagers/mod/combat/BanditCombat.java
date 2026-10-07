package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.threat.BanditService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class BanditCombat {
    private static final Set<UUID> COMBAT_GOALS = new HashSet<>();

    private BanditCombat() {
    }

    public static void enableCombatGoals(Villager villager) {
        if (!(villager instanceof PathfinderMob mob) || !COMBAT_GOALS.add(villager.getUUID())) {
            return;
        }
        mob.goalSelector.addGoal(2, new SoldierMeleeAttackGoal(mob));
        mob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, Villager.class, 10, false, false,
                target -> target.hasData(VillagerAttachments.SOLDIER_DATA.get())
                        || target.hasData(VillagerAttachments.MILITIA_DATA.get())));
        mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Player.class, true));
    }

    public static void tick(Villager villager) {
        if (!BanditService.isBandit(villager) || !(villager instanceof Mob mob)) {
            return;
        }
        enableCombatGoals(villager);
        SoldierCombat.suppressPanic(villager);
        if (mob.getTarget() == null || !mob.getTarget().isAlive()) {
            LivingEntity soldier = findNearestSoldier(villager, 32);
            if (soldier != null) {
                mob.setTarget(soldier);
            }
        }
    }

    private static LivingEntity findNearestSoldier(Villager villager, double range) {
        LivingEntity best = null;
        double bestDist = range * range;
        for (Villager other : villager.level().getEntitiesOfClass(Villager.class, villager.getBoundingBox().inflate(range))) {
            if (!other.hasData(VillagerAttachments.SOLDIER_DATA.get()) && !other.hasData(VillagerAttachments.MILITIA_DATA.get())) {
                continue;
            }
            double dist = villager.distanceToSqr(other);
            if (dist < bestDist) {
                bestDist = dist;
                best = other;
            }
        }
        return best;
    }
}
