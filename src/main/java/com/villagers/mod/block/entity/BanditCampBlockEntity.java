package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.villagers.mod.armies.BanditCampService;
import com.villagers.mod.armies.BanditWorldSavedData;

import java.util.UUID;

public class BanditCampBlockEntity extends BlockEntity {
    private UUID siteId = UUID.randomUUID();
    private BanditWorldSavedData.SiteKind kind = BanditWorldSavedData.SiteKind.CAMP;

    public BanditCampBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.BANDIT_CAMP.get(), pos, state);
    }

    public void initSite(UUID id, BanditWorldSavedData.SiteKind siteKind) {
        this.siteId = id;
        this.kind = siteKind;
        setChanged();
    }

    public UUID getSiteId() {
        return siteId;
    }

    public BanditWorldSavedData.SiteKind getKind() {
        return kind;
    }

    public void onWorldDataLinked() {
        if (level instanceof ServerLevel serverLevel) {
            BanditCampService.bootstrapSite(serverLevel, this);
        }
    }

    @Override
    public void setLevel(net.minecraft.world.level.Level level) {
        super.setLevel(level);
        if (level instanceof ServerLevel serverLevel && !level.isClientSide) {
            serverLevel.getServer().execute(this::onWorldDataLinked);
        }
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, BanditCampBlockEntity be) {
        if (level.getGameTime() % 40 != (pos.asLong() & 31)) {
            return;
        }
        BanditCampService.tickSite(level, be);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putUUID("SiteId", siteId);
        tag.putString("Kind", kind.name());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.hasUUID("SiteId")) {
            siteId = tag.getUUID("SiteId");
        }
        if (tag.contains("Kind")) {
            kind = BanditWorldSavedData.SiteKind.valueOf(tag.getString("Kind"));
        }
    }
}
