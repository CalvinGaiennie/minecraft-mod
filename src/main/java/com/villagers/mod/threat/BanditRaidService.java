package com.villagers.mod.threat;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.block.entity.TaxBoxBlockEntity;
import com.villagers.mod.entity.BanditData;
import com.villagers.mod.entity.SoldierBedSleep;
import com.villagers.mod.entity.VillagerAttachments;
import com.villagers.mod.village.VillageData;
import com.villagers.mod.village.VillageManager;
import com.villagers.mod.village.VillageZone;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Active village raids (new-moon spawns): hunt defenders and loot the tax box. */
public final class BanditRaidService {
    private static final int TAX_REACH_BLOCKS = 2;
    private static final int BANDIT_SCAN_PADDING = 16;

    private static final Map<UUID, RaidOrder> ORDERS = new HashMap<>();

    private BanditRaidService() {
    }

    public record RaidOrder(UUID villageId, @Nullable BlockPos taxBox, boolean taxLooted) {
        RaidOrder withTaxLooted() {
            return new RaidOrder(villageId, taxBox, true);
        }
    }

    public static void beginRaid(ServerLevel level, VillageData village, UUID campId) {
        BlockPos tax = findTaxBoxInVillage(level, village);
        ORDERS.put(campId, new RaidOrder(village.getVillageId(), tax, false));
    }

    @Nullable
    public static RaidOrder orderFor(BanditData data) {
        UUID camp = data.getHomeCampId();
        return camp == null ? null : ORDERS.get(camp);
    }

    public static boolean banditThreatInSoldierVillage(ServerLevel level, Villager soldier) {
        VillageData village = VillageManager.get(level).findVillageAt(
                soldier.getBlockX(), soldier.getBlockY(), soldier.getBlockZ());
        if (village == null) {
            return false;
        }
        int radius = Math.max(village.getRadius(), VillageZone.DEFAULT_TAX_RADIUS) + BANDIT_SCAN_PADDING;
        AABB scan = soldier.getBoundingBox().inflate(radius);
        for (Villager other : level.getEntitiesOfClass(Villager.class, scan)) {
            if (!BanditService.isBandit(other) || !other.isAlive()) {
                continue;
            }
            if (inVillageHorizontally(other, village)) {
                return true;
            }
        }
        return false;
    }

    public static void tickBandit(ServerLevel level, Villager bandit, Mob mob, BanditData data) {
        RaidOrder order = orderFor(data);
        if (order == null) {
            return;
        }
        if (mob.getTarget() == null || !mob.getTarget().isAlive()) {
            Villager defender = findDefenderInVillage(level, bandit, order.villageId());
            if (defender != null) {
                acquireTarget(mob, defender);
            }
        }
        if (data.isLeader() && order.taxBox() != null && !order.taxLooted()) {
            tickLeaderTaxRaid(level, bandit, mob, data, order);
        }
    }

    @Nullable
    public static Villager findDefenderInVillage(ServerLevel level, Villager bandit, UUID villageId) {
        VillageData village = VillageManager.get(level).getVillage(villageId);
        if (village == null) {
            return findNearestDefender(bandit, 64);
        }
        int radius = Math.max(village.getRadius(), VillageZone.DEFAULT_TAX_RADIUS);
        BlockPos center = new BlockPos(village.getMarkerX(), village.getMarkerY(), village.getMarkerZ());
        AABB box = new AABB(
                center.getX() - radius,
                center.getY() - 32,
                center.getZ() - radius,
                center.getX() + radius,
                center.getY() + 32,
                center.getZ() + radius);
        Villager best = null;
        double bestDist = Double.MAX_VALUE;
        for (Villager other : level.getEntitiesOfClass(Villager.class, box)) {
            if (other == bandit || !other.isAlive()) {
                continue;
            }
            if (!other.hasData(VillagerAttachments.SOLDIER_DATA.get())
                    && !other.hasData(VillagerAttachments.MILITIA_DATA.get())) {
                continue;
            }
            if (!inVillageHorizontally(other, village)) {
                continue;
            }
            double dist = bandit.distanceToSqr(other);
            if (dist < bestDist) {
                bestDist = dist;
                best = other;
            }
        }
        return best;
    }

    @Nullable
    private static Villager findNearestDefender(Villager bandit, double range) {
        Villager best = null;
        double bestDist = range * range;
        for (Villager other : bandit.level().getEntitiesOfClass(
                Villager.class, bandit.getBoundingBox().inflate(range))) {
            if (!other.hasData(VillagerAttachments.SOLDIER_DATA.get())
                    && !other.hasData(VillagerAttachments.MILITIA_DATA.get())) {
                continue;
            }
            double dist = bandit.distanceToSqr(other);
            if (dist < bestDist) {
                bestDist = dist;
                best = other;
            }
        }
        return best;
    }

    private static void acquireTarget(Mob mob, Villager defender) {
        if (defender.isSleeping()) {
            SoldierBedSleep.wakeNow(defender);
        }
        mob.setTarget(defender);
    }

    private static void tickLeaderTaxRaid(
            ServerLevel level, Villager leader, Mob mob, BanditData data, RaidOrder order) {
        BlockPos taxPos = order.taxBox();
        if (taxPos == null || !level.isLoaded(taxPos)) {
            return;
        }
        if (!level.getBlockState(taxPos).is(VillagersMod.TAX_BOX.get())) {
            markTaxLooted(data, order);
            return;
        }
        Vec3 goal = Vec3.atCenterOf(taxPos);
        double distSq = leader.position().distanceToSqr(goal);
        if (distSq > TAX_REACH_BLOCKS * TAX_REACH_BLOCKS) {
            if (mob.getTarget() == null || !mob.getTarget().isAlive()) {
                mob.getNavigation().moveTo(goal.x, goal.y, goal.z, 1.05);
            }
            return;
        }
        lootTaxBox(level, leader, taxPos);
        markTaxLooted(data, order);
    }

    private static void markTaxLooted(BanditData data, RaidOrder order) {
        UUID camp = data.getHomeCampId();
        if (camp != null) {
            ORDERS.put(camp, order.withTaxLooted());
        }
    }

    private static void lootTaxBox(ServerLevel level, Villager leader, BlockPos taxPos) {
        BlockEntity be = level.getBlockEntity(taxPos);
        if (!(be instanceof TaxBoxBlockEntity tax)) {
            return;
        }
        for (int i = 0; i < tax.getContainerSize(); i++) {
            ItemStack stack = tax.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            giveLootStack(leader, stack.copy());
            tax.setItem(i, ItemStack.EMPTY);
        }
        tax.setChanged();
    }

    private static void giveLootStack(Villager bandit, ItemStack stack) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (bandit.getItemBySlot(slot).isEmpty()) {
                bandit.setItemSlot(slot, stack);
                return;
            }
        }
        bandit.spawnAtLocation(stack);
    }

    @Nullable
    private static BlockPos findTaxBoxInVillage(ServerLevel level, VillageData village) {
        int radius = Math.max(village.getRadius(), VillageZone.DEFAULT_TAX_RADIUS);
        BlockPos center = new BlockPos(village.getMarkerX(), village.getMarkerY(), village.getMarkerZ());
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;
        BlockPos min = center.offset(-radius, -32, -radius);
        BlockPos max = center.offset(radius, 32, radius);
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            if (!level.isLoaded(pos) || !level.getBlockState(pos).is(VillagersMod.TAX_BOX.get())) {
                continue;
            }
            double dist = center.distSqr(pos);
            if (dist < bestDist) {
                bestDist = dist;
                best = pos.immutable();
            }
        }
        return best;
    }

    private static boolean inVillageHorizontally(Villager villager, VillageData village) {
        long dx = (long) villager.getBlockX() - village.getMarkerX();
        long dz = (long) villager.getBlockZ() - village.getMarkerZ();
        int radius = Math.max(village.getRadius(), VillageZone.DEFAULT_TAX_RADIUS);
        long r = radius;
        return dx * dx + dz * dz <= r * r;
    }
}
