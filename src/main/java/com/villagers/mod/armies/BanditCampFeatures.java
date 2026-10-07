package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

/** Camp dressing (leader mount). */
public final class BanditCampFeatures {
    private BanditCampFeatures() {
    }

    public static void spawnTiedLeaderHorseIfAbsent(ServerLevel level, BlockPos campOrigin, RandomSource random) {
        AABB box = new AABB(campOrigin).inflate(12);
        if (!level.getEntitiesOfClass(Horse.class, box, Horse::isSaddled).isEmpty()) {
            return;
        }
        BlockPos fencePos = findFenceSpot(level, campOrigin, random);
        if (fencePos == null) {
            return;
        }
        level.setBlock(fencePos, Blocks.OAK_FENCE.defaultBlockState(), 3);
        BlockPos horsePos = fencePos.relative(random.nextBoolean() ? net.minecraft.core.Direction.NORTH : net.minecraft.core.Direction.SOUTH);
        if (!level.getBlockState(horsePos).isAir() || !level.getBlockState(horsePos.below()).isSolidRender(level, horsePos.below())) {
            horsePos = fencePos.north();
        }
        Horse horse = EntityType.HORSE.create(level);
        if (horse == null) {
            return;
        }
        horse.setPos(horsePos.getX() + 0.5, horsePos.getY(), horsePos.getZ() + 0.5);
        horse.setTamed(true);
        horse.getInventory().setItem(0, new ItemStack(Items.SADDLE));
        level.addFreshEntity(horse);
        var knot = net.minecraft.world.entity.decoration.LeashFenceKnotEntity.getOrCreateKnot(level, fencePos);
        horse.setLeashedTo(knot, true);
    }

    private static BlockPos findFenceSpot(ServerLevel level, BlockPos origin, RandomSource random) {
        for (int i = 0; i < 8; i++) {
            BlockPos pos = origin.offset(-2 + random.nextInt(5), 0, -2 + random.nextInt(5));
            if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {
                return pos;
            }
        }
        BlockPos fallback = origin.south(2);
        if (level.getBlockState(fallback).isAir() && level.getBlockState(fallback.below()).isSolidRender(level, fallback.below())) {
            return fallback;
        }
        return null;
    }
}
