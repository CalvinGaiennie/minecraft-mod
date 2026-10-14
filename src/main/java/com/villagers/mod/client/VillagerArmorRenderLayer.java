package com.villagers.mod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

/**
 * Villagers use {@link VillagerModel}; armor expects {@link HumanoidModel}. Drive a pose model from the
 * villager skeleton per slot, then draw vanilla armor layers.
 */
public final class VillagerArmorRenderLayer extends RenderLayer<Villager, VillagerModel<Villager>> {
    private final HumanoidModel<Villager> poseModel;
    private final HumanoidModel<Villager> innerArmorModel;
    private final HumanoidModel<Villager> outerArmorModel;
    private final TextureAtlas armorTrimAtlas;

    public VillagerArmorRenderLayer(
            RenderLayerParent<Villager, VillagerModel<Villager>> parent,
            EntityRenderersEvent.AddLayers event) {
        super(parent);
        var context = event.getContext();
        ModelManager modelManager = context.getModelManager();
        poseModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER));
        innerArmorModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        outerArmorModel = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
        armorTrimAtlas = modelManager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
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
        poseModel.young = entity.isBaby();
        poseModel.attackTime = entity.getAttackAnim(partialTick);
        poseModel.riding = entity.isPassenger();

        VillagerModel<Villager> villagerModel = getParentModel();
        renderArmorPiece(
                poseStack,
                buffer,
                packedLight,
                entity,
                villagerModel,
                EquipmentSlot.CHEST,
                limbSwing,
                limbSwingAmount,
                partialTick,
                ageInTicks,
                netHeadYaw,
                headPitch);
        renderArmorPiece(
                poseStack,
                buffer,
                packedLight,
                entity,
                villagerModel,
                EquipmentSlot.LEGS,
                limbSwing,
                limbSwingAmount,
                partialTick,
                ageInTicks,
                netHeadYaw,
                headPitch);
        renderArmorPiece(
                poseStack,
                buffer,
                packedLight,
                entity,
                villagerModel,
                EquipmentSlot.FEET,
                limbSwing,
                limbSwingAmount,
                partialTick,
                ageInTicks,
                netHeadYaw,
                headPitch);
        renderArmorPiece(
                poseStack,
                buffer,
                packedLight,
                entity,
                villagerModel,
                EquipmentSlot.HEAD,
                limbSwing,
                limbSwingAmount,
                partialTick,
                ageInTicks,
                netHeadYaw,
                headPitch);
    }

    private void renderArmorPiece(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            Villager entity,
            VillagerModel<Villager> villagerModel,
            EquipmentSlot slot,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        ItemStack stack = armorStack(entity, slot);
        if (!(stack.getItem() instanceof ArmorItem armorItem) || armorItem.getEquipmentSlot() != slot) {
            return;
        }

        ModelPart villagerRoot = villagerModel.root();
        ModelPart villagerBody = villagerRoot.getChild("body");
        boolean torsoLocalSpace = slot == EquipmentSlot.CHEST;
        if (torsoLocalSpace) {
            poseStack.pushPose();
            villagerBody.translateAndRotate(poseStack);
        }

        syncPoseModelForSlot(villagerModel, slot);
        HumanoidModel<Villager> armorModel = slot == EquipmentSlot.LEGS ? innerArmorModel : outerArmorModel;
        armorModel.young = entity.isBaby();
        poseModel.copyPropertiesTo(armorModel);
        setPartVisibility(armorModel, slot);

        net.minecraft.client.model.Model model =
                net.neoforged.neoforge.client.ClientHooks.getArmorModel(entity, stack, slot, armorModel);
        boolean innerTexture = slot == EquipmentSlot.LEGS;
        ArmorMaterial armorMaterial = armorItem.getMaterial().value();
        var extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(stack);
        extensions.setupModelAnimations(
                entity, stack, slot, model, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
        int fallbackColor = extensions.getDefaultDyeColor(stack);
        for (int layerIdx = 0; layerIdx < armorMaterial.layers().size(); layerIdx++) {
            ArmorMaterial.Layer layer = armorMaterial.layers().get(layerIdx);
            int tint = extensions.getArmorLayerTintColor(stack, entity, layer, layerIdx, fallbackColor);
            ResourceLocation texture =
                    net.neoforged.neoforge.client.ClientHooks.getArmorTexture(entity, stack, layer, innerTexture, slot);
            if (texture != null) {
                renderColoredModel(poseStack, buffer, packedLight, model, tint, texture);
            }
        }

        ArmorTrim trim = stack.get(DataComponents.TRIM);
        if (trim != null) {
            TextureAtlasSprite trimSprite = armorTrimAtlas.getSprite(
                    innerTexture ? trim.innerTexture(armorItem.getMaterial()) : trim.outerTexture(armorItem.getMaterial()));
            VertexConsumer trimBuffer =
                    trimSprite.wrap(buffer.getBuffer(Sheets.armorTrimsSheet(trim.pattern().value().decal())));
            model.renderToBuffer(poseStack, trimBuffer, packedLight, OverlayTexture.NO_OVERLAY);
        }

        if (stack.hasFoil()) {
            model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.armorEntityGlint()), packedLight, OverlayTexture.NO_OVERLAY);
        }

        if (torsoLocalSpace) {
            poseStack.popPose();
        }
    }

    /** Kit is authoritative; vanilla equipment slots can lag on the client for legs/chest. */
    private static ItemStack armorStack(Villager entity, EquipmentSlot slot) {
        VillagerGearSlot gearSlot =
                switch (slot) {
                    case HEAD -> VillagerGearSlot.HELMET;
                    case CHEST -> VillagerGearSlot.CHESTPLATE;
                    case LEGS -> VillagerGearSlot.LEGGINGS;
                    case FEET -> VillagerGearSlot.BOOTS;
                    default -> null;
                };
        if (gearSlot != null) {
            ItemStack fromKit = VillagerGearRules.get(entity, gearSlot);
            if (!fromKit.isEmpty()) {
                return fromKit;
            }
        }
        return entity.getItemBySlot(slot);
    }

    private static void renderColoredModel(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            net.minecraft.client.model.Model model,
            int tint,
            ResourceLocation texture) {
        VertexConsumer consumer = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, tint);
    }

    private static void setPartVisibility(HumanoidModel<?> model, EquipmentSlot slot) {
        model.setAllVisible(false);
        switch (slot) {
            case HEAD -> {
                model.head.visible = true;
                model.hat.visible = true;
            }
            case CHEST -> {
                model.body.visible = true;
            }
            case LEGS -> {
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            case FEET -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            default -> {
            }
        }
    }

    private void syncPoseModelForSlot(VillagerModel<Villager> villagerModel, EquipmentSlot slot) {
        resetPoseModel(poseModel);
        ModelPart villagerRoot = villagerModel.root();
        ModelPart villagerBody = villagerRoot.getChild("body");
        ModelPart villagerRightLeg = villagerRoot.getChild("right_leg");
        ModelPart villagerLeftLeg = villagerRoot.getChild("left_leg");

        switch (slot) {
            case HEAD -> {
                copyRotationsOnly(villagerModel.getHead(), poseModel.head);
                copyRotationsOnly(villagerModel.getHead(), poseModel.hat);
            }
            case CHEST -> {
                // Chestplate drawn in villager body space; keep humanoid torso at local origin.
                poseModel.body.resetPose();
            }
            case LEGS -> {
                copyPartTransform(villagerBody, poseModel.body);
                copyPartTransform(villagerRightLeg, poseModel.rightLeg);
                copyPartTransform(villagerLeftLeg, poseModel.leftLeg);
            }
            case FEET -> {
                copyRotationsOnly(villagerRightLeg, poseModel.rightLeg);
                copyRotationsOnly(villagerLeftLeg, poseModel.leftLeg);
            }
            default -> {
            }
        }
    }

    private static void resetPoseModel(HumanoidModel<?> model) {
        model.head.resetPose();
        model.hat.resetPose();
        model.body.resetPose();
        model.rightArm.resetPose();
        model.leftArm.resetPose();
        model.rightLeg.resetPose();
        model.leftLeg.resetPose();
    }

    private static void copyRotationsOnly(ModelPart from, ModelPart to) {
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;
        to.xScale = from.xScale;
        to.yScale = from.yScale;
        to.zScale = from.zScale;
    }

    private static void copyPartTransform(ModelPart from, ModelPart to) {
        copyRotationsOnly(from, to);
        to.x = from.x;
        to.y = from.y;
        to.z = from.z;
    }
}
