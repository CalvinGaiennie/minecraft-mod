package com.villagers.mod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.SoldierBehavior;
import com.villagers.mod.entity.SoldierDesertion;
import com.villagers.mod.entity.VillagerAttachments;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class SoldierTickHandler {

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (level == null || level.isClientSide) {
            return;
        }

        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            var allEntities = serverLevel.getAllEntities();
            for (var entity : allEntities) {
                if (entity instanceof Villager villager) {
                    if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                        if (SoldierDesertion.shouldDeserve(villager)) {
                            SoldierDesertion.processDischarged(villager);
                        } else if (hasLostSoldierEquipment(villager)) {
                            SoldierDesertion.processDischarged(villager);
                        } else {
                            SoldierBehavior.updateDailyRoutine(villager);
                        }
                    }
                }
            }
        }
    }

    private static boolean hasLostSoldierEquipment(Villager villager) {
        var mainHandItem = villager.getMainHandItem();
        var chestArmor = villager.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST);
        var headArmor = villager.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD);

        return mainHandItem.isEmpty() || chestArmor.isEmpty() || headArmor.isEmpty();
    }
}
