# Mod goals (Armies + Citadel)

Authoritative prose for implementers. Static site: [`site/development-plan.html`](../site/development-plan.html) (built from [`docs/marketing/development-plan.md`](marketing/development-plan.md)). Marketing: [`site/index.html`](../site/index.html).

## What we want

- **More interactive world** — named followers and enemies, factions, NPCs that matter.
- **Permanent decisions** — paths that open or close optionality (Order, necromancer commit, etc.).
- **Worthwhile objectives** — exploration and quests that gate important things without feeling like fake gates.
- **Longer than the dragon** — strong pre- and post-dragon play; citadel and kingdom arcs are real chapters.
- **Vanilla-compatible** — building, farms, trading halls, and redstone remain valid; mods **encourage** empires and story, not **require** them for basic survival.
- **Two story layers** — **Citadel**: main lore and citadel arc. **Armies**: your kingdom story plus named bandits, intel chains, grey choices (ally bandits?), solo beats and strife.
- **Distinct progression** — titles, taxes, stations, paths, and stat rewards (see backlog below).

## What we avoid

- Feeling like Minecraft is **broken** without mod quests.
- Making vanilla-style farms the **only** optimal path, or making them **useless** — they should be **less necessary**, not forbidden.
- Order and necromancer paths feeling identical (Order: **work then reward**; necromancer: **decision then reward + weakness** — `necromancer-path.md`).

## Design backlog (from goals)

| Item | Notes |
| --- | --- |
| **+max HP on Order specialty lock** | All locked acolytes; amount TBD config |
| **Larger +max HP for War Leader lock** | Citadel Order specialty only (not kingdom Warlord title) |
| **War Leader acolyte: can't hurt own soldiers/militia unless sneaking** | Citadel Order — locked **War Leader** specialty only (`acolyte-path.md`) |
| **Army storylines** | Named bandit kings, loot/intel chains, optional alliance, leave-army-alone quests |
| **Taxes as soft “empire farm”** | Documented in soldiers/villages; reinforce in story design |

## How design is decided

See `docs/notes/2026-10-05-discussion-plan.md` — intake → one section → merge to `docs/*.md` → decision log.
