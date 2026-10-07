package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.villagers.mod.entity.BanditData;
import com.villagers.mod.threat.BanditService;

import java.util.UUID;

public final class BanditSpawnHelper {
    private BanditSpawnHelper() {
    }

    public static Villager spawnBandit(ServerLevel level, BlockPos pos, UUID campId, boolean leader, BanditData.BossRole boss,
            UUID questOwner, Component name) {
        Villager villager = new Villager(EntityType.VILLAGER, level);
        villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.NONE).setType(VillagerType.PLAINS));
        villager.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.random.nextFloat() * 360f, 0);
        BanditData data = new BanditData(villager.getUUID());
        data.setHomeCampId(campId);
        data.setLeader(leader);
        data.setBossRole(boss);
        data.setQuestOwnerId(questOwner);
        equipBandit(level.random, villager, leader, boss != BanditData.BossRole.NONE);
        if (leader || boss != BanditData.BossRole.NONE) {
            var health = villager.getAttribute(Attributes.MAX_HEALTH);
            if (health != null) {
                float bonus = boss != BanditData.BossRole.NONE ? 30f : 10f;
                health.setBaseValue(20 + bonus);
                villager.setHealth(20 + bonus);
            }
        }
        BanditService.makeBandit(villager, data, name);
        level.addFreshEntity(villager);
        return villager;
    }

    private static void equipBandit(RandomSource random, Villager villager, boolean leader, boolean boss) {
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.CHEST, new ItemStack(boss ? Items.DIAMOND_CHESTPLATE : Items.IRON_CHESTPLATE));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND,
                new ItemStack(random.nextBoolean() ? Items.IRON_SWORD : Items.CROSSBOW));
        if (leader && random.nextFloat() < 0.4f) {
            villager.setItemSlot(net.minecraft.world.entity.EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
        }
    }
}
