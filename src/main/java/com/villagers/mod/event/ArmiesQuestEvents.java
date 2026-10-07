package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import com.villagers.mod.Config;
import com.villagers.mod.VillagersMod;
import com.villagers.mod.armies.ArmiesQuestService;
import com.villagers.mod.armies.ArmiesQuestStage;
import com.villagers.mod.armies.ArmiesQuestSavedData;
import com.villagers.mod.entity.BanditData;
import com.villagers.mod.entity.VillagerAttachments;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class ArmiesQuestEvents {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (player.tickCount % 20 != 0) {
            return;
        }
        ArmiesQuestService.tickPlayer(player);
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Villager villager) || villager.level().isClientSide) {
            return;
        }
        if (!villager.hasData(VillagerAttachments.BANDIT_DATA.get())) {
            return;
        }
        BanditData data = villager.getData(VillagerAttachments.BANDIT_DATA.get());
        if (data == null || !data.isQuestBoss()) {
            return;
        }
        if (Config.ARMIES_QUEST_BOSS_PLAYER_KILL_ONLY.get() && !sourceIsPlayer(event.getSource())) {
            float newHealth = villager.getHealth() - event.getNewDamage();
            if (newHealth < 1f) {
                event.setNewDamage(Math.max(0, villager.getHealth() - 1f));
            }
        }
        if (data.getBossRole() == BanditData.BossRole.GARLAND && data.isD2GarlandFleeImmune()) {
            float max = villager.getMaxHealth();
            float after = villager.getHealth() - event.getNewDamage();
            if (after <= max * 0.30f && data.getQuestOwnerId() != null && villager.level() instanceof ServerLevel serverLevel) {
                ServerPlayer owner = serverLevel.getServer().getPlayerList().getPlayer(data.getQuestOwnerId());
                if (owner != null) {
                    var state = ArmiesQuestSavedData.get(serverLevel).getOrCreate(owner.getUUID());
                    if (state.stage == ArmiesQuestStage.D2_ACTIVE) {
                        var largest = com.villagers.mod.armies.VillageSoldierCounts.largestOwnedVillage(serverLevel, owner.getUUID());
                        if (largest.isPresent() && ArmiesQuestService.villageDefended(serverLevel, largest.get())) {
                            event.setNewDamage(0);
                            ArmiesQuestService.onGarlandFleeD2(owner, villager);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Villager villager) || villager.level().isClientSide) {
            return;
        }
        if (!villager.hasData(VillagerAttachments.BANDIT_DATA.get())) {
            return;
        }
        BanditData data = villager.getData(VillagerAttachments.BANDIT_DATA.get());
        if (data == null || !data.isQuestBoss() || !sourceIsPlayerKill(event.getSource())) {
            return;
        }
        Player player = event.getSource().getEntity() instanceof Player p ? p : null;
        if (!(player instanceof ServerPlayer killer)) {
            return;
        }
        if (data.getQuestOwnerId() != null && !data.getQuestOwnerId().equals(killer.getUUID())) {
            return;
        }
        if (!(villager.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        var questState = ArmiesQuestSavedData.get(serverLevel).getOrCreate(killer.getUUID());
        switch (data.getBossRole()) {
            case HALVEK -> ArmiesQuestService.onHalvekKilled(killer, villager);
            case CORVIN -> {
                if (questState.stage == ArmiesQuestStage.O1_ACTIVE || questState.stage == ArmiesQuestStage.O1_LEDGER) {
                    ArmiesQuestService.onCorvinKilled(killer);
                }
            }
            case GARLAND -> {
                if (data.isD2GarlandFleeImmune()) {
                    break;
                }
                if (questState.stage == ArmiesQuestStage.O2_ACTIVE || questState.stage == ArmiesQuestStage.O2_HUNT) {
                    ArmiesQuestService.onGarlandKilledO2(killer);
                }
            }
            default -> {
            }
        }
    }

    @SubscribeEvent
    public static void onBanditDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Villager villager) || villager.level().isClientSide) {
            return;
        }
        if (!com.villagers.mod.threat.BanditService.isBandit(villager)) {
            return;
        }
        BanditData data = villager.getData(VillagerAttachments.BANDIT_DATA.get());
        if (data == null || !data.isLeader()) {
            return;
        }
        for (net.minecraft.world.entity.EquipmentSlot slot : net.minecraft.world.entity.EquipmentSlot.values()) {
            net.minecraft.world.item.ItemStack stack = villager.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                villager.spawnAtLocation(stack.copy());
            }
        }
        if (data.getHomeCampId() != null && villager.level() instanceof ServerLevel level) {
            ServerPlayer killer = event.getSource().getEntity() instanceof ServerPlayer sp ? sp : null;
            com.villagers.mod.armies.BanditCampService.markCampCleared(level, data.getHomeCampId(), killer);
        }
    }

    private static boolean sourceIsPlayer(DamageSource source) {
        return source.getEntity() instanceof Player;
    }

    private static boolean sourceIsPlayerKill(DamageSource source) {
        return source.getEntity() instanceof Player;
    }
}
