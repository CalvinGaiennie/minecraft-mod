package com.villagers.mod.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.villagers.mod.VillagersMod;

public final class VillagerMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, VillagersMod.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<VillagerGearMenu>> VILLAGER_GEAR = MENUS.register(
            "villager_gear",
            () -> IMenuTypeExtension.create((windowId, playerInventory, data) -> {
                var entity = playerInventory.player.level().getEntity(data.readVarInt());
                return new VillagerGearMenu(
                        windowId, playerInventory, entity instanceof net.minecraft.world.entity.npc.Villager v ? v : null);
            }));

    private VillagerMenus() {
    }
}
