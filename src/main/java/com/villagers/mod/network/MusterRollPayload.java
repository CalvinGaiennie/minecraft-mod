package com.villagers.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

import java.util.List;

public record MusterRollPayload(
        int playerBlockX,
        int playerBlockZ,
        int nearbySoldierCount,
        int nearbyTotalKills,
        int ownedSoldierCount,
        int ownedTotalKills,
        List<TerritoryEntry> territories) implements CustomPacketPayload {

    public static final Type<MusterRollPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "muster_roll"));

    private static final StreamCodec<RegistryFriendlyByteBuf, java.util.List<TerritoryEntry>> TERRITORY_LIST_CODEC =
            ByteBufCodecs.collection(java.util.ArrayList::new, TerritoryEntry.STREAM_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, MusterRollPayload> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeVarInt(value.playerBlockX());
                buf.writeVarInt(value.playerBlockZ());
                buf.writeVarInt(value.nearbySoldierCount());
                buf.writeVarInt(value.nearbyTotalKills());
                buf.writeVarInt(value.ownedSoldierCount());
                buf.writeVarInt(value.ownedTotalKills());
                TERRITORY_LIST_CODEC.encode(buf, value.territories());
            },
            buf -> new MusterRollPayload(
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    buf.readVarInt(),
                    TERRITORY_LIST_CODEC.decode(buf)));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record TerritoryEntry(
            String villageName,
            int centerX,
            int centerZ,
            int radius,
            int soldierCount,
            int totalKills) {

        public static final StreamCodec<RegistryFriendlyByteBuf, TerritoryEntry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                TerritoryEntry::villageName,
                ByteBufCodecs.VAR_INT,
                TerritoryEntry::centerX,
                ByteBufCodecs.VAR_INT,
                TerritoryEntry::centerZ,
                ByteBufCodecs.VAR_INT,
                TerritoryEntry::radius,
                ByteBufCodecs.VAR_INT,
                TerritoryEntry::soldierCount,
                ByteBufCodecs.VAR_INT,
                TerritoryEntry::totalKills,
                TerritoryEntry::new);
    }

    public static MusterRollPayload empty(int playerX, int playerZ) {
        return new MusterRollPayload(playerX, playerZ, 0, 0, 0, 0, List.of());
    }
}
