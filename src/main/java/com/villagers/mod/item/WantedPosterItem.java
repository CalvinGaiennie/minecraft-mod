package com.villagers.mod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

import com.villagers.mod.player.VillageOpsPermission;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

public class WantedPosterItem extends BlockItem {
    public WantedPosterItem(net.minecraft.world.level.block.Block block, Item.Properties properties) {
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
            context.getPlayer().displayClientMessage(
                    Component.translatable("message.villagers.wanted_poster_need_village"), true);
            return false;
        }
        if (!VillageOpsPermission.mayManageVillage(level, context.getPlayer().getUUID(), village)) {
            context.getPlayer().displayClientMessage(
                    Component.translatable("message.villagers.wanted_poster_not_owner"), true);
            return false;
        }
        return true;
    }
}
