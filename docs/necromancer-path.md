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
  → (if committed) main arc — mentor, citadel, brothers, endings (TBD)
```

**Design intent:** heart loss proves you are not dabbling once; the NPC offer is the **moral fork**. Commit aligns with **no absolution** (life sentence). Opt out separates **story** from **mechanics** so Order players are not punished for a wand phase they walk back from.

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

Quest **flutes** (or pipes — one item family, name **TBD**) are **not** the existing **bone whistle** (`necromancy.md` — wolves, phylactery craft). Committed path grants **separate**, stronger instruments with **mutually exclusive or sacrificial** recipes.

**Author example effects** (balance TBD — **loaded-chunk** scope, cooldowns; **world havoc enabled by default**, see config):

| Instrument (draft) | Effect (draft) |
| --- | --- |
| **Beast dirge** (name TBD) | Calls **all tamable/neutral animals** in **loaded chunks** to **charge and die** — intentional **ecosystem / farm havoc**; server owners can disable via config. |
| **Hive pipe** (name TBD) | Summons **~100 bees** as temporary bodyguard (**duration / once per day TBD**). |
| **Bone choir** (name TBD) | Summons **5 wither skeletons** as temporary guards (**duration / cap TBD**). |

More flutes **TBD** (storm, fear/dirge debuff on soldiers, etc.). Each use should cost something (charges, hearts, stigma moment, or cooldown) so they are not spammable raid buttons.

### Exclusivity and sacrifice

- Some flutes are **exclusive** — you can only own **one** of a tier, or two that **cannot coexist** in inventory (**TBD** enforcement: craft fails, or previous item crumbles).
- **Advanced crafts** (quest **armor/robes**, **horcrux** base) require **destroying** one or two flutes (or other quest pieces) at a **ritual station / NPC** — sacrificed items **gone for good** in that world.
- **Horcrux vs flutes:** whether horcrux **locks out** flutes or other quest gear **deferred** — may **not** lock flutes at all; sacrifice forks stay for **craft recipes** only.

### Havoc, hunting, and loot (author intent)

- Top necromancer tools are **meant to be dangerous in the world** — mass animal death, large summons, etc. — so **other players want to hunt** committed necromancers.
- **Horcrux** is the main hunt target (**hidden chest**). **Flutes** and **cursed armor** remain **stealable / destroyable** on pseudo-death or normal death rules **TBD**.
- **Config:** **`necromancerWorldHavocEnabled`** (default **true**) — when **false**, disables or clamps the worst cross-player effects (e.g. beast dirge mass kill, chunk-wide summons **TBD** per item). Casual wand play unchanged.

### Cursed wearables

- **Extremely tanky** necromancer **armor or robes** (quest tier above hood/robe) plus **slow self-damage** while worn.
- **DoT tuning:** damage ticks **slightly slower** than **regeneration potions + food** can heal — so a player **without** a horcrux can wear it for **short** windows (burst fights), not indefinitely.
- **With horcrux:** **poison** and **harm/damage potions** do not affect the owner (`necromancy.md` horcrux passives) — that immunity (not the chest item “soaking” armor) is what makes **long** cursed-armor wear viable.
- **Binding (TBD yes/no):** cannot **manually unequip** (inventory/armor slot swap blocked). **Pseudo-death does not strip it.** Only **true death** lets it come off (normal death drops / corpse rules — armor does **not** “vanish” on death). Plan around **3-minute lockout** while still wearing cursed armor.

### Horcrux (quest capstone — see `necromancy.md`)

**Many allowed** — each creation costs **−1 permanent heart**; stop when you hit the **1-heart floor** (same as wand). Not a flute; built from a **phylactery shard** + ritual kill.

#### Creation ritual (all at once)

1. **Hotbar** must contain: **one phylactery shard** + **one named item of power** from a fixed mod list (**list TBD** — tree/necro quest drops, flutes, etc.).
2. While those stay in the hotbar, the player **kills** one valid **soul** target (**same moment** — not a past kill):
   - an **acolyte** (NPC Order brother or player on Order path — **TBD** rules),
   - **any player**,
   - a **defined necromancer NPC** — includes **wild lich** rogue necromancers from **necromancer crypts** (`necromancy.md`) and later **named** bosses (citadel pair, etc.; full roster **TBD**).
3. On success, the shard + power item combine into a **horcrux item** (**consumes** hotbar reagents — **TBD** exact recipe UI).

#### Wild liches — tutorial for everyone

**Term — wild lich:** mod/docs name for a **rogue necromancer NPC** who has made a **horcrux** (stored in a **crypt chest**, not player character, not citadel boss). Teaches jar hunt + destroy; counts as a **soul** for player horcrux craft when killed.

**Some** crypt rogues spawn as wild liches. Same rules as player jars: chunk pings, pseudo-death for the NPC, vanilla-hard destroy, then the rogue can be **killed for real**.

- **Destroy intro:** players learn **find chunk → find chest → unmake jar** (lightning/wither/ghast anytime; **Dragon’s Well** anytime — **before or after** Ender Dragon death) **before** any player commits to the quest.
- **Create intro:** crypt loot + **Wellkeeper’s Account** / **TBD** handout explain that **shards + power item + kill on the soul list** makes a player horcrux; watching a wild lich **survive once** shows pseudo-death.
- **Ratio / placement:** **TBD** (proposed: **subset** of crypts + one **early** lich within ~**512** blocks of a common path so most worlds see the loop pre-citadel).

**Creation costs:**

- **−1 permanent max heart** per horcrux (cannot create at **1 heart**).

#### Where it lives (hunt gameplay)

- Each horcrux **must be stored in a chest** (anywhere on the map). Owner need not carry them.
- While **at least one** horcrux exists, **passives apply** (**TBD:** whether destroying jars one-by-one **steps down** immortality or **all** must go — see **Destroying a horcrux** below).
- **Detection tiers (author):**
  1. **Near a loaded chunk** that contains one of the lich’s horcruxes → hunter knows **a horcrux is in a nearby loaded chunk** (not which chunk if several).
  2. **Inside the correct chunk** → hunter knows **it’s in this chunk** but **not** the block/chest (**no** exact waypoint).
  3. **Find the chest** by search, x-ray-proof hiding, detector items **TBD**.

**Off-map / unloaded:** no “in this chunk” ping until the chunk loads; long-range hints **TBD** (Order tool, scent quest, etc.) — at most **chunk coordinates**, not block.

#### Destroying a horcrux (author — §7)

Horcrux items are **not** unmade in a crafting grid or by normal player fire. Destruction is **deliberately hard**; the **usual** hunter finish is hauling the jar to the **Dragon’s Well** and **throwing it in** (`endgame.md`).

**Rules (chosen direction):**

- Horcrux **drops from chests** like any item when the chest breaks; **mining the item entity** or **player inventory delete** does **not** destroy it (item **immune** to despawn/pickup abuse **TBD** — e.g. always drops on break, cannot `/kill` item away).
- The **horcrux item** (on ground, in item frame, or on **lightning rod** / block **TBD**) must be destroyed by **vanilla** sources only — **v1 whitelist (author):**

| Destroyer | How hunters use it |
| --- | --- |
| **Dragon’s Well (primary)** | **Throw / drop** the horcrux **into the well** (**TBD** interaction). **Unmakes** the jar **anytime** (dragon alive or dead). Same landmark as breath bottles (`endgame.md`). Hunt loop: find chunk → steal jar → **road trip to citadel** → dunk. Well stays **hard to find** without lore. |
| **Ghast fireball** | Nether stash; fireball hits item entity. |
| **Wither** (boss or **skull projectile**) | Summon fight over the jar when you can’t reach the well. |
| **Lightning** | During **thunder**: **Channeling** trident on/near the item **or** lightning strikes a **lightning rod** the horcrux is bound to. |
| **Dragon’s breath bottle** | **Optional alternate** — splash bottle on the horcrux on the ground (**TBD**); same well lore, for hunters who already have breath but can’t move the item. |

- **Not** trivial: no crafting-grid delete, no lava bucket, no normal fire (**TBD** edge cases).
- **Config:** `necromancerHorcruxDestroyers` — default the four rows above; servers can add/remove (e.g. charged creeper **off** by default).

**When one jar breaks:** **TBD** — (a) **all jars must be destroyed** before true death; (b) each jar consumed on pseudo-death; (c) hybrid with teleport-to-nearest (**TBD**).

**Stealing:** jar stays tied to **creator**; thieves **carry it to the Dragon’s Well** (ideal) or use **wither/ghast/lightning** where the jar is stuck.

#### What it does (author — §7)

While **≥1 horcrux** exists for you:

| Effect | Behavior |
| --- | --- |
| **Poison** | **Immune** |
| **Damage / harming potions** | **Immune** (includes splash/lingering harm — **TBD** edge cases) |
| **Would-be death** | Player **does not die**. **3 minutes:** no **left/right click**; **can move**; **keeps inventory**. **Consider:** also **teleport to the nearest horcrux** (chunk/chest location) on trigger — **TBD** (helps hunters, exposes hideouts). |
| **True kill** | **All horcruxes destroyed** (**TBD** if partial destruction matters) → normal death rules. |

**Config:** servers may disable horcrux passives (`necromancerHorcruxEnabled` — **TBD** in `config-and-recipes.md`) for low-PvP worlds; default **true** for author intent.

**Flute lockout** on horcrux still **deferred**.

**Consider (author):** horcruxes **corrupt weak minds nearby** — e.g. **soldiers**, low-rank militia, or **TBD** villagers — while the jar exists (range, chunk, or kingdom-wide **TBD**). Possible outcomes: desertion toward necromancer, fear debuff, minion-adjacent behavior, mutiny pressure (`soldiers-and-villages.md`). **Not designed yet** — flag for §7 / Armies pass.

### Balance vs Order (plain language)

Order acolytes who **unlock the tree** get **major** perks at **royal stations** — e.g. **Strength III / Speed III / Regen III** brews at **3× duration**, or **Sharpness VII**-tier enchants, cheap netherite reforge (`acolyte-path.md`, `endgame.md`). That is **steady, repeatable** kingdom infrastructure.

Necromancer quest items can feel **spikier** and **world-altering** (100 bees, 5 wither skeletons, mass animal sacrifice) — **tradeoffs** (sacrificed crafts, self-damage armor, ritual murder for horcrux, hearts, stigma, hunt-me loot, no citadel king relics). Not a straight upgrade to Order **major** station buffs in every scenario. Exact tuning **TBD** playtest.

### Superseded (discussion only)

Earlier draft **Model A** (one primary affinity + one cross-track) — replaced by **item lattice + sacrifice** unless we re-merge themes as **flute families** later.

### Progress gates (§7)

Not Order feat walls. Gates = **find item**, **choose sacrifice**, **survive ritual** — optional achievement-style checks **TBD** per item tier.

## Sections still to fill

- **Intro quest** — steps, NPC identity, commit/opt-out scene, what happens if player is already deep in Order training.
- **Main quest flow** — early game, mentor, citadel access, two brothers choice, endings (e.g. citadel to End — intake TBD).
- **Who / when** — soldiers near cult sites?, claim timing, committed vs opt-out on throne/relics (both still active necromancer rules if using wand).
- **NPCs** — roster; **two named bound necromancers** in citadel (`endgame.md`, names TBD).
- **Feat gates** — achievement-style gates (TBD; parallel to Order discussion).
- **Config keys** — e.g. `necromancerQuestHeartThreshold` default **3**, `necromancerIntroMinDistanceFromAcolyte` default **128**, `necromancerWorldHavocEnabled` default **true** (`config-and-recipes.md`), intro NPC cooldown (TBD).
- **Horcrux power-item list** + **soul-target roster** (**TBD**).
- **Chunk find mechanics** + **off-chunk hint** item/quest (**TBD**).
- **Binding cursed armor** — yes/no (**TBD**).
- **Multi-jar:** all must break vs consume-on-pseudo-death; **fatal teleport** to nearest jar (**TBD**).
- **Horcrux item placement** — on rod vs frame vs ground for lightning/breath hit tests (**TBD**).
- **Horcrux corruption** — weak soldiers / NPCs near active jars (**consider**).
- **Pseudo-death** interaction with soldiers, throne sit, dungeon (**TBD**).

## Lore language

Historical chronicles may use older words (e.g. “sorcerer”); **player systems use necromancer**. Intake draft: `docs/notes/2026-10-05-design-notes-intake.md`.

## Existing mechanics (reference)

- Hood, robe, wand, minions, absolution (until commit), penalties — `necromancy.md`
- Citadel throne, barrier, relic ban — `citadel-claim.md`, `endgame.md`, `mod-split.md`

## Marketing one-liner (draft)

“Three hearts gone, something finds you — swear the dark road or walk away and leave the Order your path.”
