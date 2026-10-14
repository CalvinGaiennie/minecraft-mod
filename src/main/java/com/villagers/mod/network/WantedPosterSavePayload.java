package com.villagers.mod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

import java.util.List;

public record WantedPosterSavePayload(BlockPos pos, List<String> lines) implements CustomPacketPayload {
    public static final Type<WantedPosterSavePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "wanted_poster_save"));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<String>> LINE_LIST =
            ByteBufCodecs.collection(java.util.ArrayList::new, ByteBufCodecs.STRING_UTF8);

    public static final StreamCodec<RegistryFriendlyByteBuf, WantedPosterSavePayload> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    WantedPosterSavePayload::pos,
                    LINE_LIST,
                    WantedPosterSavePayload::lines,
                    WantedPosterSavePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
