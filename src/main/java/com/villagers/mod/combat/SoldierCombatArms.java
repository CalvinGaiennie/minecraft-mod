package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.util.SoldierStructureHelper;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Syncs kit bow/melee to entity hands and fires ranged shots using kit arrows. */
public final class SoldierCombatArms {
    private static final int BOW_COOLDOWN_TICKS = 22;
    private static final int CROSSBOW_COOLDOWN_TICKS = 28;
    private static final Map<UUID, Long> nextRangedShotGameTime = new HashMap<>();

    private SoldierCombatArms() {
    }

    public static void clearShootCooldown(UUID villagerId) {
        nextRangedShotGameTime.remove(villagerId);
    }

    @Nullable
    public static BlockPos resolveCombatAnchor(Villager villager) {
        if (!(villager.level() instanceof ServerLevel level)) {
            return null;
        }
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data != null) {
            return SoldierHoldGround.resolveAnchor(level, villager, data);
        }
        return SoldierStructureHelper.findNearestRampart(level, villager.blockPosition()).orElse(null);
    }

    public static boolean hasRangedReady(Villager villager) {
        ItemStack ranged = VillagerGearRules.get(villager, VillagerGearSlot.RANGED);
        if (ranged.isEmpty() || (!ranged.is(Items.BOW) && !ranged.is(Items.CROSSBOW))) {
            return false;
        }
        ItemStack arrows = VillagerGearRules.get(villager, VillagerGearSlot.ARROWS);
        return !arrows.isEmpty();
    }

    public static double rangedMinDistance(Villager villager, LivingEntity target) {
        if (usesRampartRangedRules(villager, target)) {
            return SoldierCombatConstants.RAMPART_RANGED_MIN_DISTANCE;
        }
        if (VillagerGearRules.get(villager, VillagerGearSlot.MELEE).isEmpty()) {
            return SoldierCombatConstants.RAMPART_RANGED_MIN_DISTANCE;
        }
        return SoldierCombatConstants.RANGED_PREFER_MIN_DISTANCE;
    }

    public static boolean inRangedAttackBand(Villager villager, LivingEntity target) {
        double dist = horizontalCombatDistance(villager, target);
        return dist >= rangedMinDistance(villager, target)
                && dist <= SoldierCombatConstants.RANGED_ATTACK_MAX_DISTANCE;
    }

    public static void syncHandsForTarget(Villager villager, @Nullable LivingEntity target) {
        syncKitFromHands(villager);
        if (target == null || !target.isAlive()) {
            equipMelee(villager);
            return;
        }
        if (hasRangedReady(villager) && inRangedAttackBand(villager, target)) {
            equipRanged(villager);
            return;
        }
        double dist = horizontalCombatDistance(villager, target);
        boolean elevatedArcher = villager.getY() > target.getY() + 2.0 && hasRangedReady(villager);
        if (elevatedArcher
                && dist <= SoldierCombatConstants.RANGED_ATTACK_MAX_DISTANCE
                && usesRampartRangedRules(villager, target)) {
            equipRanged(villager);
        } else if (!elevatedArcher
                && dist <= SoldierCombatConstants.MELEE_PREFER_MAX_DISTANCE
                && !VillagerGearRules.get(villager, VillagerGearSlot.MELEE).isEmpty()) {
            equipMelee(villager);
        } else if (hasRangedReady(villager)
                && VillagerGearRules.get(villager, VillagerGearSlot.MELEE).isEmpty()
                && dist <= SoldierCombatConstants.RANGED_ATTACK_MAX_DISTANCE) {
            equipRanged(villager);
        }
    }

    static double horizontalCombatDistance(Villager villager, LivingEntity target) {
        if (villager.level() instanceof ServerLevel level) {
            BlockPos anchor = resolveCombatAnchor(villager);
            if (SoldierRampartShooting.prefersWallArchery(villager, level, anchor)) {
                return Math.sqrt(SoldierRampartShooting.horizontalDistanceSqr(villager, target));
            }
        }
        return villager.distanceTo(target);
    }

    public static void tickBowShot(Villager villager, LivingEntity target) {
        if (!hasRangedReady(villager)) {
            return;
        }
        if (!inRangedAttackBand(villager, target)) {
            return;
        }
        syncKitFromHands(villager);
        ItemStack ranged = VillagerGearRules.get(villager, VillagerGearSlot.RANGED);
        if (!ranged.is(Items.BOW) && !ranged.is(Items.CROSSBOW)) {
            return;
        }
        if (!villager.getMainHandItem().is(Items.BOW) && !villager.getMainHandItem().is(Items.CROSSBOW)) {
            equipRanged(villager);
            ranged = VillagerGearRules.get(villager, VillagerGearSlot.RANGED);
        }
        long gameTime = villager.level().getGameTime();
        long readyAt = nextRangedShotGameTime.getOrDefault(villager.getUUID(), 0L);
        if (gameTime < readyAt) {
            return;
        }
        if (!villager.hasLineOfSight(target)) {
            return;
        }
        int cooldown = ranged.is(Items.CROSSBOW) ? CROSSBOW_COOLDOWN_TICKS : BOW_COOLDOWN_TICKS;
        if (ranged.is(Items.CROSSBOW)) {
            fireCrossbow(villager, target, ranged);
        } else {
            fireBow(villager, target, ranged);
        }
        nextRangedShotGameTime.put(villager.getUUID(), gameTime + cooldown);
    }

    private static boolean usesRampartRangedRules(Villager villager, LivingEntity target) {
        if (!(villager.level() instanceof ServerLevel level)) {
            return false;
        }
        BlockPos anchor = resolveCombatAnchor(villager);
        if (!SoldierRampartShooting.prefersWallArchery(villager, level, anchor)) {
            return false;
        }
        return villager.getY() >= target.getY() + 1.0
                || SoldierRampartShooting.horizontalDistanceSqr(villager, target)
                        < SoldierCombatConstants.RANGED_PREFER_MIN_DISTANCE * SoldierCombatConstants.RANGED_PREFER_MIN_DISTANCE;
    }

    private static void fireBow(Villager shooter, LivingEntity target, ItemStack bow) {
        fireArrow(shooter, target, bow, 1.6f, 3.0f);
    }

    private static void fireCrossbow(Villager shooter, LivingEntity target, ItemStack crossbow) {
        fireArrow(shooter, target, crossbow, 3.15f, 1.0f);
    }

    private static void fireArrow(Villager shooter, LivingEntity target, ItemStack weapon, float velocity, float inaccuracy) {
        ItemStack arrows = VillagerGearRules.get(shooter, VillagerGearSlot.ARROWS);
        if (arrows.isEmpty()) {
            return;
        }
        Level level = shooter.level();
        ItemStack arrowStack = new ItemStack(Items.ARROW);
        AbstractArrow arrow = createArrow(level, shooter, arrowStack, weapon);
        if (arrow == null) {
            return;
        }
        double dx = target.getX() - shooter.getX();
        double dy = target.getY(0.33) - shooter.getY(0.33);
        double dz = target.getZ() - shooter.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        arrow.shoot(dx, dy + horizontal * 0.2, dz, velocity, inaccuracy);
        level.addFreshEntity(arrow);
        arrows.shrink(1);
        if (arrows.isEmpty()) {
            VillagerGearRules.set(shooter, VillagerGearSlot.ARROWS, ItemStack.EMPTY);
            shooter.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        } else {
            VillagerGearRules.set(shooter, VillagerGearSlot.ARROWS, arrows);
            shooter.setItemSlot(EquipmentSlot.OFFHAND, arrows.copy());
        }
        if (weapon.isDamageableItem()) {
            weapon.hurtAndBreak(1, shooter, EquipmentSlot.MAINHAND);
            VillagerGearRules.set(shooter, VillagerGearSlot.RANGED, weapon);
            shooter.setItemSlot(EquipmentSlot.MAINHAND, weapon.copy());
        }
        shooter.swing(InteractionHand.MAIN_HAND);
    }

    @Nullable
    private static AbstractArrow createArrow(Level level, Villager shooter, ItemStack arrowStack, ItemStack weapon) {
        var projectile = ProjectileUtil.getMobArrow(shooter, arrowStack, 1.0f, weapon);
        if (projectile instanceof AbstractArrow arrow) {
            return arrow;
        }
        if (level instanceof ServerLevel serverLevel) {
            return new Arrow(serverLevel, shooter, arrowStack, weapon);
        }
        return null;
    }

    private static void equipRanged(Villager villager) {
        ItemStack ranged = VillagerGearRules.get(villager, VillagerGearSlot.RANGED).copy();
        ItemStack arrows = VillagerGearRules.get(villager, VillagerGearSlot.ARROWS).copy();
        villager.setItemSlot(EquipmentSlot.MAINHAND, ranged);
        villager.setItemSlot(EquipmentSlot.OFFHAND, arrows);
    }

    private static void equipMelee(Villager villager) {
        ItemStack melee = VillagerGearRules.get(villager, VillagerGearSlot.MELEE).copy();
        if (melee.isEmpty()) {
            return;
        }
        villager.setItemSlot(EquipmentSlot.MAINHAND, melee);
        if (villager.getOffhandItem().is(Items.ARROW)) {
            syncKitFromHands(villager);
            villager.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        }
    }

    private static void syncKitFromHands(Villager villager) {
        ItemStack off = villager.getOffhandItem();
        if (off.is(Items.ARROW)) {
            VillagerGearRules.set(villager, VillagerGearSlot.ARROWS, off.copy());
        }
        ItemStack main = villager.getMainHandItem();
        if (main.is(Items.BOW) || main.is(Items.CROSSBOW)) {
            VillagerGearRules.set(villager, VillagerGearSlot.RANGED, main.copy());
        }
    }
}
