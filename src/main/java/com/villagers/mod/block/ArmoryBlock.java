package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ArmoryBlock extends Block {
    public ArmoryBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(3.0f)
                .sound(SoundType.METAL));
    }
}
