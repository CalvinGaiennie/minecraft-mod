package com.villagers.mod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class VillageChronicleItem extends Item {
    public VillageChronicleItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.villagers.village_chronicle.desc"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        if (!level.isClientSide) {
            player.displayClientMessage(Component.translatable("item.villagers.village_chronicle.read"), true);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
