package com.villagers.mod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BedBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.bed.SoldierBedClaims;
import com.villagers.mod.bed.VanillaBedHelper;

@EventBusSubscriber(modid = VillagersMod.MODID)
public final class SoldierBedBreakHandler {
    private SoldierBedBreakHandler() {
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || event.getLevel().isClientSide()) {
            return;
        }
        if (!(event.getState().getBlock() instanceof BedBlock)) {
            return;
        }
        BlockPos foot = VanillaBedHelper.footPosition(level, event.getPos());
        SoldierBedClaims.get(level).releaseAt(foot);
    }
}
