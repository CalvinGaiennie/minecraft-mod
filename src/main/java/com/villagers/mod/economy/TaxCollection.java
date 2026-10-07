package com.villagers.mod.economy;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.villagers.mod.block.entity.TaxBoxBlockEntity;
import com.villagers.mod.entity.SoldierData;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.village.VillageZone;
import com.villagers.mod.war.BlockadeService;

import net.minecraft.world.entity.npc.Villager;

import java.util.List;

public final class TaxCollection {
    /** Cap back-tax days when a box has not been opened for a long time. */
    public static final int MAX_ACCUMULATED_DAYS = 30;
    private static final long TICKS_PER_DAY = 24000L;

    private TaxCollection() {
    }

    /** Runs when a player opens a tax box anywhere in the world. */
    public static void processOpen(ServerLevel level, BlockPos taxBoxPos, TaxBoxBlockEntity taxBox, Player player) {
        long day = level.getDayTime() / TICKS_PER_DAY;
        long lastDay = taxBox.getLastTaxDay();
        int daysDue = lastDay < 0 ? 1 : (int) Math.min(MAX_ACCUMULATED_DAYS, Math.max(1, day - lastDay));

        List<VillageZone.Circle> zone = VillageZone.taxCircles(level, taxBoxPos);
        int zoneRadius = VillageZone.primaryTaxRadius(level, taxBoxPos);
        int soldiers = countSoldiersInZone(level, zone);
        int cap = TaxCoverage.maxTaxableVillagers(soldiers);
        boolean blockaded = BlockadeService.isVillageBlockaded(level, taxBoxPos);

        int totalCollected = 0;
        if (!blockaded && soldiers > 0) {
            for (int d = 0; d < daysDue; d++) {
                totalCollected += collectOneDay(level, taxBoxPos, taxBox, zone);
            }
        }
        taxBox.setLastTaxDay(day);

        MutableComponent summary = Component.translatable(
                "message.villagers.tax_open_summary",
                daysDue,
                soldiers,
                cap,
                totalCollected,
                zoneRadius);
        if (blockaded) {
            summary = summary.append(Component.translatable("message.villagers.tax_open_blockaded"));
        } else if (soldiers == 0) {
            summary = summary.append(Component.translatable("message.villagers.tax_open_no_soldiers"));
        }
        player.displayClientMessage(summary, true);
    }

    /** @deprecated Use {@link #processOpen}; kept for tests. */
    @Deprecated
    public static int collectOnce(ServerLevel level, BlockPos taxBoxPos) {
        var be = level.getBlockEntity(taxBoxPos);
        if (!(be instanceof TaxBoxBlockEntity taxBox)) {
            return 0;
        }
        List<VillageZone.Circle> zone = VillageZone.taxCircles(level, taxBoxPos);
        return collectOneDay(level, taxBoxPos, taxBox, zone);
    }

    private static int collectOneDay(
            ServerLevel level,
            BlockPos taxBoxPos,
            TaxBoxBlockEntity taxBox,
            List<VillageZone.Circle> zone) {
        int soldiers = countSoldiersInZone(level, zone);
        if (soldiers == 0) {
            return 0;
        }
        var taxable = TaxCoverage.taxableVillagers(level, taxBoxPos, soldiers);
        int collected = 0;
        for (Villager villager : taxable) {
            if (BlockadeService.isVillageBlockaded(level, taxBoxPos)) {
                break;
            }
            ItemStack taxItem = TaxRolls.rollForVillager(villager, level.getRandom());
            if (taxItem.isEmpty()) {
                taxItem = new ItemStack(Items.BREAD);
            }
            if (taxBox.tryInsertTax(taxItem)) {
                collected++;
            } else {
                break;
            }
        }
        return collected;
    }

    private static int countSoldiersInZone(ServerLevel level, List<VillageZone.Circle> zone) {
        if (zone.isEmpty()) {
            return 0;
        }
        VillageZone.Circle anchor = zone.getFirst();
        int extent = VillageZone.queryHalfExtent(zone);
        int count = 0;
        for (var entity : level.getEntitiesOfClass(Villager.class,
                new net.minecraft.world.phys.AABB(
                        anchor.centerX() - extent,
                        -64,
                        anchor.centerZ() - extent,
                        anchor.centerX() + extent,
                        320,
                        anchor.centerZ() + extent))) {
            if (!entity.hasData(VillagerAttachments.SOLDIER_DATA.get())) {
                continue;
            }
            if (!VillageZone.villagerInTaxZone(entity, zone)) {
                continue;
            }
            SoldierData data = entity.getData(VillagerAttachments.SOLDIER_DATA.get());
            if (data != null && !data.isOnCampaign()) {
                count++;
            }
        }
        return count;
    }
}
