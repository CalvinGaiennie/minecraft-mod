package com.villagers.mod.armies;

import java.util.Optional;

import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;

public final class GarlandCompassHelper {
    private GarlandCompassHelper() {
    }

    public static void tagToGarlandCamp(ServerLevel level, ItemStack stack) {
        BanditWorldSavedData.SiteRecord garland = BanditWorldSavedData.get(level).findKind(BanditWorldSavedData.SiteKind.GARLAND);
        if (garland == null) {
            return;
        }
        stack.set(DataComponents.LODESTONE_TRACKER,
                new LodestoneTracker(Optional.of(GlobalPos.of(level.dimension(), garland.origin())), false));
    }
}
