package com.villagers.mod.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import com.villagers.mod.entity.SoldierBehavior;
import com.villagers.mod.entity.VillagerAttachments;

public class AwakenHornItem extends Item {
    private static final int RADIUS = 64;

    public AwakenHornItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            var serverLevel = (net.minecraft.server.level.ServerLevel) level;
            long gameTime = level.getGameTime();
            int awakened = 0;
            double radiusSq = (double) RADIUS * RADIUS;

            for (var entity : serverLevel.getAllEntities()) {
                if (entity instanceof net.minecraft.world.entity.npc.Villager villager
                        && villager.distanceToSqr(player) <= radiusSq
                        && villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                    SoldierBehavior.signalAwaken(villager, gameTime);
                    awakened++;
                }
            }

            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("Awaken horn: " + awakened + " soldiers alerted"),
                    true);

            if (!player.isCreative()) {
                itemStack.shrink(1);
            }

            return InteractionResultHolder.success(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }
}
