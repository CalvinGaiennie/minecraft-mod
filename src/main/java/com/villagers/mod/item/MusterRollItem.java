package com.villagers.mod.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;

import com.villagers.mod.util.MusterRollHelper;

public class MusterRollItem extends Item {
    private static final int RADIUS = 64;

    public MusterRollItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            var summary = MusterRollHelper.summarize((net.minecraft.server.level.ServerLevel) level, player, RADIUS);
            player.displayClientMessage(
                    Component.literal("Muster Roll: " + summary.soldierCount() + " soldiers, " + summary.totalKills() + " total kills"),
                    true);
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
