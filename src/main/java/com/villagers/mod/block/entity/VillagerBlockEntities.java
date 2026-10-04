package com.villagers.mod.block.entity;

import com.villagers.mod.VillagersMod;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class VillagerBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, VillagersMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MessStationBlockEntity>> MESS_STATION =
            BLOCK_ENTITIES.register("mess_station", () ->
                    BlockEntityType.Builder.of(MessStationBlockEntity::new, VillagersMod.MESS_STATION.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PostBedBlockEntity>> POST_BED =
            BLOCK_ENTITIES.register("post_bed", () ->
                    BlockEntityType.Builder.of(PostBedBlockEntity::new, VillagersMod.POST_BED.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VillageMarkerBlockEntity>> VILLAGE_MARKER =
            BLOCK_ENTITIES.register("village_marker", () ->
                    BlockEntityType.Builder.of(VillageMarkerBlockEntity::new, VillagersMod.VILLAGE_MARKER.get()).build(null));
}
