package com.villagers.mod.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.villagers.mod.network.VillagersModNetwork;

public class VillageMapItem extends Item {
    public VillageMapItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            VillagersModNetwork.sendMusterRoll(serverPlayer);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
