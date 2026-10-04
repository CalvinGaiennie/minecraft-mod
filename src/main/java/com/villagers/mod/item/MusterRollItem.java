package com.villagers.mod.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;

import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.entity.SoldierData;

public class MusterRollItem extends Item {
    public MusterRollItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            var serverLevel = (net.minecraft.server.level.ServerLevel) level;
            int radius = 64;
            int soldierCount = 0;
            int totalKills = 0;

            var allEntities = serverLevel.getAllEntities();
            for (var entity : allEntities) {
                if (entity instanceof net.minecraft.world.entity.npc.Villager villager) {
                    if (villager.distanceToSqr(player) <= radius * radius) {
                        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
                        if (data != null) {
                            soldierCount++;
                            totalKills += data.getKills();
                        }
                    }
                }
            }

            player.displayClientMessage(
                Component.literal("Muster Roll: " + soldierCount + " soldiers, " + totalKills + " total kills"),
                true
            );
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
