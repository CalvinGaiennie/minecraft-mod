package com.villagers.mod.util;

import net.minecraft.network.chat.Component;

import com.villagers.mod.util.MusterRollHelper.EmpireMusterReport;
import com.villagers.mod.util.MusterRollHelper.VillageMusterPage;

import java.util.ArrayList;
import java.util.List;

public final class MusterRollReports {
    private static final int MAX_GARRISON_NAMES_ON_PAGE = 12;

    private MusterRollReports() {
    }

    public static List<Component> toBookPages(EmpireMusterReport report) {
        List<Component> pages = new ArrayList<>();
        pages.add(Component.literal(
                "Muster Roll\n\n"
                        + report.totalSoldiers() + " soldiers under arms.\n"
                        + report.totalKills() + " kills recorded.\n\n"
                        + "Hamlet ids match your Village Map."));
        for (VillageMusterPage village : report.villages()) {
            pages.add(Component.literal(formatVillagePage(village)));
        }
        if (pages.size() == 1) {
            pages.add(Component.literal("No claimed hamlets yet.\n\nPlace a mess station and open the map to register territory."));
        }
        return pages;
    }

    private static String formatVillagePage(VillageMusterPage village) {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(village.shortId()).append("]\n");
        sb.append(village.villageName()).append("\n\n");
        sb.append("Center ").append(village.centerX()).append(", ").append(village.centerZ());
        sb.append("  r=").append(village.radius()).append("\n\n");
        sb.append("Home roster (bed): ").append(village.rosterCount()).append('\n');
        sb.append("Squad roster (box): ").append(village.squadRosterCount()).append('\n');
        sb.append("Present in hamlet: ").append(village.presentCount()).append('\n');
        sb.append("Kills (present): ").append(village.presentKills()).append("\n\n");
        if (village.presentNames().isEmpty()) {
            sb.append("Present: none in hamlet now.");
        } else {
            sb.append("Present now:\n");
            int shown = 0;
            for (String name : village.presentNames()) {
                if (shown >= MAX_GARRISON_NAMES_ON_PAGE) {
                    sb.append("… +").append(village.presentNames().size() - shown).append(" more");
                    break;
                }
                if (shown > 0) {
                    sb.append(", ");
                }
                sb.append(name);
                shown++;
            }
        }
        return sb.toString();
    }
}
