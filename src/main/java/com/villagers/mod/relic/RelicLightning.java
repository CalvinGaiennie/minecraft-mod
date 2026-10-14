package com.villagers.mod.relic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;

public final class RelicLightning {
    private RelicLightning() {
    }

    public static void strike(ServerLevel level, double x, double y, double z) {
        var bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(x, y, z);
            level.addFreshEntity(bolt);
        }
    }
}
