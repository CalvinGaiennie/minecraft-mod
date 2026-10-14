package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.MilitiaConversion;
import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.gear.GearReplacement;
import com.villagers.mod.performance.CivilianEnlistGate;
import com.villagers.mod.performance.CombatVillagerIndex;
import com.villagers.mod.squad.SquadService;
import com.villagers.mod.performance.CombatTickPolicy;
import net.minecraft.world.entity.npc.Villager;

/** Civilian enlist checks near players only — not every villager in the dimension. */
@EventBusSubscriber(modid = VillagersMod.MODID)
public final class CivilianEnlistTickHandler {
    private static final double ENLIST_RADIUS = CombatTickPolicy.FULL_RATE_PLAYER_RADIUS + 32;

    private CivilianEnlistTickHandler() {
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        long gameTime = level.getGameTime();
        for (ServerPlayer player : level.players()) {
            for (Villager villager : level.getEntitiesOfClass(
                    Villager.class, player.getBoundingBox().inflate(ENLIST_RADIUS))) {
                if (CombatVillagerIndex.isCombatRole(villager)) {
                    continue;
                }
                if (!CivilianEnlistGate.shouldRunCivilianEnlist(villager, gameTime)) {
                    continue;
                }
                GearReplacement.tryPullEnlistGearFromNearestRecruiter(villager);
                if (SoldierConversion.tryConvert(villager)) {
                    CombatVillagerIndex.track(villager);
                    SquadService.assignStandingToNearestRecruitSquad(level, villager, 32);
                } else {
                    MilitiaConversion.onGearUpdated(villager);
                    if (CombatVillagerIndex.isCombatRole(villager)) {
                        CombatVillagerIndex.track(villager);
                    }
                }
            }
        }
    }
}
