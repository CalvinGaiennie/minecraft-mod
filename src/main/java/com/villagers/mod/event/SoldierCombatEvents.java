package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.combat.SoldierRanks;
import com.villagers.mod.entity.SoldierBehavior;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.util.SoldierStructureHelper;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class SoldierCombatEvents {
    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Villager attacker)) {
            return;
        }
        float damage = event.getNewDamage();
        if (attacker.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            event.setNewDamage(applyOutgoingDamage(attacker, damage));
        } else if (attacker.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            event.setNewDamage(MilitiaEvents.applyOutgoingDamage(attacker, damage));
        }
    }

    @SubscribeEvent
    public static void onLivingDamagePost(LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof Villager villager && villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null && event.getNewDamage() > 0) {
                data.setLastDamageGameTime(villager.level().getGameTime());
                SoldierBedSleep.wakeNow(villager);
                SoldierBehavior.signalAwaken(villager, villager.level().getGameTime());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Villager dead
                && !dead.level().isClientSide
                && (dead.hasData(VillagerAttachments.SOLDIER_DATA.get())
                        || dead.hasData(VillagerAttachments.MILITIA_DATA.get()))) {
            com.villagers.mod.combat.SoldierCombatArms.clearShootCooldown(dead.getUUID());
            com.villagers.mod.combat.CombatDoorOpening.clearFor(dead.getUUID());
            if (dead.level() instanceof ServerLevel serverLevel) {
                com.villagers.mod.combat.RampartClaims.get(serverLevel).releaseDefender(dead.getUUID());
            }
            VillagerGearRules.dropEntireKit(dead);
        }
        if (event.getEntity() instanceof Villager deadSoldier
                && deadSoldier.hasData(VillagerAttachments.SOLDIER_DATA.get())
                && !deadSoldier.level().isClientSide) {
            SoldierData bedData = deadSoldier.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (bedData != null && deadSoldier.level() instanceof ServerLevel serverLevel) {
                SoldierStructureHelper.ensureAssignedBedCached(
                        serverLevel, deadSoldier.getUUID(), bedData, deadSoldier.blockPosition());
                SoldierStructureHelper.releaseSoldierBed(
                        serverLevel, deadSoldier.getUUID(), bedData.getAssignedPostBed());
            }
        }
        if (event.getEntity() instanceof Villager deadSoldier
                && deadSoldier.hasData(VillagerAttachments.SOLDIER_DATA.get())
                && sourceIsPlayerKill(event.getSource())) {
            SoldierData grumble = deadSoldier.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (grumble != null) {
                grumble.addGrumblePoint();
            }
        }
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Villager killer) || !killer.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity victim) || victim instanceof Villager) {
            return;
        }
        SoldierData data = killer.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) {
            return;
        }
        data.addKill();
        SoldierRanks.syncRank(killer, data);
        wearMeleeWeapon(killer);
    }

    private static boolean sourceIsPlayerKill(DamageSource source) {
        return source.getEntity() instanceof Player;
    }

    public static float applyOutgoingDamage(Villager attacker, float amount) {
        return (float) (amount * SoldierRanks.homeDamageMultiplier(attacker));
    }

    private static void wearMeleeWeapon(Villager soldier) {
        ItemStack melee = VillagerGearRules.get(soldier, VillagerGearSlot.MELEE);
        if (melee.isEmpty() || !melee.isDamageableItem()) {
            return;
        }
        melee.hurtAndBreak(1, soldier, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        VillagerGearRules.set(soldier, VillagerGearSlot.MELEE, melee);
    }
}
