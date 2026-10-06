package com.villagers.mod.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/** Plain block with copied vanilla properties. */
public class SimpleModBlock extends Block {
    public SimpleModBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public SimpleModBlock(Block template) {
        super(BlockBehaviour.Properties.ofFullCopy(template));
    }
}
