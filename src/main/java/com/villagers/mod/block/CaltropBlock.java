package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import com.villagers.mod.block.entity.CaltropBlockEntity;
import com.villagers.mod.block.entity.VillagerBlockEntities;

import org.jetbrains.annotations.Nullable;

public class CaltropBlock extends BaseEntityBlock {
    public static final MapCodec<CaltropBlock> CODEC = simpleCodec(CaltropBlock::new);

    public CaltropBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .strength(0.5f)
                .noOcclusion());
    }

    public CaltropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(level instanceof ServerLevel server)) {
            return;
        }
        if (!(entity instanceof LivingEntity living) || !living.isAlive()) {
            return;
        }
        if (entity.tickCount % 10 != 0) {
            return;
        }
        var be = server.getBlockEntity(pos);
        if (!(be instanceof CaltropBlockEntity caltrop)) {
            return;
        }
        living.hurt(server.damageSources().cactus(), 2.0f);
        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
        if (caltrop.registerHit()) {
            server.removeBlock(pos, false);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CaltropBlockEntity(pos, state);
    }
}
