package com.villagers.mod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

import java.util.ArrayList;
import java.util.List;

public record AttackPlanSavePayload(List<Long> selectedSquadBoxes, long targetPacked, int siegeLadders)
        implements CustomPacketPayload {
    public static final Type<AttackPlanSavePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "attack_plan_save"));

    private static final StreamCodec<RegistryFriendlyByteBuf, List<Long>> LONG_LIST =
            ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.VAR_LONG);

    public static final StreamCodec<RegistryFriendlyByteBuf, AttackPlanSavePayload> STREAM_CODEC =
            StreamCodec.composite(
                    LONG_LIST,
                    AttackPlanSavePayload::selectedSquadBoxes,
                    ByteBufCodecs.VAR_LONG,
                    AttackPlanSavePayload::targetPacked,
                    ByteBufCodecs.VAR_INT,
                    AttackPlanSavePayload::siegeLadders,
                    AttackPlanSavePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
