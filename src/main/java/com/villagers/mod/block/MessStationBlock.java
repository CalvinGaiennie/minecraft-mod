package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MessStationBlock extends Block {
    public MessStationBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)
                .strength(2.5f, 6f));
    }
}
