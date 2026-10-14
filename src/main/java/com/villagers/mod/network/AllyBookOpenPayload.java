package com.villagers.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

import java.util.List;

public record AllyBookOpenPayload(List<Entry> entries) implements CustomPacketPayload {
    public record Entry(String id, String displayName, String rank) {}

    public static final Type<AllyBookOpenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "ally_book_open"));

    private static final StreamCodec<RegistryFriendlyByteBuf, Entry> ENTRY_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            Entry::id,
            ByteBufCodecs.STRING_UTF8,
            Entry::displayName,
            ByteBufCodecs.STRING_UTF8,
            Entry::rank,
            Entry::new);

    private static final StreamCodec<RegistryFriendlyByteBuf, List<Entry>> ENTRY_LIST =
            ByteBufCodecs.collection(java.util.ArrayList::new, ENTRY_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, AllyBookOpenPayload> STREAM_CODEC =
            StreamCodec.composite(ENTRY_LIST, AllyBookOpenPayload::entries, AllyBookOpenPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
