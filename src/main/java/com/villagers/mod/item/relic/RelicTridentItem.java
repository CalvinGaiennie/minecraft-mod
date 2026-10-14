package com.villagers.mod.item.relic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;

import com.villagers.mod.relic.IKingRelic;
import com.villagers.mod.relic.RelicItemUtils;
import com.villagers.mod.relic.RelicKind;
import com.villagers.mod.relic.RelicLore;

import java.util.List;
import java.util.function.Consumer;

public class RelicTridentItem extends TridentItem implements IKingRelic {
    /** Vanilla trident durability; throw logic treats maxDamage 0 as "too damaged" and blocks the throw. */
    private static final int THROW_DURABILITY = 250;

    private final RelicKind kind;

    public RelicTridentItem(RelicKind kind) {
        super(RelicItemUtils.relicProperties().attributes(TridentItem.createAttributes()));
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
    public int getMaxDamage(ItemStack stack) {
        return THROW_DURABILITY;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
