package com.villagers.mod.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.entity.MinionData;
import com.villagers.mod.entity.VillagerAttachments;

public class NecromancerWandItem extends Item {
    public NecromancerWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.level().isClientSide || !(player.level() instanceof ServerLevel level)) {
            return InteractionResult.SUCCESS;
        }
        if (!(target instanceof Monster monster) || !monster.isAlive()) {
            return InteractionResult.PASS;
        }
        if (monster.hasData(VillagerAttachments.MINION_DATA.get())) {
            return InteractionResult.PASS;
        }
        monster.setTarget(null);
        monster.setData(VillagerAttachments.MINION_DATA.get(), new MinionData(player.getUUID()));
        monster.setCustomName(net.minecraft.network.chat.Component.translatable("entity.villagers.minion"));
        monster.setPersistenceRequired();
        if (monster instanceof Mob mob) {
            mob.setTarget(null);
        }
        if (!player.isCreative()) {
            stack.consume(1, player);
        }
        return InteractionResult.SUCCESS;
    }
}
