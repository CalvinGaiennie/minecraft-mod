# Threats and mobs

## Threats

Three new enemy systems pressure villages: raids, roving swarms, and bandits.

### New moon raids

- Raids can happen on the 3 darkest nights: the new moon and the nights before and after it. Each village near a player has a 40% chance of a raid on new moon night and 15% on the nights either side (higher near spawn until the Black Citadel falls), rolled separately, so a village can get none, one, or several.
- Only hit villages a player is currently near.
- Work differently from vanilla raids.

- **Warning:** about 1 minute before a raid arrives, a distant war horn sounds.
- **One big push:** raiders spawn 64-96 blocks out and march in together from one direction, instead of coming in waves.
- **Raiders (kept weak):** mostly pillagers and vindicators, led by a raid captain with modest buffs (40 HP, +20% damage). Witches: 0-2 per raid. Evokers and ravagers only in large raids, at most 1 of each.
- **Targets:** soldiers and iron golems first, then civilians. Raiders also try to wreck post beds and mess stations.
- **Ending:** the raid ends when every raider is dead, or at dawn. Raiders still alive at dawn loot the tax box and leave.
- **Winning:** soldiers earn kills toward rank-ups, making raids the main way seasoned soldiers and heroes are made. The raid captain drops emeralds, a raid banner, and gear.
- **No Bad Omen:** these raids come from the moon, not from killing pillager captains. Vanilla raids still work as normal alongside them.
- **Leaving mid-raid:** if the player leaves and the village unloads, the raid pauses. It picks up where it left off when a player returns, even if dawn has passed in the meantime.

Raid size is random, not tied to the village's defenses:

| Raid size | Raiders | Chance |
|---|---|---|
| Small | 3-6 | 60% |
| Medium | 7-10 | 30% |
| Large | 11-14 | 10% |

### Roving swarms

- Travel village to village killing villagers.
- When two swarms meet, they merge.
- Any hostile mob type can swarm with its own type: zombie swarms, skeleton swarms, spider swarms, and so on. Swarms only merge with swarms of the same type.
- **Spawning:** each night, every player gets one roll with about a 5% chance of a swarm spawning in the area around him: 5-10 mobs of one type. A player gets no new swarm while 2 of his swarms are still alive (proposed).
- **Size cap:** swarms max out at 30. Extra mobs from a merge stay as a second swarm. Creeper swarms max out at 8.
- **Growth:** zombie swarms grow by turning villagers they kill into zombie villagers. Other types only grow by merging.
- **Movement:** a swarm heads for the nearest village within 256 blocks, and moves on once that village has no villagers left.
- **Daylight:** zombie and skeleton swarms hide in shade or caves during the day and move at night. Spider and creeper swarms move any time.
- **Targets:** everyone, including villagers, soldiers, golems, players, and bandits. Players wearing the **necromancer hood or robe** are ignored: **undead swarms** if one piece; **other swarm types** if full set (same rules as hood/robe combat pacification).
- **No despawning:** swarm mobs stay until killed.
- **Necromancy:** necromancers can convert swarm mobs into minions; each conversion is a normal wand use with the usual heart risk.
- **While unloaded:** a swarm in unloaded chunks keeps marching toward its target as stored data, without fighting. When a player loads the area, it appears where it would have arrived. Combat only happens in loaded chunks.

### Bandits

- Formed from deserting soldiers (50% keep their items).
- Band together and roam like swarms, but stay at a captured village until its mess stations are empty.
- Kill only soldiers, not villagers, and destroy tax boxes.
- A band without a leader forms one: after 1 day, the bandit with the most kills takes over.
- **Joining up:** a new bandit heads for the nearest band within 256 blocks. If there isn't one, he wanders alone until he meets other bandits. A band forms at 3.
- **Band size:** bands max out at 12. When a 13th bandit joins, the band splits into two bands of about 6, each forming its own leader. A world holds at most 25 bands (proposed); past that, new runaways and troubled youth stay villagers, and deserters who would turn bandit become villagers instead.
- **Growth:** bands grow through new deserters, troubled youth, lone recruiting, and villagers who run away (below).
- **Necromancer robes don't protect against bandits,** since bandits are villagers, not hostile mobs.
- **Bandits and swarms** are enemies and fight each other.
- **Bandit camps:** small structures that generate in the world like pillager outposts, each with 3-5 bandits and a leader. Every world has some bands from the start.
- **Lone recruiting:** a lone bandit can turn a nitwit or unemployed villager in an unguarded village (no soldiers) into a bandit, once a day.
- **Runaways:** every adult non-soldier villager has a 0.1% daily chance to leave home and become a wandering bandit. A 20-villager village loses about one every 50 in-game days.
- **Leader buffs:** 50 HP, +25% damage, and 50% knockback resistance. Bandits near him get +10% damage.
- **Killing the leader:** the band breaks into lone bandits who wander and regroup. The leader drops his gear and the band's loot.
- **Targets:** bands go for the most weakly defended village nearby, measured by soldiers per villager.
- **Occupation:** villagers flee to havens and taxes stop. Bandits kill soldiers, sleep in post beds, and each bandit eats one food item a day from the mess stations. When the mess stations are empty, they destroy the tax box and leave.
- **Loot:** bandits take the tax box contents before destroying it, and the leader carries them. Kill him and you get your taxes back.
- **While unloaded:** bands follow the same rule as swarms. They keep traveling while unloaded, but only occupy villages, eat, and fight in loaded chunks.

If the captured village is owned by a player and has militia, bandits instead:

1. Burn it, destroy crops, and kill animals.
2. Kill all militia and half the other villagers.
3. Breed, with an 80% chance of producing troubled youth villagers.

Troubled youth can never take a trade, so they can never become traders or militia. They can become soldiers, with a 5% daily chance to desert. Left unemployed, they have a 10% daily chance to turn bandit.

### Cave hideouts

- **What they are:** bandit lairs built into large caves, rarer than bandit camps, each with 6-10 bandits and a leader. Inside are caltrop corridors and pit traps, so they teach players to watch their footing.
- **Inside:** sleeping bays, a chest of stolen taxes, and a cell with 1-2 captive villagers who join the player's nearest village as unemployed villagers when freed. A small chance of a rare chronicle.
- **Behavior:** hideout bandits follow normal band rules but travel through caves when they can.

### Underground villages

Player-made cave villages mostly work as is, since village areas already run from bedrock to sky. Five things need rules:

| Mechanic | Problem underground | Rule |
|---|---|---|
| Recruiting | Dark caves spawn mobs inside the village, and a hostile mob nearby fails soldier creation 90% of the time | No change; lighting is the player's job, and The Recruiter's Handbook says so |
| New moon raids | Raiders spawn 64-96 blocks out on the surface and may find no way in | Raiders spawn at the nearest open cave or surface spot with a path in. With no path at all, the raid brings 2 pillager sappers who dig at sapper speed |
| Swarms | Zombie and skeleton swarms hide in caves by day, so they can wander into a cave village in daylight | Keep it: cave villages fight swarms by day as well as night |
| Night penalties | Caves stay dark all day | Penalties only apply at night, whatever the light underground |
| Sieges | Siege ladders are useless underground | Sappers, axemen, and plank bridges over ravines carry cave sieges; village protection slows tunneling as on the surface |

## Animal farmers

Five new villager types raise animals: cattle, chicken, pig, and horse traders, plus sheep herders. Each one tends a herd, farms its own feed, and supplies the village with soldier food and leather. They pay these taxes instead of the biome's base tax item.

- **Job blocks:** created only with expensive, player-crafted job blocks.
- **Pens:** farmers don't build or repair pens. Wild ranches generate with large fenced pens, like vanilla village farms but much bigger, and players are responsible for fixing them. In a player's village, the player builds the pen.
- **Feed:** each farmer plants and harvests the crop his animals breed with. Horse traders also need gold from players to make golden carrots.
- **Herd cap:** each farmer stops breeding at his max herd size.
- **Horse traders:** breed horses, and also donkeys and mules if given a donkey or two. All three count toward his herd cap.

| Farmer | Job block | Herd cap | Farms | Pays taxes in |
|---|---|---|---|---|
| Cattle trader | Feed trough | 8 | Wheat | Raw beef, leather |
| Sheep herder | Shearing post | 10 | Wheat | Raw mutton |
| Chicken trader | Nesting box | 16 | Wheat (for seeds) | Raw chicken |
| Pig trader | Slop bucket | 10 | Carrots | Raw porkchops |
| Horse trader | Hitching post | 8 | Carrots (made golden with players' gold) | Leather |

**Trades:**

- They sell their products to players for emeralds. Horse traders sell for gold instead.
- They also sell gear like feathers, saddles, shears, and buckets.
- They buy the crops their animals breed with (wheat, carrots, golden carrots) to top up their own farming.

**Wild ranchers:** animal traders also spawn outside villages, near each village, with 2-4 of their animals. They build a small pen and farm where they spawn. Each village gets 0-3 nearby ranches, averaging a little over 1, so most villages have one but some are duds:

| Ranches near a village | Chance |
|---|---|
| 0 | 25% |
| 1 | 40% |
| 2 | 25% |
| 3 | 10% |

That averages 1.2 ranches per village. Like vanilla villagers, a wild rancher can be moved to a player's village, where he claims a free matching job block. Which traders spawn depends on the biome:

| Biome | Animal traders that spawn |
|---|---|
| Plains | Cattle, horse |
| Savanna | Horse, cattle |
| Meadow | Sheep |
| Forest | Pig, chicken |
| Taiga | Sheep, pig |
| Snowy plains | Sheep |
| Jungle | Chicken |

## Vanilla mob changes

| Mob | Change |
|---|---|
| Zombies | Can break wooden doors on every difficulty (vanilla only allows it on Hard) |
| Spiders | Drop a web block when damaged. Each web dissolves after a random time between 30 seconds and 7 minutes |
| Endermen | Attack anything with a potion effect. Soldier rank bonuses are built-in stats, not potion effects, so they don't count; beacon effects don't count either; real potions and tripping do |
| Skeletons | Arrows have an 80% chance to pass through undead, creepers, and spiders |
| Mob spawners | Craftable but expensive (see Mob spawners) |
| Iron golems | Attack anything that attacks a villager (soldiers and militia included), and nothing else. Attacking a soldier doesn't affect the player's reputation with the village |
| Zombies, skeletons, and combat villagers | Break glass blocks and panes in their way, the way zombies break doors. Scope to settle: see Effect on players who ignore the mod |

### Mob spawners

Players build a spawner in two steps: craft an empty cage, then bind a mob's souls to it.

1. **Empty spawner cage:** 4 diamonds in the corners, 4 iron bars on the sides, and a netherite ingot in the center.
2. **Setting the mob:** right-click the cage with a bottle of souls while holding that mob's drop:

| Mob | Drop needed |
|---|---|
| Zombie | 16 rotten flesh |
| Skeleton | 16 bones |
| Spider | 16 string |
| Creeper | 16 gunpowder |
| Enderman | 8 ender pearls |
| Blaze | 8 blaze rods |

Bosses, villagers, and the mod's own mobs can't be put in a spawner.

## Effect on players who ignore the mod

As written, the mod would noticeably change normal play even for players who never make a soldier. The worst offenders are swarms, mod raids on natural villages, and the enderman and golem changes; each has a proposed fix.

| System | What a player ignoring the mod sees | Severity | Proposed fix |
|---|---|---|---|
| Roving swarms | A 5% roll per player per night: about one swarm every 20 nights per player, or every 5 nights with 4 players on. Swarms never despawn, travel while unloaded, kill every villager they reach, and zombie swarms grow. Trading halls and natural villages get wiped | High | Only roll swarms for enlisted players; swarms that form can still hit natural villages, but only while an enlisted player is within 256 blocks of the swarm |
| New moon raids | Every village near any player rolls 40% + 15% + 15% per lunar cycle: about 0.7 raids every 8 days, 1.1 near spawn before the citadel falls. Natural villages defend with 2 militia in one armor piece each | High | Mod raids hit only claimed villages by default; a config option adds natural villages |
| Endermen and potion effects | Any player with a potion effect, including beacon effects, Night Vision, Fire Resistance, and Hero of the Village, is attacked by every enderman nearby | High | Apply only to non-player targets (soldiers, militia, villagers), and never count beacon effects |
| Iron golems | Golems stop attacking hostile mobs unless a villager is hit, so golem-guarded bases and villages lose their mob defense | High | Keep vanilla golem targeting and only add "attacks anything that attacks a villager" |
| Runaways | 0.1% per villager per day: a 30-villager trading hall loses a villager, trade and all, about every 33 days | Medium | Only from claimed villages, and only villagers who can path out of the village |
| Natural militia | 2 villagers per natural village (3-4 near spawn) are armed traders who can't trade | Medium | Arm nitwits and unemployed villagers first; arm traders only if there aren't enough |
| Zombie door breaking | Zombies break wooden doors on Easy and Normal, not just Hard | Medium | Only inside claimed village areas, or only for swarm zombies |
| Glass breaking (new) | If every zombie and skeleton breaks glass, every window in every base becomes a door | High as asked | Soldiers, militia, and bandits only during assaults; zombies and skeletons only in swarms, raids, or as minions; ordinary mobs only on Hard; follows mobGriefing |
| Shadow over spawn | More raids and militia near spawn until someone beats the citadel | Medium for spawn bases | Only claimed villages near spawn get the extra raids |
| Spider webs | Temporary webs in caves and bases | Low | Config switch only |
| Skeleton arrows | Pass through undead, creepers, and spiders, so fewer mob infights | Low | None |
| New structures | Bandit camps, hideouts, crypts, acolyte refuges, ranches, fortresses, and a citadel with an unbreakable road 1,000-2,000 blocks from spawn | Low | Generate only in new chunks; added to an existing world, place the citadel in ungenerated land |
| Bandits | Lone bandits recruit nitwits from natural villages, since those have no soldiers | Low | Bandits only burn player-owned villages with militia; lone recruiting only near enlisted players |

**Proposed rule, enlisted players:** a player becomes enlisted the first time he makes a soldier, claims a village, or uses a wand. Swarm rolls, mod raids, and runaways only happen around enlisted players and their villages. Mod raids only hit claimed villages, but swarms near an enlisted player can hit natural villages too, so the countryside around him feels the war. A server option can enlist everyone for all-out war servers. With these fixes, a player who ignores the mod sees new structures, armed villagers, and a few extra mobs, but keeps his villages and trading halls.
