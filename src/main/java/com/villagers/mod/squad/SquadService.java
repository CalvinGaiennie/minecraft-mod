package com.villagers.mod.squad;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.bed.SoldierBedClaims;
import com.villagers.mod.bed.VanillaBedHelper;
import com.villagers.mod.block.RampartBlock;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.combat.SoldierRanks;
import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.util.SoldierStructureHelper;
import com.villagers.mod.entity.MilitiaConversion;
import com.villagers.mod.entity.SoldierConversion;
import com.villagers.mod.gear.GearReplacement;
import com.villagers.mod.threat.BanditService;
import com.villagers.mod.threat.WanderingNecromancerService;
import com.villagers.mod.war.CampaignService;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/** Squad box (recruiter block) membership, campaign, leader, and beds. */
public final class SquadService {
    public static final int MAX_MEMBERS = 5;
    public static final int MAX_BEDS = 5;
    public static final int MAX_RAMPARTS = 5;
    /** Reassignment writ links beds/ramparts to the nearest squad box within this horizontal range. */
    public static final int WRIT_NEAR_BOX_RADIUS = 128;
    public static final int CHARGE_SQUAD_RADIUS = 128;
    /** Standing soldiers / militia seek Recruit squads within this horizontal range. */
    public static final int RECRUIT_SEEK_RADIUS = 64;
    /** Assign or enlist when within this range of the squad leader. */
    public static final double RECRUIT_LEADER_JOIN_DISTANCE = 6.0;

    private SquadService() {
    }

    private record RecruitTarget(RecruiterBlockEntity box, Villager leader) {
        BlockPos boxPos() {
            return box.getBlockPos();
        }
    }

    @Nullable
    public static RecruiterBlockEntity squadBox(ServerLevel level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof RecruiterBlockEntity box) {
            return box;
        }
        return null;
    }

    public static List<Villager> members(ServerLevel level, BlockPos boxPos) {
        List<Villager> out = new ArrayList<>();
        for (Villager villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(boxPos).inflate(128))) {
            if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null && boxPos.equals(data.getSquadBoxPos())) {
                out.add(villager);
            }
        }
        return out;
    }

    public static int memberCount(ServerLevel level, BlockPos boxPos) {
        return members(level, boxPos).size();
    }

    public static boolean assignMember(ServerLevel level, BlockPos boxPos, Villager villager) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        if (box == null || !villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return false;
        }
        if (memberCount(level, boxPos) >= MAX_MEMBERS) {
            return false;
        }
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) {
            return false;
        }
        BlockPos previous = data.getSquadBoxPos();
        if (previous != null && !previous.equals(boxPos)) {
            unassignMember(level, villager, false);
        }
        data.setSquadBoxPos(boxPos);
        data.setSquadLeader(false);
        data.setActingLeader(false);
        assignBedFromBox(level, villager, data, box);
        box.syncActiveRecruitsFromWorld(level);
        return true;
    }

    public static void unassignMember(ServerLevel level, Villager villager, boolean clearBed) {
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) {
            return;
        }
        BlockPos boxPos = data.getSquadBoxPos();
        if (boxPos != null && level.getBlockEntity(boxPos) instanceof RecruiterBlockEntity box) {
            if (box.getSquadLeaderId() != null && box.getSquadLeaderId().equals(villager.getUUID())) {
                box.setSquadLeaderId(null);
                box.setSquadLeaderLabel("");
            }
            box.setActiveRecruits(memberCount(level, boxPos));
            syncSquadLeadership(level, boxPos);
        }
        data.setSquadBoxPos(null);
        data.setSquadLeader(false);
        data.setActingLeader(false);
        data.setOnCampaign(false);
        data.setCampaignCamp(null);
        data.setFollowPlayerId(null);
        if (clearBed) {
            SoldierStructureHelper.releaseSoldierBed(level, villager.getUUID(), data.getAssignedPostBed());
            data.setAssignedPostBed(null);
        }
    }

    public static boolean setLeader(ServerLevel level, BlockPos boxPos, Villager villager) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (box == null || data == null || !boxPos.equals(data.getSquadBoxPos())) {
            return false;
        }
        for (Villager member : members(level, boxPos)) {
            SoldierData md = member.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (md != null) {
                md.setSquadLeader(false);
                md.setActingLeader(false);
            }
        }
        if (isSergeantOrAbove(data)) {
            data.setSquadLeader(true);
            box.setSquadLeaderId(villager.getUUID());
            box.setSquadLeaderLabel(leaderLabel(villager, data, true));
            refreshMemberLabels(level, boxPos);
            return true;
        }
        data.setActingLeader(true);
        box.setSquadLeaderId(villager.getUUID());
        box.setSquadLeaderLabel(leaderLabel(villager, data, false));
        refreshMemberLabels(level, boxPos);
        return true;
    }

    /** Ensures a leader exists and nametags match the squad box. */
    public static void syncSquadLeadership(ServerLevel level, BlockPos boxPos) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        if (box == null) {
            return;
        }
        List<Villager> squad = members(level, boxPos);
        UUID leaderId = box.getSquadLeaderId();
        if (leaderId != null) {
            UUID currentLeader = leaderId;
            if (squad.stream().noneMatch(v -> v.getUUID().equals(currentLeader))) {
                box.setSquadLeaderId(null);
                box.setSquadLeaderLabel("");
                leaderId = null;
            }
        }
        if (leaderId == null && !squad.isEmpty()) {
            Villager best = squad.stream()
                    .max(Comparator.comparingInt(v -> {
                        SoldierData d = v.getData(VillagerAttachments.SOLDIER_DATA.get());
                        return d == null ? 0 : d.getKills();
                    }))
                    .orElse(squad.getFirst());
            setLeader(level, boxPos, best);
            return;
        }
        refreshMemberLabels(level, boxPos);
    }

    private static void refreshMemberLabels(ServerLevel level, BlockPos boxPos) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        UUID leaderId = box == null ? null : box.getSquadLeaderId();
        for (Villager member : members(level, boxPos)) {
            SoldierData md = member.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (md == null) {
                continue;
            }
            boolean isLeader = leaderId != null && leaderId.equals(member.getUUID());
            if (isLeader) {
                if (!md.isSquadLeader() && !md.isActingLeader()) {
                    if (isSergeantOrAbove(md)) {
                        md.setSquadLeader(true);
                    } else {
                        md.setActingLeader(true);
                    }
                }
            } else {
                md.setSquadLeader(false);
                md.setActingLeader(false);
            }
            com.villagers.mod.entity.SoldierLabels.apply(member, md);
        }
    }

    private static String leaderLabel(Villager villager, SoldierData data, boolean sergeantLeader) {
        String name = data.getDisplayName();
        if (name == null || name.isBlank()) {
            name = villager.getName().getString();
        }
        return sergeantLeader ? "Leader: " + name : "Acting leader: " + name;
    }

    public static void onLeaderDeath(ServerLevel level, BlockPos boxPos, UUID deadId) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        if (box == null || !deadId.equals(box.getSquadLeaderId())) {
            return;
        }
        box.setSquadLeaderId(null);
        List<Villager> squad = members(level, boxPos);
        squad.removeIf(v -> v.getUUID().equals(deadId));
        Villager best = squad.stream()
                .max(Comparator.comparingInt(v -> {
                    SoldierData d = v.getData(VillagerAttachments.SOLDIER_DATA.get());
                    return d == null ? 0 : d.getKills();
                }))
                .orElse(null);
        if (best != null) {
            setLeader(level, boxPos, best);
        }
    }

    public static boolean isSergeantOrAbove(SoldierData data) {
        return SoldierRanks.rankForKills(data.getKills()).ordinal() >= SoldierRanks.Rank.SERGEANT.ordinal();
    }

    public static boolean squadNightwatch(ServerLevel level, BlockPos boxPos) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        return box != null && box.isNightwatchEnabled();
    }

    public static boolean isStandingSquad(SoldierData data) {
        return data.getSquadBoxPos() == null;
    }

    public static void toggleCampaign(ServerLevel level, BlockPos boxPos, ServerPlayer owner) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        if (box == null) {
            return;
        }
        boolean next = !box.isSquadOnCampaign();
        box.setSquadOnCampaign(next);
        for (Villager member : members(level, boxPos)) {
            SoldierData data = member.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data == null) {
                continue;
            }
            data.setOnCampaign(next);
            if (next) {
                data.setFollowPlayerId(owner.getUUID());
                data.setCampaignCamp(null);
            } else {
                data.setCampaignCamp(null);
                data.setFollowPlayerId(null);
            }
        }
        if (next) {
            CampaignService.onSquadCampaignStarted(level, boxPos, owner);
        }
    }

    public static boolean linkBedToBox(ServerLevel level, BlockPos boxPos, BlockPos bedFoot) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        if (box == null || !VanillaBedHelper.isIntactBed(level, bedFoot)) {
            return false;
        }
        if (box.getLinkedBeds().size() >= MAX_BEDS) {
            return false;
        }
        if (box.getLinkedBeds().contains(bedFoot)) {
            return true;
        }
        box.addLinkedBed(bedFoot);
        return true;
    }

    public static boolean linkBedNearBox(ServerLevel level, BlockPos bedFoot, int radius) {
        BlockPos boxPos = findNearestSquadBox(level, bedFoot, radius);
        if (boxPos == null) {
            return false;
        }
        return linkBedToBox(level, boxPos, bedFoot);
    }

    @Nullable
    public static BlockPos findNearestSquadBox(ServerLevel level, BlockPos origin, int radius) {
        BlockPos.MutableBlockPos best = null;
        double bestDist = radius * (double) radius;
        int yRange = Math.min(64, Math.max(12, radius / 2));
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-radius, -yRange, -radius), origin.offset(radius, yRange, radius))) {
            if (!(level.getBlockEntity(pos) instanceof RecruiterBlockEntity)) {
                continue;
            }
            double dist = origin.distSqr(pos);
            if (dist <= bestDist) {
                bestDist = dist;
                best = pos.mutable();
            }
        }
        return best == null ? null : best.immutable();
    }

    private static void assignBedFromBox(
            ServerLevel level, Villager villager, SoldierData data, RecruiterBlockEntity box) {
        if (data.isOnCampaign()) {
            return;
        }
        for (BlockPos bed : box.getLinkedBeds()) {
            if (!VanillaBedHelper.isIntactBed(level, bed)) {
                continue;
            }
            SoldierBedClaims claims = SoldierBedClaims.get(level);
            if (claims.isClaimedByOther(bed, villager.getUUID())) {
                continue;
            }
            SoldierStructureHelper.releaseSoldierBed(level, villager.getUUID(), data.getAssignedPostBed());
            SoldierStructureHelper.claimSoldierBed(level, bed, villager.getUUID());
            data.setAssignedPostBed(bed);
            SoldierBedSleep.syncHomeMemory(villager, bed);
            return;
        }
    }

    /** Open Recruit slots: standing soldiers path to the nearest recruiting leader. */
    public static void tickStandingRecruitFill(ServerLevel level, Villager villager, SoldierData data) {
        if (data.getSquadBoxPos() != null || data.isOnCampaign()) {
            return;
        }
        RecruitTarget target = findOpenRecruitSquad(level, villager.blockPosition(), RECRUIT_SEEK_RADIUS);
        if (target == null) {
            return;
        }
        pathTowardRecruitLeader(villager, target.leader());
        if (villager.distanceToSqr(target.leader()) < RECRUIT_LEADER_JOIN_DISTANCE * RECRUIT_LEADER_JOIN_DISTANCE) {
            assignMember(level, target.boxPos(), villager);
        }
    }

    /** Militia (and gear) pulled from the squad box, then promoted and assigned when reqs are met. */
    public static void tickMilitiaSquadRecruit(ServerLevel level, Villager villager) {
        if (!villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            return;
        }
        RecruitTarget target = findOpenRecruitSquad(level, villager.blockPosition(), RECRUIT_SEEK_RADIUS);
        if (target == null) {
            return;
        }
        pathTowardRecruitLeader(villager, target.leader());
        if (villager.distanceToSqr(target.leader()) < RECRUIT_LEADER_JOIN_DISTANCE * RECRUIT_LEADER_JOIN_DISTANCE) {
            tryEnlistAndAssign(level, target.box(), villager);
        }
    }

    /**
     * Gear from a Recruit squad box, convert civilians/militia to soldiers, assign standing soldiers.
     * @return true if the villager was assigned to the squad
     */
    /** After enlist conversion, slot a standing soldier into the nearest recruiting squad. */
    public static void assignStandingToNearestRecruitSquad(ServerLevel level, Villager villager, int radius) {
        if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null || data.getSquadBoxPos() != null || data.isOnCampaign()) {
            return;
        }
        RecruitTarget target = findOpenRecruitSquad(level, villager.blockPosition(), radius);
        if (target != null) {
            assignMember(level, target.boxPos(), villager);
        }
    }

    public static boolean tryEnlistAndAssign(ServerLevel level, RecruiterBlockEntity box, Villager villager) {
        if (!box.canAcceptRecruit() || box.getSquadMode() != SquadMode.RECRUIT || box.getSquadLeaderId() == null) {
            return false;
        }
        if (BanditService.isBandit(villager) || WanderingNecromancerService.isWanderingNecromancer(villager)) {
            return false;
        }
        BlockPos boxPos = box.getBlockPos();
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data == null || data.getSquadBoxPos() != null || data.isOnCampaign()) {
                return false;
            }
            return assignMember(level, boxPos, villager);
        }
        GearReplacement.tryPullEnlistGear(villager, box);
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            if (!MilitiaConversion.promoteToSoldier(villager)) {
                return false;
            }
        } else if (!SoldierConversion.tryConvert(villager)) {
            return false;
        }
        if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return false;
        }
        return assignMember(level, boxPos, villager);
    }

    @Nullable
    private static RecruitTarget findOpenRecruitSquad(ServerLevel level, BlockPos from, int radius) {
        RecruiterBlockEntity bestBox = null;
        Villager bestLeader = null;
        double bestDist = Double.MAX_VALUE;
        int yRange = Math.min(32, Math.max(8, radius / 4));
        for (BlockPos pos : BlockPos.betweenClosed(
                from.offset(-radius, -yRange, -radius), from.offset(radius, yRange, radius))) {
            RecruiterBlockEntity box = squadBox(level, pos);
            if (box == null || box.getSquadMode() != SquadMode.RECRUIT) {
                continue;
            }
            if (memberCount(level, pos) >= MAX_MEMBERS) {
                continue;
            }
            UUID leaderId = box.getSquadLeaderId();
            if (leaderId == null) {
                continue;
            }
            Villager leader = findSoldier(level, leaderId);
            if (leader == null) {
                continue;
            }
            double dist = from.distSqr(pos);
            if (dist <= (double) radius * radius && dist < bestDist) {
                bestDist = dist;
                bestBox = box;
                bestLeader = leader;
            }
        }
        if (bestBox == null || bestLeader == null) {
            return null;
        }
        return new RecruitTarget(bestBox, bestLeader);
    }

    private static void pathTowardRecruitLeader(Villager villager, Villager leader) {
        if (villager.getNavigation().isDone()) {
            villager.getNavigation().moveTo(leader, 1.0);
        }
    }

    public static boolean assignRampartToBox(ServerLevel level, BlockPos boxPos, BlockPos rampart) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        if (box == null || !(level.getBlockState(rampart).getBlock() instanceof RampartBlock)) {
            return false;
        }
        return box.addSquadRampart(rampart);
    }

    /** Nearest valid rampart linked to this squad box, if any. */
    @Nullable
    public static BlockPos nearestLinkedRampart(ServerLevel level, BlockPos boxPos, BlockPos origin) {
        RecruiterBlockEntity box = squadBox(level, boxPos);
        if (box == null || box.getSquadRamparts().isEmpty()) {
            return null;
        }
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        for (BlockPos rampart : box.getSquadRamparts()) {
            if (!(level.getBlockState(rampart).getBlock() instanceof RampartBlock)) {
                continue;
            }
            double dist = origin.distSqr(rampart);
            if (dist < bestDist) {
                bestDist = dist;
                best = rampart;
            }
        }
        return best;
    }

    @Nullable
    private static Villager findSoldier(ServerLevel level, UUID id) {
        for (var entity : level.getAllEntities()) {
            if (entity instanceof Villager villager && villager.getUUID().equals(id)) {
                return villager;
            }
        }
        return null;
    }

    public static void notifyPlayer(ServerPlayer player, String key, Object... args) {
        player.displayClientMessage(Component.translatable(key, args), true);
    }
}
