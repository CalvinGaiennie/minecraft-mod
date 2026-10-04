package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PostBedBlock extends Block {
    public PostBedBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(0.1f)
                .sound(SoundType.WOOD)
                .noOcclusion());
    }
}
