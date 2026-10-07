package com.villagers.mod.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.gear.VillagerGearHandEquip;
import com.villagers.mod.menu.VillagerGearMenu;

@EventBusSubscriber(modid = VillagersMod.MODID)
public class VillagerGearEquipHandler {

    @SubscribeEvent
    public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Villager villager) || event.getLevel().isClientSide()) {
            return;
        }

        if (event.getItemStack().isEmpty()) {
            if (villager.isBaby()) {
                return;
            }
            if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())
                    || villager.hasData(VillagerAttachments.MILITIA_DATA.get())
                    || villager.getVillagerData().getProfession() == VillagerProfession.NONE) {
                VillagerGearMenu.open(event.getEntity(), villager);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
            return;
        }

        if (VillagerGearHandEquip.tryEquip(villager, event.getEntity(), event.getItemStack())) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
