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
| **Payoff** | **Reward after proof** — Maelor **locks** specialty; perks follow earned trust. | **Reward and weakness together** — commit brings power (quest, skills, story) **and** costs (`necromancy.md`: hearts, stigma, Order closed); **horcrux** closes absolution. |
| **Fantasy** | Brothers, Annals, reunite the Order. | The dark road finds you; you choose how deep to go. |

When we design necromancer chapters (§6+ in discussion plan), avoid mirroring acolyte **feat checklists** (every armor type, every enchant book). Committed path still **collects rare items**, but progress is **keep vs sacrifice** branches, not “complete the grid.”

## Relationship to other systems

| Topic | Rule |
| --- | --- |
| **Order acolyte path** | Stays available while intro quest is open or if you **opt out**. **Closes only on commit** (`acolyte-path.md`). |
| **Throne / citadel owner** | Unchanged — active necromancer sit rules, wand forfeit, relic ban, tree (`citadel-claim.md`, `necromancy.md`, `endgame.md`). |
| **Necromancer commitment (Well)** | End of **intro quest** — player chooses **commit**. Order closed. Main quest + skills unlock (TBD). Does **not** block absolution. |
| **Necromancer life (horcrux)** | **First player horcrux** — absolution **forever** closed; destroying horcruxes does not reopen it (`necromancy.md`). This is the real **no turning back** beat. |
| **Opt out** | End of **intro quest** — player refuses the binding. **Necromancer questline permanently unavailable.** Order stays open (through citadel claim at least). **Active necromancer** play and **absolution** still work under `necromancy.md`. |
| **Intro trigger** | When the player has lost **3 permanent hearts** to the wand (`necromancerQuestHeartThreshold`), the **intro NPC** **always** contacts them — **no refuge distance gate**. **TBD:** multiplayer which player gets the scene. |
| **Timing** | Main arc emphasis **pre–citadel claim**; crypts, rogues, citadel pair (`endgame.md`, `threats-and-mobs.md`). |

## Intro arc → commit or opt out

```text
(Wand play — hearts, minions; absolution until **first horcrux**)
  → 3 permanent hearts lost
  → Mother (mysterious) appears in a lightning storm — always, anywhere
       (if a refuge acolyte is in sight: lightning **kills** him — **tombstone**: *She smote him.*)
  → Talk: dark arts need guidance; she does not name herself
  → Quest start: **intro chestplate rogue** — largest town ~350b from citadel; **great road runs through it**; horcrux **chunks away**
  → Return: meet her at **Dragon's Well** (hidden copse) — any time
  → **No plate:** she **chastises** you and tells you to **go get the chestplate** (quest unchanged).
  → **With plate:** she **takes the chestplate**; sends you to gather **seal ingredients** (shopping list — no “Seal-Breaker” name)
  → Return with ingredients → **commit warning** → hand over = **commit** (or **opt out** — **TBD** if refusal before hand-in)
  → **On commit:** **chestplate** + **Seal-Breaker**; lie: **son** (evil **second brother**) is innocent → **open seal**, kill **king + first brother**, **spare son**
  → Main arc — citadel, brothers, endings (§6 below)
```

**Design intent:** heart loss proves you are not dabbling; the **Well meeting** teaches **horcrux disposal** and pulls you to the **hidden well** past the citadel. **Commit** closes the Order (`necromancy.md`); **first horcrux** closes absolution.

### Intro NPC — the mother (author §6)

- **Identity:** the **second brother’s mother** in lore (**§3–5 TBD**); to the player she is **anonymous** — hood, storm, no name until much later (**display name TBD**, e.g. “Stranger in the rain”).
- **Arrival:** **lightning storm** wraps her spawn; she **always** appears once the heart threshold is met (refuge proximity **does not** block).
- **Intro lightning smite (not the Smite enchant):** during **this cutscene only**, if any **refuge acolyte** is **in the player’s line of sight**, the storm **kills** him with lightning. A **tombstone** (grave marker or quest block **TBD**) replaces the body, inscribed: **“She smote him.”** That brother is **gone for the world** — permanent loss for Order players who had him in sight during intro.
- **Opening line (paraphrase):** notices your dark arts — you will need **guidance** if you keep this path.
- **Quest briefing (author — brief):** when she sends you to the road-town rogue, she **also** tells you:
  1. **Where:** near the village, in **the chunk where the blight gathers** (sick animals / dark air — horcrux **ping**), a **chest** holds his **binding** (the **horcrux** item — display name **TBD**). The **binding** must be **destroyed** — **not** the chest; the **item** is unmade.
  2. **Destroy hint — not the Well:** she does **not** mention the Dragon’s Well or any fountain. One line like: **only the worst creatures this world has can unmake a binding like that** — steers to **wither**, **ghast fireball**, **lightning** in thunder (`necromancerHorcruxDestroyers`, **excluding** `dragons_well` / `dragon_breath_bottle` from her speech). No picks, no lava, no crafting grid.
  3. **Finding the chunk:** she describes **the blighted chunk near the town** in plain language — **never** chunk numbers or coordinates. Player finds it via **horcrux ping** (heavy blight **in** the chunk vs weaker **near** ring — all horcruxes).

**Draft dialogue (stub):** *“Down the road, a king of thieves wears what you will bring me. He hid his binding a few chunks from that town — find where the blight pools in the grass. Destroy the binding itself, not the wood around it, before you take his plate. Not fire, not steel: only the worst the world breeds — Nether wailing fire, the three-headed death, the sky’s spear in a black storm.”*

### Intro quest — chestplate rogue town (author §6)

**Internal id:** `introChestplateRogue`. **One fixed site** per world — **not** the random rogue pool. Display / quest names **TBD**.

**Placement:** **~350 blocks** from the **citadel center** on the **great road** (`necromancerIntroChestplateRogueDistanceBlocks` **350**). The **road passes through the town** (spawn → **this town** → citadel gates — `endgame.md`). This is the **largest settlement near the citadel** — a **small town**, **bigger than natural villages** (more beds, trades, footprint **TBD** in structure pass). Distinct from the **Dragon’s Well** (also ~350b from citadel but **off** the road, hidden **copse** on the opposite side).

**The site:** an **occupied town** under the intro rogue. **Frightened villagers** **lock themselves in** (barred doors / shelter AI **TBD**) while **minions** and **placed mob spawners** (undead mix **TBD** — skeletons, zombies, etc.) pressure the streets. Spawners are **part of the set piece** (breakable after quest start **TBD**). Not a vanilla village clone — authored **town** scale.

**The rogue:** lives in his **house** in town; wears the **god wizard chestplate**. **Cannot be damaged or killed** until the mother **starts** the quest (quest flag). While his **binding (horcrux)** still exists, he uses **pseudo-death** like a wild lich — **cannot be truly killed** and the **plate cannot be taken** until the horcrux is **destroyed**.

**Kill order (author):** **1)** destroy **binding** (off-site chest) → **2)** kill rogue (basement fight **TBD**) → **3)** loot **chestplate** → **4)** bring plate to **Dragon’s Well**.

**House basement:** **sealed / inaccessible** until quest start. While sealed, basement **blocks are invincible**. After quest start, basement opens for the **confrontation** once the binding is gone — **not** where the horcrux sits.

**Horcrux (tutorial):** **guaranteed**, in a **hidden chest** **4–8 chunks** from town (`necromancerIntroHorcruxMinChunkOffset` / **Max**). Mother’s **non-Well destroy hint** (wither / ghast / lightning). **Dragon’s Well** as destroy site is **not** in her intro speech; player may still use Well if they find it (**TBD** balance).

**Random rogues** unchanged (**~4%** horcrux); this set piece is **authored only**.

### Dragon's Well — intro meeting (author §6)

- **Location:** **Dragon's Well** (`endgame.md`) — **hidden in a dense tree copse** **~350 blocks** from the citadel, on the **side opposite the great road**, **not** visible from the road. Finding it is part of intro / lore / mother’s directions. Citadel remains **~1,000–2,000 blocks from spawn** via the road; the Well is a **detour past the fortress**, not at the gate.
- **When:** **any time** — no **moonless night** requirement. She says to **meet at the well**.
- **Without chestplate:** she **chastises** the player and sends them back to finish the road-town rogue — **no** story progress.
- **With chestplate (first time):** she **takes the god wizard chestplate** from the player (held by her until **commit**). She does **not** mention **Seal-Breaker** or gate rituals by name. She gives a **clear shopping list** — **separate ingredients only**; the player **does not craft** cores or the sword (`endgame.md` totals, delivered as items):

| She wants (plain speech) | Count | Notes |
| --- | ---: | --- |
| **Eyes of ender** | **16** | |
| **Dragon’s breath** (bottles) | **2** | Well fill OK |
| **Nether star** | **1** | Wither |
| **Netherite ingot** | **4** | |
| **Crying obsidian** | **4** | |
| **Shulker shell** | **4** | |
| **End crystal** | **2** | |
| **Dragon head** | **1** | |

  Quest journal UI should mirror **counts**, not recipe names (**TBD** exact dialogue).

- **Return with full list:** before any transfer, she warns: **once you hand these over to me, you are committed** (Order closed — `necromancy.md`). Player may **opt out** here (**TBD** — refuse to hand in vs separate dialogue button).
- **On commit (hand-in):** she **keeps** the ingredients (NPC crafts off-screen). She gives the player the **full god wizard armor set** (all **four** cursed netherite pieces — assembled from the quest chestplate + her craft), the **Seal-Breaker** sword (**one per world** instance — **TBD** steal rules), and **one** **15-minute** draught (`motherGodWizardWardPotion`) that **suppresses the bad parts** of the god wizard set while active: **no self-rot**, **no extra stigma** from the armor (**binding** and combat bonuses **unchanged** — still cannot manually unequip while ward lasts). **One per player per world**; cannot be re-crafted.
- **Her story (lie):** her **son** is the **evil second brother** — one of the **two bound necromancers** in the citadel (`endgame.md`). She paints **him** as the **victim** to **protect**. She wants the player to **open the seal** (Seal-Breaker on the gate) and **kill the other two** — the **Corrupted King** and the **other bound necromancer** (the **first brother** / “good” one in her telling) — **not** her son. Wording like **set my son free** / **do not harm him** (**TBD** dialogue).
- **Player belief at commit:** spare **son**, eliminate **king + first brother**. True arc: **second brother** is evil; **brother-choice / betrayal** later (`§6` climax **TBD**).
- **Vs assault (`endgame.md`):** **Corrupted King** and **both brothers** may die in **any order**. Quest tracks **brother choices** (spare second, side with first) and **mother** responses — **TBD** spare mechanics.
- **If you walk away:** no failure; Well visits repeat (**no plate** = scold; **mid gather** = reminder; **ready to hand in** = commit prompt).
- **Lament of Ash** / other rewards **TBD** (after commit).

**Config (proposed):** `necromancerIntroLightningStormDuration`, `necromancerIntroChestplateRogueStructure`, `necromancerIntroChestplateRogueDistanceBlocks` **350**, `necromancerDragonsWellDistanceBlocks` **350** (well **opposite** road in copse).

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

- **Intro / commit gate:** **3 hearts** → storm → town → **horcrux** → rogue → **plate** → **Well** (take plate → **ingredient hunt** → hand-in → **commit** → **god wizard set + ward potion + Seal-Breaker** → **free her son** lie).
- **World rogues:** common sites, shards, rare wild lich horcrux — separate from **intro chestplate rogue**; **no** flute drops from random rogues.
- **Lament of Ash:** drops from an **End flute tyrant** (§7). At **commit**, mother gives **full god wizard set** + **Seal-Breaker** (§7).

### Open — early / mid (§6)

1. ~~**Intro through commit gate**~~ — plate, ingredient list, hand-in, **Seal-Breaker** reward, **son** misdirection.
2. ~~**Son identity**~~ — **evil second brother** (bound necromancer). Mother wants **king + first brother** dead, **son spared**. **Ending branches** when player **spares** second brother or **aligns** with first.
3. **Motivation / citadel** — mother’s framing + Seal-Breaker; dragon still required for barrier (`endgame.md`).
4. ~~**Citadel access**~~ — same as everyone: dragon dead + **Seal-Breaker** (see **Citadel access & horcrux** below).
5. **Other quest necromancers** — besides **two bound** bosses + mentor, any **named** overworld NPCs (not rogues)? Village Chronicle hooks?
6. **Mid beats** — hunt **fixed flutes**, first **horcrux** ritual, god wizard armor, **when** player learns Well + compass?

### Citadel climax — brother choice (§6 — author direction)

**On committed quest citadel entry:** **both** bound brothers **speak to you** (dialogue **TBD**). **First hostile action against one brother** = you **choose the other** (attack **first** → aligned with **second** / evil son; attack **second** → aligned with **first**). **Wither hand-in** and **multiplayer** “first hit” rules = **implementation** (quest UI / per-player alignment flags — not open lore).

7. **Assault role** — Corrupted King and brothers die in **any order** (`endgame.md`). Brother **alignment** is set by who you **attack first**, not kill order alone.
8. **Mother — son killed:** she **only** fights you if you **kill the second brother** (her son). On his death she **appears at your location** (anywhere in the world), **lines of dialogue**, then **attacks**. Not citadel-locked.
9. **Mother’s wrath:** if you **aligned with the first brother** (attacked **second** first), on **son’s death** (same spawn beat) she **kills the first brother** first, **then** attacks you.

10. **Mother — god wizard armor (binding):** both beats happen **inside the citadel** only.
    - **First brother killed** (in citadel arc): she **appears in the citadel**, **helps you remove** the full god wizard set (**primary** unbind besides **TBD** edge cases). Dialogue **TBD** (she wanted him dead in her lie).
    - **Aligned with the first brother** + **Mother’s wrath** (son dead in citadel): she **strips** the god wizard armor **in the citadel**, **then** kills **first** and **attacks you**.
11. **Mother** after commit — intro / Well beats unchanged; post-citadel = **§8–10**.

### Necromancer endings (§6 — author direction)

**Shared:** **Horcrux** = no absolution ever. **King** path requires **not** active necromancer at sit (absolve if eligible). **Well XP**, **Order stations**, and **Kingstree regen** never apply to **active necromancers** (`endgame.md`).

| Branch | Trigger (after citadel arc) | Outcome |
| --- | --- | --- |
| **Kill both brothers as necromancer** | **Kill both** brothers (**any order** vs king). **Mother** spawns **on son’s death** at **your location** (dialogue → fight). **Sit:** **no horcrux ever** → **absolve** → throne (tree heal **not** required). |
| **End castle — agree** | **Spare second brother** (evil). He enlists you to **move the citadel to the End** — **wither supplies** + **End Anchor**, then ritual (**End move ritual** below). He **betrays** after the move. |
| **End castle — refuse** | Spare second brother; **refuse** the move. He **attacks** you. |
| **End castle — stall** | Spare second brother; **no** help and **no** clear refuse (stall / ignore). He **stays in the citadel** and **sends swarms** into the world. **Claim after killing him:** **Corrupted King** dead **≥ 7 in-game days** (timer **starts at king death**; brother **alive or dead** does not start or reset it). |
| **Mother’s wrath** | You **aligned with the first brother** (attacked **second** first — see above). **Mother** **kills the first brother**, then **attacks you**. |

**King vs tree (closed):** **Horcrux** blocks absolution. **Absolve** to sit (not active necromancer). **Healing the tree not required to sit** — same **sit → bloom → Hall** progression as all players (`endgame.md`); no extra “true king” bloom gate.

### End move ritual (§6 — author direction)

**Track:** **spare second brother** + **agree** to move the citadel. **Second brother alive.** Tree **not** healed (Corwin cure path still open until this runs — then **mutually exclusive**).

When you **agree**, he **gives you the End Anchor recipe** (written page — **TBD** same item as Corwin’s cure page or not).

**He asks you** to return with:
1. **Wither supplies** — vanilla set: **3 wither skeleton skulls** + **4 soul sand** (or soul soil — **TBD**).
2. **End Anchor** — you **craft** it from that recipe (`config-and-recipes.md`). **Not** learnable elsewhere on this branch.

**Sequence:**
1. You **give him the wither supplies** (hand-in **TBD** UI).
2. He **summons the Wither** at the **Blight Heart** / **Kingstree** roots (**TBD** arena — cavern under courtyard).
3. The Wither **does something to the Heart** (**TBD** — must hit it / brief channel); **black glow** pours out; **Heart dies**, **Kingstree killed** (stump). **Wither** **TBD** (despawn, die, or banished after).
4. He **starts his spell** at the **stump** and **immediately teleports you to the End** (you must have **End Anchor** in inventory).
5. You **place the End Anchor** in the End → his spell **completes** → **citadel moves** to that spot.
6. **Overworld:** **citadel-sized End portal** + **Hall of the Twelve Kings** floating above — **nothing else**.
7. He **betrays** you (**attacks**).

**Heal path:** Corwin **cure** on the Heart only — cannot run after step 3 above. **No** separate kill-bane item; **kill = this wither beat**.

**TBD:** End Anchor pearl count per slot; anchor consumed on place; Wither fate after black glow; wither ritual uses same **mined-down** Heart chamber as heal path (`endgame.md`).

### Citadel access & horcrux (§6 — closed)

- **Entry:** same as all players — **Ender Dragon dead**, **Seal-Breaker** on the gate (`endgame.md`, `citadel-claim.md`). No quest bypass.
- **Player horcrux:** **not** quest-gated. Any player may perform the ritual **as soon as they have** the required ingredients (`necromancy.md`). **First horcrux** still **permanently** blocks absolution.

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

Quest **flutes** are **not** the **bone whistle** (`necromancy.md`). **Any player** who loots one may **use** it (no commit gate on flutes). **`necromancy.md`** wand/hearts/stigma still apply only to **active necromancers**; flutes are world loot like relics. **Inventory:** hold **as many different flutes as you find** — no exclusivity cap.

**Flutes never hurt the player** who plays them — no self-damage from the effect. **Each play consumes 1 charge** (see **Charging**). Per-flute **cooldown** after use **TBD**. Havoc flutes respect **`necromancerWorldHavocEnabled`**.

**Charging (author — §7):**

| Who | Between refills |
| --- | --- |
| **Any player not on the necromancer path** (no commit) | **1 charge** — after each play the flute is **empty** until **recharged** again. |
| **Committed necromancer** | **3 charges** per fill — play up to **three times**, then **recharge** before the fourth. |

- **Loot:** flutes drop **uncharged** (0 charges); must **recharge** before first use.
- **Recharge (two steps):**
  1. **Craft a flute charge** (shapeless, vanilla only): **glowstone dust** + **blaze powder** + **ender pearl** → **1 flute charge** (consumable stack; display name **TBD**).
  2. **Refill the flute** (shapeless): **quest flute** + **1 flute charge** → same flute, bank filled to that player’s cap (**3** committed / **1** everyone else).
- **No** Dragon’s Well, **no** phylactery shard, **no** bottle of souls on the flute itself. **Not** soul sand or redstone in the charge recipe. **Cannot play** at **0** charges.
- **Third ingredient:** **ender pearl** (End tyrant / teleport tone). **Not** soul sand, redstone, or gunpowder.
- **Config:** `questFluteMaxChargesCommitted` **3**, `questFluteMaxChargesOther` **1**, `questFluteChargeThirdIngredient` **`ender_pearl`**.

**World uniqueness (author — §7):** **One of each flute type per world**, on **End flute tyrants** (below). **Relic rules:** indestructible, never despawn, void return like king relics. **Not** overworld rogue drops. Removed only by **horcrux ritual** (or other **TBD** deliberate consumption) — not lava/fire.

#### End flute tyrants (author — §7)

**What “outer End” means:** the End dimension **outside the main dragon island** — the **outer ring islands** and distant End landmasses you reach after the dragon fight (not the central obsidian pillars / dragon arena). Tyrants are placed **far enough** that they **do not interfere** with the **Ender Dragon** battle. **No** requirement that the dragon be dead for them to **exist**; they are just **not** on the main island.

Like **bandit fortresses** for relics: **fixed placements** from saved world data on those **outer** islands.

| Count | Role |
| --- | --- |
| **6** | **Boss necromancers** — each guards **one unique flute**; **uses that flute against you** in the fight. Drops flute on death. |
| **2** | **Extra bosses** — **no flute**; **no horcrux**; shard/gear loot only. |
| **8 total** | More bosses than flutes. |

**Placement:** **8** saved camps on **outer End** islands only, in a **tight annulus** around the main dragon island: **≥** inner min **and** **≤** outer max from dragon island center (defaults **`512`** / **`768`** blocks — **max kept much closer** than early drafts). Spread across **different** nearby ring islands (N/S/E/W + diagonals **TBD**).

**Aggro / home:** **Passive** until you reach the **inner sanctum** — a marked room **inside** the authored structure (not the whole island or outer yard). You must **enter the building** and reach that **inner part** to **find** the boss and **start** the fight. Outer rooms may have minions/traps first.

**Proposed defaults (author — tune in playtest):**

| Topic | Rule |
| --- | --- |
| **Leash** | Boss **won’t chase** outside the **structure bounds** + **16 blocks**; resets **TBD** on leash. |
| **Boss power** | ~**Bandit King** toughness + **2–4** minion waves; flute tyrants **reuse their flute** every **60 seconds** in combat (`endFluteTyrantFluteCooldownSeconds` **60**). |
| **Lament (no Armies)** | Same **32-block** box; **Slowness II + Weakness I** **8s** on players/soldiers/bandits (no morale fear). |
| **Hints** | **No** map markers; **shard loot** on the **2** non-flute bosses + **TBD** End book clues. |

**Usable by whoever kills them** — flute to killer’s inventory; stealable thereafter. **Horcrux soul list:** killing a tyrant counts (**§ Horcrux**).

| Flute (v1 name) | Effect (player) | Boss use |
| --- | --- | --- |
| **Beast Dirge** | **Animals only** in loaded chunks near you: **50%** of passive/neutral animals take **4–12** hearts blight/poison (rolled). Never hurts flutist. | Same vs **your animals** / any animals in chunk (**TBD** cooldown). |
| **Hivepipe** | Spawns ~**100 bees** that guard you until **all bees are killed**. | Same vs intruder until bees gone. |
| **Bone Choir** | **5 wither skeletons** guard you for **10 minutes** (or until destroyed). | Same vs intruder. |
| **Gale Flute** | Thunder + lightning in **10 blocks** of the player (**every direction**). | Boss casts at **you** in home. |
| **Lament of Ash** | **Fear** / flee on **soldiers, militia, and bandits** within **32 blocks** of the player (**TBD** Armies vs vanilla fallback). | Same vs **you + your allies** in range. |
| **Crucible Whistle** | **Debuff cloud** at feet: **Slowness + Weakness + Poison** + **Blindness or Nausea** **1–2 seconds** (author: brief **blindness** or **nausea** roll). | Boss casts under **your** feet in home. |

**God wizard armor:** **four** cursed **netherite** pieces. **Intro:** loot **chestplate** from road-town rogue → mother **holds** it through ingredient hunt → on **commit** she gives the **complete set** (all four) + **Seal-Breaker**. **Not** crafted at the Well; Well is for **breath**, **horcrux destroy**, and **Kingstree cure** craft.

### Sacrifice (no exclusivity)

- **Horcrux ritual** **consumes** a flute, **full god wizard set**, or **relic** — deliberate only. **No** separate god-wizard craft cost.
- **Horcrux does not lock out flutes** — you may still carry and play flutes while horcruxed; flutes are only lost when fed into a ritual.

### Havoc, hunting, and loot (author intent)

- Top necromancer tools are **meant to be dangerous in the world** — mass animal death, large summons, etc. — so **other players want to hunt** committed necromancers.
- **Horcrux** is the main hunt target (**hidden chest**). **Flutes** and **god wizard armor** are **stealable** but **indestructible** except **horcrux** (or **TBD** ritual); pseudo-death keeps bound armor on.
- **Config:** **`necromancerWorldHavocEnabled`** (default **true**) — see **`config-and-recipes.md`**. **Does not** affect casual **wand** play.

### Cursed wearables

Quest **cursed netherite — full four pieces** (helmet, chest, legs, boots). Author name: **god wizard armor** (display name **TBD**). Real **netherite armor** with quest enchants/attributes — **not** a robe reskin. Hood/robe (`necromancy.md`) stays separate casual gear. **Upside = combat power; downside = self-rot** — horcrux handles **poison/harm pots + fake death**.

**Indestructible (author):** Same durability rules as **kings’ relics** and quest **flutes** — no lava/cactus/void destruction, never despawn, void return **TBD** like relics. **Only** leaves the world when **consumed** in a ritual: **horcrux creation** (full set counts as **one item of power** in hotbar — **TBD** UI) and **TBD** other quest forks. Still **binding** while worn.

#### Good parts (author — proposed defaults; tune in playtest)

| Benefit | Full set (4 pieces) | Per piece (partial wear) |
| --- | --- | --- |
| **Armor** | **+8** armor / **+4** toughness on top of netherite enchants | **25%** of full-set armor/toughness bonus per piece worn |
| **Damage reduction** | **+30%** after armor (`godWizardDamageReduction` **0.30**) | **+7.5%** per piece |
| **Knockback** | **100%** resist | **25%** resist per piece |
| **Melee punish** | **4** hearts reflected + **Wither I** 3s ( **TBD** PvP cap) | **1** heart + **1s** Wither per piece |
| **Status shrug** | **50%** shorter Slowness / Weakness / Blindness | scales with pieces |
| **No burning** | **Fire Resistance** | only with **full set** |
| **Melee power** | **+25%** (`necromancerGodWizardMeleeBonus`) | **+6.25%** per piece |

**Not** on the armor: poison/harm immunity and pseudo-death stay **horcrux-only**.

#### Costs (while worn)

- **Self-DoT:** **1 heart every 15 seconds** (`godWizardRotIntervalSeconds` **15**) while **any** cursed piece is worn. Long fights need regen/food or horcrux; much gentler than burst rot.
- **Stigma visible:** hood/robe rules stack; full cursed netherite reads as **lich** on sight (**TBD** extra villager/soldier penalties).
- **Binding (yes):** **cannot manually unequip** except **mother’s citadel unbind** beats ( **first brother dead** = she **helps** remove; **Mother’s wrath** = she **strips** you before the fight). **Pseudo-death keeps the set on.** **True death** drops the set like normal gear (pieces stay **indestructible** on ground). Plan around **3-minute click lockout** in full kit.

### Horcrux (quest capstone — see `necromancy.md`)

**Many allowed** — each creation costs **−1 permanent heart**; stop when you hit the **1-heart floor** (same as wand). Not a flute; built from a **phylactery shard** + ritual kill.

#### Creation ritual (all at once)

1. **Hotbar** must contain: **one phylactery shard** + **one item of power** — **any quest flute**, **full god wizard armor set** (all four pieces — consumed as one ritual), **or** **any of the 12 kings’ relics** (`endgame.md`). The power item is **consumed** on success (relic gone from world unless you recover another copy — relics are normally unique).
2. While those stay in the hotbar, the player **kills** one valid **soul** target (**same moment** — not a past kill). **Soul list (closed):**
   - **Any player** (PvP),
   - **Any acolyte** — NPC Order brother **or** player on the Order path,
   - **Any End flute tyrant** (all **8** bosses; flute-holders and shard-only).
   **Not** on the list: overworld **rogue** necromancers, **wild liches**, citadel bound brothers, Corrupted King, wandering deserter necros.
3. On success, the shard + power item combine into a **horcrux item** (**consumes** hotbar reagents — **TBD** exact recipe UI).

#### Wild liches — tutorial for everyone

**Term — wild lich:** mod/docs name for a **rogue necromancer** who hid **one horcrux** (item in a **hidden chest** at their site) — **very rare**; almost all rogues have **none**. Teaches **destroy** flow; **not** a horcrux-creation **soul**.

#### Rogue necromancer sites (author — §7)

**Overworld rogues are necromancers, not bandits.** They are **NPC rogue necromancers** (wand/hood, minions, phylactery **shards**) at fixed camps — **not** the **bandit** deserter system and **not** **wandering necromancer** deserters unless that NPC deserts separately.

- **Uncommon–common:** **~half** as many sites as **pillager outposts** (~**2×** outpost spacing — config `necromancerRogueSiteSpacing`). Still regular exploration content, not a rare dungeon.
- **Site types (author — §7):** each rogue spawns at **one** of (default weights — **`necromancerRogueSiteWeight*`** in `config-and-recipes.md`):

| Type | Weight | Notes |
| --- | --- | --- |
| **Crypt in cave** | **30%** | Fully underground; little surface sign. |
| **Crypt + surface entrance** | **25%** | Same crypt; **surface door / ruin**. |
| **Dark tower** | **25%** | Standalone tower (**TBD** floors). |
| **Taken village** | **20%** | Occupied village; **villagers board themselves up inside their houses** (shelter AI — same idea as intro road-town). |

  **Not** bandit camps — rogues are **necromancer NPCs** only (`Bandits` = deserter soldiers in `threats-and-mobs.md`).
- **Rogue horcrux spawn (author):** **most rogues have no horcrux.** **~4%** (`necromancerRogueHorcruxChance`) spawn with **at most one** horcrux in a hidden chest — **never** two. Horcrux sites are **jackpots**, not every camp.
- **When you meet one:** same rules as player horcruxes — chunk pings, pseudo-death until the **horcrux** is destroyed, then the rogue can die for real.
- **Learn destroy:** Wellkeeper / lore + **optional** encounter — find chunk → chest → **unmake horcrux** (Well, wither, lightning, etc.) — **not** forced before commit.
- **Learn create:** **shards + power item + kill** on soul list; docs and loot explain player horcrux — wild lich is **optional** preview.

**Creation costs:**

- **−1 permanent max heart** per horcrux (cannot create at **1 heart**).

#### Where it lives (hunt gameplay)

- Each horcrux **must be stored in a chest** (anywhere on the map). Owner need not carry them.
- While **at least one** horcrux exists, **passives apply** until **all** are destroyed (see **Destroying a horcrux** below).
- **Horcrux “ping” (author — §7):** **not** a chat waypoint. **Everyone** — including the **horcrux owner** — sees **dark air / blight particles** and **poisoned animals** in/near chunks that hold a horcrux (owner’s or wild lich’s). **No** soldier debuff aura (**cut** — was a draft “corruption” idea). **Config:** `necromancerHorcruxPingEnabled` **TBD**.

| Tier | When (chunk loaded) | What you see / feel |
| --- | --- | --- |
| **Near** | Hunter within a **circle** of **10 chunks** radius (Euclidean on chunk centers — config `necromancerHorcruxPingRadiusChunks`) of any chunk that **contains** one of the lich’s horcrux chests | **Dark air:** **soul/smoke/wither** particles at head height — **lighter** than in-chunk; ramps toward the horcrux chunk, **no** block highlight, **no** ground ash. Optional low **ambient sound** (wind, whisper) **TBD**. |
| **In chunk** | Hunter standing in a chunk that **contains** a horcrux | **Heavy blight:** **noticeably** denser dark **air particles** than the near ring (config `necromancerHorcruxPingInChunkMultiplier` default **2.5×** vs near-tier density). Optional **dimming** in that chunk only (fog / slight darkness at head height — **not** full blindness; **TBD**). **All** horcruxes (player + wild lich), not intro-only. **Animals** in that chunk show **poison** — confirms “this chunk,” **not** the chest block. |
| **Find chest** | Same chunk | Manual search, x-ray-proof hides, detector item **TBD** (Order / late quest). |

- **Same chunk ≠ same block:** ping confirms **chunk only**; multiple horcruxes in one chunk still mean “dig/search here.”
- **Unloaded chunks:** no particle ping until someone loads the area.
- **Horcrux compass (craftable):** **PvP hunt tool** — so **other players** (and you) can **find horcrux chunks** and break the lich’s immortality. Needle points to the **nearest horcrux item**’s **chunk** (X/Z) in the **same dimension** — chest-stored player horcrux or wild-lich chest. **No exact block**; use **ping + dig** in that chunk. **No maximum range** — if any horcrux exists in that dimension, the needle always works. **Never** filters out **your own** horcruxes (no self-hide). **Early vs late:** many wild lich chests = needle often snaps to **closer** noise — **by design**.
  - **Crafting:** shapeless — **compass**, **phylactery shard**, **soul sand**, **wither rose** → 1 compass; **craft many** (`necromancy.md`).

#### Destroying a horcrux (author — §7)

Horcrux items are **not** unmade in a crafting grid or by normal player fire. Destruction is **deliberately hard**; the **usual** hunter finish is hauling the **horcrux** to the **Dragon’s Well** and **throwing it in** (`endgame.md`).

**Rules (chosen direction):**

- Horcrux **drops from chests** like any item when the chest breaks; **mining the item entity** or **player inventory delete** does **not** destroy it (item **immune** to despawn/pickup abuse **TBD** — e.g. always drops on break, cannot `/kill` item away).
- The **horcrux item** (on ground, in item frame, or on **lightning rod** / block **TBD**) must be destroyed by **vanilla** sources only — **v1 whitelist (author):**

| Destroyer | How hunters use it |
| --- | --- |
| **Dragon’s Well (primary)** | **Throw / drop** the **horcrux item** into the well (**TBD** interaction). **Unmakes** it **anytime** (dragon alive or dead). Hunt loop: find chunk → steal horcrux → **find hidden well copse** (~350 blocks off road past citadel) → dunk. |
| **Ghast fireball** | Nether stash; fireball hits item entity. |
| **Wither** (boss or **skull projectile**) | Summon fight over the horcrux when you can’t reach the well. |
| **Lightning** | During **thunder**: **Channeling** trident on/near the item **or** lightning strikes a **lightning rod** the horcrux is bound to. |
| **Dragon’s breath bottle** | **Optional alternate** — splash bottle on the horcrux on the ground (**TBD**); same well lore, for hunters who already have breath but can’t move the item. |

- **Not** trivial: no crafting-grid delete, no lava bucket, no normal fire (**TBD** edge cases).
- **Config:** `necromancerHorcruxDestroyers` — default the four rows above; servers can add/remove (e.g. charged creeper **off** by default).

**When one horcrux breaks:** **Decided** — **every** horcrux tied to you must be **destroyed** before you can **die for real**.

**Stealing:** horcrux stays tied to **creator**; thieves **carry it to the Dragon’s Well** (ideal) or use **wither/ghast/lightning** where it lies.

#### What it does (author — §7)

While **≥1 horcrux** exists for you:

| Effect | Behavior |
| --- | --- |
| **Poison** | **Immune** |
| **Damage / harming potions** | **Immune** (includes splash/lingering harm — **TBD** edge cases) |
| **Would-be death** | Player **does not die**. **3 minutes:** no **left/right click**; **can move**; **keeps inventory**. **No teleport** on trigger — you stay where you fell. |
| **True kill** | **All** horcruxes **destroyed** → normal death rules. |

**Config:** servers may disable horcrux passives (`necromancerHorcruxEnabled` — **TBD** in `config-and-recipes.md`) for low-PvP worlds; default **true** for author intent.

**Flute lockout** on horcrux: **no** — flutes remain usable unless sacrificed in a ritual.

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
- **Cursed netherite** final numbers, partial-set rules (**TBD**).
- **Binding cursed armor** — **yes**; **indestructible** + horcrux-sacrifice — documented above.
- **Multi-horcrux:** **all** must be **destroyed** before true kill — **decided**. **No** pseudo-death teleport — **decided**.
- **Horcrux item placement** — on rod vs frame vs ground for lightning/breath hit tests (**TBD**).
- **End flute tyrants:** eight fixed coords / structure anchors (**TBD** names on burial-style map **TBD**).
- **Rogue horcrux spawn rate** — default **~4%** (`config-and-recipes.md`).
- **Pseudo-death** interaction with soldiers, throne sit, dungeon (**TBD**).

## Lore language

Historical chronicles may use older words (e.g. “sorcerer”); **player systems use necromancer**. Intake draft: `docs/notes/2026-10-05-design-notes-intake.md`.

## Existing mechanics (reference)

- Hood, robe, wand, minions, absolution (until **horcrux**), penalties — `necromancy.md`
- Citadel throne, barrier, relic ban — `citadel-claim.md`, `endgame.md`, `mod-split.md`

## Marketing one-liner (draft)

“Three hearts gone, something finds you — swear the dark road or walk away and leave the Order your path.”
