# Player acolyte path

Optional **player status** for Citadel, aimed especially at **solo players with only Citadel** who want **pre–citadel claim** goals. It is a **guild-style questline and class**, not a rival endgame to the throne.

- **Vs necromancer path:** you can **dabble** with necromancy (wand, hearts) and still pursue the Order **until** you **commit** to the necromancer questline at the end of the intro arc. **Opt out** of that arc keeps the Order open (through pre–claim timing). See **Mutual exclusion** (`necromancer-path.md`).
- **Throne:** you **can** still become citadel king as an acolyte. **Active necromancer** rules (relic ban, throne penalties, etc.) still apply if you are a necromancer, see `necromancy.md`, `citadel-claim.md`.
- **Timing:** the Order arc must **finish before citadel claim**: training, Maelor lock, and reunite escort are **pre-claim only**. **No joining the Order after claim** (see below).

NPC acolytes remain the fixed **17** veterans; see **`endgame.md`** for refuges, homecoming, stations, and death rules. **Two** of the 17 are **citadel-dungeon prisoners** (War Leader + one craft brother), not overworld refuges, **`citadel-layout.md`**.

### Refuge sites (author 2026-10-07)

- **One overworld refuge per brother** who escaped the purge (**15** placed sites, including **Maelor**).
- **Two brothers** have **no** overworld hideout until the dungeon rescue (cells only).
- **Each refuge uses a different archetype** at world gen — e.g. **repurposed witch hut**, **small cave**, **house in a village**, hermit shack, ruined chapel, **not** one template cloned 15 times. SavedData records **which brother** maps to **which archetype + position**.
- Distance/spacing: **`endgame.md`** (500–4,000 from spawn, ≥700 apart, proposed).

## Mutual exclusion (Order vs necromancer questline)

**Casual necromancy** (wand, minions, penalties, absolution) and the **Order** can overlap until the player finishes the **intro quest** and chooses **commit** or **opt out** (`necromancer-path.md`).

| Player state | Order acolyte path | Necromancer questline |
| --- | --- | --- |
| Never started intro arc | Open (subject to pre–claim timing) | Not started until **3 permanent hearts lost** triggers the offer |
| In **intro quest** (before final choice) | Open | In progress |
| **Opt out** at end of intro | **Stays open** (intended through **citadel claim** at minimum; post-claim Order role TBD) | **Permanently closed**: no story perks/skills track |
| **Committed** at end of intro | **Closed**: no Maelor lock, brothers refuse (dialogue TBD) | Open through main arc |

**Commit** closes the Order only. **Creating a horcrux** makes **absolution impossible** (`necromancy.md`). **Opt out** does **not** clear necromancer mechanics, you can stay an active necromancer, absolve, and still walk the Order; you only forfeit the **questline**.

**Active necromancers** cannot use **citadel Order stations** (minor ask or major block use) and cannot gain **Well of Kings** XP or **Kingstree** heal aura (`endgame.md`).

**After citadel claim:** **no new Order progress**: no basic training, light/hard specialty, Maelor lock, or reunite quest for players who were not already a **locked acolyte** before claim. Opt-out necromancers who waited still **lose** the Order path at claim if they never locked.

**Pre-claim only:** “Acolyte open until claim” is a **deadline**, not a post-claim opportunity.

### Necromancer gear at refuges

Brothers **recognize** necromancy on sight. While you carry **`necromancy.md` necromancer kit** on your person, at minimum **wand**, **hood**, or **robe** (inventory or equipped; bottles of souls / minions **TBD**), within **32** blocks of a refuge **acolyte** (same radius as soldiers):

- No **basic training**, specialty quests, Maelor chain, **escort handoff**, or Annals-open dialogue until the kit is **gone** (stash away from refuge, absolve and crumble wand, etc.).
- Acolyte **warns** you: lose the wand and robes or leave. Repeated tries or refusing to leave → **chase off** (hostile or drive away, implementation TBD; should not permakill brothers).

So dabbling and Order **can** coexist, but **not in the same visit**: drop necromancer gear before Order business. The **necromancer intro** can still fire **at a refuge**; if an acolyte is **in sight** during the **intro lightning storm**, the mother’s bolt **kills** him and leaves a **tombstone** inscribed **“She smote him.”** (`necromancer-path.md`). Not the Smite enchantment. That brother is **lost** for the world.

## Four specialties

Each of the **16** brothers (every acolyte except **Maelor**) is one of four specialties, **four per specialty** (assigned at world gen):

| Specialty | Citadel role (post-tree) |
| --- | --- |
| **Arcane** | Royal enchanting table lectern |
| **Alchemist** | Royal brewing stand lectern |
| **Smith** | Royal anvil lectern |
| **War Leader** | No fourth station; campaign support, aura, and field role (pairs with Armies when loaded) |

**Maelor** is the **17th**: founder-scribe, head of the Order (`old-kingdom-lore.md`). He does not count toward the 4×4 split. His refuge is the one that holds **Kaelen’s** Royal Annals copy and *The Order of the Well* (proposed fixed placement).

The **player** commits to **one** specialty (Arcane, Alchemist, Smith, or War Leader). **Only Maelor** may **lock** it. Field brothers teach **only their own** specialty. No respec after lock.

**No specialty powers during training**: basic and light/hard training are tasks and story only until Maelor **locks** your specialty. Then **Player specialty perks** apply.

## Quest flow (player)

```text
Save a refuge acolyte → basic training
  → that brother: **light** training in **his** specialty only
  → he sends you to find **Maelor**
  → Maelor: **light** training in the **other three** specialties (he teaches, brothers do not teach outside their craft)
  → Maelor: you **pick one** specialty for **hard** mastery
  → complete **hard** training with **brother(s) of that specialty** in the world (Maelor assigns; you report back)
  → Maelor **locks** specialty, **player perks begin**
  → Maelor: Royal Annals + **escort brothers** to him (capstone feat / scaling)
  → (post-escort scaling, TBD)
```

### 1. First contact

Any acolyte you **save** (clear refuge undead, then talk) offers **basic training** because you helped him, **unless** you still carry necromancer **wand / hood / robe** (see **Necromancer gear at refuges**). Includes **training tasks** and **lore** (Order, purge, hope that Maelor still lives).

### 2. Light training (first brother)

After basic training, the brother who saved you runs the **light** chapter of **his specialty only** (short tasks + lore, no perks). He does **not** train other specialties.

When light training is done, he ** sends you to find Maelor** (map hint / Annals tie, TBD). He also tells you **other brothers are alive** and that the Order **needs Maelor**.

**Which brother you save first** only decides which specialty you sample first; it is **not** your final specialty until Maelor locks you after **hard** mastery.

### 3. Find Maelor

Hidden until the first brother sends you. Maelor is **one of the 17**, not a separate NPC.

### 4. Maelor, breadth (light three)

At Maelor’s refuge, he teaches the **light** version of the **three specialties you have not** done with the first brother (tasks + lore, no perks). Only Maelor runs this breadth step at his lectern / refuge, field brothers stay specialists.

**Annals chests:** when this chapter begins (first substantial Maelor dialogue, exact step TBD), Maelor **speaks the twelve refuge Annals chests open at once** (`acolyte-path.md` § Annals chests).

### 5. Maelor, choose and hard mastery

Maelor offers the **hard** path for **one** specialty of your choice (Arcane, Alchemist, Smith, or War Leader).

- **Hard** = feat-grade training and quests for that specialty only (**difficulty tables TBD**: relaxed vs strict config).
- **Hard training** is done with **field brothers** of that specialty (Maelor gives the quest; they teach **hard** only in their craft). Return to Maelor to **lock**.
- If you **fail or abandon** hard training before lock, you may **pick a different hard specialty** (Maelor allows retrial, limits TBD).
- When **hard** is complete, **Maelor locks** your specialty. **Only Maelor** can lock.

### 6. Maelor, reunite the Order (after lock)

**Why Maelor asks:** reunite the **brothers**, **save the Royal Annals**, and find someone who can **retake the citadel** and **release the Corrupted King** (lay him to rest, same story as the Crown on his statue in `endgame.md`).

After lock, he assigns:

1. **All 12 volumes** of the Royal Annals (from the now-open refuge chests).
2. **Escort as many brothers as you can** to **Maelor’s refuge** before the citadel is claimed.

**Minimum brothers:** required floor + **better outcome for more** (TBD; config `acolyteBrothersMinimum`). Treat escort as the **capstone feat** after specialty lock.

**Why keep brothers alive:** each brother is **permanently lost** if killed (`endgame.md`); escort fights and Corpse-style deaths are real risks.

### Citadel-trapped brothers (§10)

Two brothers were **never** at overworld refuges, they hold the inner ward during the purge and were **caged in the citadel dungeon** (lore: ordered to stand down / hold, not flee).

| Brother | Specialty |
| --- | --- |
| **First** | **War Leader** (always) |
| **Second** | **Arcane, Alchemist, or Smith**: **random per world** at world gen |

- **Rescue:** after seal break, **player opens each Order cell separately** (**not** one lever for both cells, author **2026-10-07**); then talk / escort as usual.
- **Annals:** trapped brothers do **not** carry Annals volumes, copies only in overworld **refuge chests** (twelve locked Annals chests).
- **Reunite:** they count for escort only after rescue; deliver to **Maelor’s refuge** before claim to join homecoming roster.
- **Stations:** only **Arcane / Alchemist / Smith** fill royal lecterns. Trapping one craft brother **does not** replace escorting the others, skip too many overworld escorts and lecterns stay empty (Maelor can fill one dead brother’s slot only).

### Escort → homecoming (**model A, chosen**)

For players who **locked** specialty **before claim** and finished Maelor’s reunite:

- Only brothers **delivered to Maelor’s refuge** (handoff at Maelor) are on **Maelor’s roster**.
- On **claim**, **Maelor + roster** walk to the citadel (~3 in-game days). They staff lecterns/auras per `endgame.md`.
- Brothers **not** on the roster **stay** at their refuges (or recruited villages), alive, **not** auto teleported.

**Claim without Maelor reunite** (no locked acolyte roster): **no** automatic 17-acolyte homecoming. See **`endgame.md`, Citadel escort (any player)**.

**Player power (TBD):** optional bonus stacks for large roster at lock, not required for model A.

### Annals chests

While the **citadel is unclaimed**:

- The **twelve refuge chests** that hold Royal Annals copies are **unbreakable and locked**.
- **Open when either:**
  1. **Maelor** speaks them all open at once (start of his breadth chapter, `§ 4`), **or**
  2. **First successful citadel claim** in that world, if they were **never** opened before.
- Relics and other items **in those same chests** stay locked with the chest until it opens.
- Horn-keeper and ordinary refuge chests are **not** part of this lock (**Annals chests only**).

After claim, unlooted refuge chests follow normal rules in `endgame.md`.

### Escort brothers

When **Maelor’s reunite quest** starts (**after specialty lock**), brothers you have already **saved** (cleared refuge, talked) can be **called to follow**: while you are **near** a brother, he **joins your escort** and paths with you until you deliver him to **Maelor’s refuge**. Repeat until you meet the minimum and as many above that as you want for bonuses.

- Brothers already following stay with the group until handoff or death.
- **No soldiers (Armies):** if **your** soldiers (or militia) are within **32** blocks of a brother, he **will not** start escort, **will not** hand out Maelor-chain quests, and says clearly to **come back without your goons**: the Order trusts a person, not an army camped on the refuge. Send soldiers away, then talk again.
- **No necromancer kit:** same radius, wand / hood / robe must not be on you (see **Necromancer gear at refuges**).
- **Corpse mod:** play-test escort + player death so following brothers and Annals progress do not soft-lock (`compatibility-planned-mods.md`).

### Open design (TBD)

- Post-escort **ongoing duties** and **reward scaling** for brother count.

## Specialty feat gates (§8)

| Phase | Who | Purpose |
| --- | --- | --- |
| **Basic training** | First saved brother | Join the Order, no specialty yet. |
| **Light ×4** | First brother (**1**) + Maelor (**3**) | Taste every specialty; no perks. |
| **Hard ×1** | Field brother of **chosen** specialty | **Only your pick**: complete **all** hard steps for that specialty, then report to Maelor for **lock**. Other three hard tracks are defined but not required. |
| **Escort + Annals** | After lock | Reunite quest (`acolyteBrothersMinimum` + bonus). |

Brothers **verify** via turn-in, counters, or matching advancements where they exist.

---

### Basic training (all specialties, first brother)

**Order work, not generic fetch.** After you save him:

1. **Clear the refuge**: kill **every** necromancer-tainted mob tied to his structure (initial spawn **plus** any waves the brother flags until he says the site is **clean**). No oath until the structure is clear.
2. **Break the nearby curse**: defeat the **necromancer threat** the brother points to (implementation: **rogue necromancer** from the nearest **necromancer crypt** within ~**512** blocks, **or** a refuge-specific mini-boss if no crypt, TBD placement). Personal kill or kill credit on the necromancer NPC required.
3. **Oath**: vow at the brother (serve the tree, deny the wand, retake the citadel in time).

Then **light** training in **his** specialty (`§ Light training` below).

---

### Hard mastery (one specialty, before Maelor lock)

Maelor assigns a **field brother** (**master**) of your **chosen** specialty. Turn in **physical proof** to the master; complete **every** bullet for **that** specialty, then report to Maelor to **lock**.

See **`§ Light training`** for all four light chapters.

#### Smith (hard), agreed

Turn in to the Smith master:

1. **Every weapon and armor category** craftable in survival, **sword, axe, pickaxe, shovel, hoe**, **helmet, chestplate, leggings, boots** (one of each **type**).
2. **Every material tier** the Order counts for those crafts, at least **one crafted item** from **each** of: **leather, wood, stone, iron, gold, diamond, netherite** (where vanilla allows that material on that item; netherite via smithing upgrade counts). The full turn-in set must **cover all types** and **cover all materials** across the set (exact matrix in config `acolyteSmithHardTurnIn`).

#### Alchemist (hard), agreed

Turn in to the Alchemist master:

1. **One potion** (any vessel type) for **each distinct effect** in config `acolyteAlchemistHardEffects` (all brewable/obtainable effects in survival for this version).
2. **Vessel proof:** among the turn-in set, include **at least one** **normal (drink)**, **one splash**, and **one lingering** (three different bottles; effects may differ, e.g. drink healing, splash poison, lingering slowness).

#### Arcane (hard), agreed (books) + proposed (proofs)

Turn-in + proofs to the Arcane master:

1. **Enchanted books (agreed):** turn in **one enchanted book of each enchantment type** on config **`acolyteArcaneHardEnchantList`**: every distinct **enchantment** that can appear on an **enchanted book** from a **vanilla enchanting table** in **1.21.1** (one book per type; level on the book = any level you obtained for that type). **Treasure-only** enchants (Mending, Frost Walker, Soul Speed, Swift Sneak, Wind Burst, curses) are **not** on this list unless we extend a separate optional museum turn-in.
2. **Proof feats (proposed, confirm or cut):** complete **each** row below while holding/wearing the stated enchant (vanilla table levels). Config `acolyteArcaneHardProofs` mirrors this list.

| # | Proof (you must…) | Enchant on gear |
| --- | --- | --- |
| 1 | Fall **≥ 40** blocks and survive without totem | **Feather Falling IV** boots |
| 2 | Kill **1** hostile mob | **Looting III** sword |
| 3 | Pick up **grass block** or **deepslate** as item | **Silk Touch** tool |
| 4 | Kill **1** undead (zombie/skeleton/drowned/husk/stray) | **Smite V** sword *(not same sword as Sharpness)* |
| 5 | Kill **1** spider or cave spider | **Bane of Arthropods V** sword |
| 6 | Kill **1** mob from **≥ 20** blocks away | **Power V** bow |
| 7 | Kill **1** mob while it is on fire from your **bow** | **Flame** bow |
| 8 | During **thunder**, hit a mob with a **thrown trident** (lightning strike) | **Channeling** trident *(needs storm, not thunderless)* |
| 9 | **Throw** trident and catch it on return | **Loyalty III** trident *(clear weather; separate trident from #8)* |
| 10 | Break blocks **underwater** continuously **30** seconds without surfacing | **Aqua Affinity** + **Respiration III** helmet |
| 11 | Travel **40** blocks in **water** in under **25** seconds | **Depth Strider III** boots |
| 12 | Hit **2+** hostiles with **one** sweep attack (Java sweep) | **Sweeping Edge III** sword |
| 13 | **Catch 3** fish | **Lure III** or **Luck of the Sea III** rod |
| 14 | **Fire** a crossbow shot after full Quick Charge | **Quick Charge III** crossbow |
| 15 | With **Density V** mace, kill a mob after you fell **≥ 4** blocks this tick **or** with **Breach IV** mace, hit a mob wearing **iron+** armor | **Density V** or **Breach IV** mace *(mace + breeze rod from Trial Chambers, 1.21)* |

**Not proofs:** Knockback, Punch, Infinity, Impaling, Riptide, Thorns, Unbreaking, and the extra Protection variants, those are satisfied by the **book turn-in** only. **Riptide** optional swap for proof #9, TBD.

#### War Leader (hard), agreed

**Worn gear turn-in only**: bring to the War Leader master **each** item below with **≤ 20% durability remaining** (worn down through **combat use**, not crafted low or commands):

| Item |
| --- |
| **Diamond sword** |
| **Diamond axe** |
| **Bow** |
| **Crossbow** |
| **Mace** |
| **Full diamond armor** (helmet, chestplate, leggings, boots) |
| **3 shields** |

Master verifies durability on turn-in.

---

### Light training (agreed)

**Who:** first brother runs **light** for **his** specialty; **Maelor** runs the **other three** before you pick **hard**.

**One hard task each**: same *kind* of work as hard mastery, but a **single** bite. Progress **counts toward hard** (flags on the hard checklist; do not repeat completed slots).

After all **four** lights → Maelor offers **hard** → field master → **lock** when hard is complete.

| Specialty | Light (one task) | Counts toward hard |
| --- | --- | --- |
| **Smith** | **Craft** a **netherite helmet** and **netherite chestplate** (smithing upgrade counts). Turn in to brother/Maelor. | **Helmet** + **chestplate** types; **netherite** material on hard matrix (`acolyteSmithHardTurnIn`). |
| **Alchemist** | Brew and turn in **Turtle Master II** and **Regeneration II** (see below). | Those **two effects** on `acolyteAlchemistHardEffects`; vessel types can count toward hard **drink / splash / lingering** proof if you use different forms. |
| **Arcane** | Turn in one **perfect mace** (**Density V** + **Unbreaking III**) and one **perfect shield** (**Unbreaking III**, full durability), see below. | Credits **two** enchant **types** on the hard book list from the enchants on those items. If **proof feats** are enabled, mace can satisfy **Density** proof row when rules match. |
| **War Leader** | Turn in **one sword** and **one shield** (diamond recommended) with **less than one-fifth durability left** (**< 20%** remaining; combat wear, not broken). | Credits **sword** + **1 shield** toward hard turn-in set; hard still requires the **full** kit at **≤ 20%** (light slots count, do not re-craft; wear down or deliver same categories at hard completion). |

#### Alchemist light, agreed

**Turtle Master II** (turtle scutes / breeding grind) and **Regeneration II** (ghast tear + glowstone).

#### Arcane light, agreed

**Perfect mace:** **Density V** + **Unbreaking III** (table max; **Density** chosen over **Breach**).  
**Perfect shield:** **Unbreaking III**, full durability.  
Turn in **items**; enchants credit two hard **book** types.

---

### Maelor museum (optional, after lock)

Separate from hard lock: hand in **categories** Maelor requests (intake: **every weapon type**, **every armor type**, **every potion**, **every book**: run as **rotating bundles**, not one mega turn-in). Improves **escort bonus** scaling only.

---

### Config keys (§8)

| Key | Default / role |
| --- | --- |
| `acolyteSmithHardCraftList` | Data-driven craft checklist for Smith hard |
| `acolyteAlchemistHardEffects` | All required potion effect IDs |
| `acolyteArcaneHardEnchantList` | All required enchant book types |
| `acolyteArcaneHardProofs` | Fall, Looting, etc. |
| `acolyteArcaneFallMinBlocks` | 50 (tune toward max fall) |
| `acolyteWarLeaderHardMobList` | Hostiles requiring 100 kills each |

## Player specialty perks

**Zero mechanical perks until Maelor locks your specialty.** Basic and specialty **training** are tasks and story only.

After **lock**, perks below apply.

**Citadel stations (post-tree):** see **`endgame.md`, two tiers:**

- **Minor:** any owner/allied player **asks** the acolyte at the lectern, small station benefit, **no** matching specialty required.
- **Major:** **you** use the job block directly, **only** if your **locked specialty matches** that station (full royal power). NPC must still be at the lectern (or Maelor filling in). You do **not** stand at the lectern; the brother does.

**Post-tree** = royal **major** tier + anything marked post-tree below. **Pre-tree** perks work anywhere.

### Arcane (locked)

- **Pre-tree:** **vanilla** enchanting tables, enchant without spending **XP or lapis** (tier rules unchanged).
- **Post-tree, major (you at royal enchant table):** **locked Arcane only**: you pick one enchant on **gear or book** at **vanilla max + 2** (e.g. Sharp **VII**), **0** XP, **0** lapis (`endgame.md`).
- **Post-tree, minor:** **any owner/ally** asks Arcane brother, **random** roll, up to vanilla+1, gear or book, no lapis.

### Alchemist (locked)

- **Pre-tree:** potions **you brew** last **+50%** longer.
- **Post-tree, major:** **locked Alchemist only** at stand, tier **III**, **3×** duration, Resolve + Rallying, no blaze/coal (`endgame.md`).
- **Post-tree, minor:** **any owner/ally** asks, **2×** duration, no tier III, no Resolve/Rally, no blaze/coal.

### Smith (locked)

**Pre-tree (works alone, no citadel)**: all of the following stack:

- **Steady hand:** at a **vanilla anvil**, each repair material restores **50% more durability** than normal (same iron/diamond spent).
- **Frugal repair:** anvil repair **XP level cost −1**, minimum **0**.
- **Hard wear:** gear in your **armor and main-hand slots** loses durability **10%** slower.
- **Tempered upgrade:** at a **smithing table**, diamond → netherite (and other template upgrades you perform) **never consume the smithing template** (ingots and items still consumed as normal).

**Post-tree, major (you at royal anvil / forge):** template-less netherite reforge, **70% / 75%** off royal repair mat/XP, one **full repair** per day at half material (`endgame.md`).

**Post-tree, minor:** ask, repairs **40% / 50%** off mat/XP; no netherite reforge.

### War Leader (locked)

War Leader is the **combat / escort** specialty: strong alone, stronger with Armies soldiers in range. Arcane / Alchemist / Smith stay **crafting-focused**; only War Leader gets the field aura.

#### Aura (pre-tree and post-tree)

**Who it affects:** **you**, **citadel allies**, and **your soldiers and militia** (Armies) within **16** blocks of you, same radius as NPC War Leader soldier aura (`endgame.md`).

**Out of combat (OOC):** no hostile within **16** blocks and no damage taken in the last **30** seconds.

| Phase | Damage reduction | Heal (OOC) |
| --- | --- | --- |
| **Pre-tree** (locked, before Kingstree blooms) | **15%** | **1 heart** every **8** s |
| **Post-tree** | **20%** | **1 heart** every **6** s |

**Solo:** you always count, the aura is self-sustain while exploring or escorting brothers.

**With soldiers:** your troops in **16** blocks get these numbers too. For soldiers, **player War Leader aura** competes with **NPC acolyte aura** and Kingstree / Kingswood / beacons, **strongest DR** and **strongest heal tick** each win separately (`endgame.md`).

#### Other War Leader perks

**Friendly fire (locked War Leader only):**

- You **cannot damage your own soldiers or militia** with direct attacks (melee, arrows, tridents, etc.) unless **sneaking**. Sneaking allows deliberate hits (mutiny / discipline rules in `soldiers-and-villages.md` still apply).
- Other Order specialties and non-acolytes use normal Armies friendly-fire rules.

**Permanent max health (planned):** all locked acolytes gain **+max HP** on lock; **War Leader** gains a **larger** boost than Arcane / Alchemist / Smith (amounts TBD config).

**Pre-tree:**

- **Escort captain:** brothers **following you** on Maelor’s escort within **16** blocks gain **+25%** to their **escort regen** (`endgame.md` brother regen), solo escort survives ambushes better.

**Post-tree:**

- **Stronger aura** (table above: 20% DR, 1 heart / 6 s), main post-tree combat upgrade for solo and for soldiers/allies in range.
- **Post-tree War Leader bonus (Armies / solo):** TBD, **not** campaign boots or camp blocks (those are normal Armies items any player uses for **soldiers**; see below).

**Campaign boots / camp block (clarification):** In **Armies**, **campaign boots** go on **soldiers** (boots + compass); a **camp block** is the field mess/bed soldiers hold. **Any player** with Armies can place a camp and boot soldiers, that is **not** a War Leader perk. **NPC War Leader acolytes** are special: only they can be sent on campaign with boots like a field chaplain (`endgame.md`). Player War Leader power is the **aura** and **escort captain**, not duplicating camp placement.

## Player status (implementation notes)

- Track per-player: basic training done, specialty (pending / locked), Annals progress, brothers delivered, Maelor chapter complete, **necromancer quest** (none / intro / committed / opted_out), **Order blocked** (committed only).
- **Citadel owner** and **relic rules** are separate flags; **Order progress** is blocked by **raised dead**, not by owning the throne.

## Corpse mod compatibility

Quests that depend on **talking to acolytes**, **escorting**, or **recovering items from fights** must remain fair when **[Corpse](https://modrinth.com/mod/corpse)** is installed (NeoForge **1.21.1** target).

**Design requirements (Citadel mod):**

- Acolyte deaths should not **soft-lock** the quest line: quest-critical items (Annals entrusted to the player, brother tokens, etc.) need defined behavior on acolyte or player death (e.g. return to refuge chest, persist on acolyte corpse entity, or player corpse inventory, align with Corpse’s item retention).
- **Play-test** with Corpse on 1.21.1: refuge clear → train → specialty → Maelor rally with player death and acolyte death cases.
- No hard dependency on Corpse; when absent, use vanilla death drops.

See **`compatibility-planned-mods.md`**.

## Config (proposed)

| Key | Default | Meaning |
| --- | --- | --- |
| `acolyteBrothersMinimum` | TBD | Minimum brothers for Maelor’s quest to complete |
| `acolyteBrothersMaximum` | 16 | Brothers countable toward bonus (excludes Maelor) |
| `acolyteNecromancerGearBlockRadius` | 32 | No Order dialogue if player has wand/hood/robe within this range of a refuge acolyte |
