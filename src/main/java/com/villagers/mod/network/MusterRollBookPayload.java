package com.villagers.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

import java.util.List;

public record MusterRollBookPayload(List<String> pages) implements CustomPacketPayload {
    public static final Type<MusterRollBookPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "muster_roll_book"));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<String>> PAGE_LIST_CODEC =
            ByteBufCodecs.collection(java.util.ArrayList::new, ByteBufCodecs.STRING_UTF8);

    public static final StreamCodec<RegistryFriendlyByteBuf, MusterRollBookPayload> STREAM_CODEC = StreamCodec.composite(
            PAGE_LIST_CODEC,
            MusterRollBookPayload::pages,
            MusterRollBookPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
