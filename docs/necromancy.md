# Necromancy

The necromancer's hood and robe keep hostile mobs off the player; the wand turns hostile mobs into minions at a cost in hearts.

**Planned player questline:** **`docs/necromancer-path.md`**. Wand use and rules below apply **independently** of the quest until the intro arc’s **commit** choice. **Commit** closes the Order path only. **Opt out** keeps the Order (see acolyte doc); you can remain an active necromancer either way. **Absolution** is blocked by **creating a horcrux** (see below), not by commit.


### Hood and robe

| Wearer | One piece (hood or robe) | Full set (hood + robe) |
|---|---|---|
| **Non-necromancer** (incl. absolved) | Undead won't attack; undead **swarms** ignore wearer | Other hostiles won't attack **except Wither and Warden** |
| **Active necromancer** | Same undead rule | All hostiles won't attack, **including Wither and Warden** |

- The Ender Dragon, guardians, and elder guardians always attack. **Bandits** are not hostiles, robes don't stop them.
- **Durability:** about the same as leather (hood 55, robe 80). Repaired with phantom membranes.
- **Stigma (anyone in hood/robe):** ~**half** of necromancer social penalties (config `stigmaRobeFraction`, default 0.5): e.g. +25% villager trades, 1.25× soldier desertion, **3×** slower recruiters for **necromancers only** (see penalties), 50% chance villagers flee / others' soldiers attack per check.
- **Necromancer-only penalties** while active (full strength, not half): +50% trades in robes, **double fear tax** in **your** villages (robes alone do **not** add fear tax), **3×** recruiter interval, double desertion, mutiny double-roll (soldier mod).

### Wand

- Hitting a hostile mob with a charged wand turns it into a minion that fights for the player, including spiders and creepers.
- The wand holds up to 20 charges.
- Each conversion costs charges by mob:

| Mob | Charges |
|---|---|
| Zombie, skeleton, spider, husk, stray, drowned | 1 |
| Creeper, witch, cave spider | 2 |
| Enderman, wither skeleton, blaze | 3 |
| Bosses | Can't be converted |

### Charging the wand

- The wand is charged with a **bottle of souls**: 1 glass bottle, 1 ghast tear, 1 soul sand (shapeless).
- Each bottle adds 20 charges, a full refill.

### Minions

- **Max minions:** no cap. The permanent heart loss from each wand use is what limits a necromancer's army. Server owners can set a max soldiers and minions per player; the default is no cap.
- **No food or beds:** minions don't use mess stations or post beds, and don't count toward tax coverage.
- **Sunlight:** zombies and skeletons still burn in daylight unless they wear a helmet.
- **Allies:** the player's own soldiers treat his minions as allies. Everyone else's soldiers attack them.
- **Decay:** minions crumble after 30 in-game days (one month) unless the player hits them with the wand again. Renewing costs 1 charge and counts as a wand use, with the same heart risk. Warnings: with 5 days left, a minion gives off faint soul particles; with 1 day left, the particles turn heavy and the player gets a chat message. Right-clicking with the wand lists how many minions expire in the next 5 days.

### Commanding minions

A necromancer can shield minions from the sun, park them at a base, and send them against a marked target at night.

| Item | What it does |
|---|---|
| Grave shroud (minion helmet) | Put on a minion to stop it burning in daylight. In direct sunlight it won't attack or step out of the shade; in shade or at night it acts normally |
| Grave marker (block) | Parks minions. Shift-right-click it with the wand and all following minions stay within 16 blocks of it. Shift-right-click again to have them follow you |
| Cursed effigy (block) | Marks a target. At night, following minions within 64 blocks of it break off and attack everything within 32 blocks of it except the player, his soldiers, and his minions |

- **Effigy timing:** effigies only activate between dusk and dawn. At dawn, minions stop attacking and go back to following the player, or to their last grave marker if he's out of range. The effigy stays in place and works again the next night.
- **Effigy defense:** other players' soldiers attack an effigy only if they come within 4 blocks of it.
- **Unlimited effigies:** a player can place as many as he wants. Minions attack around the nearest active effigy within 64 blocks.
- **Parked minions still decay:** renewal timers keep running at a grave marker, so a hidden army still needs visits.

Example raid: park the army at a grave marker in your base, sneak a cursed effigy into the enemy base, return and shift-right-click the marker so the army follows you, lead it within 64 blocks of the effigy, and wait for night.

### Risks

- Each wand use: 3% chance to lose a heart permanently in that world, and a 5% chance of a short side effect: blindness (2 minutes), slowness (30 seconds), or weakness (30 seconds).
- Using a wand makes the player a necromancer, with heavy penalties (see Necromancer penalties).
- A necromancer's own minions don't count as hostile mobs when he makes soldiers (see Soldier requirements).

- A player can't drop below 1 heart of max health from permanent loss. At 1 heart, the wand keeps working with no further heart cost, on purpose: a necromancer willing to live on 1 heart can raise as many minions as he wants, limited only by charges, mobs, and the server cap.

Expected heart loss from wand uses (conversions and renewals both count):

| Wand uses | Permanent hearts lost (avg) | Hearts left (from 10) | Chance of losing at least 1 |
|---|---|---|---|
| 10 | 0.3 | about 10 | 26% |
| 50 | 1.5 | about 8.5 | 78% |
| 100 | 3 | about 7 | 95% |
| 200 | 6 | about 4 | over 99% |
| 300 | 9 (floor) | 1 | about 100% |

A 100-minion army costs about 3 hearts to build, then about 3 more each month to renew.

### Necromancer penalties

A player becomes an **active necromancer** the first time he uses a wand (convert or renew), unless the questline gates the wand until later (**TBD**). Penalties below apply while active.

**Absolution:** clears **active** necromancer status **only if** the player has **never created a horcrux** (player horcrux ritual below). **Commit** to the necromancer quest at the Well does **not** block absolution. **First horcrux = commitment to the necromancer life:** absolution **forever impossible** afterward (potion fails, **TBD** UX). **Destroying** horcruxes does **not** restore eligibility. A committed quest necromancer who **never** made a horcrux may still **retake the citadel**, **heal the tree**, **absolve**, and **sit the throne** as a non-active necromancer (`citadel-claim.md`).

While active and **eligible** for absolution:

- **Slow recruiting:** recruiters still work, but take **3×** as long between recruits (configurable).
- **Distrust:** his soldiers have double the normal desertion chances from hunger and morale (soldier mod).
- **Bad reputation:** while wearing the hood or robe, villagers charge him 50% more in trades.
- **Fear tax:** villagers in **his** claimed villages pay double taxes (not applied to non-necromancers in robes alone).

### Potion of absolution

Drinking it (when still **eligible**: no player horcrux ever created) clears active necromancer status: penalties end. **Permanently lost hearts stay lost.** His **wand crumbles.** **Minions** revert to **hostile** and stop following. Using a new wand makes him active again. Hood/robe **combat** rules stay as for any non-necromancer wearer (Wither/Warden can attack).

After the player **creates a horcrux**, the potion fails or has no effect (**TBD**).

1. **Potion of penance:** brew an awkward potion with an enchanted golden apple.
2. **Potion of absolution:** brew the potion of penance with a nether star.

Enchanted golden apples only come from loot chests, and nether stars only from killing the Wither, so absolution is a major undertaking.

### Recipes

Hood (black wool, phantom membrane, bone):

|  |  |  |
|---|---|---|
| Black wool | Phantom membrane | Black wool |
| Black wool | Bone | Black wool |
|  |  |  |

Robe (black wool, phantom membrane, soul sand):

|  |  |  |
|---|---|---|
| Black wool |  | Black wool |
| Phantom membrane | Soul sand | Phantom membrane |
| Black wool | Black wool | Black wool |

Wand (echo shard, wither skeleton skull, blaze rod), diagonal:

|  |  |  |
|---|---|---|
|  |  | Echo shard |
|  | Wither skeleton skull |  |
| Blaze rod |  |  |

Grave shroud (black wool, soul sand):

|  |  |  |
|---|---|---|
| Black wool | Black wool | Black wool |
| Black wool | Soul sand | Black wool |
|  |  |  |

Grave marker (soul lantern, bone block, cobblestone):

|  |  |  |
|---|---|---|
|  | Soul lantern |  |
|  | Bone block |  |
| Cobblestone | Cobblestone | Cobblestone |

Cursed effigy (carved pumpkin, bone, hay bale, stick), a scarecrow shape:

|  |  |  |
|---|---|---|
|  | Carved pumpkin |  |
| Bone | Hay bale | Bone |
|  | Stick |  |

**Horcrux compass** (shapeless): **compass**, **phylactery shard**, **soul sand**, **wither rose** → 1 horcrux compass. Craftable in unlimited quantity; points to nearest horcrux chunk (`necromancer-path.md`).

### Rogue necromancers and phylactery shards

Hunting **rogue necromancers** earns rewards found nowhere else.

- **Sites (author):** **~half** as dense as **pillager outposts** (~2× spacing). **Rogues = necromancer NPCs**, not bandits. **Site roll:** cave crypt, **surface-entrance crypt**, **taken village**, or **dark tower** (`necromancer-path.md`). Killing the rogue clears the site; a new one moves in after the next **new moon** (proposed).
- **Horcrux compass:** **craft many**; **no range cap**; **always** includes **your own** horcruxes; points to **nearest horcrux** chunk (player or wild lich). **Stronger early** (few wild decoys), **weaker late** as exploration adds closer rogue horcruxes, chunk-level only (`necromancer-path.md`).
- **Wild lich (term):** a rogue with **one horcrux** (item) in a **hidden chest**: **very rare**; almost all rogues have **none** (`necromancer-path.md`).
- **Optional preview:** if you find one, same pseudo-death / ping / destroy rules as player liches, **not** a required tutorial step.
- **Phylactery shard:** dropped by rogue necromancers (1–2 each) and the **two named bound necromancers** in the citadel (`endgame.md`, names TBD). A player necromancer killed by another player drops 1, at most once per in-game day.
- **Horcrux on rogues:** **~4%**, **max one** horcrux each; config `necromancerRogueHorcruxChance`. No fixed tutorial site.
- **Bone whistle:** 2 phylactery shards + a bone + a goat horn. Blowing it summons 3 wolves that fight for the player for 3 minutes, once per in-game day; 20 uses (proposed). **Not** the same as committed-quest **flutes/pipes** (`necromancer-path.md`).

### Horcrux (player-created)

**Not quest-gated**: any player may attempt the ritual **when they have** the ingredients (`necromancer-path.md`). **Many horcruxes** allowed; each costs **1 permanent heart** until the **1-heart floor**. **Casual** necromancers often use shards for the **bone whistle** only; **quest flutes / god wizard set / relics** are optional power-item fuels. **Wild liches** in crypts demonstrate destroy flow first (see crypts above).

**Ritual:** **Phylactery shard** + **any quest flute**, **full god wizard armor set**, or **any kings’ relic** (consumed) in **hotbar** while killing a **soul** on the closed list: **any player**, **any acolyte** (NPC or Order player), or **any End flute tyrant** (`necromancer-path.md`). On **first successful** ritual, the player **permanently** loses **absolution** eligibility (`Potion of absolution` above).

**Storage:** Each horcrux in a **chest** anywhere; passives while **≥1** exists. **Ping:** environmental, dark particles, **poisoned animals** in horcrux chunk (`necromancer-path.md`).

**Quest flutes:** **never damage the player** who plays them; **one per type per world** on **End flute tyrants**; **any player** may loot and use; **relic-style** indestructibility; **1 charge per play**: **non-necromancers** refill after **every** use; **committed necromancers** get **3** plays per refill; **recharge** = craft **flute charge** (glowstone dust + blaze powder + ender pearl), then shapeless **flute + charge** (`necromancer-path.md`); removed only by **ritual consumption**.

**Passives:**

- **Immune** to **poison** and **harm / damage potions** (long **cursed armor** wear).
- **Lethal damage** → **pseudo-death** (3 min no clicks, move, keep inv). **No teleport** on trigger.
- **True death** only when **all** horcruxes are **destroyed** (not merely dropped or stolen).

**Destroy horcrux:** survives normal mining/fire. **Primary (hunter finish):** **throw into the Dragon’s Well**: **anytime** (dragon alive or dead). **Also:** ghast fireball, wither, lightning (rod/channeling), optional **breath bottle** splash. Config `necromancerHorcruxDestroyers`. **Intro quest:** the mother’s hint covers **monster / storm** methods only, **not** the Well (`necromancer-path.md`). **God wizard armor:** **indestructible** like relics; **binding**: no manual unequip; **pseudo-death keeps armor**; consumable only via **horcrux** (or **TBD** rituals). True death drops pieces normally.

**Consider:** active horcrux **corrupts weak nearby units** (soldiers, etc.), **`necromancer-path.md`**; not implemented.

**Config:** **`necromancerHorcruxEnabled`** (default **true**). Must not bypass **relic ban** or throne rules.
