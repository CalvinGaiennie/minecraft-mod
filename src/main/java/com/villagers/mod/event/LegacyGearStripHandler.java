package com.villagers.mod.event;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import com.villagers.mod.VillagersMod;

/** Removes obsolete campaign boots / nightwatch helmet from equipped soldiers. */
@EventBusSubscriber(modid = VillagersMod.MODID)
public final class LegacyGearStripHandler {
    private LegacyGearStripHandler() {
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof net.minecraft.world.entity.npc.Villager villager)
                || villager.level().isClientSide()) {
            return;
        }
        if (villager.tickCount % 40 != 0) {
            return;
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = villager.getItemBySlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id != null && VillagersMod.MODID.equals(id.getNamespace()) && isRemoved(id.getPath())) {
                villager.setItemSlot(slot, ItemStack.EMPTY);
            }
        }
    }

    private static boolean isRemoved(String path) {
        return path.equals("campaign_boots") || path.equals("nightwatch_helmet") || path.equals("post_block");
    }
}
