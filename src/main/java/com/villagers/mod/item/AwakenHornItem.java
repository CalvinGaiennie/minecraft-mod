package com.villagers.mod.item;

import com.villagers.mod.war.SoldierSignalType;

public class AwakenHornItem extends SoldierHornItem {
    public AwakenHornItem(Properties properties) {
        super(SoldierSignalType.AWAKEN, properties.stacksTo(1));
    }
}
