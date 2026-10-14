package com.villagers.mod.war;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/** Dye color assigned to vanilla levers (soldier levers). */
public final class LeverColorSavedData extends SavedData {
    private static final String DATA_ID = "villagers_soldier_levers";
    private final Map<Long, String> colorByPos = new HashMap<>();

    public static LeverColorSavedData get(ServerLevel level) {
        return level.getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(LeverColorSavedData::new, LeverColorSavedData::load, null), DATA_ID);
    }

    private static LeverColorSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        LeverColorSavedData data = new LeverColorSavedData();
        CompoundTag map = tag.getCompound("Colors");
        for (String key : map.getAllKeys()) {
            data.colorByPos.put(Long.parseLong(key), map.getString(key));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag map = new CompoundTag();
        colorByPos.forEach((pos, color) -> map.putString(Long.toString(pos), color));
        tag.put("Colors", map);
        return tag;
    }

    public void setColor(BlockPos pos, DyeColor color) {
        colorByPos.put(pos.asLong(), color.getName());
        setDirty();
    }

    @Nullable
    public DyeColor getColor(BlockPos pos) {
        String name = colorByPos.get(pos.asLong());
        if (name == null) {
            return null;
        }
        for (DyeColor color : LeverHornService.HORN_COLORS) {
            if (color.getName().equals(name)) {
                return color;
            }
        }
        return null;
    }

    public void clear(BlockPos pos) {
        if (colorByPos.remove(pos.asLong()) != null) {
            setDirty();
        }
    }
}
