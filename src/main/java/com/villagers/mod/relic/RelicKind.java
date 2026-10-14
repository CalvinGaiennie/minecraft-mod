package com.villagers.mod.relic;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

import com.villagers.mod.VillagersMod;

public enum RelicKind {
    DAWNBREAKER("dawnbreaker", GearType.SWORD, 3),
    OATHKEEPER("oathkeeper", GearType.SHIELD, 3),
    LONG_HUNT("long_hunt", GearType.BOW, 3),
    THORNHEART_CIRCLET("thornheart_circlet", GearType.HELMET, 2),
    STORMCALLER("stormcaller", GearType.AXE, 3),
    CORRUPTED_KINGS_CROWN("corrupted_kings_crown", GearType.HELMET, 3),
    KINGSMAUL("kingsmaul", GearType.MACE, 2),
    TIDESPEAR("tidespear", GearType.TRIDENT, 3),
    IRONROOT_HELM("ironroot_helm", GearType.HELMET, 3),
    LAST_MANTLE("last_mantle", GearType.CHESTPLATE, 2),
    GREAVES_LONG_MARCH("greaves_long_march", GearType.LEGGINGS, 2),
    STORMSTRIDERS("stormstriders", GearType.BOOTS, 2);

    public enum GearType {
        SWORD, BOW, SHIELD, AXE, MACE, TRIDENT, HELMET, CHESTPLATE, LEGGINGS, BOOTS
    }

    private final String id;
    private final GearType gearType;
    private final int loreLines;

    RelicKind(String id, GearType gearType, int loreLines) {
        this.id = id;
        this.gearType = gearType;
        this.loreLines = loreLines;
    }

    public int loreLines() {
        return loreLines;
    }

    public String id() {
        return id;
    }

    public GearType gearType() {
        return gearType;
    }

    public EquipmentSlot slot() {
        return switch (gearType) {
            case HELMET -> EquipmentSlot.HEAD;
            case CHESTPLATE -> EquipmentSlot.CHEST;
            case LEGGINGS -> EquipmentSlot.LEGS;
            case BOOTS -> EquipmentSlot.FEET;
            case SHIELD -> EquipmentSlot.OFFHAND;
            default -> EquipmentSlot.MAINHAND;
        };
    }

    public static RelicKind fromItem(Item item) {
        if (item instanceof IKingRelic relic) {
            return relic.relicKind();
        }
        return null;
    }

    public String translationKey() {
        return "item." + VillagersMod.MODID + ".relic." + id;
    }
}
