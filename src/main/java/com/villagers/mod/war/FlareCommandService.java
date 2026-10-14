package com.villagers.mod.war;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class FlareCommandService {
    private FlareCommandService() {
    }

    public static int detonate(ServerPlayer player, Vec3 burst, ItemStack fireworkStack, boolean strike) {
        int gunpowder = countGunpowder(fireworkStack);
        int level = Math.clamp(gunpowder, 1, 3);
        int respond = switch (level) {
            case 1 -> 32;
            case 2 -> 64;
            default -> 128;
        };
        int strikeRadius = switch (level) {
            case 1 -> 8;
            case 2 -> 16;
            default -> 32;
        };
        SoldierSignalType type = strike ? SoldierSignalType.STRIKE : SoldierSignalType.RALLY;
        return HornCommandService.applyFlare(player, type, burst, respond, strikeRadius);
    }

    private static int countGunpowder(ItemStack stack) {
        var fireworks = stack.get(net.minecraft.core.component.DataComponents.FIREWORKS);
        if (fireworks == null) {
            return 1;
        }
        return Math.max(1, fireworks.flightDuration());
    }
}
