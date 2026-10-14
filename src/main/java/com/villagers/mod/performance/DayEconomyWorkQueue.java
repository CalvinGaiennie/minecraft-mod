package com.villagers.mod.performance;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.villagers.mod.block.entity.SupplyDepotBlockEntity;
import com.villagers.mod.economy.RecruiterService;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Spreads dawn recruiter/supply work across ticks instead of one spike. */
public final class DayEconomyWorkQueue {
    /** ~1 s between each recruiter/supply block on day rollover. */
    public static final int TICKS_BETWEEN_JOBS = 20;

    private enum JobKind {
        RECRUITER_PLAYER,
        SUPPLY_DEPOT
    }

    private record Job(JobKind kind, BlockPos pos, ServerLevel level) {
    }

    private static final Map<ResourceKey<Level>, Deque<Job>> QUEUES = new HashMap<>();
    private static final Map<ResourceKey<Level>, Long> LAST_JOB_TICK = new HashMap<>();

    private DayEconomyWorkQueue() {
    }

    public static void enqueueDayRollover(ServerLevel level) {
        ResourceKey<Level> dim = level.dimension();
        Deque<Job> queue = new ArrayDeque<>();
        for (ServerPlayer player : level.players()) {
            queue.add(new Job(JobKind.RECRUITER_PLAYER, player.blockPosition(), level));
            for (BlockPos pos : BlockPos.betweenClosed(
                    player.blockPosition().offset(-48, -8, -48),
                    player.blockPosition().offset(48, 8, 48))) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof SupplyDepotBlockEntity) {
                    queue.add(new Job(JobKind.SUPPLY_DEPOT, pos.immutable(), level));
                }
            }
        }
        QUEUES.put(dim, queue);
        LAST_JOB_TICK.put(dim, level.getGameTime());
    }

    public static void tick(ServerLevel level) {
        Deque<Job> queue = QUEUES.get(level.dimension());
        if (queue == null || queue.isEmpty()) {
            return;
        }
        long now = level.getGameTime();
        long last = LAST_JOB_TICK.getOrDefault(level.dimension(), now);
        if (now - last < TICKS_BETWEEN_JOBS) {
            return;
        }
        LAST_JOB_TICK.put(level.dimension(), now);
        Job job = queue.pollFirst();
        if (job == null) {
            return;
        }
        long week = level.getDayTime() / 24000L / 7L;
        switch (job.kind()) {
            case RECRUITER_PLAYER -> RecruiterService.tickNearPlayer(job.level(), job.pos());
            case SUPPLY_DEPOT -> {
                BlockEntity be = job.level().getBlockEntity(job.pos());
                if (be instanceof SupplyDepotBlockEntity depot) {
                    depot.tickWeeklySupply(week);
                }
            }
        }
    }
}
