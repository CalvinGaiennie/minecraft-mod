package com.villagers.mod.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class TrainingDummyHandler {
    @SubscribeEvent
    public static void onUseDummy(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!level.getBlockState(event.getPos()).is(VillagersMod.TRAINING_DUMMY.get())) {
            return;
        }
        for (Villager villager : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(event.getPos()).inflate(4))) {
            if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null) {
                data.addPracticeKill();
            }
        }
        event.getEntity().displayClientMessage(
                net.minecraft.network.chat.Component.translatable("message.villagers.dummy_practice"), true);
    }
}
