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
import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.util.SoldierStructureHelper;

import net.minecraft.world.entity.npc.Villager;

import java.util.UUID;

public class ReassignmentWritItem extends Item {
    private static final String TAG_SOLDIER = "Soldier";

    public ReassignmentWritItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, net.minecraft.world.entity.LivingEntity target, InteractionHand hand) {
        if (player.level().isClientSide || !(target instanceof Villager villager)) {
            return InteractionResult.PASS;
        }
        if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return InteractionResult.PASS;
        }
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = data.copyTag();
        tag.putUUID(TAG_SOLDIER, villager.getUUID());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("Writ: " + villager.getName().getString()));
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(net.minecraft.world.item.context.UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }
        if (!(level.getBlockState(context.getClickedPos()).getBlock() instanceof BedBlock)) {
            return InteractionResult.PASS;
        }
        ItemStack stack = context.getItemInHand();
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        var tag = data.copyTag();
        if (!tag.hasUUID(TAG_SOLDIER)) {
            return InteractionResult.PASS;
        }
        BlockPos foot = VanillaBedHelper.footPosition(level, context.getClickedPos());
        if (!VanillaBedHelper.isIntactBed(level, foot)) {
            return InteractionResult.FAIL;
        }
        UUID soldierId = tag.getUUID(TAG_SOLDIER);
        SoldierBedClaims claims = SoldierBedClaims.get(serverLevel);
        if (claims.isClaimedByOther(foot, soldierId)) {
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
        SoldierStructureHelper.releaseSoldierBed(serverLevel, soldierId, soldierData.getAssignedPostBed());
        SoldierStructureHelper.claimSoldierBed(serverLevel, foot, soldierId);
        soldierData.setAssignedPostBed(foot);
        SoldierBedSleep.syncHomeMemory(villager, foot);
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
}
