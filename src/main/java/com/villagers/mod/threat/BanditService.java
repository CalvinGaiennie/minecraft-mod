package com.villagers.mod.threat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.Config;
import com.villagers.mod.combat.BanditCombat;
import com.villagers.mod.entity.BanditData;
import com.villagers.mod.entity.VillagerAttachments;

public final class BanditService {
    private BanditService() {
    }

    public static void tryConvertDeserter(Villager villager) {
        if (!(villager.level() instanceof ServerLevel) || villager.level().isClientSide) {
            return;
        }
        double roll = villager.level().random.nextDouble();
        double villagerShare = Config.DESERTER_OUTCOME_VILLAGER.get();
        double banditShare = Config.DESERTER_OUTCOME_BANDIT.get();
        double necroShare = Config.DESERTER_OUTCOME_NECROMANCER.get();
        if (roll < villagerShare) {
            resetToVillager(villager);
        } else if (roll < villagerShare + banditShare) {
            makeBandit(villager, new BanditData(villager.getUUID()), null);
        } else if (roll < villagerShare + banditShare + necroShare) {
            // Wandering necromancer: stub as bandit with hood for now; full arc deferred.
            makeBandit(villager, new BanditData(villager.getUUID()), null);
        } else {
            resetToVillager(villager);
        }
    }

    public static void resetToVillager(Villager villager) {
        com.villagers.mod.gear.VillagerGearRules.dropEntireKit(villager);
        villager.setCustomName(null);
        villager.setCustomNameVisible(false);
    }

    public static void makeBandit(Villager villager, BanditData data, net.minecraft.network.chat.Component name) {
        villager.setData(VillagerAttachments.BANDIT_DATA.get(), data);
        villager.setCustomName(name != null ? name : net.minecraft.network.chat.Component.translatable("entity.villagers.bandit"));
        villager.setCustomNameVisible(true);
        BanditCombat.enableCombatGoals(villager);
    }

    public static boolean isBandit(Villager villager) {
        return villager.hasData(VillagerAttachments.BANDIT_DATA.get());
    }

}
