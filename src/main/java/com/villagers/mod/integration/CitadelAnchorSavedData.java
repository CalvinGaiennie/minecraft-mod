package com.villagers.mod.integration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Optional;

/** Overworld citadel center; set by Citadel mod when the Black Citadel is placed. */
public class CitadelAnchorSavedData extends SavedData {
    private static final String DATA_ID = "villagers_citadel_anchor";
    private boolean set;
    private int x;
    private int y;
    private int z;

    public static CitadelAnchorSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(CitadelAnchorSavedData::new, CitadelAnchorSavedData::load, null),
                DATA_ID);
    }

    private static CitadelAnchorSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        CitadelAnchorSavedData data = new CitadelAnchorSavedData();
        data.set = tag.getBoolean("Set");
        data.x = tag.getInt("X");
        data.y = tag.getInt("Y");
        data.z = tag.getInt("Z");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putBoolean("Set", set);
        tag.putInt("X", x);
        tag.putInt("Y", y);
        tag.putInt("Z", z);
        return tag;
    }

    public static void setCitadelCenter(ServerLevel level, BlockPos center) {
        CitadelAnchorSavedData data = get(level);
        data.set = true;
        data.x = center.getX();
        data.y = center.getY();
        data.z = center.getZ();
        data.setDirty();
    }

    public static Optional<BlockPos> citadelCenter(ServerLevel level) {
        if (!level.dimension().equals(Level.OVERWORLD)) {
            return Optional.empty();
        }
        CitadelAnchorSavedData data = get(level);
        if (!data.set) {
            return Optional.empty();
        }
        return Optional.of(new BlockPos(data.x, data.y, data.z));
    }
}
