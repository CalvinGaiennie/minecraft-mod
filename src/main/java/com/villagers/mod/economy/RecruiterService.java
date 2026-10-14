package com.villagers.mod.economy;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.villagers.mod.block.RecruiterBlock;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.squad.SquadService;
import com.villagers.mod.util.SoldierStructureHelper;

public final class RecruiterService {
    public static final int SCAN = 48;
    public static final long TICKS_PER_DAY = 24000L;

    private RecruiterService() {
    }

    /** One player origin — used by {@link com.villagers.mod.performance.DayEconomyWorkQueue}. */
    public static void tickNearPlayer(ServerLevel level, BlockPos origin) {
        tickNearRecruiters(level, origin);
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
            recruiter.syncActiveRecruitsFromWorld(level);
            SquadSupplyService.tickDailySupply(level, recruiter);
            if (!recruiter.canAcceptRecruit()) {
                continue;
            }
            if (recruiter.getSquadMode() == com.villagers.mod.squad.SquadMode.RECRUIT) {
                tryRecruitNear(level, pos, recruiter);
            }
        }
    }

    private static void tryRecruitNear(ServerLevel level, BlockPos recruiterPos, RecruiterBlockEntity recruiter) {
        if (recruiter.getSquadLeaderId() == null) {
            return;
        }
        for (var villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(recruiterPos).inflate(SoldierStructureHelper.SCAN_RADIUS))) {
            if (recruiter.getActiveRecruits() >= RecruiterBlockEntity.MAX_RECRUITS) {
                return;
            }
            SquadService.tryEnlistAndAssign(level, recruiter, villager);
        }
    }

}
