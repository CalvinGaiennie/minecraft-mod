package com.villagers.mod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.entity.MessStationBlockEntity;

public class VillageBannerItem extends Item {
    public VillageBannerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (!level.getBlockState(context.getClickedPos()).is(VillagersMod.MESS_STATION.get())) {
            return InteractionResult.PASS;
        }
        var be = level.getBlockEntity(context.getClickedPos());
        if (!(be instanceof MessStationBlockEntity mess)) {
            return InteractionResult.PASS;
        }
        ItemStack held = context.getItemInHand();
        ItemStack banner = held.getItem() instanceof BannerItem
                ? held.copyWithCount(1)
                : new ItemStack(Items.WHITE_BANNER);
        mess.setVillageBanner(banner);
        if (!context.getPlayer().isCreative() && held.getItem() instanceof BannerItem) {
            held.shrink(1);
        }
        context.getPlayer().displayClientMessage(
                Component.translatable("message.villagers.banner_set"), true);
        return InteractionResult.CONSUME;
    }
}
