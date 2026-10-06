package com.villagers.mod.entity;

import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.gear.VillagerGearSlot;

/** All recruit/soldier kit items; synced to clients for rendering and gear menu. */
public class VillagerKitData {
    public static final Codec<VillagerKitData> CODEC = ItemStack.CODEC.listOf()
            .xmap(VillagerKitData::fromList, VillagerKitData::toList);
    public static final StreamCodec<RegistryFriendlyByteBuf, VillagerKitData> STREAM_CODEC =
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list(VillagerGearSlot.values().length))
                    .map(VillagerKitData::fromList, VillagerKitData::toList);

    private final NonNullList<ItemStack> stacks = NonNullList.withSize(VillagerGearSlot.values().length, ItemStack.EMPTY);

    public ItemStack get(VillagerGearSlot slot) {
        return stacks.get(slot.ordinal());
    }

    public void set(VillagerGearSlot slot, ItemStack stack) {
        stacks.set(slot.ordinal(), stack == null ? ItemStack.EMPTY : stack.copy());
    }

    private static VillagerKitData fromList(java.util.List<ItemStack> list) {
        VillagerKitData data = new VillagerKitData();
        for (int i = 0; i < VillagerGearSlot.values().length && i < list.size(); i++) {
            data.stacks.set(i, list.get(i));
        }
        return data;
    }

    private java.util.List<ItemStack> toList() {
        return java.util.List.copyOf(stacks);
    }
}
