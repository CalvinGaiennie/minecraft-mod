package com.villagers.mod.defense;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.villagers.mod.entity.VillagerAttachments;

/** Applies buffs to soldiers and militia standing under an active beacon beam. */
public final class BeaconSoldierBuffs {
    private static final int BEACON_SCAN = 48;

    private BeaconSoldierBuffs() {
    }

    public static void tick(ServerLevel level, Villager soldier) {
        if (!soldier.hasData(VillagerAttachments.SOLDIER_DATA.get())
                && !soldier.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            return;
        }
        if (!isUnderActiveBeacon(level, soldier.blockPosition())) {
            return;
        }
        soldier.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 220, 0, true, false));
        soldier.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 220, 0, true, false));
        soldier.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 220, 0, true, false));
    }

    private static boolean isUnderActiveBeacon(ServerLevel level, BlockPos feet) {
        for (int dy = 0; dy <= BEACON_SCAN; dy++) {
            BlockPos scan = feet.above(dy);
            if (!level.getBlockState(scan).is(Blocks.BEACON)) {
                continue;
            }
            BlockEntity be = level.getBlockEntity(scan);
            if (be == null) {
                continue;
            }
            return be.getBlockState().is(Blocks.BEACON);
        }
        return false;
    }
}
