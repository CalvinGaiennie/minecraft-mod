<!-- Canonical source for site/development-plan.html. Regenerate: python3 docs/marketing/build_marketing.py -->

## Mod goals {#goals}

::: want
### What we want players to feel

- **More interactive Minecraft** — better NPCs: named followers, named enemies, factions that react to you.
- **Decisions that stick** — choices that *permanently* open or close paths (Order vs committed necromancer, opt-out, no Order after citadel claim, etc.).
- **Objectives that matter** — goals that feel necessary or worth it to unlock places, gear, stations, and story — not checklist busywork for its own sake.
- **A longer game than the dragon** — meaningful **pre-dragon** and **post-dragon** arcs. The citadel seal, kingdom play, and army campaigns are part of the real timeline.
- **Vanilla still works** — never block the normal loop: build, farm, trade hall, redstone, explore your way. Mods *encourage* quests, empires, and story; they don't replace survival.
- **Two kinds of story** — **Citadel**: main lore, citadel, Order, necromancer path, relics, Minecraft tie-ins (TBD). **Armies**: your emergent kingdom *plus* authored bandit kings, named foes, loot and intel that unlocks the next threat — optional alliance with bandits, forced solo beats, emotional strife.
- **Progression that feels different** — not just better gear: titles, taxes, stations, paths, and **character stats** (see backlog).
:::

::: avoid
### What we do not want

- Minecraft feeling **broken** or "wrong" if you ignore our quests.
- Optimal play being **only** vanilla farms and trading halls — they should stay viable, but feel **less mandatory** when the mod's stories and empire loop are engaging.
- Armies becoming a spreadsheet that kills roleplay — or Citadel becoming a gate that hard-blocks creative builders.
- Copy-paste quest design — Order is **work → reward**; necromancer path is **decision → reward + weakness** (see `docs/necromancer-path.md`).
:::

## Two mods, one vision {#mods}

::: two
### Minecraft Kingdom: Armies

Villages, soldiers, taxes, horns, campaigns, bandits. Build towns and cities that **behave a bit like farms** — recurring tax and supply — while generating **your** story.

Planned: named bandit leaders, chained intel/loot, moral grey (help or hinder bandits), moments where you leave the army behind.

### Minecraft Kingdom: Citadel

Black Citadel, Corrupted King, Kingstree, Order acolyte path, necromancer path, relics, refuges. Main **Minecraft-world** story spine.

Optional with Armies; kingdom scope amplifies when both are loaded.
:::

## How we plan (plans to plan the plans) {#process}

::: process
Design is **author-owned**. Implementation follows written docs in `docs/`, not the other way around.

1. **Intake** — raw notes land in dated files under `docs/notes/` (not canon until merged).
2. **Section order** — `docs/notes/2026-10-05-discussion-plan.md` lists topics (lore, paths, feats, layout). We do **one section at a time**.
3. **Discuss → decide → merge** — you call the shot; we update the real doc (`acolyte-path.md`, `endgame.md`, etc.) and log the row in the discussion plan.
4. **No skip ahead** on canon until the current section is merged (unless you explicitly defer a block, e.g. lore §2–6).
5. **This section** — high-level goals and process; detail stays in markdown for implementers.

**Status snapshot (2026-10-06):** Order flow + feat gates (§8) merged; **§10 citadel layout/siege** and **§7 necromancer items/horcrux** documented (mockup + tuning TBD); necromancer philosophy + fork set; lore §2–6 and full necromancer story arc (§6) deferred. Active queue: finish §10 TBDs → §9 world notes → §7 implementation specs.
:::

## Design progress (canon docs) {#progress}

::: process
### Black Citadel — layout & assault (§10, in progress)

Written to `docs/citadel-layout.md` and `docs/citadel-defenders.md`:

- Template fortress: strong shell, guttable wings, **30-block** soft dig, **no lava moat**.
- **Defensive mode:** one main entrance, closable inner gate, trap corridors (tripwire/plate potion traps, webs, water, ice, lava lanes), **breakable spawners** and trap parts mid-assault.
- Garrison mix: undead, skeletons, wither skeletons, spiders, baby zombies, witches, **few ravagers**; difficulty from layout, effects, and kit — not HP bloat.
- **Player-driven** post-claim: levers, dismantle traps, break spawners. Blockout still needed for lever map, wing volumes, shadow-ring counts.

### Necromancer path — items & horcrux (§7, in progress)

Written to `docs/necromancer-path.md` and `docs/necromancy.md`:

- Progression = **quest items + sacrifices** (flutes/pipes, cursed armor), not Order-style class grids.
- **Horcrux:** ritual craft (shard + power item in hotbar + kill on soul list); **many jars** limited by permanent hearts; stored in **chests**; pseudo-death instead of dying; destroy via **Dragon’s Well** (anytime), wither, ghast, lightning, etc.
- **Wild lich** crypt rogues (subset) teach hunt/destroy before players commit; **Dragon’s Well** also fills breath bottles (unlimited cap; well is hard to find).
- **Consider:** horcrux **corruption** of weak nearby soldiers — not specced yet.
:::

## Design backlog (from goals — not all implemented) {#backlog}

::: planned
Items called out in goal-setting that still need specs in `docs/` and then code:

| Item | Mod | Intent |
| --- | --- | --- |
| **Permanent max-health boost** when Maelor locks Order specialty | Citadel | Progression reward for acolyte commit; tune in config. |
| **Larger permanent max-HP boost** for locked **War Leader** acolyte | Citadel (Order) | Bigger than other three specialties; amount TBD config. |
| **War Leader acolyte: no damage to own troops unless sneaking** | Citadel (Order) | See callout below — locked War Leader specialty only. |
| Named bandit kings, loot/intel chains, optional bandit alliance, solo-forced quests | Armies (+ Citadel hooks) | Army-side storylines; less "main lore" than citadel, still authored. |
| Tax / claim loop as soft "farm" replacement | Armies | Empire income without requiring vanilla iron farms for fun. |
:::

::: planned
### War Leader acolyte — friendly-fire rule (design intent)

A player whose Order specialty is **locked as War Leader** (`acolyte-path.md`) **cannot hurt their own soldiers or militia** with direct player damage (melee, arrows, tridents, etc.) while **not sneaking**.

- **Default:** hits against *your* soldiers/militia do not apply (cancelled or no damage).
- **Sneaking:** you *can* damage your own troops — intentional only (discipline, mutiny edge cases per Armies rules).
- **Other specialties** (Arcane, Alchemist, Smith) follow normal Armies friendly-fire rules.
- **Not** kingdom NPC "Warlords" raids or a separate kingdom title — Order War Leader only.
- Explosions, lava, and fire may still harm troops unless we add exceptions later (TBD).
:::

## Mod compatibility targets {#compat}

::: process
**Not dependencies.** Armies and Citadel must run on their own. When other mods are in the pack we aim for two levels:

- **Coexist** — verify claims, quests, spawns, death/loot, and UI don't fight each other (smoke-test per release).
- **Extend** — optional integration that wakes **dormant** content in our mobs, quests, or abilities only when that mod is loaded (no crash if absent).

Pack makers pin versions we test against; nothing here is required to play.
:::

Priority list (initial — expand in `docs/integration.md` when specced):

| Mod | Kind | Intent |
| --- | --- | --- |
| [Small Ships](https://modrinth.com/mod/small-ships) | Coexist + extend | Naval campaigns; future soldier crew aboard ship entities (not vanilla boats). |
| [Ice and Fire: Community Edition](https://www.curseforge.com/minecraft/mc-mods/ice-and-fire-community-edition) | Coexist + extend | Mythic mobs and dragons alongside citadel lore; parallel fantasy, not a hard gate. |
| [Mowzie's Mobs](https://www.curseforge.com/minecraft/mc-mods/mowzies-mobs) | Extend | Selected bosses/mobs feed quests or unlock abilities in our content when present. |
| [Alex's Mobs](https://www.curseforge.com/minecraft/mc-mods/alexs-mobs) | Extend | Same pattern — optional encounter/quest hooks, not replacements for our roster. |
| [Corpse](https://modrinth.com/mod/corpse) | Coexist | Death body / loot recovery compatible with army wipes and citadel defeats. |
| [Comforts](https://modrinth.com/mod/comforts) | Coexist | Sleeping bags/hammocks don't break campaign pacing or claim rules. |
| [Grappling Hooks](https://modrinth.com/mod/grappling-hooks) | Coexist | Traversal during citadel exploration and siege prep. |
| World map mod (**TBD**) | Coexist | Waypoints/markers for refuges, claims, citadel — **confirm Xaero's vs JourneyMap** with author. |
| [Biomes O' Plenty](https://modrinth.com/mod/biomes-o-plenty) | Coexist | Extra biomes for worldgen; quest placement and structures must still find valid terrain. |
| [Create](https://modrinth.com/mod/create) | Coexist | Village protection vs moving contraptions; factory bases beside claims. |
| [Epic Knights: Shields, Armor and Weapons](https://modrinth.com/mod/epic-knights-shields-armor-and-weapons) | Coexist + gear | Soldier kits and supply accept modded medieval gear. |
| [Medieval Siege Machines](https://modrinth.com/mod/medieval-siege-machines) | Coexist + extend (planned) | Player siege complements walls; soldier-operated siege is integration target, not day-one AI. |

### Canonical doc index (implementers)

- `docs/overview.md` — scope and build order
- `docs/acolyte-path.md` — Order quest + feats
- `docs/necromancer-path.md` — dark path philosophy + fork
- `docs/endgame.md`, `docs/citadel-claim.md`, `docs/citadel-layout.md`, `docs/citadel-defenders.md` — citadel loop & siege
- `docs/necromancy.md` — wand, crypts, horcrux design
- `docs/soldiers-and-villages.md`, `docs/war-and-defense.md` — Armies
- `docs/notes/2026-10-05-discussion-plan.md` — section queue + decision log
