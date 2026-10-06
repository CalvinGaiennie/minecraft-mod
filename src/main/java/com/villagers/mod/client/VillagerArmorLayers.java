package com.villagers.mod.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
        addArmorLayer(event, EntityType.VILLAGER);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addArmorLayer(EntityRenderersEvent.AddLayers event, EntityType<?> type) {
        var renderer = event.getRenderer(type);
        if (!(renderer instanceof LivingEntityRenderer<?, ?> livingRenderer)) {
            return;
        }
        var context = event.getContext();
        HumanoidModel<LivingEntity> inner = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        HumanoidModel<LivingEntity> outer = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
        LivingEntityRenderer parent = (LivingEntityRenderer) livingRenderer;
        parent.addLayer(new HumanoidArmorLayer(parent, inner, outer, context.getModelManager()));
    }
}
