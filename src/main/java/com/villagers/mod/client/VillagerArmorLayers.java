package com.villagers.mod.client;

import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.villagers.mod.VillagersMod;

/** Villagers skip armor layers by default; add them for visible soldier kit. */
@EventBusSubscriber(modid = VillagersMod.MODID, value = Dist.CLIENT)
public final class VillagerArmorLayers {
    private VillagerArmorLayers() {
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        if (!(event.getRenderer(EntityType.VILLAGER) instanceof VillagerRenderer villagerRenderer)) {
            return;
        }
        villagerRenderer.addLayer(new VillagerArmorRenderLayer(villagerRenderer, event));
    }
}
