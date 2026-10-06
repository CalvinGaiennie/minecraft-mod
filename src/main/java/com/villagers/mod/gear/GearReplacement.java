package com.villagers.mod.gear;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.villagers.mod.block.RecruiterBlock;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.entity.VillagerAttachments;

public final class GearReplacement {
    private static final int SCAN = 32;

    private GearReplacement() {
    }

    /** Pull missing enlist gear from the nearest recruiter box (within {@link #SCAN} blocks). */
    public static void tryPullEnlistGearFromNearestRecruiter(Villager villager) {
        if (villager.level().isClientSide || !(villager.level() instanceof ServerLevel level)) {
            return;
        }
        if (villager.isBaby() || villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        if (!lacksEnlistGear(villager)) {
            return;
        }
        RecruiterBlockEntity recruiter = findNearestRecruiter(level, villager.blockPosition());
        if (recruiter == null) {
            return;
        }
        pullEnlistGear(villager, recruiter);
    }

    public static void tryPullEnlistGear(Villager villager, RecruiterBlockEntity recruiter) {
        if (villager.level().isClientSide || villager.isBaby()
                || villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        pullEnlistGear(villager, recruiter);
    }

    public static void tryReplaceBrokenGear(Villager soldier) {
        if (soldier.level().isClientSide || !(soldier.level() instanceof ServerLevel level)) {
            return;
        }
        BlockPos origin = soldier.blockPosition();
        RecruiterBlockEntity recruiter = findNearestRecruiter(level, origin);
        if (recruiter == null) {
            return;
        }
        for (VillagerGearSlot slot : new VillagerGearSlot[] {
                VillagerGearSlot.HELMET, VillagerGearSlot.CHESTPLATE, VillagerGearSlot.MELEE, VillagerGearSlot.RANGED,
                VillagerGearSlot.ARROWS }) {
            ItemStack current = VillagerGearRules.get(soldier, slot);
            if (!needsRecruiterGear(slot, current)) {
                continue;
            }
            ItemStack replacement = recruiter.withdrawMatchingGear(slot);
            if (!replacement.isEmpty()) {
                VillagerGearRules.set(soldier, slot, replacement);
            }
        }
    }

    private static void pullEnlistGear(Villager villager, RecruiterBlockEntity recruiter) {
        for (VillagerGearSlot slot : new VillagerGearSlot[] {
                VillagerGearSlot.HELMET, VillagerGearSlot.CHESTPLATE, VillagerGearSlot.MELEE }) {
            pullSlotIfNeeded(villager, recruiter, slot);
        }
        if (SoldierConversion.canBecomeSoldier(villager)) {
            return;
        }
        for (VillagerGearSlot slot : new VillagerGearSlot[] {
                VillagerGearSlot.BOOTS, VillagerGearSlot.RANGED }) {
            pullSlotIfNeeded(villager, recruiter, slot);
        }
    }

    private static void pullSlotIfNeeded(Villager villager, RecruiterBlockEntity recruiter, VillagerGearSlot slot) {
        ItemStack current = VillagerGearRules.get(villager, slot);
        if (!needsRecruiterGear(slot, current)) {
            return;
        }
        ItemStack pulled = recruiter.withdrawMatchingGear(slot);
        if (!pulled.isEmpty()) {
            VillagerGearRules.set(villager, slot, pulled);
        }
    }

    private static boolean lacksEnlistGear(Villager villager) {
        for (VillagerGearSlot slot : new VillagerGearSlot[] {
                VillagerGearSlot.HELMET, VillagerGearSlot.CHESTPLATE, VillagerGearSlot.MELEE,
                VillagerGearSlot.BOOTS, VillagerGearSlot.RANGED }) {
            if (needsRecruiterGear(slot, VillagerGearRules.get(villager, slot))) {
                return true;
            }
        }
        return false;
    }

    private static boolean needsRecruiterGear(VillagerGearSlot slot, ItemStack current) {
        if (current.isEmpty()) {
            return true;
        }
        if (current.isDamageableItem() && current.getDamageValue() >= current.getMaxDamage()) {
            return true;
        }
        return !VillagerGearRules.accepts(slot, current);
    }

    public static RecruiterBlockEntity findNearestRecruiter(ServerLevel level, BlockPos origin) {
        RecruiterBlockEntity nearest = null;
        double best = Double.MAX_VALUE;
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN, -5, -SCAN), origin.offset(SCAN, 5, SCAN))) {
            if (!(level.getBlockState(pos).getBlock() instanceof RecruiterBlock)) {
                continue;
            }
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof RecruiterBlockEntity recruiter) {
                double dist = origin.distSqr(pos);
                if (dist < best) {
                    best = dist;
                    nearest = recruiter;
                }
            }
        }
        return nearest;
    }
}
