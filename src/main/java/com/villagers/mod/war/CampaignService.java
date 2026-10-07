package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.entity.CampBlockEntity;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.item.CampaignBootsItem;
import com.villagers.mod.util.SoldierStructureHelper;

public final class CampaignService {
    private static final int FOLLOW_RANGE = 48;
    private static final int CAMP_HOLD_RANGE = 16;

    private CampaignService() {
    }

    public static void tickSoldier(Villager soldier, SoldierData data) {
        if (soldier.level().isClientSide || !(soldier.level() instanceof ServerLevel level)) {
            return;
        }
        ItemStack boots = VillagerGearRules.get(soldier, VillagerGearSlot.BOOTS);
        boolean wearingCampaignBoots = CampaignBootsItem.isCampaignBoots(boots);
        if (!wearingCampaignBoots) {
            if (data.isOnCampaign()) {
                data.setOnCampaign(false);
                data.setCampaignCamp(null);
                data.setFollowPlayerId(null);
            }
            return;
        }
        data.setOnCampaign(true);
        if (data.getCampaignCamp() != null && level.isLoaded(data.getCampaignCamp())) {
            holdNearCamp(soldier, data.getCampaignCamp());
            return;
        }
        ServerPlayer follow = resolveFollowPlayer(level, soldier, data);
        if (follow != null) {
            data.setFollowPlayerId(follow.getUUID());
            if (soldier.getNavigation().isDone() || soldier.distanceToSqr(follow) > 12 * 12) {
                soldier.getNavigation().moveTo(follow, 1.0);
            }
        }
    }

    public static void onCampPlaced(ServerLevel level, BlockPos pos, ServerPlayer placer) {
        CampBlockEntity camp = level.getBlockEntity(pos) instanceof CampBlockEntity be ? be : null;
        if (camp != null) {
            camp.setOwnerId(placer.getUUID());
        }
        CampaignState.get(level).setCamp(placer.getUUID(), pos, level.dimension());
        for (var villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(pos).inflate(FOLLOW_RANGE))) {
            if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data == null || !data.isOnCampaign()) {
                continue;
            }
            if (data.getFollowPlayerId() != null && !data.getFollowPlayerId().equals(placer.getUUID())) {
                continue;
            }
            data.setCampaignCamp(pos);
            data.setFollowPlayerId(placer.getUUID());
        }
    }

    public static boolean tryEatAtCamp(Villager soldier, SoldierData data) {
        if (data.getCampaignCamp() == null || !(soldier.level() instanceof ServerLevel level)) {
            return false;
        }
        if (!level.isLoaded(data.getCampaignCamp())) {
            return false;
        }
        if (!(level.getBlockEntity(data.getCampaignCamp()) instanceof CampBlockEntity camp)) {
            return false;
        }
        return camp.consumeOneFood();
    }

    private static void holdNearCamp(Villager soldier, BlockPos camp) {
        double dist = soldier.blockPosition().distSqr(camp);
        if (dist > CAMP_HOLD_RANGE * CAMP_HOLD_RANGE && soldier.getNavigation().isDone()) {
            soldier.getNavigation().moveTo(camp.getX() + 0.5, camp.getY(), camp.getZ() + 0.5, 0.7);
        }
    }

    private static ServerPlayer resolveFollowPlayer(ServerLevel level, Villager soldier, SoldierData data) {
        if (data.getFollowPlayerId() != null) {
            var player = level.getServer().getPlayerList().getPlayer(data.getFollowPlayerId());
            if (player != null) {
                return player;
            }
        }
        ServerPlayer nearest = null;
        double best = FOLLOW_RANGE * (double) FOLLOW_RANGE;
        for (ServerPlayer player : level.players()) {
            double dist = player.distanceToSqr(soldier);
            if (dist < best) {
                best = dist;
                nearest = player;
            }
        }
        return nearest;
    }

    public static void pathRetreatToCamp(Villager soldier, SoldierData data) {
        BlockPos camp = data.getCampaignCamp();
        if (camp == null) {
            SoldierStructureHelper.findNearestCamp(soldier.level(), soldier.blockPosition())
                    .ifPresent(found -> soldier.getNavigation().moveTo(
                            found.getX() + 0.5, found.getY(), found.getZ() + 0.5, 1.0));
            return;
        }
        soldier.getNavigation().moveTo(camp.getX() + 0.5, camp.getY(), camp.getZ() + 0.5, 1.0);
    }
}
