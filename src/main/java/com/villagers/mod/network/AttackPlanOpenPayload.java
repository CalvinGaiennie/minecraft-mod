package com.villagers.mod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public record AttackPlanOpenPayload(List<SquadEntry> squads, long targetPacked, int siegeLadders)
        implements CustomPacketPayload {
    public record SquadEntry(long boxPos, int members, boolean selected) {}

    public static final Type<AttackPlanOpenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "attack_plan_open"));

    private static final StreamCodec<RegistryFriendlyByteBuf, SquadEntry> ENTRY_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            SquadEntry::boxPos,
            ByteBufCodecs.VAR_INT,
            SquadEntry::members,
            ByteBufCodecs.BOOL,
            SquadEntry::selected,
            SquadEntry::new);

    private static final StreamCodec<RegistryFriendlyByteBuf, List<SquadEntry>> ENTRY_LIST =
            ByteBufCodecs.collection(java.util.ArrayList::new, ENTRY_CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, AttackPlanOpenPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ENTRY_LIST,
                    AttackPlanOpenPayload::squads,
                    ByteBufCodecs.VAR_LONG,
                    AttackPlanOpenPayload::targetPacked,
                    ByteBufCodecs.VAR_INT,
                    AttackPlanOpenPayload::siegeLadders,
                    AttackPlanOpenPayload::new);

    @Nullable
    public BlockPos target() {
        return targetPacked == 0L ? null : BlockPos.of(targetPacked);
    }

    public static long packTarget(@Nullable BlockPos target) {
        return target == null ? 0L : target.asLong();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
