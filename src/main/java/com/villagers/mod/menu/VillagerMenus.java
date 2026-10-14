package com.villagers.mod.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.entity.RecruiterBlockEntity;

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

    public static final DeferredHolder<MenuType<?>, MenuType<SquadBoxMenu>> SQUAD_BOX = MENUS.register(
            "squad_box",
            () -> IMenuTypeExtension.create((windowId, playerInventory, buf) -> {
                if (buf == null) {
                    throw new IllegalStateException("Squad box menu requires block pos in open packet");
                }
                BlockPos pos = buf.readBlockPos();
                var level = playerInventory.player.level();
                if (level.getBlockEntity(pos) instanceof RecruiterBlockEntity box) {
                    return new SquadBoxMenu(windowId, playerInventory, box);
                }
                return new SquadBoxMenu(
                        windowId,
                        playerInventory,
                        new RecruiterBlockEntity(pos, level.getBlockState(pos)));
            }));

    private VillagerMenus() {
    }
}
