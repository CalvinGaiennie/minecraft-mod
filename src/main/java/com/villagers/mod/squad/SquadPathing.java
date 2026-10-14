package com.villagers.mod.squad;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.pathfinder.Path;

import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

/** Squad members follow their leader's path instead of each pathfinding alone. */
public final class SquadPathing {
    private SquadPathing() {
    }

    public static void tickFollowLeader(ServerLevel level, Villager villager, SoldierData data) {
        BlockPos boxPos = data.getSquadBoxPos();
        if (boxPos == null || data.isOnCampaign()) {
            return;
        }
        List<Villager> members = SquadService.members(level, boxPos);
        if (members.size() < 2) {
            return;
        }
        Villager leader = resolveLeader(level, boxPos, members);
        if (leader == null || leader.getUUID().equals(villager.getUUID())) {
            return;
        }
        if (!(villager instanceof Mob follower)) {
            return;
        }
        if (leader.getNavigation().isDone()) {
            return;
        }
        if (follower.distanceToSqr(leader) < 4 * 4) {
            return;
        }
        Path leaderPath = leader.getNavigation().getPath();
        if (leaderPath == null || leaderPath.getNodeCount() == 0) {
            follower.getNavigation().moveTo(leader, 1.0);
            return;
        }
        int node = Math.min(leaderPath.getNextNodeIndex() + 1, leaderPath.getNodeCount() - 1);
        var target = leaderPath.getNode(node);
        follower.getNavigation().moveTo(target.x, target.y, target.z, 1.0);
    }

    @Nullable
    private static Villager resolveLeader(ServerLevel level, BlockPos boxPos, List<Villager> members) {
        if (level.getBlockEntity(boxPos) instanceof RecruiterBlockEntity box && box.getSquadLeaderId() != null) {
            for (Villager member : members) {
                if (member.getUUID().equals(box.getSquadLeaderId())) {
                    return member;
                }
            }
        }
        return members.stream()
                .max(Comparator.comparingInt(v -> {
                    SoldierData d = v.getData(VillagerAttachments.SOLDIER_DATA.get());
                    return d == null ? 0 : d.getKills();
                }))
                .orElse(null);
    }
}
