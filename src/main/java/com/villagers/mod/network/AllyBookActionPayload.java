package com.villagers.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

public record AllyBookActionPayload(Action action, String target, String rank) implements CustomPacketPayload {
    public enum Action {
        ADD,
        REMOVE,
        SET_RANK
    }

    public static final Type<AllyBookActionPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "ally_book_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AllyBookActionPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT.map(i -> Action.values()[i], Action::ordinal),
                    AllyBookActionPayload::action,
                    ByteBufCodecs.STRING_UTF8,
                    AllyBookActionPayload::target,
                    ByteBufCodecs.STRING_UTF8,
                    AllyBookActionPayload::rank,
                    AllyBookActionPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
