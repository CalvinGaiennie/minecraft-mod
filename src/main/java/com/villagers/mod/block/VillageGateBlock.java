package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class VillageGateBlock extends Block {
    public VillageGateBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(3.5f)
                .sound(SoundType.WOOD));
    }
}
