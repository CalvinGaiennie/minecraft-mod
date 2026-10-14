package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Per-commander charge plan: which squad boxes join the next charge and assault target. */
public final class AttackPlanSavedData extends SavedData {
    private static final String DATA_ID = "villagers_attack_plans";
    private final Map<UUID, Plan> byOwner = new HashMap<>();

    public record Plan(Set<Long> squadBoxes, @Nullable BlockPos target, int siegeLadders) {}

    public static AttackPlanSavedData get(ServerLevel level) {
        return level.getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(AttackPlanSavedData::new, AttackPlanSavedData::load, null), DATA_ID);
    }

    private static AttackPlanSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        AttackPlanSavedData data = new AttackPlanSavedData();
        CompoundTag owners = tag.getCompound("Owners");
        for (String key : owners.getAllKeys()) {
            UUID owner = UUID.fromString(key);
            CompoundTag one = owners.getCompound(key);
            Set<Long> boxes = new HashSet<>();
            for (long packed : one.getLongArray("Squads")) {
                boxes.add(packed);
            }
            BlockPos target = one.contains("Target") ? BlockPos.of(one.getLong("Target")) : null;
            int ladders = one.getInt("Ladders");
            data.byOwner.put(owner, new Plan(Set.copyOf(boxes), target, ladders));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag owners = new CompoundTag();
        for (var entry : byOwner.entrySet()) {
            CompoundTag one = new CompoundTag();
            long[] arr = entry.getValue().squadBoxes().stream().mapToLong(l -> l).toArray();
            one.putLongArray("Squads", arr);
            if (entry.getValue().target() != null) {
                one.putLong("Target", entry.getValue().target().asLong());
            }
            one.putInt("Ladders", entry.getValue().siegeLadders());
            owners.put(entry.getKey().toString(), one);
        }
        tag.put("Owners", owners);
        return tag;
    }

    public Plan getPlan(UUID owner) {
        return byOwner.getOrDefault(owner, new Plan(Set.of(), null, 0));
    }

    public void setPlan(UUID owner, Plan plan) {
        if (plan.squadBoxes().isEmpty() && plan.target() == null && plan.siegeLadders() == 0) {
            byOwner.remove(owner);
        } else {
            byOwner.put(owner, plan);
        }
        setDirty();
    }
}
