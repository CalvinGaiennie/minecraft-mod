package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.BlockGetter;

import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.player.AllyService;
import com.villagers.mod.threat.BanditService;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/** Who paths around caltrops ({@code war-and-defense.md}). */
public final class CaltropPathing {
    private CaltropPathing() {
    }

    public static boolean shouldPathAround(Villager villager, BlockGetter level, BlockPos caltropPos) {
        if (BanditService.isBandit(villager)) {
            return false;
        }
        if (villager.hasData(VillagerAttachments.MILITIA_DATA.get())) {
            return false;
        }
        if (!villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return false;
        }
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null) {
            return false;
        }
        if (!(level instanceof ServerLevel server)) {
            return isSeasonedOrAbove(data);
        }
        UUID soldierOwner = resolveSoldierOwner(server, villager);
        if (soldierOwner == null) {
            return isSeasonedOrAbove(data);
        }
        VillageData atCaltrop = VillageManager.get(server).findVillageAt(
                caltropPos.getX(), caltropPos.getY(), caltropPos.getZ());
        if (atCaltrop != null) {
            UUID villageOwner = atCaltrop.getOwnerId();
            if (villageOwner.equals(soldierOwner)) {
                return true;
            }
            if (AllyService.isAlly(server, villageOwner, soldierOwner)) {
                return true;
            }
        }
        return isSeasonedOrAbove(data);
    }

    private static boolean isSeasonedOrAbove(SoldierData data) {
        return SoldierRanks.rankForKills(data.getKills()) != SoldierRanks.Rank.SOLDIER;
    }

    public static boolean shouldPathAround(Entity entity, BlockGetter level, BlockPos caltropPos) {
        return entity instanceof Villager villager && shouldPathAround(villager, level, caltropPos);
    }

    @Nullable
    private static UUID resolveSoldierOwner(ServerLevel level, Villager villager) {
        VillageData village = VillageManager.get(level).findVillageAt(
                villager.blockPosition().getX(),
                villager.blockPosition().getY(),
                villager.blockPosition().getZ());
        return village == null ? null : village.getOwnerId();
    }
}
