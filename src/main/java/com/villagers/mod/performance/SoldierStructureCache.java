package com.villagers.mod.performance;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Short-lived nearest-structure results to avoid repeated block scans. */
public final class SoldierStructureCache {
    public static final int TTL_TICKS = 60;

    private enum Kind {
        MESS,
        RAMPART,
        /** {@link com.villagers.mod.combat.RampartPathing#RAMPART_SEEK_RADIUS} scans (chunk-shared). */
        RAMPART_WIDE,
        ARROW_RESUPPLY
    }

    private record Key(ChunkPos chunk, Kind kind) {
    }

    private record Entry(@Nullable BlockPos pos, long gameTime) {
    }

    private static final Map<Key, Entry> CACHE = new HashMap<>();

    private SoldierStructureCache() {
    }

    public static Optional<BlockPos> getMess(Level level, BlockPos origin, long gameTime) {
        return get(level, origin, Kind.MESS, gameTime);
    }

    public static void putMess(Level level, BlockPos origin, long gameTime, Optional<BlockPos> found) {
        put(level, origin, Kind.MESS, gameTime, found.orElse(null));
    }

    public static Optional<BlockPos> getRampart(Level level, BlockPos origin, long gameTime) {
        return get(level, origin, Kind.RAMPART, gameTime);
    }

    public static void putRampart(Level level, BlockPos origin, long gameTime, Optional<BlockPos> found) {
        put(level, origin, Kind.RAMPART, gameTime, found.orElse(null));
    }

    public static Optional<BlockPos> getRampartWide(Level level, BlockPos origin, long gameTime) {
        return get(level, origin, Kind.RAMPART_WIDE, gameTime);
    }

    public static void putRampartWide(Level level, BlockPos origin, long gameTime, Optional<BlockPos> found) {
        put(level, origin, Kind.RAMPART_WIDE, gameTime, found.orElse(null));
    }

    public static Optional<BlockPos> getArrowResupply(Level level, BlockPos origin, long gameTime) {
        return get(level, origin, Kind.ARROW_RESUPPLY, gameTime);
    }

    public static void putArrowResupply(Level level, BlockPos origin, long gameTime, Optional<BlockPos> found) {
        put(level, origin, Kind.ARROW_RESUPPLY, gameTime, found.orElse(null));
    }

    private static Optional<BlockPos> get(Level level, BlockPos origin, Kind kind, long gameTime) {
        Entry entry = CACHE.get(new Key(new ChunkPos(origin), kind));
        if (entry == null || gameTime - entry.gameTime() > TTL_TICKS) {
            return null;
        }
        return Optional.ofNullable(entry.pos());
    }

    private static void put(Level level, BlockPos origin, Kind kind, long gameTime, @Nullable BlockPos pos) {
        CACHE.put(new Key(new ChunkPos(origin), kind), new Entry(pos, gameTime));
    }
}
