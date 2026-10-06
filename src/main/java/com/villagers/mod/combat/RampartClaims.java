package com.villagers.mod.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.saveddata.SavedData;

import com.villagers.mod.entity.VillagerAttachments;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** One soldier or militiaman per rampart tile (walk surface). */
public class RampartClaims extends SavedData {
    private static final String DATA_ID = "villagers_rampart_posts";
    private final Map<BlockPos, UUID> rampartToDefender = new HashMap<>();

    public static RampartClaims get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(RampartClaims::new, RampartClaims::load, null),
                DATA_ID);
    }

    private static RampartClaims load(CompoundTag tag, HolderLookup.Provider registries) {
        RampartClaims data = new RampartClaims();
        ListTag list = tag.getList("Claims", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            data.rampartToDefender.put(BlockPos.of(entry.getLong("Pos")).immutable(), entry.getUUID("Defender"));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (var entry : rampartToDefender.entrySet()) {
            CompoundTag row = new CompoundTag();
            row.putLong("Pos", entry.getKey().asLong());
            row.putUUID("Defender", entry.getValue());
            list.add(row);
        }
        tag.put("Claims", list);
        return tag;
    }

    public boolean isAvailableFor(ServerLevel level, BlockPos rampartBlock, UUID defenderId) {
        BlockPos key = rampartBlock.immutable();
        UUID owner = rampartToDefender.get(key);
        if (owner == null) {
            return true;
        }
        if (owner.equals(defenderId)) {
            return true;
        }
        if (!isActiveDefender(level, owner)) {
            rampartToDefender.remove(key);
            setDirty();
            return true;
        }
        return false;
    }

    public void claim(ServerLevel level, BlockPos rampartBlock, UUID defenderId) {
        purgeStaleClaims(level);
        releaseDefender(defenderId);
        rampartToDefender.put(rampartBlock.immutable(), defenderId);
        setDirty();
    }

    public void releaseAt(BlockPos rampartBlock) {
        if (rampartToDefender.remove(rampartBlock.immutable()) != null) {
            setDirty();
        }
    }

    public void releaseDefender(UUID defenderId) {
        if (rampartToDefender.entrySet().removeIf(e -> e.getValue().equals(defenderId))) {
            setDirty();
        }
    }

    @Nullable
    public BlockPos rampartBlockUnder(Villager villager) {
        BlockPos feet = BlockPos.containing(villager.getX(), villager.getY() - 0.1, villager.getZ());
        return feet.below().immutable();
    }

    private void purgeStaleClaims(ServerLevel level) {
        if (rampartToDefender.entrySet().removeIf(e -> !isActiveDefender(level, e.getValue()))) {
            setDirty();
        }
    }

    private static boolean isActiveDefender(ServerLevel level, UUID defenderId) {
        Entity entity = level.getEntity(defenderId);
        if (!(entity instanceof Villager villager) || !villager.isAlive()) {
            return false;
        }
        return villager.hasData(VillagerAttachments.SOLDIER_DATA.get())
                || villager.hasData(VillagerAttachments.MILITIA_DATA.get());
    }
}
