package com.villagers.mod.armies;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.armies.BanditWorldSavedData.SiteKind;

public final class BanditLoot {
    private BanditLoot() {
    }

    public static void fillSiteChests(ServerLevel level, BlockPos origin, SiteKind kind, RandomSource random) {
        fillSiteChest(level, origin.north(), kind, random, true);
        fillSiteChest(level, origin.east(), kind, random, false);
    }

    private static void fillSiteChest(ServerLevel level, BlockPos chestPos, SiteKind kind, RandomSource random, boolean primary) {
        BlockEntity be = level.getBlockEntity(chestPos);
        if (!(be instanceof ChestBlockEntity chest)) {
            return;
        }
        HolderLookup.RegistryLookup<Enchantment> enchants = level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT);
        boolean hideoutTier = kind == SiteKind.HIDEOUT || kind == SiteKind.GARLAND;
        fillCamp(chest, random, enchants, hideoutTier, primary);
        if (primary && kind == SiteKind.CORVIN) {
            addValuables(chest, random, 1.4);
        }
        if (primary && kind == SiteKind.GARLAND) {
            addValuables(chest, random, 2.0);
        }
        if (primary) {
            float mapRoll = hideoutTier ? 0.08f : 0.05f;
            if (random.nextFloat() < mapRoll) {
                insertIfRoom(chest, new ItemStack(VillagersMod.VILLAGE_MAP.get()));
            }
            float chronicleRoll = hideoutTier ? 0.08f : 0.05f;
            if (random.nextFloat() < chronicleRoll) {
                insertIfRoom(chest, new ItemStack(VillagersMod.VILLAGE_CHRONICLE.get()));
            }
        }
    }

    private static void fillCamp(ChestBlockEntity chest, RandomSource random, HolderLookup.RegistryLookup<Enchantment> enchants, boolean hideoutTier,
            boolean primary) {
        int sets = hideoutTier ? (primary ? 5 : 3) : (primary ? 4 : 2);
        for (int i = 0; i < sets; i++) {
            insertIfRoom(chest, enchantedArmor(random, enchants, hideoutTier));
            insertIfRoom(chest, enchantedWeapon(random, enchants, hideoutTier));
        }
        insertIfRoom(chest, new ItemStack(Items.IRON_BLOCK, hideoutTier ? 6 : 4));
        insertIfRoom(chest, new ItemStack(Items.GOLD_BLOCK, hideoutTier ? 4 : 2));
        insertIfRoom(chest, new ItemStack(Items.EMERALD_BLOCK, hideoutTier ? 2 : 1));
        insertIfRoom(chest, new ItemStack(Items.ARROW, 64));
        insertIfRoom(chest, new ItemStack(Items.BREAD, 16));
    }

    private static void addValuables(ChestBlockEntity chest, RandomSource random, double scale) {
        insertIfRoom(chest, new ItemStack(Items.DIAMOND, (int) (2 * scale)));
        insertIfRoom(chest, new ItemStack(Items.EMERALD, (int) (8 * scale)));
        insertIfRoom(chest, new ItemStack(Items.IRON_INGOT, (int) (12 * scale)));
        insertIfRoom(chest, new ItemStack(Items.GOLD_INGOT, (int) (8 * scale)));
        if (random.nextBoolean()) {
            insertIfRoom(chest, new ItemStack(Items.RAW_IRON, (int) (16 * scale)));
        }
    }

    private static ItemStack enchantedWeapon(RandomSource random, HolderLookup.RegistryLookup<Enchantment> enchants, boolean hideoutTier) {
        ItemStack stack = new ItemStack(random.nextBoolean() ? Items.IRON_SWORD : Items.BOW);
        int level = hideoutTier ? 2 + random.nextInt(3) : 2 + random.nextInt(2);
        stack.enchant(enchants.getOrThrow(Enchantments.SHARPNESS), level);
        if (stack.is(Items.BOW)) {
            stack.enchant(enchants.getOrThrow(Enchantments.POWER), level);
        } else if (random.nextFloat() < 0.25f) {
            stack.enchant(enchants.getOrThrow(Enchantments.UNBREAKING), 1 + random.nextInt(2));
        }
        return stack;
    }

    private static ItemStack enchantedArmor(RandomSource random, HolderLookup.RegistryLookup<Enchantment> enchants, boolean hideoutTier) {
        ItemStack[] pool = hideoutTier
                ? new ItemStack[] {
                        new ItemStack(Items.IRON_HELMET),
                        new ItemStack(Items.IRON_CHESTPLATE),
                        new ItemStack(Items.IRON_LEGGINGS),
                        new ItemStack(Items.IRON_BOOTS),
                        new ItemStack(Items.DIAMOND_CHESTPLATE)
                }
                : new ItemStack[] {
                        new ItemStack(Items.IRON_HELMET),
                        new ItemStack(Items.IRON_CHESTPLATE),
                        new ItemStack(Items.IRON_LEGGINGS),
                        new ItemStack(Items.IRON_BOOTS)
                };
        ItemStack stack = pool[random.nextInt(pool.length)].copy();
        int prot = hideoutTier ? 3 + random.nextInt(2) : 2 + random.nextInt(2);
        stack.enchant(enchants.getOrThrow(Enchantments.PROTECTION), prot);
        return stack;
    }

    private static void insertIfRoom(ChestBlockEntity chest, ItemStack stack) {
        for (int i = 0; i < chest.getContainerSize(); i++) {
            ItemStack slot = chest.getItem(i);
            if (slot.isEmpty()) {
                chest.setItem(i, stack);
                return;
            }
        }
    }
}
