package com.villagers.mod.mixin;

import net.minecraft.world.entity.projectile.FireworkRocketEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.villagers.mod.event.SignalRocketEvents;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin {

    @Inject(method = "explode", at = @At("HEAD"))
    private void villagers$signalRocketOnExplode(CallbackInfo ci) {
        SignalRocketEvents.tryDetonate((FireworkRocketEntity) (Object) this);
    }
}
