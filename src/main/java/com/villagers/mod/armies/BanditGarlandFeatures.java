package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;

import com.mojang.datafixers.util.Pair;

import java.util.Optional;

/** Pillager-outpost style shell when no vanilla outpost is nearby. */
public final class BanditGarlandFeatures {
    private static final ResourceKey<Structure> PILLAGER_OUTPOST =
            ResourceKey.create(Registries.STRUCTURE, ResourceLocation.withDefaultNamespace("pillager_outpost"));

    private BanditGarlandFeatures() {
    }

    public static boolean nearVanillaOutpost(ServerLevel level, BlockPos origin) {
        var registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Optional<Holder.Reference<Structure>> holder = registry.getHolder(PILLAGER_OUTPOST);
        if (holder.isEmpty()) {
            return false;
        }
        Pair<BlockPos, Holder<Structure>> pair = level.getChunkSource()
                .getGenerator()
                .findNearestMapStructure(level, HolderSet.direct(holder.get()), origin, 8, true);
        return pair != null && pair.getFirst().distSqr(origin) < 48 * 48L;
    }

    public static void dressGarlandSite(ServerLevel level, BlockPos origin, RandomSource random) {
        BlockPos watch = origin.offset(3, 0, 2);
        for (int dy = 0; dy < 4; dy++) {
            setIfAirOrReplaceable(level, watch.above(dy), Blocks.BIRCH_PLANKS.defaultBlockState());
        }
        setIfAirOrReplaceable(level, watch.above(4), Blocks.DARK_OAK_FENCE.defaultBlockState());
        BlockPos fire = origin.offset(-2, 0, -1);
        setIfAirOrReplaceable(level, fire, Blocks.CAMPFIRE.defaultBlockState());
        for (int i = 0; i < 6; i++) {
            BlockPos tent = origin.offset(-3 + random.nextInt(7), 0, -3 + random.nextInt(7));
            if (tent.distSqr(origin) < 4) {
                continue;
            }
            setIfAirOrReplaceable(level, tent, Blocks.WHITE_WOOL.defaultBlockState());
        }
        setIfAirOrReplaceable(level, origin.west(2), Blocks.COBBLESTONE.defaultBlockState());
        setIfAirOrReplaceable(level, origin.west(2).north(), Blocks.COBBLESTONE.defaultBlockState());
        setIfAirOrReplaceable(level, origin.west(2).south(), Blocks.COBBLESTONE.defaultBlockState());
    }

    private static void setIfAirOrReplaceable(ServerLevel level, BlockPos pos, BlockState state) {
        BlockState existing = level.getBlockState(pos);
        if (existing.isAir() || existing.canBeReplaced()) {
            level.setBlock(pos, state, 3);
        }
    }
}
