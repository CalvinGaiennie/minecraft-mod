# Cross-mod integration

Implement with **`ModList.get().isLoaded("other_mod_id")`** or NeoForge **InterModComms** / **events** on a shared bus. Prefer **necromancer publishes world state**, **soldier subscribes** (and vice versa for village data).

Mod IDs: **`minecraft_kingdom_armies`**, **`minecraft_kingdom_citadel`**. Display: **Minecraft Kingdom: Armies** / **Citadel**. Monolith today: **`villagers`**.

## Necromancer → Soldier (optional)

| Event / query | Payload | Soldier use |
|---------------|---------|-------------|
| `CitadelEvents.KING_DEFEATED` | ServerLevel, game time | Raise/lower spawn raid odds & natural militia near spawn |
| `CitadelQueries.isCitadelFallen(level)` | boolean | Threat tuning |
| `CitadelQueries.getOwner(level)` | UUID or empty | Display, warlords (if owner kingdom size) |
| `CitadelQueries.isOwnerOrAlly(level, playerUuid)` | boolean | Rarely needed on soldier side |

## Soldier → Necromancer (optional)

| Event / query | Payload | Necromancer use |
|---------------|---------|-----------------|
| `VillageQueries.getOwnedVillageCount(uuid)` | int | High King / golden age scope |
| `VillageQueries.getAllyUuids(ownerUuid)` | set UUID | Barrier allowlist, Roost, Well ally XP |
| `VillageQueries.isEnlisted(uuid)` | boolean | Not required for citadel; do not tie wand to enlist |
| `VillageQueries.hasPlayerSoldiersNear(uuid, pos, radius)` | boolean | Citadel: acolyte escort / Order quests refuse if true (`acolyte-path.md`, default radius 32) |
| `CitadelQueries.hasNecromancerKit(player)` | boolean | Wand, hood, or robe in inventory or armor slots (`acolyte-path.md`, `necromancy.md`) |
| `CitadelQueries.nearestAcolyteRefugeDistance(pos)` | blocks | Necromancer intro quest spawn gate (`necromancer-path.md`) |

## Shared conventions

- Store **player IDs**, not names, for owner, allies, credits, ban list, wanted posters.
- **Fortress spawn:** if soldier loaded, spawn soldier **bandit**; else necro **fortress raider** (fortress-only).
- **Relics:** necro registers items; on equip tick, if `NecromancerQueries.isActiveNecromancer(uuid)` → block equip.
- **Citadel mess:** soldier registers block; necro cancel placement if `!isCitadelOwner(uuid)` in citadel bounds.

## Versioning

Document breaking changes to payloads in `mod-split.md` decisions log. First split may invalidate old `villagers_*` SavedData keys, migration note in release notes.
