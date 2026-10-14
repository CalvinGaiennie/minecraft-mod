package com.villagers.mod.performance;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.threat.WanderingNecromancerService;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/** Tracks combat-role villagers so tick handlers never scan every villager in the dimension. */
@EventBusSubscriber(modid = VillagersMod.MODID)
public final class CombatVillagerIndex {
    private static final Map<ResourceKey<Level>, Set<UUID>> BY_DIMENSION = new ConcurrentHashMap<>();

    private CombatVillagerIndex() {
    }

    public static void track(Villager villager) {
        if (villager.level().isClientSide() || !isCombatRole(villager)) {
            return;
        }
        BY_DIMENSION.computeIfAbsent(villager.level().dimension(), d -> ConcurrentHashMap.newKeySet())
                .add(villager.getUUID());
    }

    public static void untrack(Villager villager) {
        Set<UUID> set = BY_DIMENSION.get(villager.level().dimension());
        if (set != null) {
            set.remove(villager.getUUID());
        }
    }

    public static void forEachAlive(ServerLevel level, Consumer<Villager> action) {
        Set<UUID> ids = BY_DIMENSION.get(level.dimension());
        if (ids == null || ids.isEmpty()) {
            return;
        }
        Iterator<UUID> it = ids.iterator();
        while (it.hasNext()) {
            UUID id = it.next();
            Entity entity = level.getEntity(id);
            if (entity instanceof Villager villager && villager.isAlive()) {
                action.accept(villager);
            } else {
                it.remove();
            }
        }
    }

    public static boolean isCombatRole(Villager villager) {
        return villager.hasData(VillagerAttachments.SOLDIER_DATA.get())
                || villager.hasData(VillagerAttachments.MILITIA_DATA.get())
                || villager.hasData(VillagerAttachments.BANDIT_DATA.get())
                || villager.hasData(VillagerAttachments.VETERAN_DATA.get())
                || WanderingNecromancerService.isWanderingNecromancer(villager);
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Villager villager)) {
            return;
        }
        track(villager);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Villager villager) {
            untrack(villager);
        }
    }
}
