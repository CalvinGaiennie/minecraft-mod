package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PostBedBlock extends Block {
    public PostBedBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_BED)
                .strength(0.1f));
    }
}
