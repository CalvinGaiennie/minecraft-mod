package com.villagers.mod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

public record SquadBoxActionPayload(long boxPosPacked, int actionId) implements CustomPacketPayload {
    public static final Type<SquadBoxActionPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "squad_box_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SquadBoxActionPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_LONG,
                    SquadBoxActionPayload::boxPosPacked,
                    ByteBufCodecs.VAR_INT,
                    SquadBoxActionPayload::actionId,
                    SquadBoxActionPayload::new);

    public BlockPos boxPos() {
        return BlockPos.of(boxPosPacked);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
