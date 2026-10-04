package com.villagers.mod.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;

public class AwakenHornItem extends Item {
    public AwakenHornItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide && player != null) {
            var serverLevel = (net.minecraft.server.level.ServerLevel) level;

            int radius = 64;
            int x = player.getBlockX();
            int y = player.getBlockY();
            int z = player.getBlockZ();

            var allEntities = serverLevel.getAllEntities();
            for (var entity : allEntities) {
                if (entity instanceof net.minecraft.world.entity.npc.Villager villager) {
                    if (villager.distanceToSqr(player) <= radius * radius) {
                        if (villager.hasData(com.villagers.mod.entity.VillagerAttachments.SOLDIER_DATA.get())) {
                            player.displayClientMessage(
                                net.minecraft.network.chat.Component.literal(
                                    "Awoken soldier at " + villager.getBlockX() + ", " + villager.getBlockZ()
                                ),
                                true
                            );
                        }
                    }
                }
            }

            if (!player.isCreative()) {
                itemStack.shrink(1);
            }

            return InteractionResultHolder.success(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }
}
