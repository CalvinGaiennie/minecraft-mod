package com.villagers.mod.performance;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

/** Tick-budget helpers for background work. */
public final class ServerPerformance {
    /** Skip optional background work when the server is already behind. */
    public static final float MAX_AVERAGE_TICK_MS_FOR_BACKGROUND = 50.0f;

    private ServerPerformance() {
    }

    public static boolean hasTickBudget(ServerLevel level) {
        return hasTickBudget(level.getServer(), level);
    }

    public static boolean hasTickBudget(MinecraftServer server, ServerLevel level) {
        long[] times = server.getTickTime(level.dimension());
        if (times == null || times.length == 0) {
            return true;
        }
        long sum = 0L;
        int count = 0;
        for (long nanos : times) {
            if (nanos <= 0L) {
                continue;
            }
            sum += nanos;
            count++;
        }
        if (count == 0) {
            return true;
        }
        float avgMs = (sum / (float) count) / 1_000_000f;
        return avgMs <= MAX_AVERAGE_TICK_MS_FOR_BACKGROUND;
    }
}
