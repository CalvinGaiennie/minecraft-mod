package com.villagers.mod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.bed.VanillaBedHelper;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.threat.BanditService;
import com.villagers.mod.threat.WanderingNecromancerService;
import com.villagers.mod.village.VillageClaimService;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;
import com.villagers.mod.village.VillageShortId;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MusterRollHelper {
    public static final int NEARBY_RADIUS = 64;

    private MusterRollHelper() {
    }

    public record Summary(int soldierCount, int totalKills) {
    }

    public record EmpireMusterReport(int totalSoldiers, int totalKills, List<VillageMusterPage> villages) {
    }

    public record VillageMusterPage(
            String shortId,
            String villageName,
            int centerX,
            int centerZ,
            int radius,
            /** Soldiers whose home bed (or feet) registers to this hamlet. */
            int rosterCount,
            /** Soldiers physically inside this hamlet's radius right now. */
            int presentCount,
            int presentKills,
            List<String> presentNames,
            /** Soldiers assigned to a squad box block inside this hamlet (reassignment writ). */
            int squadRosterCount) {
    }

    private record SoldierRollup(
            Villager villager,
            SoldierData data,
            @Nullable VillageData homeVillage,
            @Nullable VillageData garrisonVillage) {
    }

    public static EmpireMusterReport buildEmpireReport(ServerLevel level, Player player) {
        VillageClaimService.backfillNearbyMessClaims(level, player);
        UUID ownerId = player.getUUID();

        VillageManager manager = VillageManager.get(level);
        List<VillageData> owned = manager.getVillagesOwnedBy(ownerId);
        owned.sort(Comparator.comparing(v -> v.getVillageName(), String.CASE_INSENSITIVE_ORDER));

        Map<UUID, SoldierRollup> roster = collectRoster(level, owned);

        int ownedKills = 0;
        for (SoldierRollup rollup : roster.values()) {
            ownedKills += rollup.data().getKills();
        }

        Map<UUID, List<String>> presentByVillageId = new HashMap<>();
        Map<UUID, Integer> rosterByVillageId = new HashMap<>();
        Map<UUID, Integer> squadRosterByVillageId = new HashMap<>();
        for (VillageData village : owned) {
            presentByVillageId.put(village.getVillageId(), new ArrayList<>());
            rosterByVillageId.put(village.getVillageId(), 0);
            squadRosterByVillageId.put(village.getVillageId(), 0);
        }
        for (SoldierRollup rollup : roster.values()) {
            if (rollup.homeVillage() != null) {
                UUID id = rollup.homeVillage().getVillageId();
                rosterByVillageId.merge(id, 1, Integer::sum);
            }
            if (rollup.garrisonVillage() != null) {
                presentByVillageId
                        .computeIfAbsent(rollup.garrisonVillage().getVillageId(), k -> new ArrayList<>())
                        .add(musterPresentLabel(rollup.villager(), rollup.data()));
            }
            BlockPos squadBox = rollup.data().getSquadBoxPos();
            if (squadBox != null) {
                VillageData boxHamlet = ownedVillageContaining(owned, squadBox.getX(), squadBox.getZ());
                if (boxHamlet != null) {
                    squadRosterByVillageId.merge(boxHamlet.getVillageId(), 1, Integer::sum);
                }
            }
        }

        List<VillageMusterPage> pages = new ArrayList<>();
        for (VillageData village : owned) {
            List<String> present = presentByVillageId.getOrDefault(village.getVillageId(), List.of());
            present.sort(String.CASE_INSENSITIVE_ORDER);
            int presentKills = 0;
            for (SoldierRollup rollup : roster.values()) {
                if (rollup.garrisonVillage() != null
                        && rollup.garrisonVillage().getVillageId().equals(village.getVillageId())) {
                    presentKills += rollup.data().getKills();
                }
            }
            pages.add(new VillageMusterPage(
                    VillageShortId.format(village),
                    village.getVillageName(),
                    village.getMarkerX(),
                    village.getMarkerZ(),
                    village.getRadius(),
                    rosterByVillageId.getOrDefault(village.getVillageId(), 0),
                    present.size(),
                    presentKills,
                    List.copyOf(present),
                    squadRosterByVillageId.getOrDefault(village.getVillageId(), 0)));
        }

        return new EmpireMusterReport(roster.size(), ownedKills, pages);
    }

    private static Map<UUID, SoldierRollup> collectRoster(ServerLevel level, List<VillageData> owned) {
        Map<UUID, SoldierRollup> roster = new LinkedHashMap<>();
        for (var entity : level.getAllEntities()) {
            if (entity instanceof Villager villager) {
                collectSoldier(level, owned, villager, roster);
            }
        }
        return roster;
    }

    private static void collectSoldier(
            ServerLevel level,
            List<VillageData> owned,
            Villager villager,
            Map<UUID, SoldierRollup> roster) {
        if (!villager.isAlive() || villager.isBaby()) {
            return;
        }
        if (BanditService.isBandit(villager) || WanderingNecromancerService.isWanderingNecromancer(villager)) {
            return;
        }
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())
                && !villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        if (villager.hasData(VillagerAttachments.VETERAN_DATA.get())
                && !villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return;
        }
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) {
            return;
        }
        VillageData home = homeVillage(level, owned, villager, data);
        VillageData garrison = garrisonVillage(owned, villager.getBlockX(), villager.getBlockZ());
        if (home == null && garrison == null) {
            return;
        }
        roster.put(villager.getUUID(), new SoldierRollup(villager, data, home, garrison));
    }

    @Nullable
    private static VillageData homeVillage(
            ServerLevel level,
            List<VillageData> owned,
            Villager villager,
            SoldierData data) {
        BlockPos bed = data.getAssignedPostBed();
        if (bed != null) {
            BlockPos foot = VanillaBedHelper.footPosition(level, bed);
            VillageData fromBed = ownedVillageContaining(owned, foot.getX(), foot.getZ());
            if (fromBed != null) {
                return fromBed;
            }
        }
        return ownedVillageContaining(owned, villager.getBlockX(), villager.getBlockZ());
    }

    @Nullable
    private static VillageData garrisonVillage(List<VillageData> owned, int x, int z) {
        return ownedVillageContaining(owned, x, z);
    }

    @Nullable
    private static VillageData ownedVillageContaining(List<VillageData> owned, int x, int z) {
        for (VillageData village : owned) {
            if (!village.isActive()) {
                continue;
            }
            long dx = x - village.getMarkerX();
            long dz = z - village.getMarkerZ();
            long radiusSq = (long) village.getRadius() * village.getRadius();
            if (dx * dx + dz * dz <= radiusSq) {
                return village;
            }
        }
        return null;
    }

    /** Present-in-hamlet name line — never uses vanilla entity type names like "Villager". */
    private static String musterPresentLabel(Villager villager, SoldierData data) {
        String stored = data.getDisplayName();
        if (stored != null && !stored.isBlank()) {
            String trimmed = stored.trim();
            if (!isGenericEntityLabel(trimmed)) {
                if (data.getKills() > 0) {
                    return trimmed + " (" + data.getKills() + " kills)";
                }
                return trimmed;
            }
        }
        if (data.getKills() > 0) {
            return "Enlisted (" + data.getKills() + " kills)";
        }
        return "Enlisted";
    }

    private static boolean isGenericEntityLabel(String name) {
        return "Villager".equalsIgnoreCase(name)
                || "Soldier".equalsIgnoreCase(name)
                || "Militia".equalsIgnoreCase(name);
    }

    public static Summary summarizeInVillage(ServerLevel level, VillageData village) {
        int soldierCount = 0;
        int totalKills = 0;
        long radiusSq = (long) village.getRadius() * village.getRadius();
        int centerX = village.getMarkerX();
        int centerZ = village.getMarkerZ();

        for (var entity : level.getAllEntities()) {
            if (!(entity instanceof Villager villager) || !countsAsMusterSoldier(villager)) {
                continue;
            }
            long dx = villager.getBlockX() - centerX;
            long dz = villager.getBlockZ() - centerZ;
            if (dx * dx + dz * dz > radiusSq) {
                continue;
            }
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null) {
                soldierCount++;
                totalKills += data.getKills();
            }
        }
        return new Summary(soldierCount, totalKills);
    }

    static boolean countsAsMusterSoldier(Villager villager) {
        if (!villager.isAlive() || villager.isBaby()) {
            return false;
        }
        if (BanditService.isBandit(villager) || WanderingNecromancerService.isWanderingNecromancer(villager)) {
            return false;
        }
        return villager.hasData(VillagerAttachments.SOLDIER_DATA.get());
    }

    public static Summary summarize(ServerLevel level, Player player, int radius) {
        int soldierCount = 0;
        int totalKills = 0;
        double radiusSq = (double) radius * radius;

        for (var entity : level.getAllEntities()) {
            if (entity instanceof Villager villager
                    && countsAsMusterSoldier(villager)
                    && villager.distanceToSqr(player) <= radiusSq) {
                SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
                if (data != null) {
                    soldierCount++;
                    totalKills += data.getKills();
                }
            }
        }
        return new Summary(soldierCount, totalKills);
    }

    public static Summary summarizeNear(ServerLevel level, BlockPos center, int radius) {
        int soldierCount = 0;
        int totalKills = 0;
        double radiusSq = (double) radius * radius;

        for (var entity : level.getAllEntities()) {
            if (entity instanceof Villager villager
                    && countsAsMusterSoldier(villager)
                    && villager.blockPosition().distSqr(center) <= radiusSq) {
                SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
                if (data != null) {
                    soldierCount++;
                    totalKills += data.getKills();
                }
            }
        }
        return new Summary(soldierCount, totalKills);
    }
}
