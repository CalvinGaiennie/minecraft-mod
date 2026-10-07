# Bandit camp & hideout prefabs (Armies)

**Status:** MVP uses **marker block + chest(s) + spawned bandits**. NBT prefabs will replace block dressing.

## Design goals (author)

- **Corvin** and **Garland** camps are **not** near each other; **Garland is farther** from the player’s early kingdom.
- **Corvin** = **watch tower** on an occupied village (NBT: `corvin_tower`).
- **Garland** = **large authored compound** (NBT TBD — bigger than Corvin; no small MVP shell in code).
- Long marches are intentional: owners bring **soldiers/camps** for the trip; loot pays off **gear for the army** (owner distributes).

## Phase 1 (current code)

| Kind | Footprint | Contents |
| --- | --- | --- |
| **Camp** | 1 marker + 2 chests | Garrison 4, **no respawn**; loot scraps (gear on bandits); leader → saddled horse tied at camp |
| **Hideout** | cave floor + marker + chests + caltrops | Garrison 8, tier-up loot, **no respawn** |
| **Corvin** | **village** + **placeholder tower** (block stack) | Boss once per world, **no respawn**, rich loot bump |
| **Garland** | **village or vanilla outpost** (marker only until big NBT) | Hideout-tier loot + treasury, **no respawn** |
| **Citadel shadow** | **occupies village** on citadel ring | Same as camp (skimpy loot, no respawn) |

## Phase 2 (structure NBT)

Use **structure templates** at SavedData origin; **bandit_camp** marker at template anchor.

1. **`corvin_tower`** — watch tower, ledger desk, chest niches, spawn pad (replaces block stack).
2. **`garland_compound`** — **large** roost / ruin (author scale TBD); treasury, fires, outer ring.
3. **Generic camp** — pillager-outpost scale: fence, tent, hay.
4. **Cave hideout** — underground room: caltrop choke, cage, chest niche.

**Pipeline:** `BanditSiteGenerator.placeSite` → `StructureTemplateManager.place` at origin; then garrison bootstrap.

## Phase 3 (landmarks)

- Placement scorer: prefer **Garland** sites with high local **height variance** (ravine/pass proxy).
- Optional **surface landmark** block (banner / cracked stone) visible from 64 blocks.

## Citadel shadow

**Citadel shadow** camps only after **Citadel mod** calls `CitadelAnchorSavedData.setCitadelCenter` — ring at `citadelApproachDistance` from that point (not world spawn).
