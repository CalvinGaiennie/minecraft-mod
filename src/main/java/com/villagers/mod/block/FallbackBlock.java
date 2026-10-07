package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FallbackBlock extends Block {
    public FallbackBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.0f, 6.0f));
    }
}
