package com.villagers.mod.entity;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.combat.SoldierCombat;
import com.villagers.mod.combat.MilitiaStats;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

public final class MilitiaConversion {
    private MilitiaConversion() {
    }

    /** Called after kit changes on non-soldiers. */
    public static void onGearUpdated(Villager villager) {
        if (villager.level().isClientSide || villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        if (SoldierConversion.canBecomeSoldier(villager)) {
            if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
                disarm(villager, false);
            }
            SoldierConversion.tryConvert(villager);
            return;
        }
        if (canBecomeMilitia(villager)) {
            if (!villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
                convertToMilitia(villager);
            } else {
                MilitiaStats.sync(villager, villager.getData(VillagerAttachments.MILITIA_DATA.get()));
                MilitiaLabels.apply(villager, villager.getData(VillagerAttachments.MILITIA_DATA.get()));
            }
            return;
        }
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get()) && !hasMilitiaWeapon(villager)) {
            disarm(villager, true);
        }
    }

    public static boolean canBecomeMilitia(Villager villager) {
        if (villager == null || villager.isBaby()) {
            return false;
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return false;
        }
        if (SoldierConversion.canBecomeSoldier(villager)) {
            return false;
        }
        if (!hasMilitiaWeapon(villager)) {
            return false;
        }
        return hasSomeMilitiaArmor(villager);
    }

    public static boolean hasMilitiaWeapon(Villager villager) {
        ItemStack melee = VillagerGearRules.get(villager, VillagerGearSlot.MELEE);
        ItemStack ranged = VillagerGearRules.get(villager, VillagerGearSlot.RANGED);
        if (!melee.isEmpty() && VillagerGearRules.accepts(VillagerGearSlot.MELEE, melee)) {
            return true;
        }
        return !ranged.isEmpty() && VillagerGearRules.accepts(VillagerGearSlot.RANGED, ranged);
    }

    private static boolean hasSomeMilitiaArmor(Villager villager) {
        return !VillagerGearRules.get(villager, VillagerGearSlot.HELMET).isEmpty()
                || !VillagerGearRules.get(villager, VillagerGearSlot.CHESTPLATE).isEmpty()
                || !VillagerGearRules.get(villager, VillagerGearSlot.BOOTS).isEmpty();
    }

    public static void convertToMilitia(Villager villager) {
        if (villager.level().isClientSide) {
            return;
        }
        VillagerGearRules.set(villager, VillagerGearSlot.LEGGINGS, ItemStack.EMPTY);
        MilitiaData data = new MilitiaData(villager.getUUID());
        data.setDisplayName(VillagerPersonalNames.captureAtEnlist(villager));
        data.setTraderAtEnlist(villager.getVillagerData().getProfession() != VillagerProfession.NONE);
        long day = villager.level().getDayTime() / 24000L;
        data.setLastFoodDay(day);
        MilitiaHomeSync.syncHomeBedFromVillager(villager, data);
        villager.setData(VillagerAttachments.MILITIA_DATA.get(), data);
        VillagerGearRules.applyToEntity(villager);
        MilitiaStats.sync(villager, data);
        MilitiaLabels.apply(villager, data);
        SoldierCombat.enableCombatGoals(villager);
    }

    public static void disarm(Villager villager, boolean dropWeapon) {
        if (villager.level().isClientSide || !villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            return;
        }
        if (dropWeapon) {
            dropKitWeapons(villager);
        }
        VillagerGearRules.set(villager, VillagerGearSlot.MELEE, ItemStack.EMPTY);
        VillagerGearRules.set(villager, VillagerGearSlot.RANGED, ItemStack.EMPTY);
        VillagerGearRules.set(villager, VillagerGearSlot.ARROWS, ItemStack.EMPTY);
        SoldierCombat.disableCombatGoals(villager);
        if (villager.level() instanceof net.minecraft.server.level.ServerLevel level) {
            com.villagers.mod.combat.RampartClaims.get(level).releaseDefender(villager.getUUID());
        }
        villager.removeData(VillagerAttachments.MILITIA_DATA.get());
        MilitiaLabels.clear(villager);
    }

    private static void dropKitWeapons(Villager villager) {
        for (VillagerGearSlot slot : new VillagerGearSlot[] { VillagerGearSlot.MELEE, VillagerGearSlot.RANGED }) {
            ItemStack stack = VillagerGearRules.get(villager, slot);
            if (!stack.isEmpty()) {
                villager.spawnAtLocation(stack.copy());
            }
        }
    }
}
