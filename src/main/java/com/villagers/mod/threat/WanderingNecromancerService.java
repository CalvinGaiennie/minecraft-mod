package com.villagers.mod.threat;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.Config;
import com.villagers.mod.VillagersMod;
import com.villagers.mod.armies.VillageSoldierCounts;
import com.villagers.mod.combat.WanderingNecromancerCombat;
import com.villagers.mod.entity.DeserterIdentity;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.entity.WanderingNecromancerData;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.player.EnlistmentSavedData;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class WanderingNecromancerService {
    private WanderingNecromancerService() {
    }

    public static boolean isWanderingNecromancer(Villager villager) {
        return villager.hasData(VillagerAttachments.WANDERING_NECROMANCER_DATA.get());
    }

    public static void convertDeserter(Villager villager, UUID exOwnerId, DeserterIdentity identity) {
        if (!(villager.level() instanceof ServerLevel level) || level.isClientSide) {
            return;
        }
        WanderingNecromancerData data = new WanderingNecromancerData(villager.getUUID());
        data.setExOwnerId(exOwnerId);
        boolean ownerOnline = exOwnerId != null && level.getServer().getPlayerList().getPlayer(exOwnerId) != null;
        data.setExOwnerOnlineSinceDesert(ownerOnline);
        villager.setData(VillagerAttachments.WANDERING_NECROMANCER_DATA.get(), data);
        applyNecromancerName(villager, identity);
        villager.setCustomNameVisible(true);
        villager.setPersistenceRequired();
        equipNecromancerKit(villager);
        WanderingNecromancerCombat.applyLeaderStats(villager);
        WanderingNecromancerCombat.enableCombatGoals(villager);
        long day = level.getDayTime() / 24000L;
        int delay = Config.WANDERING_NECROMANCER_GRUDGE_DELAY_DAYS.get();
        WanderingNecromancerSavedData.get(level).add(
                new WanderingNecromancerSavedData.GrudgeRecord(villager.getUUID(), exOwnerId, day + delay));
    }

    static void applyNecromancerName(Villager villager, DeserterIdentity identity) {
        String label = deserterNameLabel(identity);
        if (label.isEmpty()) {
            villager.setCustomName(Component.translatable("entity.villagers.wandering_necromancer"));
        } else {
            villager.setCustomName(Component.literal(label));
        }
    }

    /** Personal soldier name + optional kill count (same spirit as soldier tags). */
    static String deserterNameLabel(DeserterIdentity identity) {
        if (identity == null) {
            return "";
        }
        String personal = identity.personalName() != null ? identity.personalName().trim() : "";
        if ("Villager".equalsIgnoreCase(personal) || "Soldier".equalsIgnoreCase(personal)) {
            personal = "";
        }
        int kills = identity.kills();
        if (personal.isEmpty()) {
            return kills > 0 ? "Deserter (" + kills + " kills)" : "";
        }
        return kills > 0 ? personal + " (" + kills + " kills)" : personal;
    }

    private static void equipNecromancerKit(Villager villager) {
        ItemStack hood = new ItemStack(VillagersMod.NECROMANCER_HOOD.get());
        ItemStack robe = new ItemStack(VillagersMod.NECROMANCER_ROBE.get());
        if (VillagerGearRules.accepts(VillagerGearSlot.HELMET, hood)) {
            VillagerGearRules.set(villager, VillagerGearSlot.HELMET, hood);
        }
        if (VillagerGearRules.accepts(VillagerGearSlot.CHESTPLATE, robe)) {
            VillagerGearRules.set(villager, VillagerGearSlot.CHESTPLATE, robe);
        }
    }

    public static void onExOwnerLogin(ServerLevel level, UUID ownerId) {
        for (WanderingNecromancerSavedData.GrudgeRecord record : WanderingNecromancerSavedData.get(level).grudges()) {
            if (!record.exOwnerId().equals(ownerId)) {
                continue;
            }
            if (level.getEntity(record.necromancerEntityId()) instanceof Villager necro) {
                WanderingNecromancerData data = necro.getData(VillagerAttachments.WANDERING_NECROMANCER_DATA.get());
                if (data != null) {
                    data.setExOwnerOnlineSinceDesert(true);
                }
            }
        }
    }

    public static void tryDailyStrikes(ServerLevel level, long day) {
        WanderingNecromancerSavedData saved = WanderingNecromancerSavedData.get(level);
        saved.purgeDead(level);
        int interval = Config.WANDERING_NECROMANCER_STRIKE_INTERVAL_DAYS.get();
        EnlistmentSavedData enlistment = EnlistmentSavedData.get(level);

        for (WanderingNecromancerSavedData.GrudgeRecord record : List.copyOf(saved.grudges())) {
            if (day < record.nextStrikeDay()) {
                continue;
            }
            if (record.exOwnerId() == null) {
                saved.updateNextStrike(record.necromancerEntityId(), day + interval);
                continue;
            }
            if (enlistment.isOptedOut(record.exOwnerId())) {
                saved.updateNextStrike(record.necromancerEntityId(), day + interval);
                continue;
            }
            if (!(level.getEntity(record.necromancerEntityId()) instanceof Villager necro)
                    || !isWanderingNecromancer(necro)) {
                saved.removeNecromancer(record.necromancerEntityId());
                continue;
            }
            WanderingNecromancerData data = necro.getData(VillagerAttachments.WANDERING_NECROMANCER_DATA.get());
            if (data == null || !data.isExOwnerOnlineSinceDesert()) {
                saved.updateNextStrike(record.necromancerEntityId(), day + interval);
                continue;
            }
            executeStrike(level, necro, record.exOwnerId());
            saved.updateNextStrike(record.necromancerEntityId(), day + interval);
        }
    }

    private static void executeStrike(ServerLevel level, Villager necro, UUID exOwnerId) {
        ServerPlayer owner = level.getServer().getPlayerList().getPlayer(exOwnerId);
        List<VillageData> villages = VillageManager.get(level).getVillagesOwnedBy(exOwnerId);
        boolean ambushOnly = villages.isEmpty();
        boolean ambush = ambushOnly || level.random.nextBoolean();
        if (ambush) {
            if (owner != null && owner.level() == level) {
                ambushOwner(level, necro, owner);
                speakStrikeLine(necro, owner, true, null);
            }
            return;
        }
        Optional<VillageData> weakest = weakestOwnedVillage(level, exOwnerId);
        if (weakest.isEmpty()) {
            return;
        }
        VillageData village = weakest.get();
        BlockPos target = new BlockPos(village.getMarkerX(), village.getMarkerY(), village.getMarkerZ());
        spawnUndeadSwarm(level, target, 6 + level.random.nextInt(5));
        if (owner != null) {
            speakStrikeLine(necro, owner, false, village.getVillageName());
        }
    }

    private static void speakStrikeLine(Villager necro, ServerPlayer owner, boolean ambush, String villageName) {
        int variant = owner.level().random.nextInt(2);
        String dialogKey = ambush
                ? (variant == 0 ? "dialog.villagers.wandering_necro_ambush_a" : "dialog.villagers.wandering_necro_ambush_b")
                : (variant == 0 ? "dialog.villagers.wandering_necro_village_a" : "dialog.villagers.wandering_necro_village_b");
        Component line = ambush
                ? Component.translatable(dialogKey)
                : Component.translatable(dialogKey, villageName != null ? villageName : "");
        Component said = Component.empty()
                .append(necro.getDisplayName().copy().withStyle(ChatFormatting.DARK_RED))
                .append(Component.literal(": ").withStyle(ChatFormatting.GRAY))
                .append(line.copy().withStyle(ChatFormatting.LIGHT_PURPLE));
        owner.sendSystemMessage(said);
        if (ambush) {
            level(necro).playSound(null, necro.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 1.0f, 0.85f);
        }
    }

    private static ServerLevel level(Villager villager) {
        return (ServerLevel) villager.level();
    }

    private static void ambushOwner(ServerLevel level, Villager necro, ServerPlayer owner) {
        double angle = level.random.nextDouble() * Math.PI * 2;
        double dist = 28 + level.random.nextInt(12);
        Vec3 at = owner.position().add(Math.cos(angle) * dist, 0, Math.sin(angle) * dist);
        necro.teleportTo(at.x, at.y, at.z);
        necro.getLookControl().setLookAt(owner, 30.0f, 30.0f);
        if (necro instanceof Mob mob) {
            mob.setTarget(owner);
        }
        spawnUndeadSwarm(level, owner.blockPosition(), 4 + level.random.nextInt(4));
    }

    public static void spawnUndeadSwarm(ServerLevel level, BlockPos target, int count) {
        if (!level.isLoaded(target)) {
            return;
        }
        List<UUID> members = new ArrayList<>();
        UUID swarmId = UUID.randomUUID();
        EntityType<? extends Mob> type = EntityType.ZOMBIE;
        for (int i = 0; i < count; i++) {
            EntityType<? extends Mob> pick = level.random.nextBoolean() ? EntityType.ZOMBIE : EntityType.SKELETON;
            double angle = level.random.nextDouble() * Math.PI * 2;
            double dist = 16 + level.random.nextInt(12);
            Vec3 pos = Vec3.atCenterOf(target).add(Math.cos(angle) * dist, 0, Math.sin(angle) * dist);
            Mob mob = pick.create(level);
            if (mob == null) {
                continue;
            }
            mob.moveTo(pos.x, pos.y, pos.z, level.random.nextFloat() * 360, 0);
            mob.setCustomName(Component.translatable("entity.villagers.necro_swarm_minion"));
            mob.setCustomNameVisible(false);
            mob.setPersistenceRequired();
            level.addFreshEntity(mob);
            members.add(mob.getUUID());
        }
        if (!members.isEmpty()) {
            SwarmSavedData.get(level).add(new SwarmSavedData.SwarmRecord(swarmId, type.toShortString(), target, members));
        }
    }

    public static Optional<VillageData> weakestOwnedVillage(ServerLevel level, UUID ownerId) {
        return VillageManager.get(level).getVillagesOwnedBy(ownerId).stream()
                .min(Comparator.comparingDouble(v -> {
                    int soldiers = VillageSoldierCounts.countSoldiersInVillage(level, v);
                    int members = Math.max(1, VillageManager.countMembersInVillage(level, v));
                    return (double) soldiers / members;
                }));
    }

    public static void onNecromancerDeath(ServerLevel level, Villager villager) {
        WanderingNecromancerSavedData.get(level).removeNecromancer(villager.getUUID());
        villager.spawnAtLocation(new ItemStack(VillagersMod.PHYLACTERY_SHARD.get()));
    }
}
