package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Civilians flee here during raids (Stage 3). */
public class HavenBlock extends Block {
    public HavenBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_BLOCK).strength(2.0f, 6.0f));
    }
}
