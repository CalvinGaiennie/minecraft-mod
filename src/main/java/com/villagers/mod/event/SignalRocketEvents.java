package com.villagers.mod.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.item.SignalRocketItem;
import com.villagers.mod.war.FlareCommandService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(modid = VillagersMod.MODID)
public final class SignalRocketEvents {
    private static final Set<UUID> DETONATED = new HashSet<>();

    private SignalRocketEvents() {
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        if (event.getEntity() instanceof FireworkRocketEntity rocket) {
            ItemStack stack = rocket.getItem();
            if (SignalRocketItem.isSignalRocket(stack)) {
                DETONATED.remove(rocket.getUUID());
            }
        }
    }

    @SubscribeEvent
    public static void onImpact(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof FireworkRocketEntity rocket)) {
            return;
        }
        if (event.getProjectile().level().isClientSide()) {
            return;
        }
        tryDetonate(rocket);
    }

    /** Called on block/entity impact and from {@link com.villagers.mod.mixin.FireworkRocketEntityMixin} on air burst. */
    public static void tryDetonate(FireworkRocketEntity rocket) {
        if (!DETONATED.add(rocket.getUUID())) {
            return;
        }
        ItemStack stack = rocket.getItem();
        if (!SignalRocketItem.isSignalRocket(stack)) {
            return;
        }
        if (!(rocket.level() instanceof ServerLevel)) {
            return;
        }
        if (!(rocket.getOwner() instanceof ServerPlayer player)) {
            return;
        }
        boolean strike = SignalRocketItem.isStrike(stack);
        Vec3 burst = rocket.position();
        int count = FlareCommandService.detonate(player, burst, stack, strike);
        String key = strike ? "message.villagers.flare.strike" : "message.villagers.flare.rally";
        player.displayClientMessage(Component.translatable(key, count), true);
    }
}
