package com.villagers.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.atlas.ChunkSurfaceSampler;

import java.util.ArrayList;
import java.util.List;

public record ExplorationTerrainBatchPayload(List<ChunkSlice> chunks) implements CustomPacketPayload {
    public static final Type<ExplorationTerrainBatchPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "exploration_terrain_batch"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExplorationTerrainBatchPayload> STREAM_CODEC =
            StreamCodec.of(ExplorationTerrainBatchPayload::encode, ExplorationTerrainBatchPayload::decode);

    private static void encode(RegistryFriendlyByteBuf buf, ExplorationTerrainBatchPayload value) {
        buf.writeVarInt(value.chunks.size());
        for (ChunkSlice slice : value.chunks) {
            buf.writeVarInt(slice.chunkX());
            buf.writeVarInt(slice.chunkZ());
            buf.writeBytes(slice.colors());
        }
    }

    private static ExplorationTerrainBatchPayload decode(RegistryFriendlyByteBuf buf) {
        int count = buf.readVarInt();
        List<ChunkSlice> chunks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            int cx = buf.readVarInt();
            int cz = buf.readVarInt();
            byte[] colors = new byte[ChunkSurfaceSampler.BLOCKS_PER_CHUNK];
            buf.readBytes(colors);
            chunks.add(new ChunkSlice(cx, cz, colors));
        }
        return new ExplorationTerrainBatchPayload(chunks);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record ChunkSlice(int chunkX, int chunkZ, byte[] colors) {}
}
