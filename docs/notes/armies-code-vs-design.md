# Armies mod — code vs design (living gap list)

**Not exhaustive** — tracks major doc promises vs JAR. Update when closing gaps.

## Implemented in code (recent)

| Feature | Notes |
| --- | --- |
| **Natural village militia** | On chunk load + login chunk sweep; bell + ≥3 villagers, no mess in radius |
| **Bandit structure paste** | `BanditStructurePaste` loads NBT per site kind |
| **O2 without compass** | Torn map bearing + `O2_ACTIVE` |
| **Allies** | `/villagers ally …`, **Ally book GUI**, **Friend / Officer / Co-owner** ranks (Officer+ may use horns) |
| **Horns** | Awaken, **Fallback**, **Charge**, **Guard chief** — `HornCommandService` (256 block + in-village registration) |
| **Lever horns + soldier levers/buttons** | Dyed lever horns, **soldier lever/button tags**, soldiers path and use on horn blast; instant fallback if no soldiers |
| **Flares** | **Signal rockets** only (crossbow); hand flare items removed |
| **Tax intervals** | Hamlet/town **day intervals** from population when opening tax box; **co-owners** may open |
| **Orphan-raised stats** | Attachment + config; **no orphanage build** yet |
| **Village / kingdom map** | `village_map` and `kingdom_map` open atlas with owned hamlets + terrain |
| **Wanted posters** | Wall block, edit GUI, soldiers attack listed players in village |
| **Squad box** | GUI (gear + toggles), writ beds/ramparts, weekly auto-linked smith/fletcher/armorer supply |
| **Attack plan + charge** | Item/GUI, camp use, charge filter + plan target |
| **Caltrop AI avoid** | Friendly soldiers + seasoned+ enemies path around (`CaltropPathing`) |
## Still design-only or partial

| Area | Doc | Status |
| --- | --- | --- |
| Garland **prison arc** | `tbd-garland-fortress-prison-arena.md` | **TBD / not authored** |
| D2 **capture / guide / march** | `armies-questline.md` | Not started |
| **Orphanage** prefab at gen | `soldiers-and-villages.md` §9 | Stats only |
| **Recruiter villager POI + AI** | emerald bed purchase, tax routing, trades, combat radius | **Not in code** — squad box is block/tick only, not a villager job site |
| **Roaming war bands**, bandit alliance | `threats-and-mobs.md` | Deferred |
| **Siege kit** (ladders, sappers, boat plans) | `war-and-defense.md` | **Cut** from stages 1–4 |
| **Multi-piece** Garland NBT manifest | `garland-nbt-biome-and-terrain.md` | Single file paste per kind |
| **Kingdom table / cartographer charter** | `war-and-defense.md` | Kingdom map craft only (map + gold) |
| **Spawn tutorial camp NBT** | squad notes | Not started |
