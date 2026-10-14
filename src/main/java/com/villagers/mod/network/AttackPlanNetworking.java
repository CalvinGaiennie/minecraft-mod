package com.villagers.mod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import com.villagers.mod.war.AttackPlanService;

import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public final class AttackPlanNetworking {
    private AttackPlanNetworking() {
    }

    public static void open(ServerPlayer player) {
        var squads = AttackPlanService.listChargeSquads(player);
        var plan = AttackPlanService.planFor(player.getUUID(), player.serverLevel());
        BlockPos target = plan.target();
        PacketDistributor.sendToPlayer(
                player,
                new AttackPlanOpenPayload(
                        squads,
                        AttackPlanOpenPayload.packTarget(target),
                        plan.siegeLadders()));
    }

    public static void handleSave(ServerPlayer player, AttackPlanSavePayload payload) {
        Set<Long> boxes = new HashSet<>(payload.selectedSquadBoxes());
        BlockPos target = payload.targetPacked() == 0L ? null : BlockPos.of(payload.targetPacked());
        AttackPlanService.savePlan(player, boxes, target, payload.siegeLadders());
        player.displayClientMessage(Component.translatable("message.villagers.attack_plan_saved"), true);
    }

    public static void applyAtCamp(ServerPlayer player, BlockPos campPos, @Nullable BlockPos target, int ladders) {
        if (target == null) {
            target = AttackPlanService.planFor(player.getUUID(), player.serverLevel()).target();
        }
        if (target == null) {
            player.displayClientMessage(Component.translatable("message.villagers.attack_plan_no_target"), true);
            return;
        }
        int siege = ladders > 0 ? ladders : AttackPlanService.planFor(player.getUUID(), player.serverLevel()).siegeLadders();
        AttackPlanService.applyToCamp(player.serverLevel(), campPos, player, target, siege);
        player.displayClientMessage(Component.translatable("message.villagers.attack_plan_applied_camp"), true);
    }
}
