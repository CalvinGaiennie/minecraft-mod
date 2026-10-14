package com.villagers.mod.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import com.villagers.mod.atlas.AtlasExplorationService;
import com.villagers.mod.util.MusterRollHelper;
import com.villagers.mod.util.MusterRollReports;

public final class VillagersModNetwork {
    private VillagersModNetwork() {
    }

    public static void sendMusterRollBook(ServerPlayer player) {
        var report = MusterRollHelper.buildEmpireReport(player.serverLevel(), player);
        var pages = MusterRollReports.toBookPages(report).stream()
                .map(c -> c.getString())
                .toList();
        PacketDistributor.sendToPlayer(player, new MusterRollBookPayload(pages));
    }

    public static void sendVillageAtlas(ServerPlayer player) {
        var report = MusterRollHelper.buildEmpireReport(player.serverLevel(), player);
        var hamlets = report.villages().stream()
                .map(v -> new VillageAtlasPayload.HamletEntry(
                        v.shortId(),
                        v.villageName(),
                        v.centerX(),
                        v.centerZ(),
                        v.radius(),
                        v.presentCount(),
                        v.squadRosterCount()))
                .toList();
        var pos = player.blockPosition();
        var spawn = player.serverLevel().getSharedSpawnPos();
        PacketDistributor.sendToPlayer(player, new VillageAtlasPayload(
                pos.getX(),
                pos.getZ(),
                spawn.getX(),
                spawn.getZ(),
                report.totalSoldiers(),
                report.totalKills(),
                hamlets));
        AtlasExplorationService.queueTerrainSync(player);
    }
}
