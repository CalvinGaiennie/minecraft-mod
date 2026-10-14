package com.villagers.mod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;

import com.villagers.mod.bed.SoldierBedClaims;
import com.villagers.mod.bed.VanillaBedHelper;
import com.villagers.mod.bed.VillagerBedOccupancy;
import com.villagers.mod.block.RampartBlock;
import com.villagers.mod.block.RecruiterBlock;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.squad.SquadService;
import com.villagers.mod.util.SoldierStructureHelper;

import net.minecraft.world.entity.npc.Villager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReassignmentWritItem extends Item {
    private static final String TAG_SOLDIER = "Soldier";
    /** Legacy single rampart on older writs. */
    private static final String TAG_RAMPART = "RampartPos";
    /** Up to {@link #MAX_RAMPARTS_ON_WRIT} packed {@link BlockPos} values for squad box linking. */
    private static final String TAG_RAMPART_LIST = "RampartPosList";
    public static final int MAX_RAMPARTS_ON_WRIT = SquadService.MAX_RAMPARTS;

    public ReassignmentWritItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, net.minecraft.world.entity.LivingEntity target, InteractionHand hand) {
        if (player.level().isClientSide) {
            return target instanceof Villager ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        if (!(target instanceof Villager villager)) {
            return InteractionResult.PASS;
        }
        return tagSoldier(stack, player, villager);
    }

    /** Binds this writ to a soldier (used from item interaction and entity-interact event). */
    public static InteractionResult tagSoldier(ItemStack stack, Player player, Villager villager) {
        if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            player.displayClientMessage(Component.translatable("message.villagers.writ_not_soldier"), true);
            return InteractionResult.FAIL;
        }
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = data.copyTag();
        tag.putUUID(TAG_SOLDIER, villager.getUUID());
        clearRamparts(tag);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        SoldierData soldierData = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        String label = soldierData != null && soldierData.getDisplayName() != null && !soldierData.getDisplayName().isBlank()
                ? soldierData.getDisplayName()
                : villager.getName().getString();
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("Writ: " + label));
        player.displayClientMessage(Component.translatable("message.villagers.writ_soldier_tagged", label), true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(net.minecraft.world.item.context.UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        return applyUseOnBlock(context);
    }

    public static InteractionResult applyUseOnBlock(net.minecraft.world.item.context.UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }
        BlockPos clicked = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = data.copyTag();
        UUID soldierId = tag.hasUUID(TAG_SOLDIER) ? tag.getUUID(TAG_SOLDIER) : null;
        Player player = context.getPlayer();

        if (level.getBlockState(clicked).getBlock() instanceof RecruiterBlock) {
            if (soldierId != null) {
                Villager villager = findSoldier(serverLevel, soldierId);
                if (villager == null) {
                    return InteractionResult.FAIL;
                }
                if (!SquadService.assignMember(serverLevel, clicked, villager)) {
                    return InteractionResult.FAIL;
                }
                if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
                    SquadService.setLeader(serverLevel, clicked, villager);
                }
                stack.shrink(1);
                return InteractionResult.CONSUME;
            }
            if (hasRamparts(tag)) {
                return linkRampartsFromWritToBox(serverLevel, clicked, stack, tag, player);
            }
            return InteractionResult.PASS;
        }

        if (level.getBlockState(clicked).getBlock() instanceof RampartBlock) {
            if (soldierId == null) {
                List<BlockPos> ramparts = readRamparts(tag);
                if (ramparts.contains(clicked)) {
                    if (player != null) {
                        player.displayClientMessage(
                                Component.translatable("message.villagers.writ_rampart_already_on_writ"), true);
                    }
                    return InteractionResult.FAIL;
                }
                if (ramparts.size() >= MAX_RAMPARTS_ON_WRIT) {
                    if (player != null) {
                        player.displayClientMessage(
                                Component.translatable("message.villagers.writ_rampart_writ_full"), true);
                    }
                    return InteractionResult.FAIL;
                }
                ramparts.add(clicked);
                tag.remove(TAG_SOLDIER);
                writeRamparts(tag, ramparts);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                applyRampartWritName(stack, ramparts);
                if (player != null) {
                    player.displayClientMessage(
                            Component.translatable(
                                    "message.villagers.writ_rampart_inscribed",
                                    ramparts.size(),
                                    MAX_RAMPARTS_ON_WRIT),
                            true);
                }
                return InteractionResult.SUCCESS;
            }
            Villager villager = findSoldier(serverLevel, soldierId);
            SoldierData soldierData = villager == null ? null : villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (soldierData != null) {
                soldierData.setAssignedRampart(clicked);
                stack.shrink(1);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.FAIL;
        }

        if (!(level.getBlockState(clicked).getBlock() instanceof BedBlock)) {
            return InteractionResult.PASS;
        }
        BlockPos foot = VanillaBedHelper.footPosition(level, clicked);
        if (!VanillaBedHelper.isIntactBed(level, foot)) {
            return InteractionResult.FAIL;
        }
        if (soldierId == null) {
            if (SquadService.linkBedNearBox(serverLevel, foot, SquadService.WRIT_NEAR_BOX_RADIUS)) {
                if (player != null) {
                    player.displayClientMessage(Component.translatable("message.villagers.writ_bed_linked_box"), true);
                }
                stack.shrink(1);
                return InteractionResult.CONSUME;
            }
            if (player != null) {
                player.displayClientMessage(Component.translatable("message.villagers.writ_bed_no_box"), true);
            }
            return InteractionResult.FAIL;
        }
        if (VillagerBedOccupancy.isHomeOfOtherVillager(level, foot, soldierId)) {
            return InteractionResult.FAIL;
        }
        Villager villager = findSoldier(serverLevel, soldierId);
        if (villager == null) {
            return InteractionResult.FAIL;
        }
        SoldierData soldierData = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (soldierData == null) {
            return InteractionResult.FAIL;
        }
        BlockPos boxPos = soldierData.getSquadBoxPos();
        if (boxPos == null) {
            boxPos = SquadService.findNearestSquadBox(serverLevel, foot, SquadService.WRIT_NEAR_BOX_RADIUS);
        }
        if (boxPos != null && level.getBlockEntity(boxPos) instanceof RecruiterBlockEntity box) {
            if (box.getLinkedBeds().size() >= SquadService.MAX_BEDS && !box.getLinkedBeds().contains(foot)) {
                return InteractionResult.FAIL;
            }
            box.addLinkedBed(foot);
        }
        SoldierBedClaims claims = SoldierBedClaims.get(serverLevel);
        if (claims.isClaimedByOther(foot, soldierId)) {
            return InteractionResult.FAIL;
        }
        SoldierStructureHelper.releaseSoldierBed(serverLevel, soldierId, soldierData.getAssignedPostBed());
        SoldierStructureHelper.claimSoldierBed(serverLevel, foot, soldierId);
        soldierData.setAssignedPostBed(foot);
        SoldierBedSleep.syncHomeMemory(villager, foot);
        if (player != null) {
            player.displayClientMessage(Component.translatable("message.villagers.writ_bed_assigned"), true);
        }
        stack.shrink(1);
        return InteractionResult.CONSUME;
    }

    private static Villager findSoldier(ServerLevel level, UUID id) {
        for (var entity : level.getAllEntities()) {
            if (entity instanceof Villager villager && villager.getUUID().equals(id)) {
                return villager;
            }
        }
        return null;
    }

    private static InteractionResult linkRampartsFromWritToBox(
            ServerLevel level,
            BlockPos boxPos,
            ItemStack stack,
            net.minecraft.nbt.CompoundTag tag,
            Player player) {
        List<BlockPos> onWrit = readRamparts(tag);
        if (onWrit.isEmpty()) {
            return InteractionResult.PASS;
        }
        RecruiterBlockEntity box = SquadService.squadBox(level, boxPos);
        List<BlockPos> remaining = new ArrayList<>();
        int linked = 0;
        boolean anyMissing = false;
        boolean boxFull = false;
        boolean anyAlreadyOnBox = false;

        for (BlockPos rampart : onWrit) {
            if (!(level.getBlockState(rampart).getBlock() instanceof RampartBlock)) {
                anyMissing = true;
                continue;
            }
            if (box != null
                    && box.getSquadRamparts().size() >= SquadService.MAX_RAMPARTS
                    && !box.getSquadRamparts().contains(rampart)) {
                remaining.add(rampart);
                boxFull = true;
                continue;
            }
            if (SquadService.assignRampartToBox(level, boxPos, rampart)) {
                linked++;
            } else {
                anyAlreadyOnBox = true;
            }
        }

        writeRamparts(tag, remaining);
        if (remaining.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_NAME);
        } else {
            applyRampartWritName(stack, remaining);
        }
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        if (linked > 0) {
            if (player != null) {
                player.displayClientMessage(
                        Component.translatable("message.villagers.writ_rampart_linked_box_count", linked), true);
            }
            if (remaining.isEmpty()) {
                stack.shrink(1);
            }
            return InteractionResult.CONSUME;
        }
        if (player != null) {
            if (anyMissing && !boxFull && !anyAlreadyOnBox) {
                player.displayClientMessage(Component.translatable("message.villagers.writ_rampart_missing"), true);
            } else if (boxFull) {
                player.displayClientMessage(Component.translatable("message.villagers.writ_rampart_full"), true);
            } else {
                player.displayClientMessage(
                        Component.translatable("message.villagers.writ_rampart_already_linked"), true);
            }
        }
        return InteractionResult.FAIL;
    }

    static List<BlockPos> readRamparts(net.minecraft.nbt.CompoundTag tag) {
        List<BlockPos> out = new ArrayList<>();
        if (tag.contains(TAG_RAMPART_LIST)) {
            for (long packed : tag.getLongArray(TAG_RAMPART_LIST)) {
                out.add(BlockPos.of(packed));
            }
            return out;
        }
        if (tag.contains(TAG_RAMPART)) {
            out.add(BlockPos.of(tag.getLong(TAG_RAMPART)));
        }
        return out;
    }

    private static void writeRamparts(net.minecraft.nbt.CompoundTag tag, List<BlockPos> ramparts) {
        tag.remove(TAG_RAMPART);
        if (ramparts.isEmpty()) {
            tag.remove(TAG_RAMPART_LIST);
            return;
        }
        long[] packed = new long[ramparts.size()];
        for (int i = 0; i < ramparts.size(); i++) {
            packed[i] = ramparts.get(i).asLong();
        }
        tag.putLongArray(TAG_RAMPART_LIST, packed);
    }

    private static void clearRamparts(net.minecraft.nbt.CompoundTag tag) {
        tag.remove(TAG_RAMPART);
        tag.remove(TAG_RAMPART_LIST);
    }

    private static boolean hasRamparts(net.minecraft.nbt.CompoundTag tag) {
        return !readRamparts(tag).isEmpty();
    }

    private static void applyRampartWritName(ItemStack stack, List<BlockPos> ramparts) {
        if (ramparts.size() == 1) {
            BlockPos pos = ramparts.getFirst();
            stack.set(
                    DataComponents.CUSTOM_NAME,
                    Component.translatable(
                            "item.villagers.reassignment_writ.rampart",
                            pos.getX(),
                            pos.getY(),
                            pos.getZ()));
            return;
        }
        stack.set(
                DataComponents.CUSTOM_NAME,
                Component.translatable("item.villagers.reassignment_writ.rampart_count", ramparts.size()));
    }
}
