package com.villagers.mod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.villagers.mod.war.HornCommandService;
import com.villagers.mod.war.SoldierSignalType;

public class SoldierHornItem extends Item {
    private final SoldierSignalType signalType;

    public SoldierHornItem(SoldierSignalType signalType, Properties properties) {
        super(properties);
        this.signalType = signalType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            int count = HornCommandService.blow(serverPlayer, signalType);
            player.displayClientMessage(
                    Component.translatable("message.villagers.horn." + signalType.name().toLowerCase(), count), true);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }
}
