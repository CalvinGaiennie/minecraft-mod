package com.villagers.mod.combat;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.Config;
import com.villagers.mod.entity.OrphanRaised;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.SoldierLabels;
import com.villagers.mod.gear.VillagerGearRules;

import net.minecraft.resources.ResourceLocation;

import com.villagers.mod.VillagersMod;

public final class SoldierRanks {
    private static final ResourceLocation HEALTH_MODIFIER = ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "rank_health");
    private static final ResourceLocation ORPHAN_HEALTH_MODIFIER = ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "orphan_health");
    private static final ResourceLocation DAMAGE_MODIFIER = ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "rank_damage");
    private static final ResourceLocation ORPHAN_DAMAGE_MODIFIER = ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "orphan_damage");
    private static final ResourceLocation ARMOR_MODIFIER = ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "rank_armor");
    private static final float BASE_SOLDIER_HEALTH = 20f;

    private SoldierRanks() {
    }

    public enum Rank {
        SOLDIER("Soldier", 0, 20, 0.0, 0.0),
        SEASONED("Seasoned", 5, 25, 0.10, 0.0),
        SERGEANT("Sergeant", 12, 35, 0.20, 0.0),
        HERO("Hero", 20, 50, 0.35, 0.20),
        LEGEND("Legend", 50, 100, 0.60, 0.40);

        private final String label;
        private final int minKills;
        private final float maxHealth;
        private final double damageBonus;
        private final double damageResistance;

        Rank(String label, int minKills, float maxHealth, double damageBonus, double damageResistance) {
            this.label = label;
            this.minKills = minKills;
            this.maxHealth = maxHealth;
            this.damageBonus = damageBonus;
            this.damageResistance = damageResistance;
        }

        public String label() {
            return label;
        }

        public float maxHealth() {
            return maxHealth;
        }

        public double damageBonus() {
            return damageBonus;
        }

        public double damageResistance() {
            return damageResistance;
        }
    }

    public static Rank rankForKills(int kills) {
        if (kills >= Rank.LEGEND.minKills) {
            return Rank.LEGEND;
        }
        if (kills >= Rank.HERO.minKills) {
            return Rank.HERO;
        }
        if (kills >= Rank.SERGEANT.minKills) {
            return Rank.SERGEANT;
        }
        if (kills >= Rank.SEASONED.minKills) {
            return Rank.SEASONED;
        }
        return Rank.SOLDIER;
    }

    public static void syncRank(Villager villager, SoldierData data) {
        Rank rank = rankForKills(data.getKills());
        data.setRank(rank.label());
        VillagerGearRules.syncArmorEquipment(villager);
        applyAttributes(villager, rank);
        SoldierLabels.apply(villager, data);
    }

    public static void applyAttributes(Villager villager, Rank rank) {
        double weaponDamage = SoldierGearStats.meleeAttackDamage(villager);

        AttributeInstance health = villager.getAttribute(Attributes.MAX_HEALTH);
        if (health != null) {
            health.removeModifier(HEALTH_MODIFIER);
            health.removeModifier(ORPHAN_HEALTH_MODIFIER);
            double healthBonus = rank.maxHealth() - BASE_SOLDIER_HEALTH;
            health.addPermanentModifier(new AttributeModifier(HEALTH_MODIFIER, healthBonus, AttributeModifier.Operation.ADD_VALUE));
            if (OrphanRaised.is(villager)) {
                health.addPermanentModifier(new AttributeModifier(
                        ORPHAN_HEALTH_MODIFIER,
                        Config.ORPHAN_SOLDIER_BONUS_HEALTH.get(),
                        AttributeModifier.Operation.ADD_VALUE));
            }
            if (villager.getHealth() > villager.getMaxHealth()) {
                villager.setHealth(villager.getMaxHealth());
            }
        }
        AttributeInstance damage = villager.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(weaponDamage);
            damage.removeModifier(DAMAGE_MODIFIER);
            damage.removeModifier(ORPHAN_DAMAGE_MODIFIER);
            if (rank.damageBonus() > 0) {
                damage.addPermanentModifier(new AttributeModifier(
                        DAMAGE_MODIFIER, weaponDamage * rank.damageBonus(), AttributeModifier.Operation.ADD_VALUE));
            }
            if (OrphanRaised.is(villager)) {
                damage.addPermanentModifier(new AttributeModifier(
                        ORPHAN_DAMAGE_MODIFIER,
                        weaponDamage * Config.ORPHAN_SOLDIER_BONUS_DAMAGE.get(),
                        AttributeModifier.Operation.ADD_VALUE));
            }
        }
        AttributeInstance armor = villager.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.removeModifier(ARMOR_MODIFIER);
            if (rank.damageResistance() > 0) {
                armor.addPermanentModifier(new AttributeModifier(
                        ARMOR_MODIFIER, rank.damageResistance() * 20.0, AttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    public static double homeDamageMultiplier(Villager villager) {
        if (SoldierCombat.isWithinHomeBedRadius(villager)) {
            return 1.10;
        }
        return 1.0;
    }
}
