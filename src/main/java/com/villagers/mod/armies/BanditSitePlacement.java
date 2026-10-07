package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.Heightmap;
import com.mojang.datafixers.util.Pair;

import java.util.Optional;

/** Kind-specific world placement (caves, villages, outposts). */
public final class BanditSitePlacement {
    private static final ResourceKey<Structure> PILLAGER_OUTPOST =
            ResourceKey.create(Registries.STRUCTURE, ResourceLocation.withDefaultNamespace("pillager_outpost"));
    private static final int VILLAGE_SEARCH_CHUNK_RADIUS = 48;
    private static final int OUTPOST_SEARCH_CHUNK_RADIUS = 32;
    private static final int VILLAGE_OCCUPATION_SEPARATION = 96;

    private BanditSitePlacement() {
    }

    public static BlockPos sampleSiteOrigin(
            ServerLevel level,
            BanditWorldSavedData data,
            BlockPos worldSpawn,
            RandomSource random,
            BanditWorldSavedData.SiteKind kind,
            int minDist,
            int maxDist,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        return switch (kind) {
            case CAMP -> BanditSiteGenerator.samplePlacementPos(level, worldSpawn, random, minDist, maxDist, chunkBudget);
            case HIDEOUT -> sampleHideoutCave(level, worldSpawn, random, minDist, maxDist, chunkBudget);
            case CORVIN -> sampleVillageInRing(level, data, worldSpawn, random, minDist, maxDist, chunkBudget);
            case GARLAND -> sampleGarlandSite(level, data, worldSpawn, random, minDist, maxDist, chunkBudget);
        };
    }

    /** Citadel approach camps always claim a vanilla village on the ring. */
    public static BlockPos sampleShadowCampAtVillage(
            ServerLevel level,
            BanditWorldSavedData data,
            BlockPos ringSearch,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        BlockPos village = findUnclaimedVillageNear(level, data, ringSearch, chunkBudget);
        if (village == null) {
            return null;
        }
        return pickCampSurfaceAt(level, village.getX(), village.getZ(), chunkBudget);
    }

    private static BlockPos sampleHideoutCave(
            ServerLevel level,
            BlockPos spawn,
            RandomSource random,
            int minDist,
            int maxDist,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        for (int attempt = 0; attempt < 12; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2;
            int dist = minDist + random.nextInt(Math.max(1, maxDist - minDist));
            int x = spawn.getX() + (int) (Math.cos(angle) * dist);
            int z = spawn.getZ() + (int) (Math.sin(angle) * dist);
            BlockPos cave = pickCaveFloor(level, x, z, chunkBudget, random);
            if (cave != null && ringDistanceOk(spawn, cave, minDist, maxDist)) {
                return cave;
            }
        }
        return null;
    }

    private static BlockPos sampleVillageInRing(
            ServerLevel level,
            BanditWorldSavedData data,
            BlockPos spawn,
            RandomSource random,
            int minDist,
            int maxDist,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        for (int attempt = 0; attempt < 24; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2;
            int dist = minDist + random.nextInt(Math.max(1, maxDist - minDist));
            int x = spawn.getX() + (int) (Math.cos(angle) * dist);
            int z = spawn.getZ() + (int) (Math.sin(angle) * dist);
            if (!chunkBudget.ensureChunk(x >> 4, z >> 4)) {
                continue;
            }
            BlockPos village = findUnclaimedVillageNear(level, data, new BlockPos(x, 64, z), chunkBudget);
            if (village == null) {
                continue;
            }
            BlockPos surface = pickCampSurfaceAt(level, village.getX(), village.getZ(), chunkBudget);
            if (surface != null && ringDistanceOk(spawn, surface, minDist, maxDist)) {
                return surface;
            }
        }
        return null;
    }

    private static BlockPos sampleGarlandSite(
            ServerLevel level,
            BanditWorldSavedData data,
            BlockPos spawn,
            RandomSource random,
            int minDist,
            int maxDist,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        BlockPos villageSite = sampleVillageInRing(level, data, spawn, random, minDist, maxDist, chunkBudget);
        if (villageSite == null) {
            return null;
        }
        BlockPos outpost = findNearestStructure(level, PILLAGER_OUTPOST, villageSite, OUTPOST_SEARCH_CHUNK_RADIUS, chunkBudget);
        if (outpost != null && outpost.distSqr(villageSite) < 256 * 256L) {
            BlockPos atOutpost = pickCampSurfaceAt(level, outpost.getX(), outpost.getZ(), chunkBudget);
            if (atOutpost != null) {
                return atOutpost;
            }
        }
        return villageSite;
    }

    private static BlockPos findUnclaimedVillageNear(
            ServerLevel level,
            BanditWorldSavedData data,
            BlockPos searchFrom,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        if (!chunkBudget.ensureChunk(searchFrom.getX() >> 4, searchFrom.getZ() >> 4)) {
            return null;
        }
        BlockPos village = level.findNearestMapStructure(StructureTags.VILLAGE, searchFrom, VILLAGE_SEARCH_CHUNK_RADIUS, false);
        if (village == null || villageOccupied(data, village)) {
            return null;
        }
        return village;
    }

    static boolean villageOccupied(BanditWorldSavedData data, BlockPos villageCenter) {
        long minSq = (long) VILLAGE_OCCUPATION_SEPARATION * VILLAGE_OCCUPATION_SEPARATION;
        for (var site : data.sites()) {
            if (site.origin().distSqr(villageCenter) < minSq) {
                return true;
            }
        }
        return false;
    }

    private static BlockPos findNearestStructure(
            ServerLevel level,
            ResourceKey<Structure> key,
            BlockPos from,
            int chunkRadius,
            BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        if (!chunkBudget.ensureChunk(from.getX() >> 4, from.getZ() >> 4)) {
            return null;
        }
        var registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Optional<Holder.Reference<Structure>> holder = registry.getHolder(key);
        if (holder.isEmpty()) {
            return null;
        }
        HolderSet<Structure> set = HolderSet.direct(holder.get());
        Pair<BlockPos, Holder<Structure>> pair = level.getChunkSource()
                .getGenerator()
                .findNearestMapStructure(level, set, from, chunkRadius, false);
        return pair != null ? pair.getFirst() : null;
    }

    private static boolean ringDistanceOk(BlockPos spawn, BlockPos site, int minDist, int maxDist) {
        long distSq = horizontalDistSq(spawn, site);
        long minSq = (long) minDist * minDist;
        long maxSq = (long) maxDist * maxDist;
        return distSq >= minSq && distSq <= maxSq;
    }

    private static long horizontalDistSq(BlockPos a, BlockPos b) {
        long dx = a.getX() - b.getX();
        long dz = a.getZ() - b.getZ();
        return dx * dx + dz * dz;
    }

    private static BlockPos pickCampSurfaceAt(
            ServerLevel level, int x, int z, BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        return pickCampSurface(level, x, z, chunkBudget);
    }

    private static BlockPos pickCaveFloor(
            ServerLevel level, int x, int z, BanditGenerationScheduler.ChunkGenBudget chunkBudget, RandomSource random) {
        if (!chunkBudget.ensureChunk(x >> 4, z >> 4)) {
            return null;
        }
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        int startY = Math.min(surfaceY - 6, surfaceY);
        for (int ring = 0; ring < 10; ring++) {
            int dx = ring == 0 ? 0 : random.nextInt(ring * 2 + 1) - ring;
            int dz = ring == 0 ? 0 : random.nextInt(ring * 2 + 1) - ring;
            int cx = x + dx;
            int cz = z + dz;
            if (!chunkBudget.ensureChunk(cx >> 4, cz >> 4)) {
                continue;
            }
            int localSurface = level.getHeight(Heightmap.Types.WORLD_SURFACE, cx, cz);
            for (int y = Math.min(localSurface - 8, 56); y > level.getMinBuildHeight() + 12; y--) {
                BlockPos floor = new BlockPos(cx, y, cz);
                if (isCaveFloor(level, floor) && caveHeadroom(level, floor, 2)) {
                    return floor;
                }
            }
        }
        return null;
    }

    private static boolean isCaveFloor(ServerLevel level, BlockPos floor) {
        if (!level.getBlockState(floor).isAir()) {
            return false;
        }
        BlockState ground = level.getBlockState(floor.below());
        if (ground.isAir() || !ground.isSolidRender(level, floor.below())) {
            return false;
        }
        return level.getFluidState(floor).isEmpty() && level.getFluidState(floor.above()).isEmpty();
    }

    private static boolean caveHeadroom(ServerLevel level, BlockPos floor, int height) {
        for (int dy = 1; dy <= height; dy++) {
            if (!level.getBlockState(floor.above(dy)).isAir()) {
                return false;
            }
        }
        return true;
    }

    private static BlockPos pickCampSurface(
            ServerLevel level, int x, int z, BanditGenerationScheduler.ChunkGenBudget chunkBudget) {
        int chunkX = x >> 4;
        int chunkZ = z >> 4;
        if (!chunkBudget.ensureChunk(chunkX, chunkZ)) {
            return null;
        }
        BlockPos top = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));
        if (isSupportedCampSurface(level, top)) {
            return top;
        }
        for (int dy = 1; dy <= 12; dy++) {
            BlockPos up = top.above(dy);
            if (isSupportedCampSurface(level, up)) {
                return up;
            }
            BlockPos down = top.below(dy);
            if (isSupportedCampSurface(level, down)) {
                return down;
            }
        }
        return null;
    }

    private static boolean isSupportedCampSurface(ServerLevel level, BlockPos surface) {
        if (!level.getFluidState(surface).isEmpty() || !level.getFluidState(surface.above()).isEmpty()) {
            return false;
        }
        var ground = level.getBlockState(surface.below());
        return !ground.isAir() && ground.isSolidRender(level, surface.below());
    }
}
