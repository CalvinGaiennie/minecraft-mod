package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PostBlock extends Block {
    public PostBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_SLAB)
                .strength(2f, 6f));
    }
}
