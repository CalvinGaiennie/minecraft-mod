package com.villagers.mod.war;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

import com.villagers.mod.combat.SoldierCombatHelper;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.SoldierDesertion;
import com.villagers.mod.entity.VillagerAttachments;

/** MVP mutiny: high grumble soldiers desert when out of combat. */
public final class MutinyService {
    public static final int MUTINY_THRESHOLD = 5;

    private MutinyService() {
    }

    public static void tick(Villager soldier, SoldierData data) {
        if (data.getGrumblePoints() < MUTINY_THRESHOLD) {
            return;
        }
        long lastDamage = data.getLastDamageGameTime();
        if (!SoldierCombatHelper.isOutOfCombat(soldier, lastDamage < 0 ? 0 : lastDamage)) {
            return;
        }
        if (!(soldier.level() instanceof ServerLevel level)) {
            return;
        }
        for (var player : level.players()) {
            player.sendSystemMessage(Component.translatable(
                    "message.villagers.mutiny", soldier.getName()));
        }
        SoldierDesertion.desert(soldier);
    }
}
