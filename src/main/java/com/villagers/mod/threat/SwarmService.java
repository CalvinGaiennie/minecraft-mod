package com.villagers.mod.threat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.player.EnlistmentService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SwarmService {
    public static final double SPAWN_CHANCE = 0.05;
    public static final int MAX_SWARMS_PER_PLAYER = 2;

    private SwarmService() {
    }

    public static void trySpawnNightly(ServerLevel level, ServerPlayer player) {
        if (!EnlistmentService.isEnlisted(player)) {
            return;
        }
        SwarmSavedData data = SwarmSavedData.get(level);
        data.purgeDead(level);
        long owned = data.swarms().stream().filter(s -> s.members().stream()
                .anyMatch(id -> {
                    var e = level.getEntity(id);
                    return e != null && e.distanceToSqr(player) < 256 * 256;
                })).count();
        if (owned >= MAX_SWARMS_PER_PLAYER) {
            return;
        }
        if (level.random.nextDouble() > SPAWN_CHANCE) {
            return;
        }
        EntityType<? extends Mob> type = level.random.nextBoolean() ? EntityType.ZOMBIE : EntityType.SKELETON;
        BlockPos target = player.blockPosition();
        int count = 5 + level.random.nextInt(6);
        List<UUID> members = new ArrayList<>();
        UUID swarmId = UUID.randomUUID();
        for (int i = 0; i < count; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double dist = 24 + level.random.nextInt(16);
            Vec3 pos = player.position().add(Math.cos(angle) * dist, 0, Math.sin(angle) * dist);
            Mob mob = type.create(level);
            if (mob == null) {
                continue;
            }
            mob.moveTo(pos.x, pos.y, pos.z, level.random.nextFloat() * 360, 0);
            mob.setCustomName(net.minecraft.network.chat.Component.literal("Swarm"));
            mob.setCustomNameVisible(false);
            mob.setPersistenceRequired();
            level.addFreshEntity(mob);
            members.add(mob.getUUID());
        }
        if (!members.isEmpty()) {
            data.add(new SwarmSavedData.SwarmRecord(swarmId, type.toShortString(), target, members));
        }
    }

    public static void tick(ServerLevel level) {
        SwarmSavedData data = SwarmSavedData.get(level);
        data.purgeDead(level);
        for (SwarmSavedData.SwarmRecord swarm : data.swarms()) {
            BlockPos target = swarm.target();
            for (UUID id : swarm.members()) {
                if (!(level.getEntity(id) instanceof Monster monster)) {
                    continue;
                }
                if (monster.getNavigation().isDone()) {
                    monster.getNavigation().moveTo(
                            target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 1.0);
                }
            }
        }
    }
}
