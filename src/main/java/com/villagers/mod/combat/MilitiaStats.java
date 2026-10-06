package com.villagers.mod.combat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.MilitiaData;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;

public final class MilitiaStats {
    private static final ResourceLocation HEALTH = ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "militia_health");
    private static final ResourceLocation DAMAGE = ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "militia_damage");
    private static final float SEASONED_HP_BONUS = 10f;

    private MilitiaStats() {
    }

    public static void sync(Villager villager, MilitiaData data) {
        VillagerGearRules.syncArmorEquipment(villager);
        float seasoned = data.isSeasoned() ? SEASONED_HP_BONUS : 0f;

        AttributeInstance health = villager.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            health.removeModifier(HEALTH);
            health.addPermanentModifier(new AttributeModifier(HEALTH, seasoned, AttributeModifier.Operation.ADD_VALUE));
            if (villager.getHealth() > villager.getMaxHealth()) {
                villager.setHealth(villager.getMaxHealth());
            }
        }

        double weaponDamage = weaponDamage(villager);
        AttributeInstance damage = villager.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(weaponDamage);
            damage.removeModifier(DAMAGE);
        }
    }

    private static double weaponDamage(Villager villager) {
        ItemStack melee = VillagerGearRules.get(villager, VillagerGearSlot.MELEE);
        if (!melee.isEmpty()) {
            return SoldierGearStats.attackDamageFromStack(melee);
        }
        ItemStack ranged = VillagerGearRules.get(villager, VillagerGearSlot.RANGED);
        if (!ranged.isEmpty()) {
            return 2.0;
        }
        return 1.0;
    }

    public static double homeDamageMultiplier(Villager villager, MilitiaData data) {
        if (data.getHomeBed() == null) {
            return 1.0;
        }
        if (villager.blockPosition().distSqr(data.getHomeBed()) <= 128 * 128) {
            return 1.25;
        }
        return 1.0;
    }
}
