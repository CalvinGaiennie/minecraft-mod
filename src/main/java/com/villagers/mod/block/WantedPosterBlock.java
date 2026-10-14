package com.villagers.mod.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import com.villagers.mod.block.entity.WantedPosterBlockEntity;
import com.villagers.mod.network.WantedPosterNetworking;
import com.villagers.mod.player.VillageOpsPermission;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;
import com.villagers.mod.war.WantedPosterNames;

import org.jetbrains.annotations.Nullable;

public class WantedPosterBlock extends BaseEntityBlock {
    public static final MapCodec<WantedPosterBlock> CODEC = simpleCodec(WantedPosterBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 1);

    public WantedPosterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public WantedPosterBlock() {
        this(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(0.5f).noOcclusion());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WantedPosterBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && level instanceof ServerLevel server && placer instanceof ServerPlayer player) {
            if (level.getBlockEntity(pos) instanceof WantedPosterBlockEntity poster) {
                VillageData village = VillageManager.get(server).findVillageAt(pos.getX(), pos.getY(), pos.getZ());
                if (village != null && VillageOpsPermission.mayManageVillage(server, player.getUUID(), village)) {
                    poster.setOwnerAndVillage(village.getOwnerId(), village.getVillageId());
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof WantedPosterBlockEntity poster) {
                poster.clearFromIndex();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide || !(level instanceof ServerLevel server) || !(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.SUCCESS;
        }
        if (!(level.getBlockEntity(pos) instanceof WantedPosterBlockEntity poster)) {
            return InteractionResult.PASS;
        }
        VillageData village = VillageManager.get(server).findVillageAt(pos.getX(), pos.getY(), pos.getZ());
        if (village == null || !VillageOpsPermission.mayManageVillage(server, serverPlayer.getUUID(), village)) {
            return InteractionResult.FAIL;
        }
        var names = new java.util.ArrayList<String>();
        for (java.util.UUID id : poster.getWantedIds()) {
            names.add(WantedPosterNames.displayName(server.getServer(), id));
        }
        while (names.size() < WantedPosterBlockEntity.MAX_NAMES) {
            names.add("");
        }
        WantedPosterNetworking.openEditor(serverPlayer, pos, names);
        return InteractionResult.CONSUME;
    }
}
