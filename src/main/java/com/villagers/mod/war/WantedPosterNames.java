package com.villagers.mod.war;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class WantedPosterNames {
    private WantedPosterNames() {
    }

    public static List<UUID> resolveNames(ServerLevel level, List<String> rawLines) {
        MinecraftServer server = level.getServer();
        List<UUID> out = new ArrayList<>();
        for (String line : rawLines) {
            if (out.size() >= 4) {
                break;
            }
            UUID id = resolveOne(server, line);
            if (id != null) {
                out.add(id);
            }
        }
        return out;
    }

    @Nullable
    private static UUID resolveOne(MinecraftServer server, @Nullable String raw) {
        if (raw == null) {
            return null;
        }
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(trimmed);
        } catch (IllegalArgumentException ignored) {
            // fall through
        }
        Optional<GameProfile> profile = server.getProfileCache().get(trimmed);
        return profile.map(GameProfile::getId).orElse(null);
    }

    public static String displayName(MinecraftServer server, UUID id) {
        return server.getProfileCache().get(id).map(GameProfile::getName).orElse(id.toString());
    }
}
