package com.villagers.mod.block;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.DyeColor;

public class PostBedBlock extends BedBlock {
    public PostBedBlock() {
        super(DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_BED)
                .strength(0.1f));
    }
}
