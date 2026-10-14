package com.villagers.mod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import com.villagers.mod.combat.SoldierRanks;
import com.villagers.mod.entity.SoldierConversion;

/** Creative/debug spawn egg: instant soldier at a rank (no mess/bed requirements). */
public class SoldierRankSpawnEggItem extends Item {
    private final SoldierRanks.Rank rank;

    public SoldierRankSpawnEggItem(SoldierRanks.Rank rank, Properties properties) {
        super(properties);
        this.rank = rank;
    }

    public SoldierRanks.Rank getRank() {
        return rank;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockPos spawnPos = context.getClickedPos().relative(context.getClickedFace());
        if (!level.getBlockState(spawnPos).isAir()) {
            return InteractionResult.FAIL;
        }
        if (!(level instanceof ServerLevel server)) {
            return InteractionResult.FAIL;
        }
        SoldierConversion.spawnRankedSoldier(server, spawnPos, rank);
        Player player = context.getPlayer();
        if (player != null && !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        HitResult hit = player.pick(5.0, 0, false);
        if (!(hit instanceof BlockHitResult blockHit) || !(level instanceof ServerLevel server)) {
            return InteractionResultHolder.pass(stack);
        }
        BlockState state = level.getBlockState(blockHit.getBlockPos());
        BlockPos spawnPos = blockHit.getBlockPos().relative(blockHit.getDirection());
        if (!state.isAir() && !state.getCollisionShape(level, blockHit.getBlockPos()).isEmpty()) {
            if (!level.getBlockState(spawnPos).isAir()) {
                return InteractionResultHolder.fail(stack);
            }
        } else {
            spawnPos = blockHit.getBlockPos();
        }
        SoldierConversion.spawnRankedSoldier(server, spawnPos, rank);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.consume(stack);
    }
}
