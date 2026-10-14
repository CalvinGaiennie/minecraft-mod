package com.villagers.mod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import com.villagers.mod.block.CampBlock;
import com.villagers.mod.network.AttackPlanNetworking;
import com.villagers.mod.war.AttackPlanService;

public class AttackPlanItem extends Item {
    public AttackPlanItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            AttackPlanNetworking.open(serverPlayer);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof CampBlock && !level.isClientSide && context.getPlayer() instanceof ServerPlayer serverPlayer) {
            var plan = AttackPlanService.planFor(serverPlayer.getUUID(), serverPlayer.serverLevel());
            AttackPlanNetworking.applyAtCamp(serverPlayer, pos, plan.target(), plan.siegeLadders());
            if (!serverPlayer.getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.CONSUME;
        }
        return super.useOn(context);
    }
}
