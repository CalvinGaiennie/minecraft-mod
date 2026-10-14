package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.BedBlock;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.RampartBlock;
import com.villagers.mod.block.RecruiterBlock;
import com.villagers.mod.item.ReassignmentWritItem;

@EventBusSubscriber(modid = VillagersMod.MODID)
public final class ReassignmentWritBedHandler {
    private ReassignmentWritBedHandler() {
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onWritBed(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!(event.getItemStack().getItem() instanceof ReassignmentWritItem)) {
            return;
        }
        var block = level.getBlockState(event.getPos()).getBlock();
        if (!(block instanceof BedBlock) && !(block instanceof RampartBlock) && !(block instanceof RecruiterBlock)) {
            return;
        }
        event.setUseBlock(TriState.FALSE);
        var context = new UseOnContext(
                level,
                event.getEntity(),
                event.getHand(),
                event.getItemStack(),
                event.getHitVec());
        var result = ReassignmentWritItem.applyUseOnBlock(context);
        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
}
