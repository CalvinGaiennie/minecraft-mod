package com.villagers.mod.event;

import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.combat.HostileMobVillagerTargeting;

@EventBusSubscriber(modid = VillagersMod.MODID)
public final class HostileMobTargetingEvents {
    private HostileMobTargetingEvents() {
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Mob mob)) {
            return;
        }
        HostileMobVillagerTargeting.tryEnable(mob);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            HostileMobVillagerTargeting.clear(mob);
        }
    }
}
