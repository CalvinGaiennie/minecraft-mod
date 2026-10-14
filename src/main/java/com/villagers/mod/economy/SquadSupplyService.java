package com.villagers.mod.economy;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import com.villagers.mod.block.entity.RecruiterBlockEntity;
import com.villagers.mod.squad.SquadService;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/** Weekly issued gear from smith/fletcher/armorer assigned to a squad box. */
public final class SquadSupplyService {
    public static final int WORKER_RADIUS = 24;

    public enum WorkerKind {
        WEAPONSMITH,
        FLETCHER,
        ARMORER
    }

    private SquadSupplyService() {
    }

    private static void autoLinkMissingWorkers(ServerLevel level, RecruiterBlockEntity box) {
        if (box.getAssignedWeaponsmithId() == null) {
            linkNearestWorker(level, box, WorkerKind.WEAPONSMITH);
        }
        if (box.getAssignedFletcherId() == null) {
            linkNearestWorker(level, box, WorkerKind.FLETCHER);
        }
        if (box.getAssignedArmorerId() == null) {
            linkNearestWorker(level, box, WorkerKind.ARMORER);
        }
    }

    public static boolean linkNearestWorker(ServerLevel level, RecruiterBlockEntity box, WorkerKind kind) {
        Villager worker = findNearestWorker(level, box.getBlockPos(), kind);
        if (worker == null) {
            return false;
        }
        switch (kind) {
            case WEAPONSMITH -> box.setAssignedWeaponsmithId(worker.getUUID());
            case FLETCHER -> box.setAssignedFletcherId(worker.getUUID());
            case ARMORER -> box.setAssignedArmorerId(worker.getUUID());
        }
        box.setChanged();
        return true;
    }

    /** Called once per in-game day per squad box near online players. */
    public static void tickDailySupply(ServerLevel level, RecruiterBlockEntity box) {
        autoLinkMissingWorkers(level, box);
        long day = level.getDayTime() / RecruiterService.TICKS_PER_DAY;
        if (day % 7 != 0) {
            return;
        }
        trySupplyFromWorker(level, box, box.getAssignedWeaponsmithId(), WorkerKind.WEAPONSMITH);
        trySupplyFromWorker(level, box, box.getAssignedFletcherId(), WorkerKind.FLETCHER);
        trySupplyFromWorker(level, box, box.getAssignedArmorerId(), WorkerKind.ARMORER);
    }

    private static void trySupplyFromWorker(
            ServerLevel level, RecruiterBlockEntity box, @Nullable UUID workerId, WorkerKind kind) {
        if (workerId == null) {
            return;
        }
        Villager worker = findWorker(level, workerId);
        if (worker == null || worker.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(box.getBlockPos())) > WORKER_RADIUS * WORKER_RADIUS) {
            return;
        }
        if (!worker.getVillagerData().getProfession().equals(professionFor(kind))) {
            return;
        }
        int tier = Math.max(1, worker.getVillagerData().getLevel());
        ItemStack stack = rollGear(kind, tier);
        if (!stack.isEmpty()) {
            box.tryInsertIssuedGear(stack);
            box.setChanged();
        }
    }

    private static ItemStack rollGear(WorkerKind kind, int tier) {
        return switch (kind) {
            case WEAPONSMITH -> switch (tier) {
                case 1 -> new ItemStack(Items.WOODEN_SWORD);
                case 2 -> new ItemStack(Items.STONE_SWORD);
                case 3 -> new ItemStack(Items.IRON_SWORD);
                case 4 -> new ItemStack(Items.DIAMOND_SWORD);
                default -> new ItemStack(Items.NETHERITE_SWORD);
            };
            case FLETCHER -> tier >= 3 ? new ItemStack(Items.BOW) : new ItemStack(Items.ARROW, 16);
            case ARMORER -> tier >= 4
                    ? new ItemStack(Items.DIAMOND_CHESTPLATE)
                    : tier >= 2 ? new ItemStack(Items.IRON_CHESTPLATE) : new ItemStack(Items.LEATHER_CHESTPLATE);
        };
    }

    @Nullable
    private static Villager findWorker(ServerLevel level, UUID id) {
        for (var entity : level.getAllEntities()) {
            if (entity instanceof Villager villager && villager.getUUID().equals(id)) {
                return villager;
            }
        }
        return null;
    }

    @Nullable
    private static Villager findNearestWorker(ServerLevel level, BlockPos boxPos, WorkerKind kind) {
        VillagerProfession profession = professionFor(kind);
        Villager best = null;
        double bestDist = WORKER_RADIUS * (double) WORKER_RADIUS;
        for (Villager villager : level.getEntitiesOfClass(
                Villager.class, new net.minecraft.world.phys.AABB(boxPos).inflate(WORKER_RADIUS))) {
            if (!villager.getVillagerData().getProfession().equals(profession)) {
                continue;
            }
            double dist = villager.distanceToSqr(net.minecraft.world.phys.Vec3.atCenterOf(boxPos));
            if (dist <= bestDist) {
                bestDist = dist;
                best = villager;
            }
        }
        return best;
    }

    private static VillagerProfession professionFor(WorkerKind kind) {
        return switch (kind) {
            case WEAPONSMITH -> VillagerProfession.WEAPONSMITH;
            case FLETCHER -> VillagerProfession.FLETCHER;
            case ARMORER -> VillagerProfession.ARMORER;
        };
    }

    public static void notifyLinkResult(ServerPlayer player, boolean ok) {
        SquadService.notifyPlayer(
                player, ok ? "message.villagers.squad_worker_linked" : "message.villagers.squad_worker_none");
    }
}
