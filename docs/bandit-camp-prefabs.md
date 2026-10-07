# Bandit camp & hideout prefabs (Armies)

**Status:** MVP uses **marker block + chest(s) + spawned bandits**. This doc is the build plan for authored micro-structures.

## Design goals (author)

- **Corvin** and **Garland** camps are **not** near each other; **Garland is farther** from the player’s early kingdom.
- Long marches are intentional: owners bring **soldiers/camps** for the trip; loot pays off **gear for the army** (owner distributes).
- **Future:** biome-tagged sites (ravine lip, narrow pass, broken tower) — Garland favors **dramatic terrain**; Corvin favors **approachable** foothills.

## Phase 1 (current code)

| Kind | Footprint | Contents |
| --- | --- | --- |
| **Camp** | 1 marker + 2 chests | Garrison 4, pillager respawn |
| **Hideout** | 1 marker + 2 chests + caltrops | Garrison 8, tier-up loot |
| **Corvin** | camp rules + valuables bump | Boss once per world |
| **Garland** | hideout-tier + treasury | Boss once per world |

## Phase 2 (prefab, no custom NBT art required)

Use **structure templates** (saved building blueprints the game loads — often called **NBT** files in Minecraft modding) or **jigsaw** 15×15×8 max:

1. **Corvin’s tally camp** — palisade, single watchtower, **ledger desk** (lectern), 2 loot chests, spawn pad.
2. **Garland’s roost** — broken tower base (3–4 stories ruin), **treasury room**, outer fires, tighter spawn ring.
3. **Generic camp** — pillager-outpost scale: fence, 1 tent (wool), chest, hay.
4. **Cave hideout** — underground room: **caltrop** choke, pit (1 deep water), cage (iron bars), chest niche.

**Pipeline:** `BanditSiteGenerator.placeSite` → `StructureTemplateManager.place` at SavedData origin; marker block at template anchor for garrison tick.

## Phase 3 (landmarks)

- Placement scorer: prefer **Garland** sites with high local **height variance** (ravine/pass proxy).
- Optional **surface landmark** block (banner / cracked stone) visible from 64 blocks.

## Citadel shadow

**Citadel shadow** camps only after **Citadel mod** calls `CitadelAnchorSavedData.setCitadelCenter` — ring at `citadelApproachDistance` from that point (not world spawn).
