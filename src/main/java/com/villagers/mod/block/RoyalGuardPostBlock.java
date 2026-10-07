package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class RoyalGuardPostBlock extends Block {
    public RoyalGuardPostBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.5f, 8.0f));
    }
}
