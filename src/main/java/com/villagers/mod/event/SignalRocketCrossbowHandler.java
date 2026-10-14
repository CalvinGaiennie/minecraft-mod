package com.villagers.mod.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.item.SignalRocketItem;

/** Prefer signal rockets over arrows (especially creative default arrows) when loading a crossbow. */
@EventBusSubscriber(modid = VillagersMod.MODID)
public final class SignalRocketCrossbowHandler {
    private SignalRocketCrossbowHandler() {
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void preferSignalRockets(LivingGetProjectileEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (!(event.getProjectileWeaponItemStack().getItem() instanceof CrossbowItem)) {
            return;
        }
        ItemStack current = event.getProjectileItemStack();
        if (SignalRocketItem.isSignalRocket(current) || current.is(Items.FIREWORK_ROCKET)) {
            return;
        }
        ItemStack signal = findSignalRocket(player);
        if (!signal.isEmpty()) {
            event.setProjectileItemStack(signal);
        }
    }

    /** Free hand first, then any inventory slot (hotbar included). */
    static ItemStack findSignalRocket(Player player) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        if (!(main.getItem() instanceof CrossbowItem) && SignalRocketItem.isSignalRocket(main)) {
            return main;
        }
        if (!(off.getItem() instanceof CrossbowItem) && SignalRocketItem.isSignalRocket(off)) {
            return off;
        }
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (SignalRocketItem.isSignalRocket(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
