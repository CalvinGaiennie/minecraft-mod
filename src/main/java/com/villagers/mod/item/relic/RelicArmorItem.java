package com.villagers.mod.item.relic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;

import com.villagers.mod.relic.IKingRelic;
import com.villagers.mod.relic.RelicItemUtils;
import com.villagers.mod.relic.RelicKind;
import com.villagers.mod.relic.RelicLore;

import java.util.List;

public class RelicArmorItem extends ArmorItem implements IKingRelic {
    private final RelicKind kind;

    public RelicArmorItem(RelicKind kind, Type type) {
        super(ArmorMaterials.NETHERITE, type, RelicItemUtils.relicProperties());
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
