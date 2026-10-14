package com.villagers.mod.combat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import com.villagers.mod.entity.BanditData;
import com.villagers.mod.entity.MilitiaLabels;
import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.threat.BanditRaidService;
import com.villagers.mod.threat.BanditService;

import org.jetbrains.annotations.Nullable;

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
        sanitizeBanditIdentity(villager);
        if (villager.level().getGameTime() % 80 == villager.getId() % 80) {
            SoldierMovement.applyCombatWalkSpeed(villager);
        }
        enableCombatGoals(villager);
        SoldierCombat.suppressPanic(villager);
        BanditData data = villager.getData(VillagerAttachments.BANDIT_DATA.get());
        if (villager.level() instanceof ServerLevel server && data != null) {
            BanditRaidService.tickBandit(server, villager, mob, data);
        }
        if (mob.getTarget() == null || !mob.getTarget().isAlive()) {
            if (villager.level().getGameTime() % 20 == villager.getId() % 20) {
                LivingEntity soldier = pickDefenderTarget(villager, data);
                if (soldier != null) {
                    if (soldier instanceof Villager defender && defender.isSleeping()) {
                        SoldierBedSleep.wakeNow(defender);
                    }
                    mob.setTarget(soldier);
                }
            }
        } else if (mob.getTarget() instanceof Villager defender && defender.isSleeping()) {
            SoldierBedSleep.wakeNow(defender);
        }
    }

    @Nullable
    private static LivingEntity pickDefenderTarget(Villager bandit, @Nullable BanditData data) {
        if (bandit.level() instanceof ServerLevel server && data != null) {
            BanditRaidService.RaidOrder order = BanditRaidService.orderFor(data);
            if (order != null) {
                Villager inVillage = BanditRaidService.findDefenderInVillage(server, bandit, order.villageId());
                if (inVillage != null) {
                    return inVillage;
                }
            }
        }
        return findNearestSoldier(bandit, 48);
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

    /** Repair saves where bandit gear was imported into kit and tagged as militia/soldier. */
    private static void sanitizeBanditIdentity(Villager villager) {
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            villager.removeData(VillagerAttachments.MILITIA_DATA.get());
            MilitiaLabels.clear(villager);
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            villager.removeData(VillagerAttachments.SOLDIER_DATA.get());
        }
        VillagerGearRules.clearKitForVanillaEquipmentMob(villager);
        if (villager.getCustomName() == null) {
            villager.setCustomName(net.minecraft.network.chat.Component.translatable("entity.villagers.bandit"));
            villager.setCustomNameVisible(true);
        }
    }
}
