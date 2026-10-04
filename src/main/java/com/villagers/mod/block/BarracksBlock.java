package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BarracksBlock extends Block {
    public BarracksBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(2.0f)
                .sound(SoundType.WOOD));
    }
}
