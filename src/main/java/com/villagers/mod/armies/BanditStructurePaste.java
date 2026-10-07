package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import com.villagers.mod.VillagersMod;

import java.util.Optional;

/** Pastes bandit prefabs from {@code data/villagers/structures/*.nbt} when present. */
public final class BanditStructurePaste {
    private BanditStructurePaste() {
    }

    public static boolean tryPaste(ServerLevel level, BlockPos origin, BanditWorldSavedData.SiteKind kind) {
        Optional<ResourceLocation> id = templateId(kind);
        if (id.isEmpty()) {
            return false;
        }
        StructureTemplate template = level.getServer().getStructureManager().get(id.get()).orElse(null);
        if (template == null) {
            return false;
        }
        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(Rotation.NONE)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false);
        template.placeInWorld(level, origin, origin, settings, level.getRandom(), 2);
        VillagersMod.LOGGER.info("Pasted bandit structure {} at {}", id.get(), origin);
        return true;
    }

    private static Optional<ResourceLocation> templateId(BanditWorldSavedData.SiteKind kind) {
        return switch (kind) {
            case CAMP -> Optional.of(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "camp_generic"));
            case HIDEOUT -> Optional.of(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "hideout_cave"));
            case CORVIN -> Optional.of(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "corvin_tower"));
            case GARLAND -> Optional.of(ResourceLocation.fromNamespaceAndPath(VillagersMod.MODID, "garland_compound"));
        };
    }
}
