package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.combat.CombatTargetAssign;
import com.villagers.mod.combat.RampartPathing;
import com.villagers.mod.combat.SoldierRetreat;
import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.player.AllyRank;
import com.villagers.mod.player.AllySavedData;
import com.villagers.mod.player.AllyService;
import com.villagers.mod.squad.SquadService;
import com.villagers.mod.war.AttackPlanService;
import com.villagers.mod.util.SoldierStructureHelper;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Horn and flare command routing (see {@code war-and-defense.md}). */
public final class HornCommandService {
    public static final int HORN_RADIUS = 256;
    private static final long SIGNAL_TICKS = 600L;
    /** Within this range of the flare, rally override ends and combat picks targets. */
    private static final double RALLY_ARRIVED_DISTANCE = 6.0;

    private record MilitiaRally(Vec3 pos, long untilGameTime) {
    }

    private static final Map<UUID, MilitiaRally> MILITIA_RALLY = new HashMap<>();

    private HornCommandService() {
    }

    public static int blow(ServerPlayer player, SoldierSignalType type) {
        ServerLevel level = player.serverLevel();
        long gameTime = level.getGameTime();
        int count = 0;

        @Nullable VillageData villageAtPlayer = VillageManager.get(level).findVillageAt(
                player.blockPosition().getX(),
                player.blockPosition().getY(),
                player.blockPosition().getZ());

        Set<UUID> seen = new HashSet<>();
        for (Villager villager : level.getEntitiesOfClass(Villager.class, player.getBoundingBox().inflate(HORN_RADIUS))) {
            if (!seen.add(villager.getUUID())) {
                continue;
            }
            if (shouldReceiveSignal(player, villager, villageAtPlayer, type)) {
                if (applySignal(villager, type, gameTime, player, null, 0)) {
                    count++;
                }
            }
        }

        if (villageAtPlayer != null && villageAtPlayer.getOwnerId().equals(player.getUUID())) {
            for (Villager villager : level.getEntitiesOfClass(Villager.class, new AABB(
                    villageAtPlayer.getMarkerX() - 512,
                    -64,
                    villageAtPlayer.getMarkerZ() - 512,
                    villageAtPlayer.getMarkerX() + 512,
                    320,
                    villageAtPlayer.getMarkerZ() + 512))) {
                if (!seen.add(villager.getUUID())) {
                    continue;
                }
                if (isRegisteredToVillage(level, villager, villageAtPlayer)
                        && shouldReceiveSignal(player, villager, villageAtPlayer, type)) {
                    if (applySignal(villager, type, gameTime, player, null, 0)) {
                        count++;
                    }
                }
            }
        }

        scareNearbyCivilians(level, player.blockPosition(), HORN_RADIUS);
        SoldierRedstoneInteract.dispatchOnCommandHorn(player);
        return count;
    }

    public static int applyFlare(
            ServerPlayer player, SoldierSignalType flareType, Vec3 burst, int respondRadius, int strikeRadius) {
        ServerLevel level = player.serverLevel();
        long gameTime = level.getGameTime();
        Set<UUID> seen = new HashSet<>();
        int count = 0;
        count += applyFlareAt(
                player,
                flareType,
                snapFlareToGround(level, burst),
                respondRadius,
                strikeRadius,
                gameTime,
                seen,
                level);
        count += applyFlareAt(
                player,
                flareType,
                snapFlareToGround(level, player.position()),
                respondRadius,
                strikeRadius,
                gameTime,
                seen,
                level);
        return count;
    }

    private static int applyFlareAt(
            ServerPlayer player,
            SoldierSignalType flareType,
            Vec3 groundAnchor,
            int respondRadius,
            int strikeRadius,
            long gameTime,
            Set<UUID> seen,
            ServerLevel level) {
        int count = 0;
        long respondSq = (long) respondRadius * respondRadius;
        BlockPos anchor = BlockPos.containing(groundAnchor.x, groundAnchor.y, groundAnchor.z);
        AABB search = new AABB(
                anchor.getX() - respondRadius,
                level.getMinBuildHeight(),
                anchor.getZ() - respondRadius,
                anchor.getX() + respondRadius,
                level.getMaxBuildHeight(),
                anchor.getZ() + respondRadius);
        for (Villager villager : level.getEntitiesOfClass(Villager.class, search)) {
            if (!seen.add(villager.getUUID())) {
                continue;
            }
            double dx = villager.getX() - groundAnchor.x;
            double dz = villager.getZ() - groundAnchor.z;
            if (dx * dx + dz * dz > respondSq) {
                continue;
            }
            if (!shouldReceiveSignal(player, villager, null, flareType)) {
                continue;
            }
            if (applySignal(villager, flareType, gameTime, player, groundAnchor, strikeRadius)) {
                count++;
            }
        }
        return count;
    }

    /** Flares use horizontal range; burst Y is where the rocket popped, not where troops should go. */
    private static Vec3 snapFlareToGround(ServerLevel level, Vec3 burst) {
        int x = BlockPos.containing(burst.x, burst.y, burst.z).getX();
        int z = BlockPos.containing(burst.x, burst.y, burst.z).getZ();
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        return new Vec3(burst.x, y, burst.z);
    }

    public static boolean canCommandSoldiers(ServerPlayer player, UUID soldierOwnerId) {
        if (player.getUUID().equals(soldierOwnerId)) {
            return true;
        }
        if (!AllySavedData.get(player.serverLevel()).isAlly(soldierOwnerId, player.getUUID())) {
            return false;
        }
        AllyRank rank = AllySavedData.get(player.serverLevel()).getRank(soldierOwnerId, player.getUUID());
        return rank != null && rank.canUseHornsAndFlares();
    }

    private static boolean shouldReceiveSignal(
            ServerPlayer player,
            Villager villager,
            @Nullable VillageData villageAtPlayer,
            SoldierSignalType type) {
        UUID ownerId = resolveSoldierOwner(villager.level(), villager);
        if (ownerId == null && villager.level() instanceof ServerLevel serverLevel) {
            for (VillageData owned : VillageManager.get(serverLevel).getVillagesOwnedBy(player.getUUID())) {
                if (isRegisteredToVillage(serverLevel, villager, owned)
                        || isMilitiaRegisteredToVillage(serverLevel, villager, owned)) {
                    ownerId = owned.getOwnerId();
                    break;
                }
            }
        }
        if (ownerId == null) {
            return false;
        }
        if (!canCommandSoldiers(player, ownerId)) {
            return false;
        }
        if (type == SoldierSignalType.RALLY || type == SoldierSignalType.STRIKE) {
            return villager.hasData(VillagerAttachments.SOLDIER_DATA.get())
                    || villager.hasData(VillagerAttachments.MILITIA_DATA.get());
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            if (type == SoldierSignalType.CHARGE) {
                SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
                if (data == null || !data.isOnCampaign()) {
                    return false;
                }
                if (villager.distanceToSqr(player) > (long) SquadService.CHARGE_SQUAD_RADIUS * SquadService.CHARGE_SQUAD_RADIUS) {
                    return false;
                }
                if (villager.level() instanceof ServerLevel serverLevel
                        && !AttackPlanService.squadSelectedForCharge(
                                player.getUUID(), serverLevel, data.getSquadBoxPos())) {
                    return false;
                }
            }
            return true;
        }
        return villager.hasData(VillagerAttachments.MILITIA_DATA.get());
    }

    private static boolean applySignal(
            Villager villager,
            SoldierSignalType type,
            long gameTime,
            ServerPlayer commander,
            @Nullable Vec3 flareBurst,
            int strikeRadius) {
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            if (type == SoldierSignalType.RALLY && flareBurst != null) {
                registerRally(villager, gameTime, flareBurst);
                return true;
            }
            if (type == SoldierSignalType.STRIKE && flareBurst != null) {
                LivingEntity foe = findStrikeTarget(villager, flareBurst, strikeRadius, commander);
                if (foe != null && villager instanceof Mob mob) {
                    mob.setTarget(foe);
                }
                return true;
            }
            return applyMilitiaSignal(villager, type, gameTime, commander);
        }
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) {
            return false;
        }
        data.setActiveSignal(type);
        data.setSignalUntilGameTime(gameTime + SIGNAL_TICKS);
        if (flareBurst != null) {
            data.setSignalX(flareBurst.x);
            data.setSignalY(flareBurst.y);
            data.setSignalZ(flareBurst.z);
            data.setSignalRadius(strikeRadius);
        } else {
            data.clearSignalTarget();
        }

        SoldierBedSleep.wakeNow(villager);
        switch (type) {
            case AWAKEN -> {
                SoldierStructureHelper.findNearestArrowBin(villager.level(), villager.blockPosition())
                        .or(() -> RampartPathing.findBestRampartToStandOn(
                                villager, RampartPathing.RAMPART_SEEK_RADIUS))
                        .ifPresent(target -> villager.getNavigation().moveTo(
                                target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 1.0));
                return true;
            }
            case FALLBACK -> {
                if (data.isOnCampaign() && data.getCampaignCamp() != null) {
                    BlockPos camp = data.getCampaignCamp();
                    villager.getNavigation().moveTo(camp.getX() + 0.5, camp.getY(), camp.getZ() + 0.5, 1.2);
                } else {
                    SoldierRetreat.retreatToFallback(villager);
                }
                return true;
            }
            case CHARGE -> {
                LivingEntity foe = findChargeTarget(villager, commander);
                if (foe != null && villager instanceof Mob mob) {
                    mob.setTarget(foe);
                }
                return true;
            }
            case GUARD_CHIEF -> {
                if (villager instanceof Mob mob) {
                    mob.getNavigation().moveTo(commander, 1.2);
                }
                return true;
            }
            case RALLY -> {
                if (flareBurst != null) {
                    registerRally(villager, gameTime, flareBurst);
                }
                return true;
            }
            case STRIKE -> {
                if (flareBurst != null) {
                    LivingEntity foe = findStrikeTarget(villager, flareBurst, strikeRadius, commander);
                    if (foe != null && villager instanceof Mob mob) {
                        mob.setTarget(foe);
                    }
                }
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private static boolean applyMilitiaSignal(
            Villager villager, SoldierSignalType type, long gameTime, ServerPlayer commander) {
        if (type == SoldierSignalType.GUARD_CHIEF && villager.getRandom().nextFloat() > 0.10f) {
            return false;
        }
        if (type == SoldierSignalType.AWAKEN) {
            RampartPathing.tickSeekNearestRampart(villager, 0.65);
            return true;
        }
        if (type == SoldierSignalType.FALLBACK) {
            SoldierRetreat.retreatToFallback(villager);
            return true;
        }
        if (type == SoldierSignalType.GUARD_CHIEF && villager instanceof Mob mob) {
            mob.getNavigation().moveTo(commander, 1.0);
            return true;
        }
        return type == SoldierSignalType.CHARGE;
    }

    @Nullable
    public static UUID resolveSoldierOwner(net.minecraft.world.level.Level level, Villager villager) {
        if (!(level instanceof ServerLevel server)) {
            return null;
        }
        VillageData village = VillageManager.get(server).findVillageAt(
                villager.blockPosition().getX(),
                villager.blockPosition().getY(),
                villager.blockPosition().getZ());
        if (village != null) {
            return village.getOwnerId();
        }
        com.villagers.mod.entity.MilitiaData militia = villager.getData(VillagerAttachments.MILITIA_DATA.get());
        if (militia != null && militia.getHomeBed() != null) {
            UUID owner = ownerAt(server, militia.getHomeBed());
            if (owner != null) {
                return owner;
            }
        }
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) {
            return null;
        }
        SoldierStructureHelper.ensureAssignedBedCached(server, villager.getUUID(), data, villager.blockPosition());
        UUID owner = ownerAt(server, data.getAssignedPostBed());
        if (owner != null) {
            return owner;
        }
        owner = ownerAt(server, data.getSquadBoxPos());
        if (owner != null) {
            return owner;
        }
        return ownerAt(server, data.getCampaignCamp());
    }

    @Nullable
    private static UUID ownerAt(ServerLevel server, @Nullable BlockPos pos) {
        if (pos == null) {
            return null;
        }
        VillageData village = VillageManager.get(server).findVillageAt(pos.getX(), pos.getY(), pos.getZ());
        return village == null ? null : village.getOwnerId();
    }

    private static boolean isMilitiaRegisteredToVillage(ServerLevel level, Villager villager, VillageData village) {
        com.villagers.mod.entity.MilitiaData data = villager.getData(VillagerAttachments.MILITIA_DATA.get());
        if (data == null || data.getHomeBed() == null) {
            return false;
        }
        BlockPos bed = data.getHomeBed();
        VillageData bedVillage = VillageManager.get(level).findVillageAt(bed.getX(), bed.getY(), bed.getZ());
        return bedVillage != null && bedVillage.getVillageId().equals(village.getVillageId());
    }

    private static boolean isRegisteredToVillage(ServerLevel level, Villager villager, VillageData village) {
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) {
            return false;
        }
        if (data.getAssignedPostBed() != null) {
            VillageData bedVillage = VillageManager.get(level).findVillageAt(
                    data.getAssignedPostBed().getX(),
                    data.getAssignedPostBed().getY(),
                    data.getAssignedPostBed().getZ());
            if (bedVillage != null && bedVillage.getVillageId().equals(village.getVillageId())) {
                return true;
            }
        }
        VillageData here = VillageManager.get(level).findVillageAt(
                villager.blockPosition().getX(),
                villager.blockPosition().getY(),
                villager.blockPosition().getZ());
        return here != null && here.getVillageId().equals(village.getVillageId());
    }

    private static void scareNearbyCivilians(ServerLevel level, BlockPos origin, int radius) {
        for (Villager villager : level.getEntitiesOfClass(Villager.class, new AABB(origin).inflate(radius))) {
            if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())
                    || villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
                continue;
            }
            if (villager.getSleepingPos().isPresent()) {
                BlockPos bed = villager.getSleepingPos().get();
                villager.getNavigation().moveTo(bed.getX() + 0.5, bed.getY(), bed.getZ() + 0.5, 1.0);
                continue;
            }
            SoldierStructureHelper.findNearestHaven(level, villager.blockPosition()).ifPresent(haven -> villager
                    .getNavigation()
                    .moveTo(haven.getX() + 0.5, haven.getY(), haven.getZ() + 0.5, 1.0));
        }
    }

    @Nullable
    private static LivingEntity findChargeTarget(Villager soldier, ServerPlayer commander) {
        ServerLevel level = (ServerLevel) soldier.level();
        UUID ownerId = resolveSoldierOwner(level, soldier);
        if (ownerId == null) {
            return null;
        }
        BlockPos planTarget = AttackPlanService.chargeTarget(commander.getUUID(), level);
        if (planTarget != null) {
            LivingEntity atTarget = findHostileNearPoint(soldier, commander, ownerId, planTarget, AttackPlanService.ASSAULT_ENGAGE_RADIUS);
            if (atTarget != null) {
                return atTarget;
            }
        }
        LivingEntity best = null;
        double bestDist = 48 * 48;
        for (Player player : level.getEntitiesOfClass(Player.class, soldier.getBoundingBox().inflate(48))) {
            if (player == commander || !player.isAlive()) {
                continue;
            }
            if (AllyService.isAlly(level, ownerId, player.getUUID())) {
                continue;
            }
            double dist = soldier.distanceToSqr(player);
            if (dist < bestDist) {
                bestDist = dist;
                best = player;
            }
        }
        for (LivingEntity hostile : level.getEntitiesOfClass(
                LivingEntity.class, soldier.getBoundingBox().inflate(48), com.villagers.mod.combat.SoldierHostileTargets::isEngageable)) {
            double dist = soldier.distanceToSqr(hostile);
            if (dist < bestDist) {
                bestDist = dist;
                best = hostile;
            }
        }
        return best;
    }

    @Nullable
    private static LivingEntity findHostileNearPoint(
            Villager soldier, ServerPlayer commander, UUID ownerId, BlockPos point, double radius) {
        ServerLevel level = (ServerLevel) soldier.level();
        net.minecraft.world.phys.AABB box = new net.minecraft.world.phys.AABB(point).inflate(radius);
        LivingEntity best = null;
        double bestDist = radius * radius;
        for (Player player : level.getEntitiesOfClass(Player.class, box)) {
            if (player == commander || !player.isAlive() || AllyService.isAlly(level, ownerId, player.getUUID())) {
                continue;
            }
            double dist = player.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(point));
            if (dist <= bestDist) {
                bestDist = dist;
                best = player;
            }
        }
        for (LivingEntity hostile : level.getEntitiesOfClass(LivingEntity.class, box, com.villagers.mod.combat.SoldierHostileTargets::isEngageable)) {
            double dist = hostile.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(point));
            if (dist <= bestDist) {
                bestDist = dist;
                best = hostile;
            }
        }
        return best;
    }

    @Nullable
    private static LivingEntity findStrikeTarget(
            Villager soldier, Vec3 burst, int strikeRadius, ServerPlayer commander) {
        ServerLevel level = (ServerLevel) soldier.level();
        UUID ownerId = resolveSoldierOwner(level, soldier);
        if (ownerId == null) {
            return null;
        }
        AABB box = new AABB(burst, burst).inflate(strikeRadius);
        LivingEntity best = null;
        double bestDist = strikeRadius * strikeRadius;
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box)) {
            if (entity == soldier || !entity.isAlive()) {
                continue;
            }
            if (entity instanceof Player player && AllyService.isAlly(level, ownerId, player.getUUID())) {
                continue;
            }
            double dist = entity.position().distanceToSqr(burst);
            if (dist <= bestDist) {
                bestDist = dist;
                best = entity;
            }
        }
        return best;
    }

    public static boolean isChargeActive(SoldierData data, long gameTime) {
        return data.getActiveSignal() == SoldierSignalType.CHARGE && data.getSignalUntilGameTime() > gameTime;
    }

    public static boolean isRallyActive(SoldierData data, long gameTime) {
        return data.getActiveSignal() == SoldierSignalType.RALLY && data.getSignalUntilGameTime() > gameTime;
    }

    public static boolean isRallyActive(Villager villager, long gameTime) {
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data != null && isRallyActive(data, gameTime)) {
            return true;
        }
        MilitiaRally rally = MILITIA_RALLY.get(villager.getUUID());
        if (rally != null && rally.untilGameTime() > gameTime) {
            return true;
        }
        if (rally != null) {
            MILITIA_RALLY.remove(villager.getUUID());
        }
        return false;
    }

    @Nullable
    public static Vec3 getRallyDestination(Villager villager, long gameTime) {
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data != null && isRallyActive(data, gameTime)) {
            return new Vec3(data.getSignalX(), data.getSignalY(), data.getSignalZ());
        }
        MilitiaRally rally = MILITIA_RALLY.get(villager.getUUID());
        if (rally != null && rally.untilGameTime() > gameTime) {
            return rally.pos();
        }
        return null;
    }

    /**
     * While rally is active: drop combat target and path to the flare. Returns true if rally handling ran.
     */
    public static boolean tickRallyOverride(Villager villager, Mob mob) {
        long gameTime = villager.level().getGameTime();
        if (!isRallyActive(villager, gameTime)) {
            return false;
        }
        Vec3 dest = getRallyDestination(villager, gameTime);
        if (dest != null
                && villager.distanceToSqr(dest.x, dest.y, dest.z)
                        <= RALLY_ARRIVED_DISTANCE * RALLY_ARRIVED_DISTANCE) {
            return false;
        }
        CombatTargetAssign.assign(mob, null);
        if (dest != null && (mob.getNavigation().isDone() || villager.tickCount % 10 == 0)) {
            mob.getNavigation().moveTo(dest.x, dest.y, dest.z, 1.0);
        }
        return true;
    }

    private static void registerRally(Villager villager, long gameTime, Vec3 point) {
        SoldierBedSleep.wakeNow(villager);
        if (villager instanceof Mob mob) {
            CombatTargetAssign.assign(mob, null);
        }
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data != null) {
            data.setActiveSignal(SoldierSignalType.RALLY);
            data.setSignalUntilGameTime(gameTime + SIGNAL_TICKS);
            data.setSignalX(point.x);
            data.setSignalY(point.y);
            data.setSignalZ(point.z);
        } else if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            MILITIA_RALLY.put(villager.getUUID(), new MilitiaRally(point, gameTime + SIGNAL_TICKS));
        }
        villager.getNavigation().moveTo(point.x, point.y, point.z, 1.0);
    }
}
