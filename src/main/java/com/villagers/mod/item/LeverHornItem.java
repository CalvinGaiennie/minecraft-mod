package com.villagers.mod.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import com.villagers.mod.war.LeverHornService;

public class LeverHornItem extends Item {
    public static final String TAG_COLOR = "LeverColor";
    public static final String TAG_RANDOM = "RandomOnly";

    public LeverHornItem(Properties properties) {
        super(properties);
    }

    public static ItemStack withColor(DyeColor color, boolean randomOnly) {
        ItemStack stack = new ItemStack(com.villagers.mod.VillagersMod.LEVER_HORN.get());
        var tag = new net.minecraft.nbt.CompoundTag();
        tag.putString(TAG_COLOR, color.getName());
        tag.putBoolean(TAG_RANDOM, randomOnly);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    public static DyeColor readColor(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        String name = data.copyTag().getString(TAG_COLOR);
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

    public static boolean randomOnly(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean(TAG_RANDOM);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            int count = LeverHornService.blow(serverPlayer, readColor(stack), randomOnly(stack));
            player.displayClientMessage(Component.translatable("message.villagers.horn.lever", count), true);
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }
}
