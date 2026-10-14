package com.villagers.mod.performance;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import com.villagers.mod.player.EnlistmentService;
import com.villagers.mod.threat.NewMoonRaidService;
import com.villagers.mod.threat.SwarmService;
import com.villagers.mod.threat.WanderingNecromancerService;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Spreads nightly threat rolls and new-moon checks across ticks. */
public final class ThreatWorkScheduler {
    public static final int SWARM_PATH_INTERVAL_TICKS = 60;
    public static final int NIGHTLY_PLAYER_INTERVAL_TICKS = 40;
    public static final int NEW_MOON_PLAYER_INTERVAL_TICKS = 200;

    private static final Map<ResourceKey<Level>, Deque<ServerPlayer>> NIGHTLY_QUEUE = new HashMap<>();
    private static final Map<ResourceKey<Level>, Integer> NEW_MOON_INDEX = new HashMap<>();

    private ThreatWorkScheduler() {
    }

    public static void onNewDay(ServerLevel level, long day) {
        List<ServerPlayer> enlisted = new ArrayList<>();
        for (ServerPlayer player : level.players()) {
            if (EnlistmentService.isEnlisted(player)) {
                enlisted.add(player);
            }
        }
        NIGHTLY_QUEUE.put(level.dimension(), new ArrayDeque<>(enlisted));
        WanderingNecromancerService.tryDailyStrikes(level, day);
    }

    public static void tick(ServerLevel level, long gameTime) {
        if (gameTime % SWARM_PATH_INTERVAL_TICKS == 0) {
            SwarmService.tick(level);
        }
        Deque<ServerPlayer> nightly = NIGHTLY_QUEUE.get(level.dimension());
        if (nightly != null && !nightly.isEmpty() && gameTime % NIGHTLY_PLAYER_INTERVAL_TICKS == 0) {
            ServerPlayer player = nightly.pollFirst();
            if (player != null && player.isAlive()) {
                SwarmService.trySpawnNightly(level, player);
            }
            if (nightly.isEmpty()) {
                NIGHTLY_QUEUE.remove(level.dimension());
            }
        }
        if (gameTime % NEW_MOON_PLAYER_INTERVAL_TICKS != 0) {
            return;
        }
        List<ServerPlayer> players = level.players();
        if (players.isEmpty()) {
            return;
        }
        int index = NEW_MOON_INDEX.getOrDefault(level.dimension(), 0);
        NewMoonRaidService.tickForPlayer(level, players.get(index % players.size()));
        NEW_MOON_INDEX.put(level.dimension(), index + 1);
    }
}
