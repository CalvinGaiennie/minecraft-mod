package com.villagers.mod.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.SoldierProfessionGuard;
import com.villagers.mod.item.ReassignmentWritItem;

/** Block vanilla villager trading for soldiers, militia, and bandits (gear menu / writ still work). */
@EventBusSubscriber(modid = VillagersMod.MODID)
public final class CombatVillagerTradeHandler {
    private CombatVillagerTradeHandler() {
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void blockMerchantTrade(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide() || event.isCanceled()) {
            return;
        }
        if (!(event.getTarget() instanceof Villager villager)) {
            return;
        }
        if (!SoldierProfessionGuard.isCombatRole(villager)) {
            return;
        }
        if (event.getItemStack().getItem() instanceof ReassignmentWritItem) {
            return;
        }
        if (event.getItemStack().isEmpty()) {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}
