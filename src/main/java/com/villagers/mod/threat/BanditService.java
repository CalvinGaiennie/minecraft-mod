package com.villagers.mod.threat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.entity.BanditData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;

public final class BanditService {
    private BanditService() {
    }

    public static void tryConvertDeserter(Villager villager) {
        if (!(villager.level() instanceof ServerLevel) || villager.level().isClientSide) {
            return;
        }
        if (villager.level().random.nextBoolean()) {
            return;
        }
        villager.setData(VillagerAttachments.BANDIT_DATA.get(), new BanditData(villager.getUUID()));
        villager.setCustomName(net.minecraft.network.chat.Component.translatable("entity.villagers.bandit"));
        villager.setCustomNameVisible(true);
    }

    public static boolean isBandit(Villager villager) {
        return villager.hasData(VillagerAttachments.BANDIT_DATA.get());
    }

    public static void tick(Villager villager) {
        if (!isBandit(villager)) {
            return;
        }
        if (!(villager.level() instanceof ServerLevel level)) {
            return;
        }
        Villager nearestSoldier = null;
        double best = 32 * 32;
        for (var entity : level.getEntitiesOfClass(Villager.class, villager.getBoundingBox().inflate(32))) {
            if (!entity.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            double dist = villager.distanceToSqr(entity);
            if (dist < best) {
                best = dist;
                nearestSoldier = entity;
            }
        }
        if (nearestSoldier == null) {
            return;
        }
        if (best > 4) {
            villager.getNavigation().moveTo(nearestSoldier, 1.1);
            return;
        }
        if (villager.tickCount % 20 == 0) {
            nearestSoldier.hurt(level.damageSources().mobAttack(villager), 3.0f);
        }
    }
}
