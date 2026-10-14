package com.villagers.mod.squad;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import com.villagers.mod.combat.CombatTargetAssign;
import com.villagers.mod.combat.SoldierCombat;
import com.villagers.mod.combat.SoldierCombatConstants;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.performance.CombatVillagerIndex;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Squads share one hostile scan twice per second instead of every soldier scanning every tick. */
public final class SquadCombatScan {
    private record SquadKey(ResourceKey<Level> dim, long boxPacked) {}

    private record Cache(UUID targetId, long expiresGameTime) {}

    private static final Map<SquadKey, Cache> SHARED = new HashMap<>();

    private SquadCombatScan() {
    }

    public static void tickLevel(ServerLevel level) {
        if (level.getGameTime() % 10 != 0) {
            return;
        }
        Map<BlockPos, List<Villager>> byBox = new HashMap<>();
        CombatVillagerIndex.forEachAlive(level, villager -> {
            if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                return;
            }
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data == null || data.getSquadBoxPos() == null) {
                return;
            }
            byBox.computeIfAbsent(data.getSquadBoxPos(), p -> new java.util.ArrayList<>()).add(villager);
        });
        long expire = level.getGameTime() + 15;
        for (var entry : byBox.entrySet()) {
            BlockPos boxPos = entry.getKey();
            List<Villager> members = entry.getValue();
            if (members.isEmpty()) {
                continue;
            }
            Villager probe = members.get(0);
            LivingEntity hostile = SoldierCombat.findVisibleHostile(probe, SoldierCombatConstants.WAKE_HOSTILE_RANGE);
            SquadKey key = new SquadKey(level.dimension(), boxPos.asLong());
            if (hostile != null) {
                SHARED.put(key, new Cache(hostile.getUUID(), expire));
            } else {
                SHARED.remove(key);
            }
        }
    }

    @Nullable
    public static LivingEntity sharedTarget(ServerLevel level, Villager self, SoldierData data) {
        BlockPos boxPos = data.getSquadBoxPos();
        if (boxPos == null) {
            return null;
        }
        SquadKey key = new SquadKey(level.dimension(), boxPos.asLong());
        Cache cache = SHARED.get(key);
        if (cache == null || cache.expiresGameTime() < level.getGameTime()) {
            return null;
        }
        if (level.getEntity(cache.targetId()) instanceof LivingEntity living && living.isAlive()) {
            return living;
        }
        return null;
    }

    public static void applySharedTarget(Villager villager, SoldierData data) {
        if (!(villager.level() instanceof ServerLevel server) || !(villager instanceof Mob mob)) {
            return;
        }
        LivingEntity shared = sharedTarget(server, villager, data);
        if (shared != null && (mob.getTarget() == null || !mob.getTarget().isAlive())) {
            CombatTargetAssign.assign(mob, shared);
        }
    }
}
