package com.villagers.mod.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import com.villagers.mod.war.SoldierSignalType;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/** NBT persistence for combat villager attachments (kills, beds, campaign state). */
public final class CombatAttachmentCodecs {
    public static final Codec<SoldierData> SOLDIER_DATA = nbtCodec(CombatAttachmentCodecs::readSoldier, CombatAttachmentCodecs::writeSoldier);
    public static final Codec<MilitiaData> MILITIA_DATA = nbtCodec(CombatAttachmentCodecs::readMilitia, CombatAttachmentCodecs::writeMilitia);
    public static final Codec<VeteranData> VETERAN_DATA = nbtCodec(CombatAttachmentCodecs::readVeteran, CombatAttachmentCodecs::writeVeteran);

    private CombatAttachmentCodecs() {
    }

    private static <T> Codec<T> nbtCodec(java.util.function.Function<CompoundTag, T> read, java.util.function.Function<T, CompoundTag> write) {
        return CompoundTag.CODEC.flatXmap(
                tag -> DataResult.success(read.apply(tag)),
                value -> DataResult.success(write.apply(value)));
    }

    private static void writeBlockPos(CompoundTag tag, String key, @Nullable BlockPos pos) {
        if (pos != null) {
            tag.put(key, NbtUtils.writeBlockPos(pos));
        }
    }

    @Nullable
    private static BlockPos readBlockPos(CompoundTag tag, String key) {
        if (tag.contains(key, net.minecraft.nbt.Tag.TAG_COMPOUND)) {
            return NbtUtils.readBlockPos(tag, key).orElse(null);
        }
        return null;
    }

    private static CompoundTag writeSoldier(SoldierData data) {
        CompoundTag tag = new CompoundTag();
        if (data.getSoldierUUID() != null) {
            tag.putUUID("SoldierUUID", data.getSoldierUUID());
        }
        tag.putInt("Kills", data.getKills());
        tag.putString("Rank", data.getRank());
        tag.putString("DisplayName", data.getDisplayName());
        tag.putInt("HomelessNights", data.getHomelessNights());
        tag.putLong("LastFoodDay", data.getLastFoodDay());
        tag.putLong("AwakenUntil", data.getAwakenUntilGameTime());
        tag.putLong("LastDamage", data.getLastDamageGameTime());
        tag.putBoolean("OnCampaign", data.isOnCampaign());
        writeBlockPos(tag, "CampaignCamp", data.getCampaignCamp());
        if (data.getFollowPlayerId() != null) {
            tag.putUUID("FollowPlayer", data.getFollowPlayerId());
        }
        tag.putInt("Grumble", data.getGrumblePoints());
        tag.putInt("PracticeKills", data.getPracticeKills());
        writeBlockPos(tag, "PostBed", data.getAssignedPostBed());
        writeBlockPos(tag, "SquadBox", data.getSquadBoxPos());
        tag.putBoolean("SquadLeader", data.isSquadLeader());
        tag.putBoolean("ActingLeader", data.isActingLeader());
        writeBlockPos(tag, "Rampart", data.getAssignedRampart());
        tag.putString("ActiveSignal", data.getActiveSignal().name());
        tag.putLong("SignalUntil", data.getSignalUntilGameTime());
        tag.putDouble("SignalX", data.getSignalX());
        tag.putDouble("SignalY", data.getSignalY());
        tag.putDouble("SignalZ", data.getSignalZ());
        tag.putInt("SignalRadius", data.getSignalRadius());
        writeBlockPos(tag, "RedstoneTarget", data.getRedstoneTargetPos());
        tag.putLong("RedstoneUntil", data.getRedstoneTargetUntil());
        return tag;
    }

    private static SoldierData readSoldier(CompoundTag tag) {
        SoldierData data = tag.hasUUID("SoldierUUID") ? new SoldierData(tag.getUUID("SoldierUUID")) : new SoldierData();
        data.setKills(tag.getInt("Kills"));
        if (tag.contains("Rank")) {
            data.setRank(tag.getString("Rank"));
        }
        data.setDisplayName(tag.getString("DisplayName"));
        data.setHomelessNights(tag.getInt("HomelessNights"));
        data.setLastFoodDay(tag.getLong("LastFoodDay"));
        data.setAwakenUntilGameTime(tag.getLong("AwakenUntil"));
        data.setLastDamageGameTime(tag.getLong("LastDamage"));
        data.setOnCampaign(tag.getBoolean("OnCampaign"));
        data.setCampaignCamp(readBlockPos(tag, "CampaignCamp"));
        if (tag.hasUUID("FollowPlayer")) {
            data.setFollowPlayerId(tag.getUUID("FollowPlayer"));
        }
        data.setGrumblePoints(tag.getInt("Grumble"));
        BlockPos bed = readBlockPos(tag, "PostBed");
        if (bed != null) {
            data.setAssignedPostBed(bed);
        }
        data.setSquadBoxPos(readBlockPos(tag, "SquadBox"));
        data.setSquadLeader(tag.getBoolean("SquadLeader"));
        data.setActingLeader(tag.getBoolean("ActingLeader"));
        data.setAssignedRampart(readBlockPos(tag, "Rampart"));
        if (tag.contains("ActiveSignal")) {
            try {
                data.setActiveSignal(SoldierSignalType.valueOf(tag.getString("ActiveSignal")));
            } catch (IllegalArgumentException ignored) {
                data.setActiveSignal(SoldierSignalType.NONE);
            }
        }
        data.setSignalUntilGameTime(tag.getLong("SignalUntil"));
        data.setSignalX(tag.getDouble("SignalX"));
        data.setSignalY(tag.getDouble("SignalY"));
        data.setSignalZ(tag.getDouble("SignalZ"));
        data.setSignalRadius(tag.getInt("SignalRadius"));
        data.setRedstoneTargetPos(readBlockPos(tag, "RedstoneTarget"));
        data.setRedstoneTargetUntil(tag.getLong("RedstoneUntil"));
        return data;
    }

    private static CompoundTag writeMilitia(MilitiaData data) {
        CompoundTag tag = new CompoundTag();
        if (data.getMilitiaId() != null) {
            tag.putUUID("MilitiaId", data.getMilitiaId());
        }
        tag.putInt("Kills", data.getKills());
        tag.putLong("LastFoodDay", data.getLastFoodDay());
        tag.putString("DisplayName", data.getDisplayName());
        tag.putBoolean("TraderAtEnlist", data.wasTraderAtEnlist());
        tag.putLong("LastDamage", data.getLastDamageGameTime());
        writeBlockPos(tag, "HomeBed", data.getHomeBed());
        return tag;
    }

    private static MilitiaData readMilitia(CompoundTag tag) {
        MilitiaData data = tag.hasUUID("MilitiaId") ? new MilitiaData(tag.getUUID("MilitiaId")) : new MilitiaData();
        data.setKills(tag.getInt("Kills"));
        data.setLastFoodDay(tag.getLong("LastFoodDay"));
        data.setDisplayName(tag.getString("DisplayName"));
        data.setTraderAtEnlist(tag.getBoolean("TraderAtEnlist"));
        data.setLastDamageGameTime(tag.getLong("LastDamage"));
        data.setHomeBed(readBlockPos(tag, "HomeBed"));
        return data;
    }

    private static CompoundTag writeVeteran(VeteranData data) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Kills", data.getKills());
        tag.putString("Rank", data.getRank());
        writeBlockPos(tag, "HomeBed", data.getHomeBed());
        return tag;
    }

    private static VeteranData readVeteran(CompoundTag tag) {
        VeteranData data = new VeteranData(tag.getInt("Kills"), tag.getString("Rank"));
        data.setHomeBed(readBlockPos(tag, "HomeBed"));
        return data;
    }
}
