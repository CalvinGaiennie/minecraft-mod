package com.villagers.mod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;
import com.villagers.mod.village.VillageProtection;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class VillageProtectionHandler {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        BlockPos pos = event.getPosition().orElse(player.blockPosition());
        VillageData village = VillageManager.get(level).findVillageAt(pos.getX(), pos.getY(), pos.getZ());
        if (village == null || VillageProtection.mayBypassProtection(level, player.getUUID(), village)) {
            return;
        }
        int soldiers = VillageProtection.soldiersInsideVillage(level, village);
        float factor = VillageProtection.breakSlowdownFactor(soldiers);
        if (factor > 1.0f) {
            event.setNewSpeed(event.getNewSpeed() / factor);
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        Player source = event.getExplosion().getIndirectSourceEntity() instanceof Player p ? p : null;
        if (source == null) {
            return;
        }
        event.getAffectedBlocks().removeIf(pos -> {
            VillageData village = VillageManager.get(level).findVillageAt(pos.getX(), pos.getY(), pos.getZ());
            if (village == null) {
                return false;
            }
            return !VillageProtection.mayBypassProtection(level, source.getUUID(), village);
        });
    }
}
