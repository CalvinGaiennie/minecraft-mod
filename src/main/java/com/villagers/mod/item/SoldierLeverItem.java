package com.villagers.mod.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.LeverBlock;

import com.villagers.mod.war.LeverColorSavedData;
import com.villagers.mod.war.LeverHornService;

public class SoldierLeverItem extends Item {
    private static final String TAG_COLOR = "SignalColor";

    public SoldierLeverItem(Properties properties) {
        super(properties);
    }

    public static ItemStack withColor(DyeColor color) {
        ItemStack stack = new ItemStack(com.villagers.mod.VillagersMod.SOLDIER_LEVER_ITEM.get());
        var tag = new net.minecraft.nbt.CompoundTag();
        tag.putString(TAG_COLOR, color.getName());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    public static DyeColor colorOf(ItemStack stack) {
        String name = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(TAG_COLOR);
        if (name.isEmpty()) {
            return DyeColor.WHITE;
        }
        for (DyeColor color : LeverHornService.HORN_COLORS) {
            if (color.getName().equals(name)) {
                return color;
            }
        }
        return DyeColor.WHITE;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide() || !(context.getLevel() instanceof ServerLevel level)) {
            return InteractionResult.SUCCESS;
        }
        if (!(level.getBlockState(context.getClickedPos()).getBlock() instanceof LeverBlock)) {
            return InteractionResult.PASS;
        }
        DyeColor color = colorOf(context.getItemInHand());
        LeverColorSavedData.get(level).setColor(context.getClickedPos(), color);
        if (context.getPlayer() != null) {
            context.getPlayer()
                    .displayClientMessage(
                            Component.translatable("message.villagers.soldier_lever_tagged", color.getName()),
                            true);
        }
        if (context.getPlayer() == null || !context.getPlayer().isCreative()) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }
}
