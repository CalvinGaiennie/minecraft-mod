package com.villagers.mod.block;

import com.villagers.mod.block.entity.PostBedBlockEntity;
import com.villagers.mod.block.entity.VillagerBlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PostBedBlock extends Block {
    public PostBedBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(0.1f)
                .sound(SoundType.WOOD)
                .noOcclusion());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PostBedBlockEntity(pos, state);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }
}
