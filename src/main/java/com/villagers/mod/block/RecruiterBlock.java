package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;
import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.squad.SquadService;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public class RecruiterBlock extends OpenableContainerBlock {
    public static final MapCodec<RecruiterBlock> CODEC = simpleCodec(RecruiterBlock::new);

    public RecruiterBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public RecruiterBlock() {
        this(BlockBehaviour.Properties.ofFullCopy(Blocks.SMITHING_TABLE).strength(2.5f, 6f));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RecruiterBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level instanceof ServerLevel server && player instanceof ServerPlayer serverPlayer) {
            if (player.isShiftKeyDown() && level.getBlockEntity(pos) instanceof RecruiterBlockEntity box) {
                SquadService.toggleCampaign(server, pos, serverPlayer);
                SquadService.notifyPlayer(
                        serverPlayer,
                        box.isSquadOnCampaign()
                                ? "message.villagers.squad_campaign_on"
                                : "message.villagers.squad_campaign_off");
                return InteractionResult.CONSUME;
            }
            if (level.getBlockEntity(pos) instanceof RecruiterBlockEntity box) {
                serverPlayer.openMenu(box, buf -> buf.writeBlockPos(pos));
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.SUCCESS;
    }
}
