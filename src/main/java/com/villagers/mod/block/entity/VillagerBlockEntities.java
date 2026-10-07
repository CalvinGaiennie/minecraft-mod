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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VillageMarkerBlockEntity>> VILLAGE_MARKER =
            BLOCK_ENTITIES.register("village_marker", () ->
                    BlockEntityType.Builder.of(VillageMarkerBlockEntity::new, VillagersMod.VILLAGE_MARKER.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TaxBoxBlockEntity>> TAX_BOX =
            BLOCK_ENTITIES.register("tax_box", () ->
                    BlockEntityType.Builder.of(TaxBoxBlockEntity::new, VillagersMod.TAX_BOX.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArrowBinBlockEntity>> ARROW_BIN =
            BLOCK_ENTITIES.register("arrow_bin", () ->
                    BlockEntityType.Builder.of(ArrowBinBlockEntity::new, VillagersMod.ARROW_BIN.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RecruiterBlockEntity>> RECRUITER_BOX =
            BLOCK_ENTITIES.register("recruiter_box", () ->
                    BlockEntityType.Builder.of(RecruiterBlockEntity::new, VillagersMod.RECRUITER_BOX.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SupplyDepotBlockEntity>> SUPPLY_DEPOT =
            BLOCK_ENTITIES.register("supply_depot", () ->
                    BlockEntityType.Builder.of(SupplyDepotBlockEntity::new, VillagersMod.SUPPLY_DEPOT.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CampBlockEntity>> CAMP =
            BLOCK_ENTITIES.register("camp_block", () ->
                    BlockEntityType.Builder.of(CampBlockEntity::new, VillagersMod.CAMP_BLOCK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CaltropBlockEntity>> CALTROP =
            BLOCK_ENTITIES.register("caltrop", () ->
                    BlockEntityType.Builder.of(CaltropBlockEntity::new, VillagersMod.CALTROP_BLOCK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MobSpawnerCapBlockEntity>> MOB_SPAWNER_CAP =
            BLOCK_ENTITIES.register("mob_spawner_cap", () ->
                    BlockEntityType.Builder.of(MobSpawnerCapBlockEntity::new, VillagersMod.MOB_SPAWNER_CAP.get()).build(null));
}
