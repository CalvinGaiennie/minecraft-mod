package com.villagers.mod.combat;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.entity.WanderingNecromancerData;
import com.villagers.mod.threat.WanderingNecromancerService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class WanderingNecromancerCombat {
    private static final Set<UUID> COMBAT_GOALS = new HashSet<>();

    private WanderingNecromancerCombat() {
    }

    public static void enableCombatGoals(Villager villager) {
        if (!(villager instanceof PathfinderMob mob) || !COMBAT_GOALS.add(villager.getUUID())) {
            return;
        }
        mob.goalSelector.addGoal(2, new SoldierMeleeAttackGoal(mob));
        mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, Player.class, 10, false, false,
                target -> {
                    if (!(target instanceof Player player)) {
                        return false;
                    }
                    WanderingNecromancerData data = villager.getData(VillagerAttachments.WANDERING_NECROMANCER_DATA.get());
                    return data != null && player.getUUID().equals(data.getExOwnerId());
                }));
        mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Villager.class, 10, false, false,
                target -> target.hasData(VillagerAttachments.SOLDIER_DATA.get())
                        || target.hasData(VillagerAttachments.MILITIA_DATA.get())));
    }

    public static void tick(Villager villager) {
        if (!WanderingNecromancerService.isWanderingNecromancer(villager) || !(villager instanceof Mob mob)) {
            return;
        }
        enableCombatGoals(villager);
        SoldierCombat.suppressPanic(villager);
        WanderingNecromancerData data = villager.getData(VillagerAttachments.WANDERING_NECROMANCER_DATA.get());
        if (data == null || data.getExOwnerId() == null) {
            return;
        }
        if (mob.getTarget() == null || !mob.getTarget().isAlive()) {
            var owner = villager.level().getServer().getPlayerList().getPlayer(data.getExOwnerId());
            if (owner != null && owner.level() == villager.level() && villager.distanceToSqr(owner) < 48 * 48) {
                mob.setTarget(owner);
            }
        }
    }

    public static void applyLeaderStats(Villager villager) {
        var health = villager.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            health.setBaseValue(50.0);
            villager.setHealth(50.0f);
        }
        var knockback = villager.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (knockback != null) {
            knockback.setBaseValue(0.5);
        }
        var damage = villager.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(damage.getBaseValue() * 1.25);
        }
    }
}
