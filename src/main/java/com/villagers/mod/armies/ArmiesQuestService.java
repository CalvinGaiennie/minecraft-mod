package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;

import com.villagers.mod.Config;
import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.MessStationBlock;
import com.villagers.mod.block.PostBlock;
import com.villagers.mod.entity.BanditData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.player.EnlistmentService;
import com.villagers.mod.village.VillageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ArmiesQuestService {
    private static final Map<UUID, List<UUID>> assaultMinions = new HashMap<>();
    private static final Map<UUID, D2AssaultState> d2Assaults = new HashMap<>();

    private ArmiesQuestService() {
    }

    public static void tickPlayer(ServerPlayer player) {
        if (!EnlistmentService.isEnlisted(player)) {
            return;
        }
        ServerLevel level = player.serverLevel();
        UUID owner = player.getUUID();
        ArmiesQuestSavedData questData = ArmiesQuestSavedData.get(level);
        ArmiesQuestSavedData.PlayerQuestState state = questData.getOrCreate(owner);

        checkD1Threshold(level, owner, state, questData);
        tryStartD1OnEnter(level, player, state, questData);
        tryStartD2OnEnter(level, player, state, questData);
        tickActiveAssault(level, owner, state, questData);
    }

    private static void checkD1Threshold(ServerLevel level, UUID owner, ArmiesQuestSavedData.PlayerQuestState state,
            ArmiesQuestSavedData questData) {
        if (state.stage != ArmiesQuestStage.NOT_STARTED) {
            return;
        }
        int threshold = Config.ARMIES_QUEST_D1_SOLDIER_THRESHOLD.get();
        for (VillageData village : VillageManager.get(level).getVillagesOwnedBy(owner)) {
            if (VillageSoldierCounts.countSoldiersInVillage(level, village) >= threshold) {
                state.stage = ArmiesQuestStage.D1_WAITING_ENTER;
                state.d1VillageId = village.getVillageId();
                questData.markDirtySelf();
                playerMessage(level, owner, Component.translatable("quest.villagers.d1_waiting"));
                return;
            }
        }
    }

    private static void tryStartD1OnEnter(ServerLevel level, ServerPlayer player, ArmiesQuestSavedData.PlayerQuestState state,
            ArmiesQuestSavedData questData) {
        if (state.stage != ArmiesQuestStage.D1_WAITING_ENTER || state.d1VillageId == null) {
            return;
        }
        VillageData village = VillageManager.get(level).getVillage(state.d1VillageId);
        if (village == null) {
            return;
        }
        if (!VillageSoldierCounts.playerInVillage(level, player.getUUID(), village)) {
            return;
        }
        if (VillageSoldierCounts.countSoldiersInVillage(level, village)
                < Config.ARMIES_QUEST_D1_SOLDIER_THRESHOLD.get()) {
            return;
        }
        state.stage = ArmiesQuestStage.D1_ACTIVE;
        questData.markDirtySelf();
        spawnD1Assault(level, player.getUUID(), village);
        player.sendSystemMessage(Component.translatable("quest.villagers.d1_start"));
    }

    private static void spawnD1Assault(ServerLevel level, UUID owner, VillageData village) {
        BlockPos center = new BlockPos(village.getMarkerX(), village.getMarkerY(), village.getMarkerZ());
        List<UUID> minions = new ArrayList<>();
        int count = 18 + level.random.nextInt(5);
        for (int i = 0; i < count; i++) {
            BlockPos spawn = center.offset(level.random.nextInt(40) - 20, 0, level.random.nextInt(40) - 20);
            Villager bandit = BanditSpawnHelper.spawnBandit(level, spawn, UUID.randomUUID(), false, BanditData.BossRole.NONE, owner, null);
            minions.add(bandit.getUUID());
        }
        Villager halvek = BanditSpawnHelper.spawnBandit(level, center.offset(0, 0, 20), UUID.randomUUID(), true,
                BanditData.BossRole.HALVEK, owner, Component.literal("Halvek"));
        minions.add(halvek.getUUID());
        assaultMinions.put(owner, minions);
    }

    private static void tryStartD2OnEnter(ServerLevel level, ServerPlayer player, ArmiesQuestSavedData.PlayerQuestState state,
            ArmiesQuestSavedData questData) {
        if (state.stage != ArmiesQuestStage.D2_ARMED) {
            return;
        }
        var largest = VillageSoldierCounts.largestOwnedVillage(level, player.getUUID());
        if (largest.isEmpty() || !VillageSoldierCounts.playerInVillage(level, player.getUUID(), largest.get())) {
            return;
        }
        state.stage = ArmiesQuestStage.D2_ACTIVE;
        questData.markDirtySelf();
        spawnD2Assault(level, player.getUUID(), largest.get());
        player.sendSystemMessage(Component.translatable("quest.villagers.d2_start"));
    }

    private static void spawnD2Assault(ServerLevel level, UUID owner, VillageData village) {
        BlockPos center = new BlockPos(village.getMarkerX(), village.getMarkerY(), village.getMarkerZ());
        List<UUID> minions = new ArrayList<>();
        int count = 24 + level.random.nextInt(7);
        for (int i = 0; i < count; i++) {
            BlockPos spawn = center.offset(level.random.nextInt(48) - 24, 0, level.random.nextInt(48) - 24);
            Villager bandit = BanditSpawnHelper.spawnBandit(level, spawn, UUID.randomUUID(), false, BanditData.BossRole.NONE, owner, null);
            minions.add(bandit.getUUID());
        }
        Villager garland = BanditSpawnHelper.spawnBandit(level, center.offset(0, 0, 24), UUID.randomUUID(), true,
                BanditData.BossRole.GARLAND, owner, Component.literal("Garland"));
        garland.getData(VillagerAttachments.BANDIT_DATA.get()).setD2GarlandFleeImmune(true);
        minions.add(garland.getUUID());
        assaultMinions.put(owner, minions);
        d2Assaults.put(owner, new D2AssaultState(new ArrayList<>(minions), count, garland.getUUID()));
    }

    private static void tickActiveAssault(ServerLevel level, UUID owner, ArmiesQuestSavedData.PlayerQuestState state,
            ArmiesQuestSavedData questData) {
        List<UUID> ids = assaultMinions.get(owner);
        if (ids == null || ids.isEmpty()) {
            return;
        }
        if (state.stage == ArmiesQuestStage.D2_ACTIVE) {
            var largest = VillageSoldierCounts.largestOwnedVillage(level, owner);
            if (largest.isPresent() && !villageDefended(level, largest.get())) {
                failAssault(level, owner, state, questData, Component.translatable("quest.villagers.d2_failed"));
                return;
            }
        }
        if (state.stage != ArmiesQuestStage.D1_ACTIVE && state.stage != ArmiesQuestStage.D2_ACTIVE) {
            return;
        }
        ids.removeIf(id -> level.getEntity(id) == null || !level.getEntity(id).isAlive());
        if (state.stage == ArmiesQuestStage.D2_ACTIVE) {
            var player = level.getServer().getPlayerList().getPlayer(owner);
            if (player != null) {
                tryGarlandFleeFromLosses(level, player, state);
            }
        }
    }

    /** Garland flees when below 30% HP (damage handler) or when more than half his bandits are gone. Bandits only — no undead. */
    public static void tryGarlandFleeFromLosses(ServerLevel level, ServerPlayer owner, ArmiesQuestSavedData.PlayerQuestState state) {
        D2AssaultState d2 = d2Assaults.get(owner.getUUID());
        if (d2 == null || state.stage != ArmiesQuestStage.D2_ACTIVE) {
            return;
        }
        var largest = VillageSoldierCounts.largestOwnedVillage(level, owner.getUUID());
        if (largest.isEmpty() || !villageDefended(level, largest.get())) {
            return;
        }
        if (!(level.getEntity(d2.garlandId()) instanceof Villager garland) || !garland.isAlive()) {
            return;
        }
        int aliveBandits = 0;
        for (UUID id : d2.entityIds()) {
            if (id.equals(d2.garlandId())) {
                continue;
            }
            var entity = level.getEntity(id);
            if (entity != null && entity.isAlive()) {
                aliveBandits++;
            }
        }
        double lossFraction = com.villagers.mod.Config.ARMIES_QUEST_D2_GARLAND_FLEE_BANDIT_LOSS_FRACTION.get();
        int dead = d2.initialBanditCount() - aliveBandits;
        boolean enoughLoss = d2.initialBanditCount() > 0 && dead >= (int) Math.ceil(d2.initialBanditCount() * lossFraction);
        if (enoughLoss) {
            onGarlandFleeD2(owner, garland);
        }
    }

    private static void failAssault(ServerLevel level, UUID owner, ArmiesQuestSavedData.PlayerQuestState state,
            ArmiesQuestSavedData questData, Component message) {
        for (UUID id : assaultMinions.getOrDefault(owner, List.of())) {
            var entity = level.getEntity(id);
            if (entity != null) {
                entity.discard();
            }
        }
        assaultMinions.remove(owner);
        d2Assaults.remove(owner);
        if (state.stage == ArmiesQuestStage.D2_ACTIVE) {
            state.stage = ArmiesQuestStage.D2_ARMED;
        } else if (state.stage == ArmiesQuestStage.D1_ACTIVE) {
            state.stage = ArmiesQuestStage.D1_WAITING_ENTER;
        }
        questData.markDirtySelf();
        playerMessage(level, owner, message);
    }

    public static void onHalvekKilled(ServerPlayer killer, Villager halvek) {
        ServerLevel level = killer.serverLevel();
        ArmiesQuestSavedData questData = ArmiesQuestSavedData.get(level);
        ArmiesQuestSavedData.PlayerQuestState state = questData.getOrCreate(killer.getUUID());
        if (state.stage != ArmiesQuestStage.D1_ACTIVE) {
            return;
        }
        state.stage = ArmiesQuestStage.O1_LEDGER;
        questData.markDirtySelf();
        assaultMinions.remove(killer.getUUID());
        halvek.spawnAtLocation(new ItemStack(VillagersMod.BANDIT_LEDGER.get()));
        killer.sendSystemMessage(Component.translatable("quest.villagers.o1_ledger"));
    }

    public static void onCorvinKilled(ServerPlayer killer) {
        ServerLevel level = killer.serverLevel();
        ArmiesQuestSavedData questData = ArmiesQuestSavedData.get(level);
        ArmiesQuestSavedData.PlayerQuestState state = questData.getOrCreate(killer.getUUID());
        if (state.stage != ArmiesQuestStage.O1_ACTIVE && state.stage != ArmiesQuestStage.O1_LEDGER) {
            return;
        }
        state.stage = ArmiesQuestStage.D2_ARMED;
        state.corvinKilled = true;
        BanditWorldSavedData.get(level).setCorvinBossDefeated();
        questData.markDirtySelf();
        killer.sendSystemMessage(Component.translatable("quest.villagers.corvin_line"));
        killer.sendSystemMessage(Component.translatable("quest.villagers.d2_armed"));
    }

    public static void onGarlandFleeD2(ServerPlayer owner, Villager garland) {
        ServerLevel level = owner.serverLevel();
        ArmiesQuestSavedData questData = ArmiesQuestSavedData.get(level);
        ArmiesQuestSavedData.PlayerQuestState state = questData.getOrCreate(owner.getUUID());
        if (state.stage != ArmiesQuestStage.D2_ACTIVE) {
            return;
        }
        state.stage = ArmiesQuestStage.O2_HUNT;
        questData.markDirtySelf();
        garland.spawnAtLocation(new ItemStack(VillagersMod.TORN_MAP_HALF.get()));
        garland.discard();
        assaultMinions.remove(owner.getUUID());
        d2Assaults.remove(owner.getUUID());
        owner.sendSystemMessage(Component.translatable("quest.villagers.garland_fled"));
    }

    public static void onGarlandKilledO2(ServerPlayer killer) {
        ServerLevel level = killer.serverLevel();
        ArmiesQuestSavedData questData = ArmiesQuestSavedData.get(level);
        ArmiesQuestSavedData.PlayerQuestState state = questData.getOrCreate(killer.getUUID());
        if (state.stage != ArmiesQuestStage.O2_ACTIVE && state.stage != ArmiesQuestStage.O2_HUNT) {
            return;
        }
        state.stage = ArmiesQuestStage.COMPLETE;
        state.garlandKilled = true;
        state.rookbreakerTitle = true;
        BanditWorldSavedData.get(level).setGarlandBossDefeated();
        questData.markDirtySelf();
        killer.spawnAtLocation(new ItemStack(VillagersMod.GARLAND_SIGNET.get()));
        killer.sendSystemMessage(Component.translatable("quest.villagers.rookbreaker"));
    }

    public static void onLedgerRead(ServerPlayer player) {
        ArmiesQuestSavedData questData = ArmiesQuestSavedData.get(player.serverLevel());
        ArmiesQuestSavedData.PlayerQuestState state = questData.getOrCreate(player.getUUID());
        if (state.stage != ArmiesQuestStage.O1_LEDGER && state.stage != ArmiesQuestStage.O1_ACTIVE) {
            return;
        }
        state.ledgerRead = true;
        state.stage = ArmiesQuestStage.O1_ACTIVE;
        questData.markDirtySelf();
        BanditWorldSavedData.SiteRecord corvin = BanditWorldSavedData.get(player.serverLevel()).findKind(BanditWorldSavedData.SiteKind.CORVIN);
        if (corvin != null) {
            player.sendSystemMessage(Component.translatable("quest.villagers.corvin_bearing",
                    corvin.origin().getX(), corvin.origin().getZ()));
        }
    }

    /** O2: no Rook's Trail Compass — use torn map + chat bearing to Garland camp. */
    public static void onTornMapRead(ServerPlayer player) {
        ArmiesQuestSavedData questData = ArmiesQuestSavedData.get(player.serverLevel());
        ArmiesQuestSavedData.PlayerQuestState state = questData.getOrCreate(player.getUUID());
        if (state.stage != ArmiesQuestStage.O2_HUNT && state.stage != ArmiesQuestStage.O2_ACTIVE) {
            return;
        }
        state.stage = ArmiesQuestStage.O2_ACTIVE;
        state.tornMapRead = true;
        questData.markDirtySelf();
        BanditWorldSavedData.SiteRecord garland = BanditWorldSavedData.get(player.serverLevel())
                .findKind(BanditWorldSavedData.SiteKind.GARLAND);
        if (garland != null) {
            player.sendSystemMessage(Component.translatable(
                    "quest.villagers.garland_bearing", garland.origin().getX(), garland.origin().getZ()));
        } else {
            player.sendSystemMessage(Component.translatable("quest.villagers.garland_bearing_unknown"));
        }
    }

    public static void onGarlandCompassCrafted(ServerPlayer player, net.minecraft.world.item.ItemStack compass) {
        ArmiesQuestSavedData questData = ArmiesQuestSavedData.get(player.serverLevel());
        ArmiesQuestSavedData.PlayerQuestState state = questData.getOrCreate(player.getUUID());
        state.garlandCompassUnlocked = true;
        state.stage = ArmiesQuestStage.O2_ACTIVE;
        questData.markDirtySelf();
        GarlandCompassHelper.tagToGarlandCamp(player.serverLevel(), compass);
        BanditWorldSavedData.SiteRecord garland = BanditWorldSavedData.get(player.serverLevel()).findKind(BanditWorldSavedData.SiteKind.GARLAND);
        if (garland != null) {
            player.sendSystemMessage(Component.translatable("quest.villagers.garland_bearing",
                    garland.origin().getX(), garland.origin().getZ()));
        }
    }

    public static boolean villageDefended(ServerLevel level, VillageData village) {
        BlockPos center = new BlockPos(village.getMarkerX(), village.getMarkerY(), village.getMarkerZ());
        int r = Math.max(village.getRadius(), 64);
        boolean messOk = false;
        boolean bedOk = false;
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-r, -16, -r), center.offset(r, 16, r))) {
            if (!messOk && level.getBlockState(pos).getBlock() instanceof MessStationBlock) {
                messOk = true;
            }
            if (!bedOk && level.getBlockState(pos).getBlock() instanceof BedBlock) {
                bedOk = true;
            }
            if (!bedOk && level.getBlockState(pos).getBlock() instanceof PostBlock) {
                bedOk = true;
            }
        }
        return messOk && bedOk;
    }

    private static void playerMessage(ServerLevel level, UUID owner, Component msg) {
        var player = level.getServer().getPlayerList().getPlayer(owner);
        if (player != null) {
            player.sendSystemMessage(msg);
        }
    }

    public static double desertionMultiplierForOwner(ServerLevel level, UUID ownerId) {
        if (ArmiesQuestSavedData.get(level).hasRookbreaker(ownerId)) {
            return Config.TITLE_ROOKBREAKER_DESERTION_MULTIPLIER.get();
        }
        return 1.0;
    }
}
