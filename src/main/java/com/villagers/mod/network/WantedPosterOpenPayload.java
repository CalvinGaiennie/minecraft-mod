package com.villagers.mod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

import java.util.List;

public record WantedPosterOpenPayload(BlockPos pos, List<String> lines) implements CustomPacketPayload {
    public static final Type<WantedPosterOpenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "wanted_poster_open"));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<String>> LINE_LIST =
            ByteBufCodecs.collection(java.util.ArrayList::new, ByteBufCodecs.STRING_UTF8);

    public static final StreamCodec<RegistryFriendlyByteBuf, WantedPosterOpenPayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    WantedPosterOpenPayload::pos,
                    LINE_LIST,
                    WantedPosterOpenPayload::lines,
                    WantedPosterOpenPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
