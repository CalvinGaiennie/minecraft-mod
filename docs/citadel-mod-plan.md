# Citadel mod — planning finish line

**Mod:** Minecraft Kingdom: Citadel (`minecraft_kingdom_citadel`). **Repo today:** monolith `villagers`; split later per `mod-split.md`.

**Purpose of this doc:** single checklist to **close planning** before implementation. Canon detail stays in linked files; this page tracks **scope**, **locked decisions**, **author calls still open**, and **build order**.

---

## What Citadel mod owns

| In scope | Out of scope (Armies / shared) |
| --- | --- |
| Black Citadel structure + world placement (1/world) | Village claim, mess, tax, soldier AI, bandit **camps** |
| Ender barrier, Seal-Breaker gate, assault + 3 bosses | Halvek→Garland **Armies quest** (`armies-questline.md`) |
| Throne claim, credits, usurp, wand forfeit (`citadel-claim.md`) | Roaming deserter bands, warlords (Armies) |
| **10 relic fortresses** + named bandit kings + relic rooms | `CitadelAnchorSavedData.setCitadelCenter` **hook** (Armies reads anchor for shadow camps) |
| **17 acolyte refuges** + player Order path (`acolyte-path.md`) | Kingdom titles from village count (Armies) |
| Player necromancer path intro + §6 arc (`necromancer-path.md`) | Enlistment / opt-out (Armies) |
| Wand, hood, robe, minions, crypts, rogues, horcrux, flutes (`necromancy.md`) | |
| Relics (12), Annals, compass, Kingstree, Well, Roost, golden age (`endgame.md`) | |
| `citadelFallen` + spawn raid tuning publish (`integration.md`) | |

**Install alone:** citadel assault, necro casual + quest, refuges, fortresses, relics (simplified wear rules in `mod-split.md`). **With Armies:** full relic soldier effects, mess in citadel, ally barrier list, shadow-ring camps when anchor set.

---

## Locked (planning done — implement from these)

### World & approach

- **One citadel** per overworld, **1,000–2,000** blocks from spawn; **great road** spawn → intro town → citadel gates (road **outside** bounds).
- **Intro town** ~**350** blocks from citadel center on the road; **Dragon's Well** ~**350** in copse **opposite** road (`necromancer-path.md`, `endgame.md`).
- **Shell:** ~**40** block unbreakable walls; **no lava** in layout; main floor = outside grade; **30** blocks soft basement then unbreakable floor (`citadel-layout.md`).
- **Bounds:** inside outer wall only; camp block **allowed** on approach ground outside wall.

### Seal & assault

- Dragon dead → barrier **weakens**; **Seal-Breaker** on gate opens ward (recipe in `endgame.md`).
- **Three bosses:** Corrupted King + **two bound necromancers**; kill order **free**; brothers **no respawn** after arc kill.
- **Defensive mode:** main inner gate **closed** until king falls; trap corridors + **minable spawners** + hazards (`citadel-layout.md`, `citadel-defenders.md`).
- **Difficulty intent:** kit + layout + effects, not pure HP soak; **ranged strongly recommended** for gate/wall segments.

### Claim & owner

- **60s throne sit**; days **1–7** after king death = killing/contender credits only; day **8+** open sit (`citadel-claim.md`).
- **Active necromancer:** cannot own; healthy Kingstree = **death** on sit; blighted = block + absolve message if no horcrux yet.
- **Barrier allowlist:** owner + allies (Armies ally list when loaded).
- **Usurp:** owner death in bounds → killer **5 min** exclusive sit.
- **Wand forfeit:** confirm UI → clear owner, **7-day** reclaim window, **permanent** citadel ownership ban.

### Necromancer intro (§6 intro — closed)

- **3 hearts lost** → mother storm; **intro chestplate rogue** town; horcrux **4–8 chunks** from town; destroy binding before plate; **Well** meeting; ingredient hand-in = **commit** → god wizard set + ward potion + Seal-Breaker; **lie:** spare **second brother** (evil son).

### Necromancer climax (§6 — direction merged, dialogue TBD)

- **First hit** on a brother = alignment with the **other**.
- **Mother** on son killed: spawn at player, talk, fight; **wrath** if aligned with first → kills first then attacks.
- **Endings table** in `necromancer-path.md` (kill both, agree/refuse/stall End move, etc.).
- **7-day claim timer** starts at **king death** only (stall branch).

### Horcrux & §7 items

- Flutes (6 End tyrants), charges, compass unlimited range, rogue density ~½ outpost, **4%** wild lich (`necromancer-path.md`, `necromancy.md`).

### Integration hooks (first split)

| Publisher (Citadel) | Subscriber (Armies) |
| --- | --- |
| `CitadelEvents.KING_DEFEATED` | Raid/militia near spawn |
| `CitadelQueries.isCitadelFallen` | Threat tuning |
| `setCitadelCenter(pos)` | Shadow camps ring ~480±24 |

---

## Author decisions still open (close these to “finish planning”)

Work **top to bottom**. Mark **Decided** in discussion plan when you call each.

### A. Lore identity (blocks naming & VO)

| # | Question | Options / notes | Canon impact |
| --- | --- | --- | --- |
| A1 | Are the **two bound necromancers** the same people as the **founding brothers** myth (Hakon/Oswin/Harren)? | Merge in lore §3–5 vs keep “Black Treaty advisers” only | Dialogue, Annals, mother lie |
| A2 | **Display names** for king, both brothers, mother, intro rogue | First pass list | All quest lines |
| A3 | **End-move ending:** citadel **relocates to End** with overworld crater portal — still a **full** ending or **optional** branch only? | MVP can ship **without** move ritual if too heavy | Structure gen, save data |

### B. Citadel structure mockup (§10 — closes layout TBDs)

| # | Question | Default if silent |
| --- | --- | --- |
| B1 | **Secret tunnel:** include in v1? | **Yes**, one rare chronicle hint |
| B2 | **Dungeon Order lever:** single lever opens both cells? | **Yes** |
| B3 | **Inner gate / ward doors:** redstone closable only in defensive mode, or player levers post-claim too? | Defensive = closed; post-claim levers for owner **TBD** |
| B4 | Spawner **counts per zone** (gate / keep / basement) | Playtest after Creative blockout |
| B5 | After **king dead**, leftover spawners **auto-off** or **player-mined only**? | **Player-mined** (doc lean) |
| B6 | **Citadel undead** mob when Armies absent | Custom zombie variant vs vanilla with tag |

### C. Necromancer §6 polish

| # | Question |
| --- | --- |
| C1 | **Opt out** at hand-in: separate button vs “don’t hand items”? |
| C2 | **Multiplayer:** one intro per world vs per player; brother **first-hit** alignment per player? |
| C3 | **End Anchor** recipe detail (pearl count, consumed on place?) |
| C4 | **Wither** after Heart kill: despawn vs fight vs banish |
| C5 | Full **dialogue pass** stubs → final lines (mother, brothers, Corwin tree) |

### D. Fortresses & relics

| # | Question |
| --- | --- |
| D1 | **10 fortress** placement rings (distances, min separation) |
| D2 | Garrison size target (“tough outpost”) — numbers |
| D3 | **Six relic forts:** fixed king names + which relic each holds |
| D4 | Thornheart **End island** outpost — in v1 or later? |

### E. Acolytes & Order

| # | Question |
| --- | --- |
| E1 | **15 vs 17** refuges — doc drift (`endgame` vs `citadel-claim`); pick one |
| E2 | Refuge **layout count** (4 templates?) |
| E3 | War Leader **friendly fire** rule — confirm explosions exception |

### F. Config & solo Citadel

| # | Question |
| --- | --- |
| F1 | Publish **`citadelFallen`** default raid table in config when no Armies |
| F2 | **Fortress defender** fallback entity id when no Armies |

---

## Recommended implementation phases

Planning is **finished** when **A1–A3**, **B1–B3**, **B5–B6**, **C1–C2**, **D1–D2**, **E1** have answers. Everything else can tune in playtest.

```text
Phase 0 — Split prep (can stay monolith folder first)
  Mod id, SavedData keys, integration events, CitadelAnchor API

Phase 1 — World skeleton
  Citadel template place + road + setCitadelCenter
  Intro town placeholder + Well copse marker
  17 refuge SavedData + 10 fortress SavedData (empty shells OK)

Phase 2 — Seal & barrier
  Ender barrier volume, dragon gate, Seal-Breaker use

Phase 3 — Assault MVP
  3 boss entities (clamp + player kill), defensive mode gates
  Spawner blocks + 2–3 trap corridor templates
  Beacon basement (breakable)

Phase 4 — Claim loop
  Throne sit, credits, usurp, wand forfeit, barrier allowlist

Phase 5 — Restoration
  Kingstree + Blight Heart, Hall relic pedestals, stations wake
  Well XP, Roost cage, golden age / shadow flags

Phase 6 — Necromancer intro
  Hearts threshold, intro town rogue, horcrux chest, Well commit

Phase 7 — Necromancer citadel arc
  Brother alignment, mother fights, ending branches (End move last)

Phase 8 — Meta progression
  Relics, fortresses, Annals, compass, acolyte path feats

Phase 9 — Armies integration polish
  citadelFallen events, ally list, mess placement rule, shadow camps
```

**MVP ship candidate:** Phases **0–5** + **6** (intro only) + minimal **8** (1–2 relics for test). **Full v1:** through **7** + all fortresses + Order feats.

---

## Testing plan (Citadel)

When implementation starts, extend `armies-manual-test-plan.md` with a **Citadel** section or sibling `citadel-manual-test-plan.md`:

- Barrier: pearl/dig/fly blocked until seal open
- Seal-Breaker only after dragon
- Spawner break mid-assault silences spawn
- Player-only king kill; credits flags
- Throne sit timers days 1–7 vs 8+
- Necro sit death / blight block
- Intro rogue invuln until quest start; horcrux destroy before plate
- `setCitadelCenter` → Armies shadow camps (both mods)

Automated: GameTests for SavedData, barrier AABB, quest stage flags (pattern `ArmiesGameTests`).

---

## Doc map (implementers)

| Topic | File |
| --- | --- |
| Layout & rooms | `citadel-layout.md` |
| Garrison & traps | `citadel-defenders.md` |
| Throne & credits | `citadel-claim.md` |
| Relics, tree, golden age | `endgame.md` |
| Necro arc | `necromancer-path.md`, `necromancy.md` |
| Order arc | `acolyte-path.md` |
| Cross-mod | `integration.md`, `mod-split.md` |
| Decision log | `notes/2026-10-05-discussion-plan.md` |

---

## Next planning session (suggested agenda, ~45 min)

1. **A1 + A2** — names and brother identity (15 min)
2. **A3** — End-move in v1 or v1.1 (5 min)
3. **B1–B3, B5–B6** — mockup contract for first structure pass (15 min)
4. **C1–C2** — MP + opt-out UX (10 min)
5. **D1–D2 + E1** — fortress rings and refuge count (10 min)

When those rows are decided, update `2026-10-05-discussion-plan.md` and treat **Citadel planning as closed** for implementation.
