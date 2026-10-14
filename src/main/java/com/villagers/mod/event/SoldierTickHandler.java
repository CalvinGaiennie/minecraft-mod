package com.villagers.mod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.combat.SoldierCombat;
import com.villagers.mod.combat.SoldierRanks;
import com.villagers.mod.combat.SoldierRetreat;
import com.villagers.mod.combat.MilitiaFlee;
import com.villagers.mod.combat.MilitiaStats;
import com.villagers.mod.entity.MilitiaBehavior;
import com.villagers.mod.entity.MilitiaConversion;
import com.villagers.mod.entity.MilitiaData;
import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.SoldierBehavior;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.SoldierProfessionGuard;
import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.entity.SoldierDesertion;
import com.villagers.mod.entity.VeteranBedSync;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.combat.ArrowRecovery;
import com.villagers.mod.combat.ArrowResupply;
import com.villagers.mod.defense.BeaconSoldierBuffs;
import com.villagers.mod.combat.CombatDoorOpening;
import com.villagers.mod.gear.GearReplacement;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.combat.BanditCombat;
import com.villagers.mod.combat.WanderingNecromancerCombat;
import com.villagers.mod.threat.WanderingNecromancerService;
import com.villagers.mod.performance.CombatTickPolicy;
import com.villagers.mod.performance.CombatVillagerIndex;
import com.villagers.mod.squad.SquadCombatScan;
import com.villagers.mod.squad.SquadPathing;
import com.villagers.mod.squad.SquadService;
import com.villagers.mod.war.CampaignService;
import com.villagers.mod.war.MutinyService;
import com.villagers.mod.war.SoldierRedstoneInteract;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class SoldierTickHandler {
    private static final Map<Level, Long> lastProcessedDay = new HashMap<>();

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level.isClientSide || !(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }

        long day = level.getDayTime() / SoldierDesertion.TICKS_PER_DAY;
        Long previousDay = lastProcessedDay.get(level);
        boolean newDay = previousDay == null || previousDay != day;
        if (newDay) {
            lastProcessedDay.put(level, day);
        }

        long gameTime = level.getGameTime();
        SquadCombatScan.tickLevel(serverLevel);

        CombatVillagerIndex.forEachAlive(serverLevel, villager -> {
            if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                if (level.getGameTime() % 80 == villager.getId() % 80) {
                    SoldierProfessionGuard.enforceForCombatRole(villager);
                }
                tickSoldier(villager, level, gameTime, newDay);
            } else if (villager.hasData(VillagerAttachments.BANDIT_DATA.get())) {
                if (level.getGameTime() % 10 == villager.getId() % 10) {
                    BanditCombat.tick(villager);
                }
            } else if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
                if (level.getGameTime() % 80 == villager.getId() % 80) {
                    SoldierProfessionGuard.enforceForCombatRole(villager);
                }
                tickMilitia(villager, level, gameTime, newDay);
            } else if (villager.hasData(VillagerAttachments.VETERAN_DATA.get())) {
                tickVeteran(villager, level, gameTime);
            } else if (WanderingNecromancerService.isWanderingNecromancer(villager)) {
                if (level.getGameTime() % 10 == villager.getId() % 10) {
                    WanderingNecromancerCombat.tick(villager);
                }
            }
        });
    }

    private static boolean shouldRunHeavyCombat(Villager villager, long gameTime) {
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null && SoldierBedSleep.restsUndisturbedByHostiles(villager, data)) {
                return false;
            }
        }
        boolean hasTarget = villager instanceof Mob mob && mob.getTarget() != null && mob.getTarget().isAlive();
        return CombatTickPolicy.shouldRunHeavyCombat(villager, gameTime, hasTarget);
    }

    private static void tickSoldier(Villager villager, Level level, long gameTime, boolean newDay) {
        if (level.getGameTime() % 100 == villager.getId() % 100) {
            GearReplacement.tryReplaceBrokenGear(villager);
        }
        if (level.getGameTime() % 40 == villager.getId() % 40) {
            VillagerGearRules.refreshVisibleEquipment(villager);
        }
        if (level.getGameTime() % 200 == villager.getId() % 200) {
            ArrowResupply.tryResupply(villager);
        }
        if (level.getGameTime() % 20 == villager.getId() % 20) {
            ArrowRecovery.tryPickup(villager);
        }
        if (newDay) {
            var data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            SoldierDesertion.recordNightWithoutBed(villager, data);
            if (SoldierDesertion.tryDesert(villager)) {
                return;
            }
        }
        boolean heavy = shouldRunHeavyCombat(villager, gameTime);
        if (heavy) {
            SoldierCombat.tick(villager);
            CombatDoorOpening.tick(villager);
            SoldierRetreat.tick(villager);
        }
        SoldierData soldierData = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (soldierData != null && level instanceof net.minecraft.server.level.ServerLevel serverLevel
                && level.getGameTime() % 40 == villager.getId() % 40
                && SquadService.isStandingSquad(soldierData)) {
            SquadService.tickStandingRecruitFill(serverLevel, villager, soldierData);
        }
        if (soldierData != null && heavy) {
            if (level.getGameTime() % 40 == villager.getId() % 40) {
                SoldierRanks.syncRank(villager, soldierData);
            }
            if (level.getGameTime() % 20 == villager.getId() % 20) {
                CampaignService.tickSoldier(villager, soldierData);
                MutinyService.tick(villager, soldierData);
            }
            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel
                    && level.getGameTime() % 40 == villager.getId() % 40) {
                SquadPathing.tickFollowLeader(serverLevel, villager, soldierData);
                BeaconSoldierBuffs.tick(serverLevel, villager);
            }
        }
        if (soldierData != null && level instanceof net.minecraft.server.level.ServerLevel serverLevel
                && level.getGameTime() % 10 == villager.getId() % 10) {
            SoldierRedstoneInteract.tick(serverLevel, villager, soldierData);
        }
        if (soldierData != null && villager.isSleeping()
                && level.getGameTime() % 20 == villager.getId() % 20) {
            SoldierBedSleep.applyRestHeal(villager, soldierData);
        }
        if (CombatTickPolicy.shouldRunBehavior(villager, gameTime, heavy)) {
            SoldierBehavior.updateDailyRoutine(villager);
        }
    }

    private static void tickMilitia(Villager villager, Level level, long gameTime, boolean newDay) {
        MilitiaData militiaData = villager.getData(VillagerAttachments.MILITIA_DATA.get());
        if (militiaData != null) {
            if (newDay) {
                MilitiaBehavior.tickDaily(villager, militiaData, true);
            }
            if (level.getGameTime() % 40 == villager.getId() % 40) {
                MilitiaStats.sync(villager, militiaData);
            }
            if (!militiaData.wasTraderAtEnlist() && SoldierConversion.canBecomeSoldier(villager)) {
                MilitiaConversion.releaseMilitiaRoleForSoldierPromotion(villager);
                SoldierConversion.tryConvert(villager);
                return;
            }
            if (!MilitiaConversion.hasMilitiaWeapon(villager)) {
                MilitiaConversion.disarm(villager, false);
                return;
            }
        }
        boolean heavy = shouldRunHeavyCombat(villager, gameTime);
        if (heavy) {
            SoldierCombat.tick(villager);
            CombatDoorOpening.tick(villager);
            MilitiaFlee.tick(villager);
        }
        if (level.getGameTime() % 40 == villager.getId() % 40) {
            MilitiaBehavior.tickGuard(villager);
            if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                SquadService.tickMilitiaSquadRecruit(serverLevel, villager);
            }
        }
        if (level.getGameTime() % 200 == villager.getId() % 200) {
            ArrowResupply.tryResupply(villager);
        }
    }

    private static void tickVeteran(Villager villager, Level level, long gameTime) {
        if (level.getGameTime() % 40 == villager.getId() % 40) {
            VeteranBedSync.syncHomeBedFromVillager(villager);
        }
        if (shouldRunHeavyCombat(villager, gameTime)) {
            SoldierCombat.tick(villager);
        }
    }
}
