package com.villagers.mod.client;

import net.minecraft.world.entity.npc.Villager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;

@EventBusSubscriber(modid = VillagersMod.MODID, value = Dist.CLIENT)
public final class VillagerKitClientSync {
    private VillagerKitClientSync() {
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() || !(event.getEntity() instanceof Villager villager)) {
            return;
        }
        applyIfKit(villager);
    }

    @SubscribeEvent
    public static void onClientTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Villager villager) || !villager.level().isClientSide()) {
            return;
        }
        if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())
                && !villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            return;
        }
        if (villager.tickCount % 20 != 0) {
            return;
        }
        applyIfKit(villager);
    }

    private static void applyIfKit(Villager villager) {
        if (!villager.hasData(VillagerAttachments.VILLAGER_KIT.get())) {
            return;
        }
        VillagerGearRules.applyToEntity(villager);
    }
}
