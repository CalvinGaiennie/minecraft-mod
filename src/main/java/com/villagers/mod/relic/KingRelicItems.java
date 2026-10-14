package com.villagers.mod.relic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;

import com.villagers.mod.VillagersMod;
import com.villagers.mod.item.relic.RelicArmorItem;
import com.villagers.mod.item.relic.RelicAxeItem;
import com.villagers.mod.item.relic.RelicBowItem;
import com.villagers.mod.item.relic.RelicMaceItem;
import com.villagers.mod.item.relic.RelicShieldItem;
import com.villagers.mod.item.relic.RelicSwordItem;
import com.villagers.mod.item.relic.RelicTridentItem;

import java.util.EnumMap;
import java.util.Map;

public final class KingRelicItems {
    private static final Map<RelicKind, DeferredItem<? extends Item>> BY_KIND = new EnumMap<>(RelicKind.class);

    public static final DeferredItem<RelicSwordItem> DAWNBREAKER =
            register(RelicKind.DAWNBREAKER, () -> new RelicSwordItem(RelicKind.DAWNBREAKER));
    public static final DeferredItem<RelicShieldItem> OATHKEEPER =
            register(RelicKind.OATHKEEPER, () -> new RelicShieldItem(RelicKind.OATHKEEPER));
    public static final DeferredItem<RelicBowItem> LONG_HUNT =
            register(RelicKind.LONG_HUNT, () -> new RelicBowItem(RelicKind.LONG_HUNT));
    public static final DeferredItem<RelicArmorItem> THORNHEART_CIRCLET =
            register(RelicKind.THORNHEART_CIRCLET, () -> new RelicArmorItem(RelicKind.THORNHEART_CIRCLET, RelicArmorItem.Type.HELMET));
    public static final DeferredItem<RelicAxeItem> STORMCALLER =
            register(RelicKind.STORMCALLER, () -> new RelicAxeItem(RelicKind.STORMCALLER));
    public static final DeferredItem<RelicArmorItem> CORRUPTED_KINGS_CROWN =
            register(RelicKind.CORRUPTED_KINGS_CROWN, () -> new RelicArmorItem(RelicKind.CORRUPTED_KINGS_CROWN, RelicArmorItem.Type.HELMET));
    public static final DeferredItem<RelicMaceItem> KINGSMAUL =
            register(RelicKind.KINGSMAUL, () -> new RelicMaceItem(RelicKind.KINGSMAUL));
    public static final DeferredItem<RelicTridentItem> TIDESPEAR =
            register(RelicKind.TIDESPEAR, () -> new RelicTridentItem(RelicKind.TIDESPEAR));
    public static final DeferredItem<RelicArmorItem> IRONROOT_HELM =
            register(RelicKind.IRONROOT_HELM, () -> new RelicArmorItem(RelicKind.IRONROOT_HELM, RelicArmorItem.Type.HELMET));
    public static final DeferredItem<RelicArmorItem> LAST_MANTLE =
            register(RelicKind.LAST_MANTLE, () -> new RelicArmorItem(RelicKind.LAST_MANTLE, RelicArmorItem.Type.CHESTPLATE));
    public static final DeferredItem<RelicArmorItem> GREAVES_LONG_MARCH =
            register(RelicKind.GREAVES_LONG_MARCH, () -> new RelicArmorItem(RelicKind.GREAVES_LONG_MARCH, RelicArmorItem.Type.LEGGINGS));
    public static final DeferredItem<RelicArmorItem> STORMSTRIDERS =
            register(RelicKind.STORMSTRIDERS, () -> new RelicArmorItem(RelicKind.STORMSTRIDERS, RelicArmorItem.Type.BOOTS));

    private KingRelicItems() {
    }

    /** Called from {@link VillagersMod} before {@code ITEMS.register(modEventBus)}. */
    public static void ensureRegisteredOnItems() {
        if (BY_KIND.size() != RelicKind.values().length) {
            throw new IllegalStateException("King relic registration incomplete");
        }
    }

    private static <T extends Item> DeferredItem<T> register(RelicKind kind, java.util.function.Supplier<T> sup) {
        DeferredItem<T> holder = VillagersMod.ITEMS.register("relic/" + kind.id(), sup);
        BY_KIND.put(kind, holder);
        return holder;
    }

    public static ItemStack stack(RelicKind kind, net.minecraft.core.HolderLookup.Provider registries) {
        DeferredItem<? extends Item> item = BY_KIND.get(kind);
        if (item == null) {
            return ItemStack.EMPTY;
        }
        return RelicItemUtils.createDefaultStack(item.get(), registries);
    }

    public static void fillCreative(net.minecraft.world.item.Item.Properties ignored, java.util.function.Consumer<ItemStack> output) {
        for (RelicKind kind : RelicKind.values()) {
            output.accept(new ItemStack(BY_KIND.get(kind).get()));
        }
    }
}
