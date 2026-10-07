# Garland fortress NBT — biomes & landscape (author guide)

**For:** building Garland before code loads it. **Expect multiple `.nbt` files** if the site includes several mountains or exceeds **48×48×48** per piece (Java Structure Block limit). **Code today:** only a **marker block** at the site anchor — **no** multi-piece paste yet (`BanditSiteGenerator.placeSite`).

---

## What an NBT structure actually is

- A **saved box of blocks** (and entities, block entities) from a **Structure Block → Save**.
- Everything is stored **relative to a structure origin** (the structure block’s “corner” / save point when you export).
- It is **not** a biome. It does **not** include world noise, caves, or trees unless **you built those blocks into the box**.

When the mod pastes it later (`StructureTemplateManager` → `placeInWorld`):

- Those blocks are **copied into the world** at the chosen **world position + rotation**.
- The **biome at that X/Z** stays whatever Minecraft already generated (plains, taiga, etc.).
- **Biome effects** (grass color, rain, mob spawns) come from the **world**, not from the file.

So: **your build palette** can match a biome; **world biome** still controls the horizon unless you **overwrite** a wide area inside the NBT or in code before paste.

---

## Three ways to tie fortress to “a biome / landscape”

### A. **Pick the biome in code, build one NBT for it** (simplest)

1. We **decide** Garland always spawns in e.g. **plains / meadow / custom tag** (see `tbd-citadel-biome-placement.md` — same idea for Garland ring).
2. You build in **`runClient`** in **that biome** (or superflat with matching ground block).
3. Export **fortress only** (walls, prison, tower) — surrounding terrain stays vanilla at paste time.

**Pros:** One file. **Cons:** Edge of paste may clip hills/trees; gate height must tolerate **small** heightmap error (code adjusts Y **TBD**).

### B. **Bake landscape into the NBT** (best for “on a cliff / ridge / ravine lip”)

1. In Creative, **sculpt** the feature (hill cut, cliff face, dry moat) **inside** the structure box.
2. Save **fortress + terrain sculpt** as one template.
3. Code picks a **relatively flat** anchor and paste **includes** your fake geology.

**Pros:** Looks authored everywhere; doesn’t depend on finding a real ravine. **Cons:** Bigger file; paste can **bury** natural trees if box is huge; need **`structure_void`** for air you don’t want to overwrite (**TBD** in paste settings).

### C. **Find a real world feature, then paste** (harder, code-heavy)

1. Generator searches for **ravine / plateau / slope** near Garland ring.
2. Rotates gate to face **approach road / army camp** direction.
3. Paste with **offset Y** so gate meets ground.

**Pros:** Natural drama. **Cons:** Unreliable seeds; more programming; you still author **fortress** mostly without terrain in NBT.

**Garland v1 recommendation:** **A or B** — you control the look before we automate search.

---

## Structure block workflow (what you do in-game)

1. **Choose test world:** flat **or** target biome **or** hand-built cliff in flat world (option B).
2. Mark volume with **Structure Block** (Corner + Corner, or single block Save mode per MC version UI).
3. Build **entire compound** inside the box: prison, **main arena**, **tower** with **sightline** toward where the army will sit, gate, escape route.
4. Place **`bandit_camp`** block at the **mod anchor** — same block the code uses today for spawn/loot (document offset from gate in a sign or `authoring/structures/garland-anchor.txt` **TBD**).
5. **Save** → copy `.nbt` to `authoring/structures/garland_compound.nbt`.
6. **Note on paper:** which way is **+Z** from anchor (army camp direction for tower vista), **size** of box, **Y** of gate floor.

**Air blocks:** In paste, air in the template may **erase** world blocks (depending on `ignoreEntities` / processors). For outdoor parts, use **`structure_void`** in modern saves where you want “don’t touch world” — confirm in your MC version when we wire code.

---

## How this connects to Garland **plot** (army visible from tower)

- **Army camp** will be **code-spawned** outside the fortress at a **fixed offset** from anchor (not in NBT v1 **TBD** e.g. south 80 blocks).
- When you build the **tower**, align windows/balcony toward that **agreed direction** so “literally see them” works without moving the build each seed.
- **Approach village** (guide escape) may be a **vanilla village** the march path hits — separate from NBT or a small pasted hamlet **TBD**.

---

## Multi-piece Garland (mountains + fortress)

Garland can be **much larger than the citadel**. Treat the site as a **layout in one Creative world**, then export **tiles** — not one monolithic save.

### Authoring rules

1. **Pick a site origin** (e.g. bedrock marker or gold block at `(0, Y, 0)` in your build world). **Never move** the finished layout after you start exporting.
2. **Grid tiles** at **48×48×48** (Structure Block max) or use **WorldEdit** for odd-sized chunks — same idea: each file has a **fixed offset** from site origin.
3. **Naming:** `garland_p_<x>_<y>_<z>.nbt` where **p** = piece index optional, **x/y/z** = **block offset** of that piece’s **structure origin** from site origin (e.g. `garland_0_0_0.nbt`, `garland_48_0_0.nbt`, `garland_0_64_-48.nbt`).
4. **One manifest** in `authoring/structures/garland-manifest.json` (**TBD** when first pieces exist): list of `{ "file", "offset": [dx, dy, dz], "rotation": 0 }` and which file contains **`bandit_camp`** (site anchor for code).
5. **Overlap 1–2 blocks** at tile seams (shared mountain ridge) so paste gaps don’t show cracks — or sculpt seams so only one tile owns the ridge (**pick one rule** and stay consistent).
6. **`structure_void`** in outdoor “sky” cells inside a tile so paste doesn’t strip the next tile’s mountain when we paste in order (**TBD** in code: paste back-to-front or void-aware).
7. **Prison / arena** can live in **one interior tile** (easier quests) even if mountains span many tiles.

### What code will do later

- Resolve anchor → paste **every manifest entry** at `anchor + offset` (same world rotation).
- Load only **chunks that intersect** the bounding box (may need **simulation distance** bump on first visit — **TBD**).

Citadel uses the **same manifest pattern**; Garland is just a **bigger** bounding box and more terrain pieces.

---

## What we need from you before coding paste

| Decision | Why |
| --- | --- |
| **Target biome** (or “biome-agnostic stone”) | Placement search vs pure paste |
| **Terrain in NBT?** (yes/no + how deep) | Pad vs sculpt vs search |
| **Anchor = bandit_camp block** position | Gate vs marker; which **tile file** holds it |
| **Army direction** from anchor | Tower vista |
| **Site bounding box** (total X×Y×Z) + **tile list** | Manifest + chunk load |
| **Seam rule** (overlap vs single-owner ridges) | Avoid holes at paste |

---

## Related docs

- `tbd-garland-fortress-prison-arena.md` — rooms to blockout inside the compound.
- `bandit-camp-prefabs.md` — pipeline once paste exists.
- `tbd-citadel-biome-placement.md` — same biome ideas for Black Citadel later.
