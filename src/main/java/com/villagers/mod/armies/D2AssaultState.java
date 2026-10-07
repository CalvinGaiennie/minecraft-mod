package com.villagers.mod.armies;

import java.util.List;
import java.util.UUID;

public record D2AssaultState(List<UUID> entityIds, int initialBanditCount, UUID garlandId) {
}
