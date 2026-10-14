package com.villagers.mod.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.Nullable;

import com.villagers.mod.combat.SoldierCombat;
import com.villagers.mod.combat.SoldierMovement;
import com.villagers.mod.combat.SoldierRanks;
import com.villagers.mod.combat.SoldierRanks.Rank;
import com.villagers.mod.gear.VillagerGearRules;
import com.villagers.mod.gear.VillagerGearSlot;
import com.villagers.mod.util.SoldierStructureHelper;
import com.villagers.mod.performance.CombatVillagerIndex;
import com.villagers.mod.threat.BanditService;
import com.villagers.mod.threat.WanderingNecromancerService;

public class SoldierConversion {
    public static boolean canBecomeSoldier(Villager villager) {
        if (villager == null || villager.isBaby() || !villager.isAlive()) {
            return false;
        }
        if (villager.getVillagerData().getProfession() != VillagerProfession.NONE) {
            return false;
        }
        if (BanditService.isBandit(villager) || WanderingNecromancerService.isWanderingNecromancer(villager)) {
            return false;
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return false;
        }

        ItemStack helmet = VillagerGearRules.get(villager, VillagerGearSlot.HELMET);
        ItemStack chestplate = VillagerGearRules.get(villager, VillagerGearSlot.CHESTPLATE);
        ItemStack mainHand = VillagerGearRules.get(villager, VillagerGearSlot.MELEE);

        if (helmet.isEmpty() || chestplate.isEmpty() || mainHand.isEmpty()) {
            return false;
        }
        if (!VillagerGearRules.accepts(VillagerGearSlot.HELMET, helmet)
                || !VillagerGearRules.accepts(VillagerGearSlot.CHESTPLATE, chestplate)
                || !VillagerGearRules.accepts(VillagerGearSlot.MELEE, mainHand)) {
            return false;
        }

        Level level = villager.level();
        BlockPos origin = villager.blockPosition();
        if (SoldierStructureHelper.findFreeSoldierBed(level, origin, villager).isEmpty()) {
            return false;
        }
        return SoldierStructureHelper.hasStockedMessStation(level, origin);
    }

    public static boolean tryConvert(Villager villager) {
        if (!canBecomeSoldier(villager)) {
            return false;
        }
        convertToSoldier(villager);
        return true;
    }

    @Nullable
    public static Component blockReason(Villager villager) {
        if (villager == null || villager.isBaby()) {
            return Component.translatable("message.villagers.convert_invalid");
        }
        if (villager.getVillagerData().getProfession() != VillagerProfession.NONE) {
            return Component.translatable("message.villagers.convert_has_job");
        }
        if (villager.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
            return null;
        }

        ItemStack helmet = VillagerGearRules.get(villager, VillagerGearSlot.HELMET);
        ItemStack chestplate = VillagerGearRules.get(villager, VillagerGearSlot.CHESTPLATE);
        ItemStack mainHand = VillagerGearRules.get(villager, VillagerGearSlot.MELEE);

        if (helmet.isEmpty() || !VillagerGearRules.accepts(VillagerGearSlot.HELMET, helmet)) {
            return Component.translatable("message.villagers.convert_need_helmet");
        }
        if (chestplate.isEmpty() || !VillagerGearRules.accepts(VillagerGearSlot.CHESTPLATE, chestplate)) {
            return Component.translatable("message.villagers.convert_need_chest");
        }
        if (mainHand.isEmpty() || !VillagerGearRules.accepts(VillagerGearSlot.MELEE, mainHand)) {
            return Component.translatable("message.villagers.convert_need_melee");
        }

        BlockPos origin = villager.blockPosition();
        Level level = villager.level();
        if (SoldierStructureHelper.findFreeSoldierBed(level, origin, villager).isEmpty()) {
            return Component.translatable("message.villagers.convert_need_bed");
        }
        if (!SoldierStructureHelper.hasStockedMessStation(level, origin)) {
            return Component.translatable("message.villagers.convert_need_mess");
        }
        return null;
    }

    public static void tryConvertNear(Level level, BlockPos center, int radius) {
        if (level.isClientSide) {
            return;
        }
        AABB box = AABB.ofSize(center.getCenter(), radius * 2.0, 8.0, radius * 2.0);
        for (Villager villager : level.getEntitiesOfClass(Villager.class, box)) {
            tryConvert(villager);
        }
    }

    /** Spawns a fully equipped soldier at {@code spawnPos} for testing (spawn eggs). */
    public static Villager spawnRankedSoldier(ServerLevel level, BlockPos spawnPos, Rank rank) {
        Villager villager = new Villager(EntityType.VILLAGER, level);
        villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.NONE).setType(VillagerType.PLAINS));
        villager.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, level.random.nextFloat() * 360f, 0f);
        applyDefaultEnlistKit(villager, rank);
        SoldierData data = new SoldierData(villager.getUUID());
        data.setKills(rank.minKills());
        data.setLastFoodDay(level.getDayTime() / 24000L);
        SoldierProfessionGuard.stripCommerceForCombatRole(villager);
        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), data);
        VillagerGearRules.applyToEntity(villager);
        SoldierRanks.syncRank(villager, data);
        SoldierCombat.enableCombatGoals(villager);
        CombatVillagerIndex.track(villager);
        level.addFreshEntity(villager);
        return villager;
    }

    private static void applyDefaultEnlistKit(Villager villager, Rank rank) {
        VillagerGearRules.set(villager, VillagerGearSlot.HELMET, new ItemStack(Items.IRON_HELMET));
        VillagerGearRules.set(villager, VillagerGearSlot.CHESTPLATE, new ItemStack(Items.IRON_CHESTPLATE));
        VillagerGearRules.set(villager, VillagerGearSlot.LEGGINGS, new ItemStack(Items.IRON_LEGGINGS));
        VillagerGearRules.set(villager, VillagerGearSlot.BOOTS, new ItemStack(Items.IRON_BOOTS));
        VillagerGearRules.set(villager, VillagerGearSlot.MELEE, new ItemStack(Items.IRON_SWORD));
        VillagerGearRules.set(villager, VillagerGearSlot.RANGED, new ItemStack(Items.BOW));
        VillagerGearRules.set(villager, VillagerGearSlot.ARROWS, new ItemStack(Items.ARROW, VillagerGearRules.MAX_ARROWS));
        if (rank.ordinal() >= Rank.HERO.ordinal()) {
            VillagerGearRules.set(villager, VillagerGearSlot.CHESTPLATE, new ItemStack(Items.DIAMOND_CHESTPLATE));
        } else if (rank.ordinal() >= Rank.SERGEANT.ordinal()) {
            VillagerGearRules.set(villager, VillagerGearSlot.HELMET, new ItemStack(Items.GOLDEN_HELMET));
        }
    }

    public static void convertToSoldier(Villager villager) {
        if (villager.level().isClientSide || !(villager.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        MilitiaConversion.releaseMilitiaRoleForSoldierPromotion(villager);

        BlockPos origin = villager.blockPosition();

        SoldierData data = new SoldierData(villager.getUUID());
        data.setDisplayName(VillagerPersonalNames.captureAtEnlist(villager));
        if (villager.hasData(VillagerAttachments.VETERAN_DATA.get())) {
            VeteranData veteran = villager.getData(VillagerAttachments.VETERAN_DATA.get());
            if (veteran.getKills() > 0) {
                data.setKills(veteran.getKills());
                data.setRank(veteran.getRank());
            }
        }
        long day = serverLevel.getDayTime() / 24000L;
        data.setLastFoodDay(day);

        SoldierStructureHelper.findFreeSoldierBed(serverLevel, origin, villager).ifPresent(bedPos -> {
            SoldierStructureHelper.claimSoldierBed(serverLevel, bedPos, villager.getUUID());
            data.setAssignedPostBed(bedPos);
            SoldierBedSleep.syncHomeMemory(villager, bedPos);
        });
        SoldierProfessionGuard.stripCommerceForCombatRole(villager);
        villager.setData(VillagerAttachments.SOLDIER_DATA.get(), data);
        VillagerGearRules.applyToEntity(villager);
        SoldierRanks.syncRank(villager, data);
        SoldierCombat.enableCombatGoals(villager);
        CombatVillagerIndex.track(villager);
    }

    public static void dischargeHonorable(Villager villager) {
        SoldierData data = villager.getData(VillagerAttachments.SOLDIER_DATA.get());
        if (data == null || !(villager.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        VeteranData veteran = new VeteranData(data.getKills(), data.getRank());
        SoldierStructureHelper.releaseSoldierBed(serverLevel, villager.getUUID(), data.getAssignedPostBed());
        SoldierCombat.disableCombatGoals(villager);
        villager.setData(VillagerAttachments.VETERAN_DATA.get(), veteran);
        villager.removeData(VillagerAttachments.SOLDIER_DATA.get());
        VeteranBedSync.syncHomeBedFromVillager(villager);
        SoldierLabels.clear(villager);
        SoldierMovement.resetVillagerWalkSpeed(villager);
    }
}
