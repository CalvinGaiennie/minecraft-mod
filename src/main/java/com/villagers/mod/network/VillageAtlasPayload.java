package com.villagers.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

import java.util.List;

public record VillageAtlasPayload(
        int playerBlockX,
        int playerBlockZ,
        int worldSpawnX,
        int worldSpawnZ,
        int totalSoldiers,
        int totalKills,
        List<HamletEntry> hamlets) implements CustomPacketPayload {

    public static final Type<VillageAtlasPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "village_atlas"));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<HamletEntry>> HAMLET_LIST_CODEC =
            ByteBufCodecs.collection(java.util.ArrayList::new, HamletEntry.STREAM_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, VillageAtlasPayload> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeVarInt(value.playerBlockX());
                buf.writeVarInt(value.playerBlockZ());
                buf.writeVarInt(value.worldSpawnX());
                buf.writeVarInt(value.worldSpawnZ());
                buf.writeVarInt(value.totalSoldiers());
                buf.writeVarInt(value.totalKills());
                HAMLET_LIST_CODEC.encode(buf, value.hamlets());
            },
            buf -> new VillageAtlasPayload(
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    HAMLET_LIST_CODEC.decode(buf)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record HamletEntry(
            String shortId,
            String villageName,
            int centerX,
            int centerZ,
            int radius,
            int presentCount,
            int squadRosterCount) {

        public static final StreamCodec<RegistryFriendlyByteBuf, HamletEntry> STREAM_CODEC = StreamCodec.of(
                (buf, entry) -> {
                    ByteBufCodecs.STRING_UTF8.encode(buf, entry.shortId());
                    ByteBufCodecs.STRING_UTF8.encode(buf, entry.villageName());
                    buf.writeVarInt(entry.centerX());
                    buf.writeVarInt(entry.centerZ());
                    buf.writeVarInt(entry.radius());
                    buf.writeVarInt(entry.presentCount());
                    buf.writeVarInt(entry.squadRosterCount());
                },
                buf -> new HamletEntry(
                        ByteBufCodecs.STRING_UTF8.decode(buf),
                        ByteBufCodecs.STRING_UTF8.decode(buf),
                        buf.readVarInt(),
                        buf.readVarInt(),
                        buf.readVarInt(),
                        buf.readVarInt(),
                        buf.readVarInt()));
    }
}
