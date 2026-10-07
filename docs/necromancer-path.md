# Necromancer questline (player)

**Design in progress** — owner: you.

Pre–citadel optional **story arc** parallel to the Order (`acolyte-path.md`). Player-facing term is **necromancer** only (no separate “sorcerer” guild).

**Casual necromancy** (`necromancy.md` — wand, hearts, minions, stigma) is **not** the same as this questline. You can play as an active necromancer without ever committing; you can **opt out** of the quest and stay eligible for the Order.

Mechanical identity for committed players stays **`necromancy.md`**, plus quest **skills/tracks** (TBD). Commitment does not replace the power/weakness loop with generic mage fantasy.

## Design philosophy (vs Order — author note)

**Opposite of the acolyte path** (`acolyte-path.md`):

| | **Order (acolyte)** | **Necromancer path** (intake may say “sorcery”) |
| --- | --- | --- |
| **Shape** | **Work first** — basic, light, hard feats, escort; specialty **earned** through grind and masters. | **Decision first** — less upfront labor; the fork (**commit** / **opt out**) and living with the path matter more than long feat walls. |
| **Payoff** | **Reward after proof** — Maelor **locks** specialty; perks follow earned trust. | **Reward and weakness together** — commit brings power (quest, skills, story) **and** immediate, lasting costs (`necromancy.md`: hearts, stigma, no absolution, Order closed). |
| **Fantasy** | Brothers, Annals, reunite the Order. | The dark road finds you; you choose how deep to go. |

When we design necromancer chapters (§6+ in discussion plan), avoid mirroring acolyte **feat checklists** (every armor type, every enchant book). Committed path still **collects rare items**, but progress is **keep vs sacrifice** branches, not “complete the grid.”

## Relationship to other systems

| Topic | Rule |
| --- | --- |
| **Order acolyte path** | Stays available while intro quest is open or if you **opt out**. **Closes only on commit** (`acolyte-path.md`). |
| **Throne / citadel owner** | Unchanged — active necromancer sit rules, wand forfeit, relic ban, tree (`citadel-claim.md`, `necromancy.md`, `endgame.md`). |
| **Necromancer commitment** | End of **intro quest** — player chooses **commit**. **Absolution permanently unavailable.** Order closed. Main quest + skills unlock (TBD). |
| **Opt out** | End of **intro quest** — player refuses the binding. **Necromancer questline permanently unavailable.** Order stays open (through citadel claim at least). **Active necromancer** play and **absolution** still work under `necromancy.md`. |
| **Intro trigger** | When the player has lost **3 permanent hearts** to the wand (configurable threshold), a **necromancer NPC** seeks them out and offers the intro arc — **only if not near Order acolytes** (no refuge acolyte within **128** blocks default; no wand/hood/robe visit overlap). If the player is gear-clean at a refuge, intro waits until they leave Order space. **TBD:** ignoring the offer, repeat prompts, multiplayer. |
| **Timing** | Main arc emphasis **pre–citadel claim**; crypts, rogues, citadel pair (`endgame.md`, `threats-and-mobs.md`). |

## Intro arc → commit or opt out

```text
(Wand play anywhere — hearts, minions, optional absolution)
  → 3 permanent hearts lost (default) → necromancer NPC finds you → intro quest
  → final choice:
       COMMIT — questline continues, no absolution, Order closed, perks/skills (TBD)
       OPT OUT — questline locked out forever, Order still valid, casual necromancy unchanged
  → (if committed) main arc — mentor, citadel, brothers, endings (§6 below)
```

**Design intent:** heart loss proves you are not dabbling once; the NPC offer is the **moral fork**. Commit aligns with **no absolution** (life sentence). Opt out separates **story** from **mechanics** so Order players are not punished for a wand phase they walk back from.

---

## Main quest arc (§6 — in discussion)

**Scope:** committed path only. **Timing:** emphasis **before citadel claim**; citadel assault + **two bound necromancers** + Corrupted King are the late arc (`endgame.md`). **Not** an Order feat grid — progress via **story beats**, **unique items**, **sacrifices** (§7).

**Intake vs canon (keep separate until lore §3–5 merge):**

| Intake idea | Current canon / note |
| --- | --- |
| Immortal **founding brothers**, End as one land, mothers, dragon egg history | **`old-kingdom-lore.md`** — Hakon, Oswin, Harren; **§3–5 deferred** |
| Citadel **two bosses** = those brothers | **`endgame.md`** — two **Black Treaty advisers** who poisoned Maldric; **may merge** with brother myth in lore pass |
| Ending: **citadel → End**, overworld **crater + mega portal** | **Conflicts** post-claim citadel flow — **§6 decides** MVP vs full fantasy (`citadel-claim.md`) |
| Sorcerer **classes** (weather, animals, bard, …) | **Superseded** by **quest flutes + items** (§7); all committed share **`necromancy.md`** debits |

### Already decided (§1 + §7)

- **Intro:** **3 hearts** lost → NPC finds you (not near refuges) → **commit** or **opt out**.
- **World rogues:** common sites, shards, rare wild lich horcrux — **not** the main quest NPCs; **no** flute drops from rogues.
- **First story flute:** **Lament of Ash** from **intro mentor** after commit (**TBD** if mentor = same as intro NPC).

### Open — early / mid (§6)

1. **Intro NPC** — identity, name, where they spawn, repeat if ignored?
2. **Main mentor** — intake leans **second brother’s mother**; is she the intro NPC or a **later** guide? What does she want?
3. **Motivation to the citadel** — revenge, cure tree, steal power, Order rivalry, wild lich / Well lore?
4. **Citadel access** — great road + barrier like everyone, or **quest key** (item, NPC clearance, disguise)? Can committed necromancers **assault** while Order players also prep siege?
5. **Other quest necromancers** — besides **two bound** bosses + mentor, any **named** overworld NPCs (not rogues)? Village Chronicle hooks?
6. **Mid beats** — hunt **fixed flutes**, first **horcrux** ritual, god wizard armor, **when** player learns Well + compass?

### Open — citadel climax (§6)

7. **Assault role** — player must kill **both bound necromancers** then king (`endgame.md`); quest adds **dialogue / choice** before or after?
8. **Brother choice** (intake) — **misleading** good/bad; **both** require killing Corrupted King, **different reasons**; **good** brother **partners**; **bad** brother **betrays** later. **Which** map to the **two bound NPCs** (names TBD)?
9. **Mother** in finale — present at citadel, astral, cutscene only?

### Open — endings (§6)

10. **Good / bad branch outcomes** — partner + world state vs betrayal + ?
11. **Mega ending** (castle to End, crater portal) — **full release**, **epilogue only**, or **cut** for v1?
12. **Post-claim** — can committed necromancer **own throne**? (Existing throne rules say active necromancer constraints — **unchanged** unless §6 overrides.)

**Decision log:** add rows to `docs/notes/2026-10-05-discussion-plan.md` as §6 closes.

---

## Quest progression — items, flutes, sacrifices (§7 — author direction)

**Committed quest only** (not granted on opt out). **Not** Order-shaped citadel jobs (Smith / Brewer lecterns).

### Core loop (chosen over “pick a class”)

```text
Commit → hunt/craft **quest items** (wearables, flutes/pipes, reagents)
  → at forks: **keep** pieces separate OR **sacrifice** one (or two) to unlock later tiers
  → late: **cursed armor** + **horcrux** (flute lockout on horcrux **deferred** — may not apply)
```

- **Decisions, not a skill tree:** same intake themes (weather, beasts, bard, brew/enchant) can map to **different instruments and robes**, not four parallel grinds.
- **Weather / bard / brew** — **TBD** which become flutes vs robes vs one-off quest objects; author examples below are **animal/combat flutes** first.
- All items assume **committed** status and full **`necromancy.md`** penalties (hearts, stigma, relic ban on throne).

### Flutes and pipes (signature tools)

Quest **flutes** are **not** the **bone whistle** (`necromancy.md`). **Committed quest only.** **Flutes never hurt the player** who plays them — no self-damage from the effect; costs are cooldown / optional hearts on some (**TBD**), not suicide. Havoc flutes respect **`necromancerWorldHavocEnabled`**.

**World uniqueness (author — §7):** Each quest flute exists **once per world** at a **fixed site** (structure chest, mentor handoff, citadel vault, etc. — **TBD** per flute), like the **12 kings’ relics**: **cannot be destroyed** by lava, cactus, void, etc.; **never despawn**; void loss returns to last solid ground (**TBD** mirror relic rules). **Not** random **rogue necromancer** drops. The **only** way to **remove** a flute from the world is **consumption** in the **horcrux ritual** (hotbar shard + flute + valid kill) or **TBD** other quest sacrifices (e.g. god wizard craft) — same “gone for good” intent as feeding a relic into horcrux.

| Flute (v1 name) | Effect | How you get it (author — **TBD** placement) |
| --- | --- | --- |
| **Beast Dirge** | In **loaded chunks** near you, **50%** of eligible **living mobs** (passive/neutral animals; **TBD** villagers/soldiers) get **blight/poison** for **4–12 hearts** damage (rolled per target). Works **with no enemies nearby**. **Never** damages the flutist. Strong farm grief in those chunks; not server-wide. | **One** per world — **fixed location** (**TBD** which structure/quest). |
| **Hivepipe** | ~**100 bees** guard you briefly. | **One** per world — **fixed location** (**TBD**). |
| **Bone Choir** | **5 wither skeletons** guard you briefly. | **One** per world — **fixed location** (**TBD**). |
| **Gale Flute** | **Weather:** thunder + lightning strikes in a wide area around you (**TBD** radius/cooldown). | **One** per world — **fixed location** (**TBD**). |
| **Lament of Ash** | **Bard:** soldiers/militia in radius get **Fear** — flee or hesitate (**TBD** Armies hook). | **One** per world — **intro mentor** after **commit** (**TBD** if also a physical stash elsewhere). |
| **Crucible Whistle** | **Brew:** splashes **harmful potion cloud** at your feet (mixed debuffs). | **One** per world — **fixed location** (**TBD**; citadel alchemy wing candidate). |

**God wizard armor** (four cursed netherite pieces): **craft ritual** **TBD** — likely **sacrifice two different flutes** + netherite set + tree/necro reagent at mentor/station.

### Exclusivity and sacrifice

- Some flutes are **exclusive** — you can only own **one** of a tier, or two that **cannot coexist** in inventory (**TBD** enforcement: craft fails, or previous item crumbles).
- **Advanced crafts** (quest **armor/robes**, **horcrux** base) **consume** one or two flutes (or other quest pieces) at a **ritual** — same rules as horcrux: only deliberate ritual removal, not environmental destruction.
- **Horcrux vs flutes:** whether horcrux **locks out** flutes or other quest gear **deferred** — may **not** lock flutes at all; sacrifice forks stay for **craft recipes** only.

### Havoc, hunting, and loot (author intent)

- Top necromancer tools are **meant to be dangerous in the world** — mass animal death, large summons, etc. — so **other players want to hunt** committed necromancers.
- **Horcrux** is the main hunt target (**hidden chest**). **Flutes** and **god wizard armor** are **stealable** but **indestructible** except **ritual consumption** (horcrux or **TBD** craft); pseudo-death keeps bound armor on.
- **Config:** **`necromancerWorldHavocEnabled`** (default **true**) — when **false**, disables or clamps the worst cross-player effects (e.g. beast dirge mass kill, chunk-wide summons **TBD** per item). Casual wand play unchanged.

### Cursed wearables

Quest **cursed netherite — full four pieces** (helmet, chest, legs, boots). Author name: **god wizard armor** (display name **TBD**). Real **netherite armor** with quest enchants/attributes — **not** a robe reskin. Hood/robe (`necromancy.md`) stays separate casual gear. **Upside = combat power; downside = self-rot** — horcrux handles **poison/harm pots + fake death**.

**Indestructible (author):** Same durability rules as **kings’ relics** and quest **flutes** — no lava/cactus/void destruction, never despawn, void return **TBD** like relics. **Only** leaves the world when **consumed** in a ritual: **horcrux creation** (full set counts as **one item of power** in hotbar — **TBD** UI) and **TBD** other quest forks. Still **binding** while worn.

#### Good parts (author — numbers TBD config)

| Benefit | Intent |
| --- | --- |
| **Armor value** | **Netherite base** + quest **armor/toughness** above normal cap (proposed ~**2×** effective vs fully enchanted netherite **TBD** playtest). **All four pieces** required for full bonus; partial wear = partial stats **TBD**. |
| **Damage reduction** | Extra **flat %** cut after armor (proposed **~25–35%** — **TBD**). |
| **Knockback immunity** | **Full knockback resist** while **full set** equipped. |
| **Melee punish** | Attackers take **strong thorns-style** damage + brief **Wither I** (**TBD** numbers, PvP cap). |
| **Status shrug** | **Slowness / Weakness / Blindness** from pots & traps reduced or blocked (**not** horcrux poison/harm immunity). |
| **No burning** | **Fire Resistance** while **full set** equipped — no fire/lava burn (god wizard armor). |
| **Melee power** | **+25% melee damage** (sword, axe, mace — **TBD** trident) while **full set** equipped (config `necromancerGodWizardMeleeBonus` default **0.25**). |

**Not** on the armor: poison/harm immunity and pseudo-death stay **horcrux-only**.

#### Costs (while worn)

- **Self-DoT:** steady damage **just below** what **Regen potion + golden food** can outheal — **short** fights without horcrux; **long** fights need horcrux + upkeep.
- **Stigma visible:** hood/robe rules stack; full cursed netherite reads as **lich** on sight (**TBD** extra villager/soldier penalties).
- **Binding (yes):** **cannot manually unequip** any piece of the cursed set. **Pseudo-death keeps the set on.** **True death** drops the set like normal gear (pieces stay **indestructible** on ground). Plan around **3-minute click lockout** in full kit.

### Horcrux (quest capstone — see `necromancy.md`)

**Many allowed** — each creation costs **−1 permanent heart**; stop when you hit the **1-heart floor** (same as wand). Not a flute; built from a **phylactery shard** + ritual kill.

#### Creation ritual (all at once)

1. **Hotbar** must contain: **one phylactery shard** + **one item of power** — **any quest flute**, **full god wizard armor set** (all four pieces — consumed as one ritual), **or** **any of the 12 kings’ relics** (`endgame.md`). The power item is **consumed** on success (relic gone from world unless you recover another copy — relics are normally unique).
2. While those stay in the hotbar, the player **kills** one valid **soul** target (**same moment** — not a past kill):
   - an **acolyte** (NPC Order brother or player on Order path — **TBD** rules),
   - **any player**,
   - a **defined necromancer NPC** — includes **wild lich** rogue necromancers (`necromancy.md`) and later **named** bosses (citadel pair, etc.; full roster **TBD**).
3. On success, the shard + power item combine into a **horcrux item** (**consumes** hotbar reagents — **TBD** exact recipe UI).

#### Wild liches — tutorial for everyone

**Term — wild lich:** mod/docs name for a **rogue necromancer** who hid **one horcrux** (item in a **hidden chest** at their site) — **very rare**; almost all rogues have **none**. Not a player; not the Corrupted King. Counts as a **soul** for player horcrux craft when killed.

#### Rogue necromancer sites (author — §7)

- **Uncommon–common:** **~half** as many sites as **pillager outposts** (~**2×** outpost spacing — config `necromancerRogueSiteSpacing`). Still regular exploration content, not a rare dungeon.
- **Placement mix:** **80%** of rogue sites are **above ground** — either a **mod custom structure** (necromancer outpost, ruined chapel, etc. **TBD** list) **or** an **existing overworld structure** retrofitted (e.g. **pillager outpost**, **woodland mansion**, **TBD** igloo/bastion variants). **20%** stay **underground crypts** (current crypt fantasy).
- **Rogue horcrux spawn (author):** **most rogues have no horcrux.** **~4%** (`necromancerRogueHorcruxChance`) spawn with **at most one** horcrux in a hidden chest — **never** two. Horcrux sites are **jackpots**, not every camp.
- **When you meet one:** same rules as player horcruxes — chunk pings, pseudo-death until the **horcrux** is destroyed, then the rogue can die for real.
- **Learn destroy:** Wellkeeper / lore + **optional** encounter — find chunk → chest → **unmake horcrux** (Well, wither, lightning, etc.) — **not** forced before commit.
- **Learn create:** **shards + power item + kill** on soul list; docs and loot explain player horcrux — wild lich is **optional** preview.

**Creation costs:**

- **−1 permanent max heart** per horcrux (cannot create at **1 heart**).

#### Where it lives (hunt gameplay)

- Each horcrux **must be stored in a chest** (anywhere on the map). Owner need not carry them.
- While **at least one** horcrux exists, **passives apply** (**TBD:** whether destroying horcruxes one-by-one **steps down** immortality or **all** must go — see **Destroying a horcrux** below).
- **Horcrux “ping” (author — §7):** **not** a chat waypoint or compass to the block. Hunters read the **world** — dark particles, sick animals, optional soldier corruption (below). Applies to **that lich’s** horcrux(es) only (UUID-tagged). **Config:** `necromancerHorcruxPingEnabled` **TBD**.

| Tier | When (chunk loaded) | What you see / feel |
| --- | --- | --- |
| **Near** | Hunter within a **circle** of **10 chunks** radius (Euclidean on chunk centers — config `necromancerHorcruxPingRadiusChunks`) of any chunk that **contains** one of the lich’s horcrux chests | **Dark air:** **soul/smoke/wither** particles at head height — stronger toward the horcrux chunk, **no** block highlight, **no** ground effects. Optional low **ambient sound** (wind, whisper) **TBD**. |
| **In chunk** | Hunter standing in a chunk that **contains** a horcrux | **Heavy blight:** denser **airborne** dark particles only (**no ground ash**). **Animals** in that chunk (passive + tameable) show **poison** (particles, reduced HP / erratic movement) — **does not** mass-kill farms instantly; confirms “this chunk,” **not** the chest block. |
| **Find chest** | Same chunk | Manual search, x-ray-proof hides, detector item **TBD** (Order / late quest). |

- **Same chunk ≠ same block:** ping confirms **chunk only**; multiple horcruxes in one chunk still mean “dig/search here.”
- **Unloaded chunks:** no particle ping until someone loads the area.
- **Horcrux compass (author — craftable):** points toward the **chunk** (X/Z) of the **nearest horcrux item** in the world (player chest-stored **or** rogue wild-lich chest). **Chunk only** — no block Y, no exact chest; needle toward **chunk center** (range **TBD**). Does **not** replace in-chunk digging / environmental ping.
  - **Early vs late world (author intent):** With **little exploration**, few wild lich horcruxes exist — the needle often aims at the **lich you care about** (or the only rogue horcrux nearby). As the map **fills in**, **~4%** rogue horcruxes accumulate in generated chunks — the needle keeps snapping to **closer wild chests**, so the compass **helps less** for hunting a **distant player lich** unless hunters clear wild noise or use ping / theft / intel. **By design**, not a late-game “find any hideout anywhere” win button.
  - **Crafting:** **Multiple compasses** per world — normal recipe, **not** unique, **not** single-use (unlike relic compass). Party can carry several. **Recipe:** shapeless on crafting table — **compass**, **phylactery shard**, **soul sand**, **wither rose** → 1 horcrux compass (`necromancy.md`). **Optional config:** ignore **your own** horcruxes when hunting another lich (`necromancerHorcruxCompassExcludeSelf` **TBD**).
- **Owner:** may see **muted** or **no** ping on their own horcruxes (**TBD** — avoid self-GPS).
- **Corruption overlap:** **weak soldiers** in pinged chunks may get separate debuffs (see below) — same “blight” fantasy.

#### Destroying a horcrux (author — §7)

Horcrux items are **not** unmade in a crafting grid or by normal player fire. Destruction is **deliberately hard**; the **usual** hunter finish is hauling the **horcrux** to the **Dragon’s Well** and **throwing it in** (`endgame.md`).

**Rules (chosen direction):**

- Horcrux **drops from chests** like any item when the chest breaks; **mining the item entity** or **player inventory delete** does **not** destroy it (item **immune** to despawn/pickup abuse **TBD** — e.g. always drops on break, cannot `/kill` item away).
- The **horcrux item** (on ground, in item frame, or on **lightning rod** / block **TBD**) must be destroyed by **vanilla** sources only — **v1 whitelist (author):**

| Destroyer | How hunters use it |
| --- | --- |
| **Dragon’s Well (primary)** | **Throw / drop** the **horcrux item** into the well (**TBD** interaction). **Unmakes** it **anytime** (dragon alive or dead). Hunt loop: find chunk → steal horcrux → **road trip to citadel** → dunk. |
| **Ghast fireball** | Nether stash; fireball hits item entity. |
| **Wither** (boss or **skull projectile**) | Summon fight over the horcrux when you can’t reach the well. |
| **Lightning** | During **thunder**: **Channeling** trident on/near the item **or** lightning strikes a **lightning rod** the horcrux is bound to. |
| **Dragon’s breath bottle** | **Optional alternate** — splash bottle on the horcrux on the ground (**TBD**); same well lore, for hunters who already have breath but can’t move the item. |

- **Not** trivial: no crafting-grid delete, no lava bucket, no normal fire (**TBD** edge cases).
- **Config:** `necromancerHorcruxDestroyers` — default the four rows above; servers can add/remove (e.g. charged creeper **off** by default).

**When one horcrux breaks:** **TBD** — default lean **all horcruxes must be destroyed** before true death.

**Stealing:** horcrux stays tied to **creator**; thieves **carry it to the Dragon’s Well** (ideal) or use **wither/ghast/lightning** where it lies.

#### What it does (author — §7)

While **≥1 horcrux** exists for you:

| Effect | Behavior |
| --- | --- |
| **Poison** | **Immune** |
| **Damage / harming potions** | **Immune** (includes splash/lingering harm — **TBD** edge cases) |
| **Would-be death** | Player **does not die**. **3 minutes:** no **left/right click**; **can move**; **keeps inventory**. **No teleport** on trigger — you stay where you fell. |
| **True kill** | **All horcruxes destroyed** (**TBD** if partial destruction matters) → normal death rules. |

**Config:** servers may disable horcrux passives (`necromancerHorcruxEnabled` — **TBD** in `config-and-recipes.md`) for low-PvP worlds; default **true** for author intent.

**Flute lockout** on horcrux still **deferred**.

**Horcrux blight on soldiers (author suggestion — **TBD**):** In the **10-chunk ping circle** (and stronger **in the horcrux chunk**), **enemy** **militia and plain soldiers** (not heroes/legends) get **Weakness I** and **+50% desertion/morale check** while in the zone. **Poisoned animals** use the same chunk. **Owner’s own** soldiers unaffected. No mind control — they **break and run** easier, matching “weak minds corrupted.”

### Balance vs Order (plain language)

Order acolytes who **unlock the tree** get **major** perks at **royal stations** — e.g. **Strength III / Speed III / Regen III** brews at **3× duration**, or **Sharpness VII**-tier enchants, cheap netherite reforge (`acolyte-path.md`, `endgame.md`). That is **steady, repeatable** kingdom infrastructure.

Necromancer quest items can feel **spikier** and **world-altering** (100 bees, 5 wither skeletons, mass animal sacrifice) — **tradeoffs** (sacrificed crafts, self-damage armor, ritual murder for horcrux, hearts, stigma, hunt-me loot, no citadel king relics). Not a straight upgrade to Order **major** station buffs in every scenario. Exact tuning **TBD** playtest.

### Superseded (discussion only)

Earlier draft **Model A** (one primary affinity + one cross-track) — replaced by **item lattice + sacrifice** unless we re-merge themes as **flute families** later.

### Progress gates (§7)

Not Order feat walls. Gates = **find item**, **choose sacrifice**, **survive ritual** — optional achievement-style checks **TBD** per item tier.

## Sections still to fill

- **§6** — see **Main quest arc** section above; decide early/mid/climax/endings in order.
- **Who / when** — soldiers near cult sites?, claim timing, committed vs opt-out on throne/relics (both still active necromancer rules if using wand).
- **NPCs** — roster; **two named bound necromancers** in citadel (`endgame.md`, names TBD).
- **Feat gates** — achievement-style gates (TBD; parallel to Order discussion).
- **Config keys** — e.g. `necromancerQuestHeartThreshold` default **3**, `necromancerIntroMinDistanceFromAcolyte` default **128**, `necromancerWorldHavocEnabled` default **true** (`config-and-recipes.md`), intro NPC cooldown (TBD).
- **Multi-horcrux** pseudo-death rule (**TBD**).
- **Soul-target roster** (full necromancer NPC list **TBD**).
- **Ping:** particle types, animal poison strength (**TBD** config). Radius = **10-chunk circle** (set).
- **Horcrux compass** — range, self-exclude (**TBD**).
- **Cursed netherite** final numbers, partial-set rules (**TBD**).
- **Binding cursed armor** — **yes**; **indestructible** + horcrux-sacrifice — documented above.
- **Multi-horcrux:** all must break vs consume-on-pseudo-death (**TBD**). **No** pseudo-death teleport — **decided**.
- **Horcrux item placement** — on rod vs frame vs ground for lightning/breath hit tests (**TBD**).
- **Rogue site spacing** and **80/20** surface/crypt split (**TBD** structure list).
- **Rogue horcrux spawn rate** — default **~4%** (`config-and-recipes.md`).
- **Pseudo-death** interaction with soldiers, throne sit, dungeon (**TBD**).

## Lore language

Historical chronicles may use older words (e.g. “sorcerer”); **player systems use necromancer**. Intake draft: `docs/notes/2026-10-05-design-notes-intake.md`.

## Existing mechanics (reference)

- Hood, robe, wand, minions, absolution (until commit), penalties — `necromancy.md`
- Citadel throne, barrier, relic ban — `citadel-claim.md`, `endgame.md`, `mod-split.md`

## Marketing one-liner (draft)

“Three hearts gone, something finds you — swear the dark road or walk away and leave the Order your path.”
