# TBD — Citadel biome & world placement (before NBT)

**Status:** **Partially decided** (2026-10-08). Still need strategy #1–9, authoring anchor/Y, and fail-placement rules before final NBT export.

## Decided (2026-10-08)

| # | Decision |
| --- | --- |
| **10** | Citadel **always faces spawn** (rotation from spawn → citadel anchor). |
| **11** | Great road surface for now: **cobblestone** (intro town / full road gen deferred with v1 scope). |
| **12** | Intro town **does not** need same biome as citadel — **distance-on-road** placement only. |
| **13** | **Dragon's Well:** OK as **dark forest copse** contrast even when citadel is plains/other. |
| **14** | **Shadow camps (~480):** **no** biome constraint. |
| **18** | **v1 test scope:** place **citadel NBT only** — no bundled intro town / well / full road pass in first implementation. |

**Today (docs/code):** **One citadel** per overworld, **~1,000–2,000** blocks from **spawn** on the **great road** (`endgame.md`, `citadel-mod-plan.md`). **No biome filter** in code yet — only `CitadelAnchorSavedData.setCitadelCenter` after placement. Intro town **~350** blocks from citadel on the road; Dragon's Well **~350** off-road in a copse (`necromancer-path.md`). Armies **shadow camps** ring **~480** from anchor when set.

---

## Why this matters for NBT

| If you build… | Risk without a biome rule |
| --- | --- |
| **Single fixed palette** (grey stone, oak, dark oak) | Usually **fine in most overworld biomes**; looks odd in **snow** (no snow on roofs) or **desert** (no sandstone blend). |
| **Biome-blended skirts** (grass paths, spruce trim, sand bleed) | **Wrong** if citadel spawns in taiga vs plains — need **variants** or **gen overwrites** ground around template. |
| **Tall gate / 40-block wall** | **Terrain variance** (mountain vs flat) changes approach drama; may need **terrace** or **cut/fill** in code. |

---

## Strategies (pick one primary for v1)

### 1. **Same biome every time (recommended default for one NBT)**

**Gen:** When placing citadel, search outward from spawn (within **1k–2k** ring) for chunks whose **dominant biome** matches a **config tag** (e.g. `#minecraft:is_overworld` + **`minecraft:plains`** or **`#villagers:citadel_biomes`**).

**Pros:** One authored NBT; predictable art pass; road and intro town can share biome.  
**Cons:** Some seeds **fail** search → retry other angles / widen ring / fallback biome list (**TBD**).

**Authoring:** Build NBT in **that biome** on a flat world (or superflat matching palette).

---

### 2. **Biome-agnostic fortress + minimal site prep**

**Gen:** Accept **any** solid overworld site in the ring; before `StructureTemplate.place`, code **clears** a **footprint** (e.g. 128×128 pad), **levels** gate approach, optional **stone platform** under walls.

**Pros:** One NBT; works on hills; no failed biome search.  
**Cons:** Surrounding **vanilla biome** still visible beyond pad (snow line, jungle trees); may need **wide clear** or **blending** mask.

---

### 3. **Multiple NBT variants (biome family)**

**Gen:** Detect biome at anchor → pick **`citadel_plains`**, **`citadel_taiga`**, **`citadel_snowy`**, **`citadel_arid`** (desert/badlands).

**Pros:** Best visual fit; same layout, swapped palette blocks in template or separate files.  
**Cons:** **4×** structure work (or palette block in structure **TBD** in 1.21).

---

### 4. **Fixed world flavor (not recommended alone)**

Require custom world preset / single biome world — **breaks** normal survival seeds. Only as **optional** data pack, not default mod behavior.

---

## Linked systems (must agree with biome choice)

| System | Note |
| --- | --- |
| **Great road** | Straight spawn → citadel; **cobblestone** for v1 when road is wired; citadel-only test may skip road strip initially. |
| **Intro town (~350b)** | On road by **distance**; **no** strict biome match with citadel. |
| **Dragon's Well copse** | **Opposite** road side; **dark forest** copse for contrast (**decided**). |
| **10 relic fortresses (~3500 ring)** | Separate ring; **own** biome rules possible (author D1). |
| **15 refuges** | Scattered; **per-archetype** biomes already planned (`citadel-mod-plan` E1). |

---

## Open questions (next discussion)

1. **Target biome for v1 single NBT:** plains, meadow, custom “kingdom heartlands” tag, or **first match** from ordered list?
2. **Terrain:** max **slope** at gate; allow **hilltop** citadel vs **forced flatten**?
3. **Snow line:** exclude **snowy** biomes explicitly, or use **snowy variant** NBT?
4. **Fail placement:** widen ring, pick second biome, or **log error** and skip citadel (bad)?
5. **Structure void / jigsaw:** single `.nbt` with **`structure_void`** under footprint vs manual pad in code?
6. **Build workflow:** you build in **Creative matching biome**; we document **anchor block** + **rotation** (faces spawn — **decided**) + **ground Y** rule (gate = `MOTION_BLOCKING` surface).

### Still need (from full questionnaire)

- **1–3, 7–9:** primary placement strategy, biome list, snow handling, spawn ring, fail behavior.
- **15–17:** Creative build biome, anchor block convention, gate Y rule.

### Specific tradeoffs (replacing vague “hard no”)

**A.** If citadel spawns on a **steep hill**, OK to **cut a 128×128 terrace** (trees/grass gone, stone rim) vs **only** fill air under walls (leave wild slope outside)?  
**B.** If search finds **only jungle/swamp** in the 1k–2k ring, **accept** grey stone citadel there vs **keep searching** up to 3k vs **force pad on nearest plains** (teleport-y)?  
**C.** **Snowy taiga** at anchor: **exclude** from candidate sites for v1, or **allow** (visual mismatch until snowy NBT exists)?  
**D.** **Ocean / river** chunk at intended anchor: **skip** and pick next angle along ring, or **allow** if pad bridges shallow water?  
**E.** For **citadel-only test**, placement trigger: **on world create**, **`/villagers` debug command**, or **first player join** only?

---

## Doc links

- `citadel-mod-plan.md` — scope, open **B3** (post-NBT layout).
- `citadel-layout.md` — shell size, gate height, outside shadow.
- `endgame.md` — distance, road, Well.
