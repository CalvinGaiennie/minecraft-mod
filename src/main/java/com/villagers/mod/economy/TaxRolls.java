package com.villagers.mod.economy;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public final class TaxRolls {
    private TaxRolls() {
    }

    public static ItemStack rollForVillager(Villager villager, RandomSource random) {
        var offers = villager.getOffers();
        if (offers == null || offers.isEmpty()) {
            return new ItemStack(Items.BREAD);
        }
        MerchantOffer offer = offers.get(random.nextInt(offers.size()));
        ItemStack result = offer.getResult();
        if (result.isEmpty()) {
            return new ItemStack(Items.BREAD);
        }
        return result.copyWithCount(1);
    }
}
