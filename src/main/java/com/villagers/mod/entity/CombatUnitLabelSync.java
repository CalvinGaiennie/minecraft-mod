package com.villagers.mod.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.combat.MilitiaStats;
import com.villagers.mod.combat.SoldierRanks;

/** Reapply rank/kill nameplates after load (syncRank only ran during heavy combat ticks). */
@EventBusSubscriber(modid = VillagersMod.MODID)
public final class CombatUnitLabelSync {
    private CombatUnitLabelSync() {
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Villager villager)) {
            return;
        }
        refresh(villager);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) {
            return;
        }
        var box = event.getEntity().getBoundingBox().inflate(128);
        for (Villager villager : level.getEntitiesOfClass(Villager.class, box)) {
            refresh(villager);
        }
    }

    public static void refresh(Villager villager) {
        if (villager.level().isClientSide()) {
            return;
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null) {
                SoldierRanks.syncRank(villager, data);
            }
            return;
        }
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            MilitiaData data = villager.getData(VillagerAttachments.MILITIA_DATA.get());
            if (data != null) {
                MilitiaStats.sync(villager, data);
                MilitiaLabels.apply(villager, data);
            }
        }
    }
}
