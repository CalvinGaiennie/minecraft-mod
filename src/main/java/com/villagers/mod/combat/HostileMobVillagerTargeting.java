package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.threat.WanderingNecromancerService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Lets vanilla spiders and skeletons acquire soldier/militia/civilian villagers as targets. */
public final class HostileMobVillagerTargeting {
    private static final Set<UUID> ENABLED = new HashSet<>();

    private HostileMobVillagerTargeting() {
    }

    public static boolean isSpiderOrSkeleton(Mob mob) {
        return mob instanceof Spider || mob instanceof AbstractSkeleton;
    }

    public static void tryEnable(Mob mob) {
        if (!isSpiderOrSkeleton(mob) || !ENABLED.add(mob.getUUID())) {
            return;
        }
        mob.targetSelector.addGoal(
                3,
                new NearestAttackableTargetGoal<>(mob, Villager.class, 10, true, false, HostileMobVillagerTargeting::canTarget));
    }

    public static void clear(Mob mob) {
        ENABLED.remove(mob.getUUID());
    }

    static boolean canTarget(LivingEntity target) {
        if (!(target instanceof Villager villager) || !villager.isAlive() || villager.isBaby()) {
            return false;
        }
        if (villager.hasData(VillagerAttachments.BANDIT_DATA.get())) {
            return false;
        }
        return !WanderingNecromancerService.isWanderingNecromancer(villager);
    }
}
