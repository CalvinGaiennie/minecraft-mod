package com.villagers.mod.item.relic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

import com.villagers.mod.Config;
import com.villagers.mod.relic.IKingRelic;
import com.villagers.mod.relic.RelicItemUtils;
import com.villagers.mod.relic.RelicKind;
import com.villagers.mod.relic.RelicLightning;
import com.villagers.mod.relic.RelicLore;

import java.util.List;

public class RelicAxeItem extends AxeItem implements IKingRelic {
    private static final double LIGHTNING_REACH = 48.0;

    private final RelicKind kind;

    public RelicAxeItem(RelicKind kind) {
        super(Tiers.NETHERITE, RelicItemUtils.relicProperties().attributes(AxeItem.createAttributes(Tiers.NETHERITE, 6, -2.8f)));
        this.kind = kind;
    }

    @Override
    public RelicKind relicKind() {
        return kind;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (kind != RelicKind.STORMCALLER) {
            return super.use(level, player, hand);
        }
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        if (!(level instanceof ServerLevel server) || !Config.RELICS_ENABLED.get()) {
            return InteractionResultHolder.pass(stack);
        }
        HitResult hit = player.pick(LIGHTNING_REACH, 0.0f, false);
        Vec3 strike = hit.getType() == HitResult.Type.MISS
                ? player.getEyePosition().add(player.getLookAngle().scale(12.0))
                : hit.getLocation();
        RelicLightning.strike(server, strike.x, strike.y, strike.z);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        RelicLore.append(kind, tooltip);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
