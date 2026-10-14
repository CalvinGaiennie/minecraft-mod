package com.villagers.mod.atlas;

import net.minecraft.world.level.material.MapColor;

public final class MapPalette {
    public static final int UNEXPLORED_ARGB = 0xFF050508;

    private MapPalette() {
    }

    public static int argbFromMapColorId(byte id) {
        MapColor mapColor = MapColor.byId(id & 0xFF);
        if (mapColor == MapColor.NONE) {
            return 0xFF1A1A1A;
        }
        return 0xFF000000 | mapColor.col;
    }
}
