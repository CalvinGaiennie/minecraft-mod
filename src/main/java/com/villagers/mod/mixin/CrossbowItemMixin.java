package com.villagers.mod.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.villagers.mod.item.SignalRocketItem;

import java.util.function.Predicate;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @ModifyReturnValue(method = "getSupportedHeldProjectiles", at = @At("RETURN"))
    private Predicate<ItemStack> villagers$heldSignalRockets(Predicate<ItemStack> original) {
        return original.or(SignalRocketItem::isSignalRocket);
    }

    @ModifyReturnValue(method = "getAllSupportedProjectiles", at = @At("RETURN"))
    private Predicate<ItemStack> villagers$inventorySignalRockets(Predicate<ItemStack> original) {
        return original.or(SignalRocketItem::isSignalRocket);
    }

    @Inject(method = "createProjectile", at = @At("HEAD"), cancellable = true)
    private void villagers$createSignalRocketProjectile(
            Level level,
            LivingEntity shooter,
            ItemStack weapon,
            ItemStack ammo,
            boolean isCrit,
            CallbackInfoReturnable<Projectile> cir) {
        if (SignalRocketItem.isSignalRocket(ammo)) {
            cir.setReturnValue(new FireworkRocketEntity(
                    level, ammo, shooter, shooter.getX(), shooter.getEyeY() - 0.15F, shooter.getZ(), true));
        }
    }

    @Inject(method = "getShootingPower", at = @At("HEAD"), cancellable = true)
    private static void villagers$signalRocketShootingPower(
            ChargedProjectiles projectiles, CallbackInfoReturnable<Float> cir) {
        for (ItemStack stack : projectiles.getItems()) {
            if (SignalRocketItem.isSignalRocket(stack) || stack.is(Items.FIREWORK_ROCKET)) {
                cir.setReturnValue(1.6F);
                return;
            }
        }
    }

    @Inject(method = "getDurabilityUse", at = @At("HEAD"), cancellable = true)
    private void villagers$signalRocketDurability(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (SignalRocketItem.isSignalRocket(stack) || stack.is(Items.FIREWORK_ROCKET)) {
            cir.setReturnValue(3);
        }
    }
}
