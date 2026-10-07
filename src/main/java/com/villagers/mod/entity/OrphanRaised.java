package com.villagers.mod.entity;

import net.minecraft.world.entity.npc.Villager;

/** Orphanage-raised flag (structure graft TBD; stats apply when flag is set). */
public final class OrphanRaised {
    private OrphanRaised() {
    }

    public static boolean is(Villager villager) {
        return villager.hasData(VillagerAttachments.ORPHAN_RAISED.get())
                && Boolean.TRUE.equals(villager.getData(VillagerAttachments.ORPHAN_RAISED.get()));
    }

    public static void set(Villager villager, boolean value) {
        if (value) {
            villager.setData(VillagerAttachments.ORPHAN_RAISED.get(), true);
        } else {
            villager.removeData(VillagerAttachments.ORPHAN_RAISED.get());
        }
    }
}
