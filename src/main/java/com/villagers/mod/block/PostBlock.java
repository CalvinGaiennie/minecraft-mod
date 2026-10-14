package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Marks where recruiters may place soldier beds. */
public class PostBlock extends Block {
    public PostBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).strength(1.5f));
    }
}
