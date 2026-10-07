package com.villagers.mod.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.mojang.blaze3d.vertex.PoseStack;

/**
 * {@link HumanoidArmorLayer} expects a humanoid parent model; villagers use {@link VillagerModel}.
 * Drive a standalone humanoid pose model from the entity, then delegate armor rendering.
 */
public final class VillagerArmorRenderLayer extends RenderLayer<Villager, VillagerModel<Villager>> {
    private final HumanoidModel<Villager> poseModel;
    private final HumanoidArmorLayer<Villager, HumanoidModel<Villager>, HumanoidModel<Villager>> armorLayer;

    public VillagerArmorRenderLayer(
            RenderLayerParent<Villager, VillagerModel<Villager>> parent,
            EntityRenderersEvent.AddLayers event) {
        super(parent);
        var context = event.getContext();
        ModelManager modelManager = context.getModelManager();
        poseModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER));
        HumanoidModel<Villager> inner = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        HumanoidModel<Villager> outer = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
        RenderLayerParent<Villager, VillagerModel<Villager>> villagerParent = parent;
        RenderLayerParent<Villager, HumanoidModel<Villager>> humanoidParent = new RenderLayerParent<>() {
            @Override
            public HumanoidModel<Villager> getModel() {
                return poseModel;
            }

            @Override
            public net.minecraft.resources.ResourceLocation getTextureLocation(Villager entity) {
                return villagerParent.getTextureLocation(entity);
            }
        };
        armorLayer = new HumanoidArmorLayer<>(humanoidParent, inner, outer, modelManager);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            Villager entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        syncHumanoidPoseFromVillager(getParentModel());
        armorLayer.render(
                poseStack,
                buffer,
                packedLight,
                entity,
                limbSwing,
                limbSwingAmount,
                partialTick,
                ageInTicks,
                netHeadYaw,
                headPitch);
    }

    /** Parent {@link VillagerModel} is already posed for this frame; player {@link HumanoidModel#setupAnim} misaligns armor. */
    private void syncHumanoidPoseFromVillager(VillagerModel<Villager> villagerModel) {
        ModelPart villagerRoot = villagerModel.root();
        poseModel.head.copyFrom(villagerModel.getHead());
        poseModel.hat.copyFrom(villagerModel.getHead().getChild("hat"));
        poseModel.body.copyFrom(villagerRoot.getChild("body"));
        ModelPart arms = villagerRoot.getChild("arms");
        poseModel.rightArm.copyFrom(arms);
        poseModel.leftArm.copyFrom(arms);
        poseModel.rightLeg.copyFrom(villagerRoot.getChild("right_leg"));
        poseModel.leftLeg.copyFrom(villagerRoot.getChild("left_leg"));
    }
}
