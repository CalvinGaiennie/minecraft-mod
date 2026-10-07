package com.villagers.mod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;
import com.villagers.mod.village.VillageProtection;

public class CaltropsItem extends BlockItem {
    public CaltropsItem(net.minecraft.world.level.block.Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        if (!super.canPlace(context, state)) {
            return false;
        }
        if (!(context.getLevel() instanceof ServerLevel level) || context.getPlayer() == null) {
            return true;
        }
        var pos = context.getClickedPos().relative(context.getClickedFace());
        VillageData village = VillageManager.get(level).findVillageAt(pos.getX(), pos.getY(), pos.getZ());
        if (village == null) {
            return true;
        }
        if (VillageProtection.mayBypassProtection(context.getPlayer().getUUID(), village)) {
            return true;
        }
        context.getPlayer().displayClientMessage(
                Component.translatable("message.villagers.caltrop_enemy_land"), true);
        return false;
    }
}
