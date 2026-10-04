# Necromancy

The necromancer's hood and robe keep hostile mobs off the player; the wand turns hostile mobs into minions at a cost in hearts.

### Hood and robe

| Worn | Effect |
|---|---|
| Hood or robe (one piece) | Undead mobs (zombies, skeletons, husks, strays, drowned, phantoms, wither skeletons) can't attack him |
| Hood and robe (full set) | All hostile mobs can't attack him |

- **Full protection:** while he wears it, those mobs can't attack him, even if he attacks them first. This includes the Wither and the Warden. The Ender Dragon, guardians, and elder guardians are unaffected and always attack.
- **Durability:** about the same as leather (hood 55, robe 80). Repaired with phantom membranes.
- **Villages:** villagers who see a robed player flee to a haven block. Other players' soldiers attack him on sight; his own soldiers don't.

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

A player becomes a necromancer the first time he uses a wand. Until he drinks a potion of absolution, he lives with these penalties:

- **No recruiters:** his recruiter boxes won't take a recruiter, and any recruiter he had goes back to being a regular soldier. He can still make soldiers by hand.
- **Distrust:** his soldiers have double the normal desertion chances from hunger and morale.
- **Shrinking army:** he can have at most 3 soldiers per heart of max health (30 at full health). If he drops below what his army needs, his newest soldiers desert until he's under the limit.
- **Bad reputation:** while wearing the hood or robe, villagers charge him 50% more in trades.
- **Fear tax:** villagers in his villages pay double taxes, because they fear him.

### Potion of absolution

Drinking it clears necromancer status: recruiters work again and every penalty above ends. Permanently lost hearts stay lost, and his wand crumbles. Using a new wand makes him a necromancer again.

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

### Necromancer crypts and phylactery shards

Hunting rogue necromancers earns rewards found nowhere else.

- **Necromancer crypts:** rare underground structures, each home to a rogue necromancer with 6-12 minions. Killing him clears the crypt; a new necromancer moves in after the next new moon (proposed).
- **Phylactery shard:** dropped by rogue necromancers (1-2 each) and the citadel's necromancers. A player necromancer killed by another player drops 1, at most once per in-game day.
- **Bone whistle:** 2 phylactery shards + a bone + a goat horn. Blowing it summons 3 wolves that fight for the player for 3 minutes, once per in-game day; 20 uses (proposed).
- **Blessed incense:** a phylactery shard + glowstone dust + a honey bottle. Given to a cleric in the player's own village, it lets him cure zombie villagers and zombie soldiers within 32 blocks of his brewing stand over one day, with no weakness potion or golden apple. Zombie soldiers cured this way return as soldiers 90% of the time instead of 60% (proposed).
