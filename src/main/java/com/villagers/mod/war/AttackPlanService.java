package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.block.entity.CampBlockEntity;
import com.villagers.mod.network.AttackPlanOpenPayload;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.squad.SquadService;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class AttackPlanService {
    public static final int ASSAULT_ENGAGE_RADIUS = 32;

    private AttackPlanService() {
    }

    public static void savePlan(
            ServerPlayer player, Set<Long> squadBoxes, @Nullable BlockPos target, int siegeLadders) {
        AttackPlanSavedData.get(player.serverLevel())
                .setPlan(player.getUUID(), new AttackPlanSavedData.Plan(Set.copyOf(squadBoxes), target, siegeLadders));
    }

    public static AttackPlanSavedData.Plan planFor(UUID owner, ServerLevel level) {
        return AttackPlanSavedData.get(level).getPlan(owner);
    }

    public static List<AttackPlanOpenPayload.SquadEntry> listChargeSquads(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        List<AttackPlanOpenPayload.SquadEntry> out = new ArrayList<>();
        AttackPlanSavedData.Plan plan = planFor(player.getUUID(), level);
        for (BlockPos pos : BlockPos.betweenClosed(
                player.blockPosition().offset(-256, -64, -256),
                player.blockPosition().offset(256, 64, 256))) {
            if (!(level.getBlockEntity(pos) instanceof RecruiterBlockEntity box)) {
                continue;
            }
            if (!box.isSquadOnCampaign()) {
                continue;
            }
            int members = SquadService.memberCount(level, pos);
            if (members == 0) {
                continue;
            }
            boolean selected = plan.squadBoxes().isEmpty() || plan.squadBoxes().contains(pos.asLong());
            out.add(new AttackPlanOpenPayload.SquadEntry(pos.asLong(), members, selected));
        }
        return out;
    }

    public static boolean squadSelectedForCharge(UUID commanderId, ServerLevel level, @Nullable BlockPos squadBox) {
        AttackPlanSavedData.Plan plan = planFor(commanderId, level);
        if (squadBox == null) {
            return false;
        }
        if (plan.squadBoxes().isEmpty()) {
            return true;
        }
        return plan.squadBoxes().contains(squadBox.asLong());
    }

    @Nullable
    public static BlockPos chargeTarget(UUID commanderId, ServerLevel level) {
        return planFor(commanderId, level).target();
    }

    public static void applyToCamp(ServerLevel level, BlockPos campPos, ServerPlayer player, BlockPos target, int ladders) {
        if (!(level.getBlockEntity(campPos) instanceof CampBlockEntity camp)) {
            return;
        }
        Set<Long> squads = new java.util.HashSet<>();
        for (Villager villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(campPos).inflate(64))) {
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null && data.isOnCampaign() && data.getSquadBoxPos() != null) {
                squads.add(data.getSquadBoxPos().asLong());
            }
        }
        savePlan(player, squads, target, ladders);
    }
}
