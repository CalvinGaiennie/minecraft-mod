package com.villagers.mod.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.Fireworks;

import com.villagers.mod.war.SoldierSignalType;

import java.util.function.Consumer;

public class SignalRocketItem extends FireworkRocketItem {
    /** Firework flight duration 1–3 → rally respond radius 32 / 64 / 128 blocks. */
    public static final int MIN_GUNPOWDER_TIER = 1;
    public static final int MAX_GUNPOWDER_TIER = 3;

    public static void acceptCreativeTiers(Consumer<ItemStack> output, boolean strike) {
        for (int tier = MIN_GUNPOWDER_TIER; tier <= MAX_GUNPOWDER_TIER; tier++) {
            output.accept(create(strike, tier));
        }
    }

    private static final String TAG_SIGNAL = "Signal";

    private final boolean strike;

    public SignalRocketItem(boolean strike, Properties properties) {
        super(properties);
        this.strike = strike;
    }

    public boolean isStrike() {
        return strike;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return create(strike, 1);
    }

    public static ItemStack create(boolean strike, int gunpowder) {
        ItemStack stack = new ItemStack(
                strike ? com.villagers.mod.VillagersMod.STRIKE_SIGNAL_ROCKET.get()
                        : com.villagers.mod.VillagersMod.RALLY_SIGNAL_ROCKET.get());
        int flight = Math.clamp(gunpowder, 1, 3);
        stack.set(DataComponents.FIREWORKS, new Fireworks(flight, java.util.List.of()));
        var tag = new net.minecraft.nbt.CompoundTag();
        tag.putString(TAG_SIGNAL, strike ? SoldierSignalType.STRIKE.name() : SoldierSignalType.RALLY.name());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    public static boolean isSignalRocket(ItemStack stack) {
        return stack.getItem() instanceof SignalRocketItem;
    }

    public static boolean isStrike(ItemStack stack) {
        if (stack.getItem() instanceof SignalRocketItem item) {
            return item.strike;
        }
        String name = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(TAG_SIGNAL);
        return SoldierSignalType.STRIKE.name().equals(name);
    }
}
