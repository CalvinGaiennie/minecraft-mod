package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class RampartBlock extends Block {
    public RampartBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).strength(2.0f, 6.0f));
    }
}
