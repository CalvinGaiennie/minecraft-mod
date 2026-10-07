package com.villagers.mod.economy;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.villagers.mod.block.RecruiterBlock;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.GearReplacement;
import com.villagers.mod.util.SoldierStructureHelper;

public final class RecruiterService {
    private static final int SCAN = 48;

    private RecruiterService() {
    }

    public static void tickLevel(ServerLevel level) {
        for (var player : level.players()) {
            tickNearRecruiters(level, player.blockPosition());
        }
    }

    private static void tickNearRecruiters(ServerLevel level, BlockPos origin) {
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-SCAN, -8, -SCAN), origin.offset(SCAN, 8, SCAN))) {
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof RecruiterBlockEntity recruiter)) {
                continue;
            }
            if (!(level.getBlockState(pos).getBlock() instanceof RecruiterBlock)) {
                continue;
            }
            if (!SoldierStructureHelper.hasPostBlockNear(level, pos)) {
                continue;
            }
            syncActiveRecruits(level, pos, recruiter);
            if (recruiter.canAcceptRecruit()) {
                tryRecruitNear(level, pos, recruiter);
            }
        }
    }

    private static void syncActiveRecruits(ServerLevel level, BlockPos recruiterPos, RecruiterBlockEntity recruiter) {
        int count = 0;
        for (var villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(recruiterPos).inflate(SCAN))) {
            if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                count++;
            }
        }
        recruiter.setActiveRecruits(Math.min(count, RecruiterBlockEntity.MAX_RECRUITS));
    }

    private static void tryRecruitNear(ServerLevel level, BlockPos recruiterPos, RecruiterBlockEntity recruiter) {
        for (var villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(recruiterPos).inflate(SoldierStructureHelper.SCAN_RADIUS))) {
            if (recruiter.getActiveRecruits() >= RecruiterBlockEntity.MAX_RECRUITS) {
                return;
            }
            GearReplacement.tryPullEnlistGear(villager, recruiter);
            if (SoldierConversion.tryConvert(villager)) {
                recruiter.setActiveRecruits(recruiter.getActiveRecruits() + 1);
            }
        }
    }

}
