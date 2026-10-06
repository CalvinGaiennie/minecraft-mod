package com.villagers.mod.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.entity.MilitiaConversion;
import com.villagers.mod.entity.MilitiaData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.util.SoldierStructureHelper;
import com.villagers.mod.war.CampaignService;

public final class MilitiaFlee {
    private static final double FLEE_RANGE = 16.0;
    private static final double HOME_SAFE_RADIUS = 128.0;

    private MilitiaFlee() {
    }

    public static void tick(Villager villager) {
        MilitiaData data = villager.getData(VillagerAttachments.MILITIA_DATA.get());
        if (data == null || villager.level().isClientSide) {
            return;
        }
        if (isWithinHomeSafeZone(villager, data)) {
            return;
        }
        if (shouldFlee(villager, data)) {
            flee(villager, data);
        }
    }

    public static void onMilitiaDeathNearby(Villager survivor, Villager dead) {
        if (!survivor.hasData(VillagerAttachments.MILITIA_DATA.get()) || survivor.level().isClientSide) {
            return;
        }
        MilitiaData data = survivor.getData(VillagerAttachments.MILITIA_DATA.get());
        if (data == null || isWithinHomeSafeZone(survivor, data)) {
            return;
        }
        if (survivor.getRandom().nextFloat() < 0.30f) {
            flee(survivor, data);
        }
    }

    private static boolean shouldFlee(Villager villager, MilitiaData data) {
        if (villager.getMaxHealth() > 0 && villager.getHealth() / villager.getMaxHealth() < 0.5f) {
            return true;
        }
        if (!data.isSeasoned() && isOutnumbered(villager)) {
            return true;
        }
        return false;
    }

    private static boolean isOutnumbered(Villager villager) {
        int enemies = 0;
        int friendlies = 0;
        AABB box = villager.getBoundingBox().inflate(FLEE_RANGE);
        for (LivingEntity entity : villager.level().getEntitiesOfClass(LivingEntity.class, box)) {
            if (entity == villager || !entity.isAlive()) {
                continue;
            }
            if (entity instanceof Monster) {
                enemies++;
            } else if (entity instanceof Villager other
                    && (other.hasData(VillagerAttachments.MILITIA_DATA.get())
                            || other.hasData(VillagerAttachments.SOLDIER_DATA.get()))) {
                friendlies++;
            }
        }
        return enemies >= 2 && enemies >= friendlies * 2;
    }

    private static void flee(Villager villager, MilitiaData data) {
        if (villager instanceof Mob mob) {
            mob.setTarget(null);
        }
        MilitiaConversion.disarm(villager, true);
        Vec3 away = findFleeTarget(villager);
        villager.getNavigation().moveTo(away.x, away.y, away.z, 1.2);
    }

    private static Vec3 findFleeTarget(Villager villager) {
        return SoldierStructureHelper.findNearestFallback(villager.level(), villager.blockPosition())
                .map(pos -> Vec3.atCenterOf(pos))
                .orElse(villager.position().add(villager.getRandom().nextGaussian() * 8, 0, villager.getRandom().nextGaussian() * 8));
    }

    private static boolean isWithinHomeSafeZone(Villager villager, MilitiaData data) {
        if (data.getHomeBed() == null) {
            return false;
        }
        return villager.blockPosition().distSqr(data.getHomeBed()) <= HOME_SAFE_RADIUS * HOME_SAFE_RADIUS;
    }
}
