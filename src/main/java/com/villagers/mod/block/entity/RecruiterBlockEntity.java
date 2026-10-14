package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.menu.SquadBoxMenu;
import com.villagers.mod.squad.SquadMode;
import com.villagers.mod.squad.SquadService;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Squad box: gear storage, up to five soldiers, campaign and recruit modes. */
public class RecruiterBlockEntity extends BaseContainerBlockEntity {
    /** @deprecated use {@link SquadService#MAX_MEMBERS} */
    @Deprecated
    public static final int MAX_RECRUITS = SquadService.MAX_MEMBERS;
    private static final int SLOTS = 27;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
    private int activeRecruits;
    private SquadMode squadMode = SquadMode.STANDARD;
    private boolean squadOnCampaign;
    private boolean nightwatchEnabled;
    @Nullable
    private UUID squadLeaderId;
    private String squadLeaderLabel = "";
    private final List<BlockPos> linkedBeds = new ArrayList<>();
    private final List<BlockPos> squadRamparts = new ArrayList<>();
    @Nullable
    private UUID assignedWeaponsmithId;
    @Nullable
    private UUID assignedFletcherId;
    @Nullable
    private UUID assignedArmorerId;

    public RecruiterBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.RECRUITER_BOX.get(), pos, state);
    }

    public int getActiveRecruits() {
        return activeRecruits;
    }

    public void setActiveRecruits(int activeRecruits) {
        this.activeRecruits = Math.max(0, Math.min(MAX_RECRUITS, activeRecruits));
        setChanged();
    }

    public boolean canAcceptRecruit() {
        return activeRecruits < SquadService.MAX_MEMBERS;
    }

    public SquadMode getSquadMode() {
        return squadMode;
    }

    public void setSquadMode(SquadMode squadMode) {
        this.squadMode = squadMode == null ? SquadMode.STANDARD : squadMode;
        setChanged();
    }

    public boolean isSquadOnCampaign() {
        return squadOnCampaign;
    }

    public void setSquadOnCampaign(boolean squadOnCampaign) {
        this.squadOnCampaign = squadOnCampaign;
        setChanged();
    }

    public boolean isNightwatchEnabled() {
        return nightwatchEnabled;
    }

    public void setNightwatchEnabled(boolean nightwatchEnabled) {
        this.nightwatchEnabled = nightwatchEnabled;
        setChanged();
    }

    @Nullable
    public UUID getSquadLeaderId() {
        return squadLeaderId;
    }

    public void setSquadLeaderId(@Nullable UUID squadLeaderId) {
        this.squadLeaderId = squadLeaderId;
        if (squadLeaderId == null) {
            squadLeaderLabel = "";
        }
        setChanged();
    }

    public String getSquadLeaderLabel() {
        return squadLeaderLabel;
    }

    public void setSquadLeaderLabel(String squadLeaderLabel) {
        this.squadLeaderLabel = squadLeaderLabel == null ? "" : squadLeaderLabel;
        setChanged();
    }

    public List<BlockPos> getLinkedBeds() {
        return List.copyOf(linkedBeds);
    }

    public void addLinkedBed(BlockPos bedFoot) {
        if (linkedBeds.size() < SquadService.MAX_BEDS && !linkedBeds.contains(bedFoot)) {
            linkedBeds.add(bedFoot.immutable());
            setChanged();
        }
    }

    public List<BlockPos> getSquadRamparts() {
        return List.copyOf(squadRamparts);
    }

    public boolean addSquadRampart(BlockPos rampart) {
        if (squadRamparts.size() >= SquadService.MAX_RAMPARTS || squadRamparts.contains(rampart)) {
            return false;
        }
        squadRamparts.add(rampart.immutable());
        setChanged();
        return true;
    }

    @Nullable
    public UUID getAssignedWeaponsmithId() {
        return assignedWeaponsmithId;
    }

    public void setAssignedWeaponsmithId(@Nullable UUID assignedWeaponsmithId) {
        this.assignedWeaponsmithId = assignedWeaponsmithId;
        setChanged();
    }

    @Nullable
    public UUID getAssignedFletcherId() {
        return assignedFletcherId;
    }

    public void setAssignedFletcherId(@Nullable UUID assignedFletcherId) {
        this.assignedFletcherId = assignedFletcherId;
        setChanged();
    }

    @Nullable
    public UUID getAssignedArmorerId() {
        return assignedArmorerId;
    }

    public void setAssignedArmorerId(@Nullable UUID assignedArmorerId) {
        this.assignedArmorerId = assignedArmorerId;
        setChanged();
    }

    public boolean hasAssignedSupplyWorker() {
        return assignedWeaponsmithId != null || assignedFletcherId != null || assignedArmorerId != null;
    }

    public void syncActiveRecruitsFromWorld(ServerLevel level) {
        setActiveRecruits(SquadService.memberCount(level, getBlockPos()));
        SquadService.syncSquadLeadership(level, getBlockPos());
    }

    /** Issued gear only leaves the box when the squad has a leader and the villager is allowed to draw. */
    public boolean mayWithdrawGear(Villager villager) {
        if (squadLeaderId == null || !(level instanceof ServerLevel server)) {
            return false;
        }
        BlockPos boxPos = getBlockPos();
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            return data != null && boxPos.equals(data.getSquadBoxPos());
        }
        return squadMode == SquadMode.RECRUIT && SquadService.memberCount(server, boxPos) < SquadService.MAX_MEMBERS;
    }

    /** Adds one issued gear stack if there is a free or stackable slot. */
    public ItemStack withdrawMatchingGear(Villager villager, VillagerGearSlot slot) {
        if (!mayWithdrawGear(villager)) {
            return ItemStack.EMPTY;
        }
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty() && VillagerGearRules.accepts(slot, stack)) {
                ItemStack taken = stack.copyWithCount(1);
                stack.shrink(1);
                if (stack.isEmpty()) {
                    items.set(i, ItemStack.EMPTY);
                }
                setChanged();
                return taken;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean hasArrows() {
        for (ItemStack stack : items) {
            if (stack.is(net.minecraft.world.item.Items.ARROW)) {
                return true;
            }
        }
        return false;
    }

    /** Pulls up to {@code max} arrows from issued storage (same rules as arrow bins for soldiers). */
    public int withdrawArrows(Villager villager, int max) {
        if (max <= 0 || !mayWithdrawGear(villager)) {
            return 0;
        }
        int taken = 0;
        for (int i = 0; i < items.size() && taken < max; i++) {
            ItemStack stack = items.get(i);
            if (!stack.is(net.minecraft.world.item.Items.ARROW)) {
                continue;
            }
            int pull = Math.min(max - taken, stack.getCount());
            stack.shrink(pull);
            if (stack.isEmpty()) {
                items.set(i, ItemStack.EMPTY);
            }
            taken += pull;
        }
        if (taken > 0) {
            setChanged();
        }
        return taken;
    }

    public boolean tryInsertIssuedGear(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            ItemStack slot = items.get(i);
            if (slot.isEmpty()) {
                items.set(i, stack.copy());
                return true;
            }
            if (ItemStack.isSameItemSameComponents(slot, stack) && slot.getCount() < slot.getMaxStackSize()) {
                slot.grow(1);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("ActiveRecruits", activeRecruits);
        tag.putString("SquadMode", squadMode.name());
        tag.putBoolean("SquadCampaign", squadOnCampaign);
        tag.putBoolean("Nightwatch", nightwatchEnabled);
        if (squadLeaderId != null) {
            tag.putUUID("SquadLeader", squadLeaderId);
        }
        if (!squadLeaderLabel.isEmpty()) {
            tag.putString("SquadLeaderLabel", squadLeaderLabel);
        }
        long[] bedLongs = new long[linkedBeds.size()];
        for (int i = 0; i < linkedBeds.size(); i++) {
            bedLongs[i] = linkedBeds.get(i).asLong();
        }
        tag.putLongArray("LinkedBeds", bedLongs);
        long[] rampartLongs = new long[squadRamparts.size()];
        for (int i = 0; i < squadRamparts.size(); i++) {
            rampartLongs[i] = squadRamparts.get(i).asLong();
        }
        tag.putLongArray("SquadRamparts", rampartLongs);
        if (assignedWeaponsmithId != null) {
            tag.putUUID("SupplySmith", assignedWeaponsmithId);
        }
        if (assignedFletcherId != null) {
            tag.putUUID("SupplyFletcher", assignedFletcherId);
        }
        if (assignedArmorerId != null) {
            tag.putUUID("SupplyArmorer", assignedArmorerId);
        }
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        activeRecruits = tag.getInt("ActiveRecruits");
        squadMode = SquadMode.fromString(tag.getString("SquadMode"));
        squadOnCampaign = tag.getBoolean("SquadCampaign");
        nightwatchEnabled = tag.getBoolean("Nightwatch");
        squadLeaderId = tag.hasUUID("SquadLeader") ? tag.getUUID("SquadLeader") : null;
        squadLeaderLabel = tag.getString("SquadLeaderLabel");
        linkedBeds.clear();
        for (long packed : tag.getLongArray("LinkedBeds")) {
            linkedBeds.add(BlockPos.of(packed));
        }
        squadRamparts.clear();
        for (long packed : tag.getLongArray("SquadRamparts")) {
            squadRamparts.add(BlockPos.of(packed));
        }
        assignedWeaponsmithId = tag.hasUUID("SupplySmith") ? tag.getUUID("SupplySmith") : null;
        assignedFletcherId = tag.hasUUID("SupplyFletcher") ? tag.getUUID("SupplyFletcher") : null;
        assignedArmorerId = tag.hasUUID("SupplyArmorer") ? tag.getUUID("SupplyArmorer") : null;
        items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    public int getContainerSize() {
        return SLOTS;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.villagers.squad_box");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new SquadBoxMenu(id, inventory, this);
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(getBlockPos());
    }
}
