# Armies mod — code vs design (living gap list)

**Not exhaustive** — tracks major doc promises vs JAR. Update when closing gaps.

## Implemented in code (recent)

| Feature | Notes |
| --- | --- |
| **Natural village militia** | On chunk load + login chunk sweep; bell + ≥3 villagers, no mess in radius; config counts + citadel-distance tiers |
| **Bandit structure paste** | `BanditStructurePaste` loads `data/villagers/structures/{camp_generic,hideout_cave,corvin_tower,garland_compound}.nbt` if present |
| **O2 without compass** | Right-click **torn map** → bearing + `O2_ACTIVE`; compass recipe removed from datagen |
| **Allies (MVP)** | `/villagers ally add|remove|list`; village build/explosion bypass for allies |
| **Orphan-raised stats** | Attachment + config HP/damage/desertion; **no orphanage build** yet |

## Still design-only or partial

| Area | Doc | Status |
| --- | --- | --- |
| Garland **prison arc** | `tbd-garland-fortress-prison-arena.md` | Not started |
| D2 **capture / guide / march** | `armies-questline.md` | Not started |
| **Orphanage** prefab at gen | `soldiers-and-villages.md` §9 | Stats only |
| **Wanted posters**, ally book screen | `soldiers-and-villages.md` | Commands only for allies |
| **Co-owner** list | `soldiers-and-villages.md` | Owner UUID only |
| **Recruiter POI**, tax tiers | `stages-1-4-completion.md` | Partial |
| **Roaming war bands**, bandit alliance | `threats-and-mobs.md` | Deferred |
| Horns, flares, caltrop AI avoid, siege kit | various | Partial / cut |
| **Multi-piece** Garland NBT manifest | `garland-nbt-biome-and-terrain.md` | Paste single file per kind today |
