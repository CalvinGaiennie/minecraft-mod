package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WatchtowerBlock extends Block {
    public WatchtowerBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(2.5f)
                .sound(SoundType.STONE));
    }
}
