# TBD — Citadel biome & world placement (before NBT)

**Status:** **Discussion only.** Decide **before** final Black Citadel structure export so art direction matches gen rules.

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
| **Great road** | Straight spawn → citadel; road blocks **TBD** (gravel/oak vs stone) — should match chosen biome or be neutral. |
| **Intro town (~350b)** | On road; likely **same biome** as citadel approach for cohesion. |
| **Dragon's Well copse** | **Opposite** road side; can be **dark forest** copse even if citadel is plains **TBD** (intentional contrast). |
| **10 relic fortresses (~3500 ring)** | Separate ring; **own** biome rules possible (author D1). |
| **15 refuges** | Scattered; **per-archetype** biomes already planned (`citadel-mod-plan` E1). |

---

## Open questions (next discussion)

1. **Target biome for v1 single NBT:** plains, meadow, custom “kingdom heartlands” tag, or **first match** from ordered list?
2. **Terrain:** max **slope** at gate; allow **hilltop** citadel vs **forced flatten**?
3. **Snow line:** exclude **snowy** biomes explicitly, or use **snowy variant** NBT?
4. **Fail placement:** widen ring, pick second biome, or **log error** and skip citadel (bad)?
5. **Structure void / jigsaw:** single `.nbt` with **`structure_void`** under footprint vs manual pad in code?
6. **Build workflow:** you build in **Creative matching biome**; we document **anchor block** + **rotation** (0/90/180/270) + **ground Y** rule (gate = `MOTION_BLOCKING` surface).

---

## Doc links

- `citadel-mod-plan.md` — scope, open **B3** (post-NBT layout).
- `citadel-layout.md` — shell size, gate height, outside shadow.
- `endgame.md` — distance, road, Well.
