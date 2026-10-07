# Black Citadel — layout and rooms

**§10 (in progress):** Template fortress — **strong shell**, **deep floor**, **guttable interiors**, protected story anchors. Defenders: **`citadel-defenders.md`**. Claim: `citadel-claim.md`.

**Design intent:** Powerful **template** players **remodel** (Create, storage, farms). Protected story blocks stay; most interior is **soft**.

**Lava:** **None** (scrapped).

---

## How deep is the citadel? (plain language)

Use **ground height at the gate** as the reference (same Y as **main citadel floors**).

1. **Main floors** (courtyard, outdoor Hall, keep ground level) are **level with the outside world** — you are not walking down a cliff to enter; the fortress spreads **out and down** from that plane.
2. **Basements** (dungeon, beacon crypt, horn tunnels) are **below** those main floors — dug downward inside the walls.
3. From main floor level down to the **unbreakable bottom** is **30 blocks** total. Everything in that **30-block column** (inside the footprint) that is **not** a protected story block is **soft** — you can mine and expand cellars after the seal is open.
4. At **30 blocks below main floor**, the **unbreakable bottom** stops all downward digging under the citadel.

So: **main floor = outside grade → down 30 blocks breakable (soft) → unbreakable floor**. Exact room placement is structure JSON.

---

## Citadel bounds

| Inside outer wall | Outside |
| --- | --- |
| Kingstree, outdoor Hall, paddock, stables, keep, yards, basements to unbreakable bottom | **Great road** only |

**Outside the wall:** **no special citadel rules** — normal overworld + Armies systems. **Camp block** on approach/mustering ground **allowed** for siege staging.

---

## Unbreakable

- **Outer walls / towers / gate frames** (~40 tall).
- **Unbreakable bottom** under the whole footprint.
- **Inner keep “box” shell** — silhouette stays; **everything inside** the box is soft (author §10).
- **Throne**, **12 king statues + bases**, **royal stations + lecterns**, **Well**, **dummies**, **Roost** cage + **phantom spawner** (post-claim farm — not garrison spawners), **main treasury**, **horn vault shell** (quest opens), **secret treasury shell**.
- **Kingstree:** **tree + Blight Heart** protected; **landscaping** around it **breakable**.
- **Citadel doors:** special player-minable doors (soldiers can't).
- **Beacon blocks:** **not** protected — **guards only** (`citadel-defenders.md`).

---

## Breakable / remodel

- **Who:** any player who **can enter** (seal broken).
- **Outdoor hall:** all except **statues + bases**.
- **Soft wings + inner keep interior:** gut and rebuild.
- **Armies inside bounds after king dies:** owner/allies place freely; non-allies use **village protection** slowdown (`soldiers-and-villages.md`).

---

## Outside the wall — citadel shadow (Armies)

Within **~256–512 blocks** of the citadel (`threats-and-mobs.md`):

- **Ruined hamlets** — abandoned village-style layouts (broken mess halls, empty beds, cracked walls).
- **Bandit-occupied ruins** — **bandits live there**; may include **surviving villagers** (traders scared, few nitwits — not a healthy village).
- **Classic bandit camps** — smaller outpost structures **in addition** to ruins (same band rules as elsewhere).
- Lore: settlements that **sheltered in the citadel’s shadow** **fell** when the kingdom did.

---

## Siege difficulty (layout + encounters)

Difficulty is **mostly** boss kits, spawner placement/type, **geometry**, and **status / kit pressure** (`citadel-defenders.md` **Siege prep & effects**) — chokepoints, funnels, sightlines — **not** a pure armor-and-HP soak.

| Layer | Design job |
| --- | --- |
| **Approach** | Shadow-ring bandits (Armies); mustering ground outside wall. |
| **Walls** | Unbreakable ~**40** tall — **too tall for siege ladders** (`endgame.md`). |
| **One main entrance** | Primary assault path: **large open gate / barbican** — **closable** (see **Assault defensive mode**). |
| **Secret tunnel** | Optional second route — tighter, not the main claim path **TBD** mockup. |
| **Gate gauntlet** | While outer **citadel doors** are mined, spawners and **ranged overwatch** cover the line — expect **bows/crossbows** (or equivalent) here and on wall walks. |
| **Chokepoints** | Stairs, bridges, courtyard killsacks — melee vs archer spawners placed for **funnels**. |
| **Branch routes** | Main push vs **secret tunnel** (shorter, tighter ambush). |
| **Boss arenas** | King + necromancers; cover, elevation, add alcoves **TBD** in blockout. |
| **Beacon basement** | Side objective; cramped deadly funnel. |
| **Vertical** | Basements add down-stack chokes (30-block soft column above unbreakable bottom). |

**Blockout:** tag each spawner with role (gate pressure, tower overlook, keep reserve, necromancer adds).

---

## Assault defensive mode (initial assault / until king dead)

While the **Corrupted King lives** (and through **first claim sit** if needed — **exact off-trigger TBD** mockup):

| Element | Behavior |
| --- | --- |
| **Main inner gate** | **Closed** — huge entrance into inner ward **not** walk-through until defensive mode ends (redstone / custom gate **TBD**). Attackers use **outer breach** + **kill corridors**, not a straight rush to throne. |
| **Corridors** | **Narrow** funnels + **trap** segments: **tripwire** + **pressure plates** → **dispensers** (splash/lingering **debuff + damage** potions — see `citadel-defenders.md`), **lava** releases/drops, gravel/drop pits **TBD**. Rotate **different effect mixes** per segment so the push feels like **several puzzles**, not one repeated choke. |
| **Terrain hazards** | Native Minecraft **siege friction**: **cobwebs**, **water** / **waterfalls**, **packed ice**, **lava** sections (intentional trap lanes — **not** the scrapped lava-in-walls moat). Requires buckets, boats, fire res, careful movement. |
| **Mid-assault dismantling** | **Redstone trap parts** and **garrison spawner blocks** are **breakable** if attackers can reach them — tower clears, side-room saps, and corridor trap stripping are all valid siege play (`citadel-defenders.md`). |
| **After king + claim** | **No full auto strip**. **Player-driven** remodel: **levers** to open gates, **remove** leftover tripwires/plates, **mine** any spawners/hazards still in place. Citadel becomes **remodelable** template. |

Mockup decides: tunnel open during defensive mode?, which gates are lever-only vs still redstone-locked until claim, default lever locations.

---

## Keep rooms (v1)

Throne, outdoor Hall, library, alchemy + gardens, war room, treasuries (secret = hidden redstone, **undocumented**), armory, **large sparse** banquet/store/guest/chamber wings (**sizes TBD**), dungeon (captives + 2 Order brothers, **lever TBD**), training + joust decor, stables + paddock **inside wall**, Well, Roost, **undead spawners** (sprinkled), beacon basement, **3 boss arenas** (king + 2 necromancers).

---

## Trapped Order brothers

War Leader + random craft specialty; **15** overworld refuges; lever **TBD**; Annals **refuge chests only**.

---

## Hall of Heroes (Armies)

Scattered plinths inside wall when mod loaded.

---

## §10 still open

- Dungeon **lever** design (one vs split).
- **Soft wing** empty volume targets.
- Shadow ring **counts** and **hamlet vs camp** ratio.

---

## Cross-links

`endgame.md`, `citadel-defenders.md`, `citadel-claim.md`, `acolyte-path.md`, `soldiers-and-villages.md`, `threats-and-mobs.md`
