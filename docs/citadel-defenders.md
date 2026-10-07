# Citadel defenders (Corrupted King garrison)

**§10:** **Three boss NPCs** + **citadel spawners** through the fortress (**breakable mid-assault**). Works **without Armies**. Difficulty = **boss kits**, **spawner types/places**, **layout** (funnels, traps, hazards), and **effect / counter-play** — see `citadel-layout.md` **Assault defensive mode**.

**Not a tank check:** the citadel should feel like a **wave of different problems** — blindness in a web hall, slowness on ice under archer fire, weakness before a ravager set-piece, poison + milk timing, lava under fire res pressure. HP and armor matter, but **tools, consumables, and tactics** matter as much.

---

## The three bosses

| Boss | Role |
| --- | --- |
| **Corrupted King** | Final fight; throne / keep. |
| **Bound necromancer A** | Mini-boss; fixed chamber. |
| **Bound necromancer B** | Mini-boss; fixed chamber. |

---

## Spawner mix (author — mockup tunes counts)

**Citadel spawner blocks** — **not** on the unbreakable list (except **Roost** phantom spawner — post-claim feature, `citadel-layout.md`). Once the seal is open, attackers **mine them like normal spawners** if they can reach them. Placement and **reachability** (overwatch, height, lava moats) do the guarding. Global/per-spawner caps **TBD**.

| Spawn type | Role (typical placement) |
| --- | --- |
| **Citadel undead** | Core “soldier” pressure — gate, keep (`citadel` custom mob when Armies absent **TBD**). |
| **Skeletons** | Wall walks, overwatch. |
| **Wither skeletons** | Bridge choke, slow + wither stack in funnels. |
| **Cave spiders** | Narrow trap corridors (poison in webs). |
| **Spiders** | Web zones, ceiling ambush **TBD**. |
| **Baby zombies** | Fast swarms in tight halls. |
| **Witches** | Splash / debuff behind chokepoints (blindness, slowness, poison, weakness, harming — **limited** spawners; stacks with dispenser traps). |
| **Ravagers** | **Few** — “siege beast” set pieces (vanilla ravager scale); not spammed. |

Placement + **defensive mode** hazard blocks (water, ice, lava lanes, webs) + **tripwire / pressure-plate potion & lava traps** (`citadel-layout.md`) do most of the work; modest stat tweaks **TBD**.

---

## Siege prep & effects (author intent)

**Bring a kit**, not just enchanted diamond:

| Category | Why |
| --- | --- |
| **Strong armor + food** | Baseline survival through long pushes and boss phases. |
| **Milk buckets** | Clears **witch + dispenser** debuffs; timing and stock matter (multiple segments back-to-back). |
| **Own potions** | Fire resistance (lava lanes), regeneration / healing, strength for burst windows, night vision in basements **TBD**, water breathing if water-lock segments exist. |
| **Blocks & movement tools** | Buckets, boats, blocks to bridge or block lava, pick for ice/webs/trap dismantling. |
| **Ranged weapon(s)** | **Required in practice** for key segments: gate gauntlet, wall walks, witch alcoves, courtyard overwatch — **melee-only groups should stall or wipe** unless they cheat sightlines (`citadel-layout.md`). Hard **mod gate** (e.g. cannot enter tower X without ranged) **optional TBD** mockup; default is **layout + spawner pressure**. |
| **Quest / relic tools** | Strong gear and undead-bonus weapons (`endgame.md`) — expected for serious attempts, not optional flavor. |

### Dispenser trap palette (splash + lingering)

Mix per corridor; avoid “all instant damage” repeats.

| Effect | Typical pairing |
| --- | --- |
| **Blindness** | Webs, tight turns, melee swarms — forces sound/memory or milk. |
| **Slowness** | Packed ice, waterfall push, archer lines — punishes panic retreat. |
| **Weakness** | Before undead rush or ravager room — shifts time-to-kill. |
| **Poison** | Stacks with cave spiders / lingering clouds. |
| **Instant damage / harming** | Spike pressure after debuffs, not the only tool. |
| **Nausea** | Optional disorient segments **TBD** sparingly. |

**Lava traps** pair with **fire res** planning or block placement; **milk does not** replace fire res for lava.

Mockup tags each trap segment with **intended counter** (milk, fire res, ranged clear, dismantle, rush) for playtest.

---

## Traps vs spawners (break rules)

| Piece | During assault (king alive) | After king + claim |
| --- | --- | --- |
| **Tripwire, plates, dispensers, exposed redstone** | **Breakable / removable** where reachable — many corridors can be **safed mid-push**. |
| **Garrison spawner blocks** | **Breakable** once seal open — valid mid-assault **sapper** play; silences that spawn source immediately. |
| **Static hazards** (webs, ice, water, lava lanes) | Mine/clear like normal blocks once in reach. |
| **Inner gates / ward doors** | **Closed** in defensive mode; **player** uses **levers** (post-claim setup) to open routes — not a global auto-open **TBD** mockup. |

---

## When spawners / traps run

| Phase | Spawners & traps |
| --- | --- |
| **Seal closed** | Off. |
| **Seal open, king alive** | Spawners **on** until broken; corridors + **potion/lava redstone traps** + hazards **live**; **main inner gate closed** (`citadel-layout.md`). Attackers may **dismantle traps** and **break spawners** they can reach. |
| **Both necromancers dead** | Rez stops per `endgame.md`; remaining spawners still run until **mined** or king milestone **TBD** (auto-off for boss-linked reserves **optional** mockup). |
| **King dead** | Any **leftover** garrison spawners **off** **TBD** vs player-cleared; traps/hazards **stay** until players strip or mine. |

---

## Difficulty intent

- **Solo:** multiple failures without strong gear, **consumables**, and quest tools — wrong kit (no milk, no ranged, no fire res for lava wing) should **lose to mechanics**, not raw DPS check.
- **Group/army:** roles help (archer clears overwatch, milk runner, trap dismantler) — still hard, not impossible.
- **No player-count scaling.**

---

## Beacon room

Guards only — beacon/pyramid **mineable** mid-siege once room cleared.

---

## Fight flow

1. Breach **outer** entrance (player-mined citadel doors).
2. Push **defensive mode** corridors (webs, water, ice, lava lanes, **tripwire/plate potion traps**, spawners) — **dismantle traps** and **break spawners** where reachable (tower saps, side rooms).
3. Kill **both necromancers** → reduce/ stop spawns.
4. **Corrupted King**; then claim / open inner ward.

---

## Armies (optional)

Player soldiers fight spawns; **cannot** break citadel doors.

---

## Mockup / playtest TBD

- Rez vs spawners-only; ravager count; witch cap; **boss-linked spawner auto-off** on king/necromancer death vs mine-only; default **lever** map for inner gates; tunnel during defensive mode; per-segment **effect + counter** tags; any **hard ranged** gate vs layout-only.

---

## Cross-links

- `endgame.md`, `citadel-layout.md`
