package com.villagers.mod.item.relic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

import com.villagers.mod.relic.IKingRelic;
import com.villagers.mod.relic.RelicItemUtils;
import com.villagers.mod.relic.RelicKind;
import com.villagers.mod.relic.RelicLore;

import java.util.List;

public class RelicSwordItem extends SwordItem implements IKingRelic {
    private final RelicKind kind;

    public RelicSwordItem(RelicKind kind) {
        super(Tiers.NETHERITE, RelicItemUtils.relicProperties().attributes(SwordItem.createAttributes(Tiers.NETHERITE, 5, -2.2f)));
        this.kind = kind;
    }

    @Override
    public RelicKind relicKind() {
        return kind;
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
