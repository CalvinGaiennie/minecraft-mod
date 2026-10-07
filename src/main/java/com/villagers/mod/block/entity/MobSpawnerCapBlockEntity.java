package com.villagers.mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class MobSpawnerCapBlockEntity extends BlockEntity {
    private int cooldown;

    public MobSpawnerCapBlockEntity(BlockPos pos, BlockState state) {
        super(VillagerBlockEntities.MOB_SPAWNER_CAP.get(), pos, state);
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, MobSpawnerCapBlockEntity be) {
        if (--be.cooldown > 0) {
            return;
        }
        be.cooldown = 200;
        if (!level.hasNearbyAlivePlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 16)) {
            return;
        }
        long nearby = level.getEntitiesOfClass(Monster.class, new AABB(pos).inflate(8)).size();
        if (nearby >= 4) {
            return;
        }
        Monster mob = EntityType.ZOMBIE.create(level);
        if (mob == null) {
            return;
        }
        mob.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, level.random.nextFloat() * 360, 0);
        level.addFreshEntity(mob);
    }
}
