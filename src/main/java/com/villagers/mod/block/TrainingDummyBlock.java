package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class TrainingDummyBlock extends Block {
    public TrainingDummyBlock() {
        super(net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK));
    }
}
