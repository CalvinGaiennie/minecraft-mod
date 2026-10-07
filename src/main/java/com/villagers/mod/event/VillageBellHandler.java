package com.villagers.mod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.SoldierBehavior;
import com.villagers.mod.entity.VillagerAttachments;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class VillageBellHandler {
    private static final int RADIUS = 64;

    @SubscribeEvent
    public static void onRightClickBell(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        BlockPos pos = event.getPos();
        if (!level.getBlockState(pos).is(Blocks.BELL)) {
            return;
        }
        long gameTime = level.getGameTime();
        double radiusSq = (double) RADIUS * RADIUS;
        int awakened = 0;
        for (var entity : level.getAllEntities()) {
            if (entity instanceof net.minecraft.world.entity.npc.Villager villager
                    && villager.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= radiusSq
                    && villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                SoldierBehavior.signalAwaken(villager, gameTime);
                awakened++;
            }
        }
        if (awakened > 0 && event.getEntity() != null) {
            event.getEntity().displayClientMessage(
                    net.minecraft.network.chat.Component.translatable("message.villagers.bell_awaken", awakened), true);
        }
    }
}
