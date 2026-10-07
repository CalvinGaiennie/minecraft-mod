package com.villagers.mod.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.combat.MilitiaFlee;
import com.villagers.mod.combat.MilitiaStats;
import com.villagers.mod.entity.MilitiaData;
import net.minecraft.world.item.ItemStack;
import com.villagers.mod.entity.MilitiaLabels;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class MilitiaEvents {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Villager dead && dead.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            if (!(event.getEntity().level() instanceof net.minecraft.server.level.ServerLevel level)) {
                return;
            }
            for (var entity : level.getEntitiesOfClass(Villager.class, dead.getBoundingBox().inflate(16))) {
                if (entity != dead) {
                    MilitiaFlee.onMilitiaDeathNearby(entity, dead);
                }
            }
        }
        DamageSource source = event.getSource();
        if (!(source.getEntity() instanceof Villager killer) || !killer.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity victim) || victim instanceof Villager) {
            return;
        }
        MilitiaData data = killer.getData(VillagerAttachments.MILITIA_DATA.get());
        if (data == null) {
            return;
        }
        data.addKill();
        MilitiaStats.sync(killer, data);
        MilitiaLabels.apply(killer, data);
        wearWeapon(killer);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide() || !(event.getTarget() instanceof Villager villager)) {
            return;
        }
        if (!villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            return;
        }
        if (villager.getOffers() != null && !event.getItemStack().isEmpty()) {
            event.setCanceled(true);
        }
    }

    public static float applyOutgoingDamage(Villager attacker, float amount) {
        MilitiaData data = attacker.getData(VillagerAttachments.MILITIA_DATA.get());
        if (data == null) {
            return amount;
        }
        return (float) (amount * MilitiaStats.homeDamageMultiplier(attacker, data));
    }

    private static void wearWeapon(Villager militia) {
        ItemStack melee = VillagerGearRules.get(militia, VillagerGearSlot.MELEE);
        if (!melee.isEmpty() && melee.isDamageableItem()) {
            melee.hurtAndBreak(1, militia, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            VillagerGearRules.set(militia, VillagerGearSlot.MELEE, melee);
        }
    }
}
