package com.villagers.mod.event;

import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.item.ReassignmentWritItem;

/** Villager trading would swallow the writ before {@link ReassignmentWritItem#interactLivingEntity} runs. */
@EventBusSubscriber(modid = VillagersMod.MODID)
public final class ReassignmentWritEntityHandler {
    private ReassignmentWritEntityHandler() {
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onWritSoldier(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        if (!(event.getItemStack().getItem() instanceof ReassignmentWritItem)) {
            return;
        }
        if (!(event.getTarget() instanceof Villager villager)) {
            return;
        }
        var result = ReassignmentWritItem.tagSoldier(event.getItemStack(), event.getEntity(), villager);
        if (result.consumesAction() || result == net.minecraft.world.InteractionResult.FAIL) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
}
