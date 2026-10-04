package com.villagers.mod.entity;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import com.villagers.mod.VillagersMod;

public class VillagerAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, VillagersMod.MODID);

    @SuppressWarnings("unchecked")
    public static final net.neoforged.neoforge.registries.DeferredHolder<AttachmentType<?>, AttachmentType<SoldierData>> SOLDIER_DATA =
            (net.neoforged.neoforge.registries.DeferredHolder<AttachmentType<?>, AttachmentType<SoldierData>>) (Object)
                    ATTACHMENTS.register("soldier_data", () -> AttachmentType.builder(() -> new SoldierData()).build());

    @SuppressWarnings("unchecked")
    public static final net.neoforged.neoforge.registries.DeferredHolder<AttachmentType<?>, AttachmentType<VeteranData>> VETERAN_DATA =
            (net.neoforged.neoforge.registries.DeferredHolder<AttachmentType<?>, AttachmentType<VeteranData>>) (Object)
                    ATTACHMENTS.register("veteran_data", () -> AttachmentType.builder(() -> new VeteranData()).build());
}
