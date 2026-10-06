# Soldiers and villages

## Villager roles

A villager's job and setup decide whether a weapon makes them a soldier or a militiaman.

| Role | How they get it | Gear allowed | Notes |
|---|---|---|---|
| Soldier | Villager with no trade is given a weapon, chestplate, and helmet, and has a free post bed and a stocked mess station | Full armor | Unequipping can turn them into a trader, but a trader can never become a soldier again, unless he served as a soldier before he ever traded (see Veterans) |
| Militiaman | Trader or unemployed villager is given a weapon. Unemployed villagers who meet every soldier requirement become soldiers instead | Helmet, chestplate, and boots; no leggings or shields | Can't trade while in the militia; sometimes run from enemies |
| Bandit | Soldier deserts (50% chance to keep their items) | Whatever they kept | See Threats |
| Troubled youth | Born from bandit-occupied villages (80% chance) | n/a | Can never trade; can become soldiers or bandits (see Bandits) |

- Villagers with a trade cannot become soldiers, unless they served as soldiers before they ever traded.
- Soldiers cannot breed with soldiers, bandits cannot breed with bandits, and soldiers and bandits cannot breed with each other.
- Out of combat, soldiers and militiamen pick up any item they're allowed to use if that slot is empty and the item is on the ground. They never take items from chests.

### Inventory

| Slot | Soldier | Militia |
|---|---|---|
| Weapon | Melee weapon (sword or axe) | Either a melee weapon or a bow, not both |
| Shield | Shield | None |
| Bow | Bow, crossbow, or pickaxe (sappers) | (uses the weapon slot) |
| Arrows | Up to 16 | Up to 16 (bow militia only) |
| Utility | One boat, only while carrying out a boat plan | One boat, only while carrying out a boat plan |
| Armor | Helmet, chestplate, leggings, boots | Helmet, chestplate, boots |

- **Switching:** soldiers use their bow at range and switch to melee weapon and shield when an enemy gets within 4 blocks, or when they run out of arrows. If a soldier has to switch away from the item in his bow slot during combat, he drops it: an archer switching to melee drops his bow, and a sapper drawing his sword drops his pickaxe. Switching from his melee weapon to his bow never drops anything. Dropped items can be picked back up after the fight.
- **Bow militia** have no melee weapon, so they back away to keep their distance; near their home bed, they fight with their fists.
- **Refilling arrows:** at 16 arrows max, archers refill often from arrow bins at home or the camp block on campaign, and by picking up shot arrows.
- Special armor (campaign boots, nightwatch, royal guard) uses the normal armor slots.
- **Depositing arrows:** a soldier or militiaman carrying arrows without a bow or crossbow drops them off at the nearest camp block or arrow bin.

### Militia

Militia are traders or unemployed villagers who take up arms: a cheap home guard that backs up soldiers.

- **Gear:** helmet, chestplate, and boots. No leggings or shields.
- **Food:** they eat 1 meal a day from the mess station, but don't need a post bed. If they go 2 days without food, they drop their weapon and go back to trading.
- **Ranks:** militia have one rank. At 10 kills a militiaman becomes seasoned militia: +10 HP, and he no longer flees from being outnumbered. His kills carry over if he upgrades to soldier.
- **Orders:** they follow horns, but not flares.
- **Campaigns:** they can go on campaign wearing campaign boots.
- **Home bonus:** +25% damage within 128 blocks of their home bed.
- **Disarming:** taking away their weapon turns them back into a normal villager.
- **Trade-offs:** they flee from fights going badly (see Fleeing), don't count toward tax coverage, and make bandits burn the village instead of just occupying it.
- **Upgrading:** an unemployed militiaman becomes a soldier automatically as soon as he meets every soldier requirement. Traders in the militia can never become soldiers.
- **No trading:** militiamen can't trade while in the militia. Disarming them lets them trade again.

**Fleeing:** militia use the vanilla villager panic behavior. They flee when any of these happen:

- **Low health:** below 50% health.
- **Badly outnumbered:** at least 2 enemies for every friendly fighter within 16 blocks.
- **A comrade dies:** 30% chance when a nearby militiaman dies.
- **Mounted enemies:** 92% chance, as before.

Fleeing militia drop their weapon, which turns them back into normal villagers until a player re-arms them. They flee to their camp block on campaign, or to the nearest fallback block at home. If there's neither, they just run away from the enemy. None of this applies within 128 blocks of their home bed: there, militia never flee.

## Ownership and allies

- **Owning a village:** the first player to have both a soldier and a mess station in a village claims it, including natural villages. The claim holds until all of the owner's mess stations in the village are destroyed and another player places his own. Soldiers belong to the owner of the village they were recruited in. A village changes hands the moment another player places his mess station after every one of the owner's has been destroyed. Then its soldiers, militia, post beds, recruiters, and tax box all convert to the new owner; soldiers away on campaign keep serving the old owner until they come home, then convert.
- **Ally book:** each player keeps an ally list through an ally book (recipe: book and quill + emerald). Right-clicking it opens a screen to add or remove players and set their rank. Allying is one-way: adding someone protects them from your forces and gives them their rank's powers, but doesn't make you their ally. The list is saved to the player, not the book, so losing the book doesn't lose allies. It saves player IDs, so name changes don't matter.
- **Allies:** your soldiers, militia, and minions never attack allies, and your soldiers and your allies' soldiers don't fight each other.
- **Village area:** vanilla has no real village object, so the mod defines one. A village needs at least one mess station to exist, but its area is set by surveyor's markers. Each marker claims the area around it, from bedrock to the build limit, with a radius that grows with the village's size: 32 blocks for a hamlet (1-5 members), 48 for a small village (6-10), 64 for a village (11-20), 96 for a town (21-35), and 128 for a city (36+). The village's first mess station claims land around it just like a marker, so every village has some land. On top of that, the village can place one surveyor's marker for every 5 members it has in total, wherever they live, so land can only be claimed by growing the village. If the village shrinks, its existing markers keep working, but no new ones can be placed until the population supports them.
- **Members:** villagers whose bed or job block is inside the area belong to the village, including soldiers and militia. Members decide the village's size. Soldiers and militia away on campaign still count as members, since their beds stay reserved at home.
- **No overlap:** a player can't place a mess station or surveyor's marker inside another player's village area unless he's that owner's co-owner, in which case it counts as the owner's. When a village grows into another's area, its growth stops at the border, and land both could claim belongs to the older village.
- **Natural villages:** a natural village is a vanilla village bell with at least 3 villagers within 48 blocks and no mess station; its area is 48 blocks around the bell, and it becomes a normal mod village the moment someone claims it with a mess station and a soldier. Natural villages spawn with 2 militiamen (more near spawn while the Black Citadel stands), traders armed for defense. Each gets one random armor piece (helmet, chestplate, or boots; leather, chainmail, or iron) and an iron sword or iron axe. Each has a 25% chance to carry a bow and 16 arrows instead.
- **Strangers:** by default, soldiers leave non-allied players alone unless they're wanted, steal from the tax box, or are a charge horn target. Servers can turn on an option for soldiers to attack any non-ally on sight.
- **Village protection:** inside a claimed village's area, from bedrock to the sky, players who aren't the owner or his allies break blocks more slowly. Protection depends on how many of the owner's soldiers are actually inside the village area at the time, so an army away on campaign leaves its home softer: 0 soldiers, normal speed; 1-9, 3 times slower; 10-24, 5 times slower; 25 or more, 10 times slower. This covers walls, gates, and the ground under the walls. Explosions caused by non-allied players do no block damage inside a claimed village.
- **Kingdom:** all the villages a player owns together make up his kingdom. Kingdom titles count them, and kingdom-wide effects (like the golden age) apply to all of them.
- **Surveyor's marker (block):** markers owned by the same player whose areas overlap or touch form one village, chaining across as many markers as he places. A group of markers only counts as a village if it contains at least one of his mess stations. Markers can only be broken with a surveyor's knife. While the village has any mess station left, only the owner and his co-owners can break them. Once every mess station in the village is destroyed, the claim ends and anyone with a surveyor's knife can break its markers. Recipe (proposed): lodestone-style, a compass surrounded by stone bricks with an iron ingot.
- **Surveyor's knife (item):** lets the owner or a co-owner pick up their markers instantly. Right-clicking with it shows the village's borders for a few seconds. Recipe (proposed): iron ingot, stick, and string.
- **Merging and splitting:** if two of a player's villages grow until their marker areas touch, they become one village; if both had tax boxes, the older stays active and the other becomes plain storage. If removing a marker breaks a village into two groups, each becomes its own village; the group with the tax box keeps it, and the other needs its own.
- **Size is per village:** members are counted across the whole connected village, so every marker in a city gets the city radius.

- **Abandoned kingdoms:** if an owner hasn't logged in for 60 in-game days (proposed), his villages lose village protection and stop recruiting, and another player can take them by placing a mess station once all of his are destroyed. His soldiers keep defending. In-game days only pass while the world is running, so on a server this counts days other players are online.

- **No building in enemy land:** players who aren't the owner or his allies can't place blocks anywhere in a claimed village's area, bedrock to sky, while at least one of the owner's soldiers is in the village. With no soldiers home, they can place only one block every 2 seconds (proposed). Siege gear deployed by soldiers (siege ladders, plank bridges, boats) is exempt. This stops players pillaring over walls or boxing in defenders. The ban also covers emptying water and lava buckets; lighting fires is still allowed.
- **No building in the citadel:** no one can place blocks inside the citadel's walls until the Corrupted King falls. After that, only the citadel's owner and his allies can.
- **Building under attack:** while a village is under attack (same test as the royal guard post, except a blockade alone doesn't count), everyone in its area, the owner included, can place at most one block per second (proposed). Defenders can still repair walls and replace sapped blocks, but can't instantly wall themselves in mid-fight.

| Rank | What they can do |
|---|---|
| Friend | Not attacked by your forces |
| Officer | Can use horns and flares on your soldiers, and use attack plans at your camps |
| Co-owner | Everything an officer can do, plus open your tax box, place recruiter boxes, and manage your post beds |

## Soldier behavior

Soldiers rank up with kills, follow a daily routine, and have a few weaknesses.

### Ranks

| Kills | Rank |
|---|---|
| 5 | Seasoned soldier |
| 20 | Hero |
| 50 | Legend |

Each rank gets combat effects, including archery effects.

What counts as a kill:

- **Counts:** hostile mobs (zombies, skeletons, spiders, creepers), raiders (pillagers, vindicators, ravagers, evokers), bandits, and enemy soldiers or militia.
- **Doesn't count:** animals, friendly villagers, and tiny mobs like silverfish and endermites.
- **Credit:** whoever lands the killing blow gets the kill.

### Combat

- Any mob hit with the gold-outline arrow is targeted first by soldiers and militiamen.
- Soldiers use up real arrows: each shot spends one. Soldiers with a bow pathfind to arrow bins until they run out, then to the nearest rampart. If an enemy gets close, they switch to melee (see Inventory).
- Soldiers can light fire arrows at any open flame, one arrow at a time: light one, fire it, light the next. Fire arrows can set blocks, crops, and buildings on fire.
- Enemy soldiers flee from mounted players 50% of the time; militia flee 92% of the time.
- Soldiers can carry shields, which show the village banner, but don't block with them. Militia can't carry shields.
- **Home bonus:** soldiers get +10% damage within 128 blocks of their own post bed.
- **Arrow recovery:** out of combat, soldiers pick up any shot arrows stuck in the ground within 16 blocks that they can reach: their own, other soldiers', players', and skeletons'. Tipped and spectral arrows stay what they are. Arrows they can't reach, like ones high in walls or across water, are skipped.

- **Friendly arrows pass through:** arrows from a player's soldiers and militia, and his own arrows, fly through him, his soldiers, his allies, and their soldiers.
- **No accidental hits:** a player's melee hits do nothing to his own soldiers unless he's sneaking.
- **Careful fire arrows:** soldiers won't shoot fire arrows at a target with a friendly within 3 blocks of it.
- **What still hurts friends:** explosions, lava, fire, and (for most players) **sneaking** melee/ranged hits. Friendly kills (see Mutiny) are always on the player.
- **Order War Leader acolyte (locked):** you **cannot** damage **your** soldiers or militia with direct attacks **unless sneaking** (`acolyte-path.md`). Non-sneak friendly hits are blocked/cancelled. Sneaking still counts as a deliberate friendly kill for mutiny if applicable.

### Gear wear

- Soldiers' and militia's armor, weapons, and shields lose durability the way a player's do. They can't use XP, so Mending never repairs their gear.
- **Replacements:** when an item breaks, the soldier goes to his recruiter box for a replacement of the same kind from the issued gear. Until then, a soldier without a weapon fights with his fists.
- **Militia without a weapon** don't fight with fists. They flee, unless they're within 128 blocks of their home bed, where they fight with their fists to defend it.
- **Death drops:** soldiers and militia drop everything they're wearing and holding when they die, at its current durability. Anyone can pick it up, so the winner of a battle can collect the loser's gear.

### Daily routine

- Each day, soldiers find the nearest mess station and take two meals: bread, cooked meat, baked potato, or sweet berries (2 berries make 1 meal).
- When not eating or sleeping, soldiers pathfind to ramparts.
- Soldiers sleep in post beds and treat them as home.

### Health and healing

Soldiers heal through their daily routine and fall back before they die, so good care lets them rank up.

- **Heal in post beds:** soldiers fully heal when they sleep in their post bed. Post beds only work out of combat (no enemy within 16 blocks and no damage taken in the last 30 seconds).
- **Heal on meals:** each meal from the mess station restores 3 hearts (6 HP).
- **Retreat when hurt:** below 30% health, soldiers fall back to the nearest fallback block.
- **More health per rank:**
- **Retreat after heavy losses:** if 65% of a squad is killed in one fight, the survivors retreat to the nearest fallback block (or camp block on campaign). This doesn't apply within 128 blocks of their own post bed, where they hold their ground.

| Rank | Max health | Melee and arrow damage | Damage resistance | Knockback resistance |
|---|---|---|---|---|
| Soldier | 20 HP | Normal | None | None |
| Seasoned soldier | 30 HP | +15% | None | 25% |
| Hero | 50 HP | +30% | 20% | 50% |
| Legend | 100 HP | +50% | 40% | 100% |

A Legend in iron armor takes several minutes for a well-geared player to kill alone. Optional flavor: a Legend takes double damage from hits to his back, his one weakness.

### Desertion

Soldiers desert when they go hungry, lose their home, or see their squad wiped out. Seasoned soldiers, heroes, and Legends are far more loyal.

- **Hunger:** a soldier who misses meals for 2 days becomes hungry (slower, weaker attacks). From day 3 to day 6 he has a daily chance to desert: 10%, 20%, 35%, then 50%. On day 7 he deserts for sure.
- **No home:** if his post bed is destroyed and not replaced within 3 nights, he deserts.
- **Broken morale:** if more than half of a squad dies in one fight, each survivor has a 10% chance to desert. Seasoned soldiers have 5%; heroes and Legends never desert from morale. This doesn't apply within 128 blocks of the soldier's own post bed.
- **Ranked soldiers:** seasoned soldiers, heroes, and Legends never get hungry or desert from hunger or homelessness.
- **Warnings:** hungry or homeless soldiers show angry-villager particles. The player gets a chat message when a soldier becomes hungry or loses his bed, and again the day before he would be forced to desert.
- **What happens:** 50% drop their gear, walk to the nearest other village, and become regular unemployed villagers there. 50% keep their gear and become bandits, joining the nearest band or starting a new one.
- **Slots open up:** a deserter frees his post bed and recruit slot, so the recruiter can replace him.
- **Honorable discharge:** the owner (or a co-owner) can take a soldier's weapon to discharge him. He drops the rest of his gear and becomes an unemployed villager, freeing his post bed and recruit slot. He keeps his kill count, so if he's re-enlisted later he comes back at his old rank. Nobody else can discharge another player's soldiers.

### Mutiny

Soldiers who feel thrown away may turn on their own lord. Every costly victory or friendly kill raises the chance, but no mutiny is ever certain, and the player is only told his soldiers are upset.

- **Grumble points:** each group of soldiers (a camp, or a village's soldiers at home) keeps a grumble count. It gains 1 point for each costly victory: a fight the player started (an assault or a battle on campaign) that the army wins while losing at least 40% of its soldiers. Defending against raids, swarms, and bandits never counts. It also gains 1 point each time the player kills one of its soldiers; killing his own zombie soldiers doesn't count. Points travel with the soldiers: when they leave for or return from campaign they bring their points, and when two groups merge the higher count stands.
- **Cooling off:** the count drops by 1 for every 3 in-game days without a new point. A clean victory (losses under 10%) removes 2 points, and a feast (every mess station in that village or camp fully stocked at the next meal) clears them all.
- **Warnings:** while a group has any points, its soldiers show angry-villager particles, and the player gets a chat message that his soldiers at that village or camp are upset. Nothing ever says a mutiny is coming.
- **Mutiny chance:** each time a point is added, the group rolls for a mutiny: 2% at 1 point, 8% at 2, 15% at 3, 25% at 4, and 35% at 5 or more (proposed). It's never certain, however many points pile up. With no cooling off, the chance a group has mutinied by the time it reaches 3 points is about 23%, by 5 points about 63%, and by 7 points about 84%. Points have no cap, but every point past 5 uses the 5+ odds. While a group has any points, it also rolls once a day as it cools off: 1% per point, up to 5% at 5 or more points (proposed). The figures above don't include these daily rolls.
- **Joining:** when a mutiny starts, every soldier in the group rolls to join, using the table below. The more points the group had, the more join.

| Rank | 1-2 points | 3 points | 4 points | 5+ points |
|---|---|---|---|---|
| Soldier | 25% | 40% | 55% | 70% |
| Seasoned soldier | 15% | 25% | 40% | 55% |
| Hero | 5% | 10% | 20% | 30% |
| Legend | 2% | 3% | 5% | 8% |

- **The mutiny:** the joiner with the most kills leads it. Mutineers attack the player on sight and try to kill him; survivors become a bandit band under the leader. Soldiers who don't join stay loyal and fight the mutineers. If nobody joins, the mutiny fizzles and the points stay.
- **Who never mutinies:** royal guards, soldiers within 30 days of royal training, and soldiers carrying the banner of the old kingdom. A necromancer's soldiers roll twice to join and join if either roll succeeds.

### Veterans

A discharged soldier can be given a veteran's sword to defend his home for free, and called back to service later.

- **Veteran's sword:** an iron sword + an iron nugget (shapeless). It fights like an iron sword.
- **Who can use it:** only honorably discharged soldiers. Given to anyone else, it counts as a normal weapon.
- **What a veteran does:** he claims a normal villager bed like any villager, lives as a normal villager, and carries the sword. He fights anything that threatens the area within 32 blocks of his home bed. Outside that area he acts like a civilian.
- **No upkeep:** veterans don't eat from the mess, don't need a post bed, never desert, and don't count toward tax coverage.
- **Looks:** he keeps his old rank badge, labeled "Veteran" + name.
- **Recalling him:** give him a better weapon. Kills he made as a veteran carry over. If he meets every soldier requirement, he's a soldier again at his rank; otherwise he becomes a militiaman and upgrades once he meets them.
- **Trading:** what a villager was first decides it. A villager who was a trader first can never become a soldier. A villager who served as a soldier first, at any rank, can switch back and forth between soldier and trader as often as the player likes: he keeps his kills and rank while trading, and his trades while serving, but doesn't trade while serving. The risk is that a valuable trader sent back to the front can die.

### Zombie soldiers

- A soldier killed by a zombie can turn into a zombie soldier, like vanilla zombie villagers. He keeps his gear and rank.
- **Curing (proposed odds):** cured the vanilla way, he comes back as one of these:
    - 60%: a soldier again, with his gear, rank, and kills.
    - 25%: he drops his gear and refuses to serve. He can take a trade, but can never become a soldier or militiaman again.
    - 15%: he deserts, following the normal desertion rules.

### Quirks

- Soldiers within 2 blocks of a loose brewing stand (not a villager's job block or one in the citadel), axolotl, frog, or decorated pot have a 20% chance to take on negative effects and behavior. Each soldier is only checked once per day.
- Soldiers close to a mushroom or poppy have a 20% chance to eat it, with the same result.
- Either case gives 2 minutes of "Tripping Nuts": they wander aimlessly and hit anything they bump into, but don't target anything on purpose.
- Soldiers can drive boats and ride in each other's boats. They only carry and place boats during a boat plan.

### Night fighting

Soldiers in the dark sometimes stumble, so lit villages and lantern-carrying armies keep their footing and dark ground gives swarms, minions, and necromancers an edge. Stumbling is the only night penalty.

- **Who stumbles:** soldiers and militia between dusk and dawn, while standing in light below level 8 and not carrying a light. In a well-lit village, soldiers never stumble.
- **Stumbling:** an unlit soldier who's moving has a 5% chance each in-game hour to stumble (proposed). He loses 2 hearts and is slowed for 3 seconds, but a stumble never drops him below 1 heart. The slowness is a built-in stat, not a potion effect, so endermen ignore it.
- **Carried lights:** soldiers and militia can carry a torch, lantern, or soul lantern in the utility slot when it isn't holding a boat, and never stumble while carrying one. Torches burn out after one night; lanterns last. Players hand lights over by right-clicking, and soldiers restock torches from arrow bins and camp blocks. Held lights don't light up the world (vanilla has no moving light), so this is a stat, not a visual.
- **Lighting the village:** placed light counts. Torches, lanterns, candles, and glowstone around ramparts and paths keep defenders on their feet.
- **Exempt:** nightwatch soldiers, heroes, and Legends never stumble.
- **Minions:** necromancer minions deal 10% more damage at night (proposed).

## Village economy

Food keeps soldiers fed, taxes fund the village, and recruiters turn both into more soldiers.

### Mess station

- Holds soldier food: bread, cooked meat, baked potatoes, and sweet berries (2 berries count as 1 meal). Multiple blocks can be joined to make a bigger one.
- Soldiers who can't get food go hungry and may desert (see Desertion).

### Taxes

- A village stores taxes in a tax box if it has a player-owned mess block and at least one active soldier.
- Militiamen don't make a village store taxes; only soldiers do.
- Each taxable villager adds 1 of the biome's base tax item per collection (see Tax by biome).
- If anyone other than the village owner or a co-owner opens the tax box, all nearby soldiers attack them; militia attack 50% of the time.

### Who pays taxes

A villager is taxable only if all of these are true:

- **Covered by a soldier:** each soldier covers up to 5 villagers. A village with 3 soldiers taxes at most 15 villagers; the rest pay nothing.
- **Can reach the tax box:** the villager must be able to pathfind to it. Villagers locked in trading cells or walled off pay nothing.
- **Rolls netherite only if eligible:** only armorers, weaponsmiths, and toolsmiths can roll netherite. Other traders skip that roll.

### Tax box cap

The tax box stops collecting once it's full. Players have to empty it regularly to keep taxes coming in. Big villages can upgrade it; it stays a single box, so it's always one target worth defending.

| Tier | Holds | Upgrade cost |
|---|---|---|
| Tax box | 1 stack of base items + 9 slots for trader items | Normal recipe |
| Reinforced tax box | 3 stacks + 18 slots | Tax box + 4 iron blocks |
| Treasury | 9 stacks + 27 slots | Reinforced box + 4 diamond blocks + 1 netherite ingot |

### Tax by trader level

At each tax collection, every trader rolls once per item below. Higher levels pay more; the old "high-level" odds are now the Master row.

| Trader level | Bread | Emerald | Iron ingot | Gold ingot | Diamond | Netherite |
|---|---|---|---|---|---|---|
| Novice | 20% | 2% | 2% | 0% | 0% | 0% |
| Apprentice | 30% | 4% | 4% | 1% | 0% | 0% |
| Journeyman | 40% | 6% | 6% | 2% | 1% | 0% |
| Expert | 45% | 8% | 8% | 3% | 2% | 0% |
| Master | 50% | 11% | 11% | 4% | 3% | 0.5% |

### Tax by biome

The biome sets the base tax item.

| Village biome | Base tax item |
|---|---|
| Plains | Bread |
| Savanna | Bread |
| Desert | Wheat (harsh biome: 3 wheat make 1 meal) |
| Taiga | Sweet berries (2 berries make 1 meal) |
| Snowy | Potatoes |

Most base tax items are worth one meal, so each covered villager pays one meal per collection. Taiga villagers pay half a meal in sweet berries, and desert is the harshest: its villagers pay a third of a meal in wheat.

### Tax by village size

Village size sets how often taxes are collected.

| Village size | Villagers | Collects taxes every |
|---|---|---|
| Hamlet | 1-5 | 3 days |
| Small village | 6-10 | 2 days |
| Village | 11-20 | 2 days |
| Town | 21-35 | 1 day |
| City | 36+ | 1 day |

### Recruiting

- **Job block:** the recruiter box is a job block like other villager job blocks. One recruiter per box. A nearby soldier who reaches an unclaimed box takes the job.
- **Recruits:** the recruiter turns unemployed villagers (no trade) into soldiers, so the village needs enough of them. Breeding keeps the supply going.
- **Capacity:** each recruiter can have up to 10 recruits at a time. A recruit's death frees his slot.
- **Post beds:** 1 post bed = 1 recruit. The recruiter buys post beds for 5 emeralds each, paid from the tax box or the recruiter box, and places them on post blocks.
- **Post block:** a block the mod defines that marks where recruiters may place post beds. Recipe: a cobblestone slab and an iron ingot.
- **Feeding:** the recruiter keeps nearby mess stations stocked with food from the tax box. He only moves food that's ready to eat. He never crafts or cooks: raw items (wheat, raw potatoes, raw meat) stay in the tax box until a player turns them into bread, baked potatoes, or cooked meat.
- **Gear:** blacksmiths, fletchers, and armorers can be set to fill the recruiter box over time (see the supply tables below). Supplied gear is issued gear: only the recruiter can take it out of the box, not players.
- **Recruiter in combat:** the recruiter only fights within 32 blocks of his recruiter box, so he isn't lost chasing enemies across the map.

- **Breaking the box:** anyone can break a recruiter box, though village protection slows non-allies. When it breaks, its emeralds and food drop as normal items, but issued gear is destroyed, so raiders hurt a village's supply and no one can farm issued gear by breaking and replacing boxes. An owner moving a box loses its issued gear.

### Soldier requirements

Every soldier, including the first one the player makes by hand, needs all of these:

- No trade (unemployed villager)
- A weapon (a melee weapon, a bow or crossbow, or a pickaxe on its own all count), chestplate, and helmet
- A free post bed
- A well-stocked mess station
- No hostile mob nearby: with one nearby, making a soldier fails 90% of the time. The player's own minions don't count, and making militia isn't affected.

### Rank bonus

Seasoned soldiers are common, so they add little; heroes and Legends are rare and add more.

| Soldier near the recruiter | Kills needed | Extra recruit slots |
|---|---|---|
| Seasoned soldier | 5 | +1 each, up to 3 seasoned soldiers (+3) |
| Hero | 20 | +5 |
| Legend | 50 | +10 |

- Seasoned soldier bonuses stack up to +3. For heroes and Legends, only the best one near the recruiter counts: two heroes still give +5.
- **If a bonus soldier dies:** the extra slots disappear, but soldiers already recruited stay. The recruiter is over capacity and can't recruit again until deaths bring him back under his new limit.

To set up a recruiter, the player:

1. Places enough post blocks on the ground.
2. Makes the first soldier by hand: gives an unemployed villager a weapon, chestplate, and helmet (he still needs a free post bed and a stocked mess station).
3. Places the recruiter box near that soldier so he takes the job.
4. Keeps the recruiter supplied with emeralds, gear, and food in the tax box or recruiter box.

### Blacksmith supply

A weaponsmith assigned to a recruiter box adds one weapon a week; his level sets which weapons he can add. He only adds an item on days he works at his job block, and stops when the box is full.

| Blacksmith level | Items he can add (1 per week) |
|---|---|
| Novice | Stone sword, stone axe |
| Apprentice | Above + iron axe |
| Journeyman | Above + iron sword |
| Expert | Iron sword, iron axe, shield |
| Master | Expert items + 5% chance of a diamond sword |

- Shields only go to soldiers, since militiamen can't use them.
- Bows, crossbows, and arrows come from fletchers, not blacksmiths (see Fletcher supply).

### Fletcher supply

A fletcher assigned to a recruiter box adds ranged gear on the same rules as the blacksmith: only on days he works at his job block, and never past a full box.

| Fletcher level | Items he can add (1 per week) |
|---|---|
| Novice | 20 arrows |
| Apprentice | Above + bow |
| Journeyman | Above + crossbow |
| Expert | Bow, crossbow, 32 arrows |
| Master | Bow, crossbow, 48 arrows + 5% chance of a gold-outline arrow |

### Armorer supply

An armorer assigned to a recruiter box adds armor on the same rules as the blacksmith and fletcher. He only adds helmets and chestplates, since those are what soldiers need to enlist and all militia can wear.

| Armorer level | Items he can add (1 per week) |
|---|---|
| Novice | Chainmail helmet, chainmail chestplate |
| Apprentice | Above + iron helmet |
| Journeyman | Above + iron chestplate |
| Expert | Iron helmet, iron chestplate |
| Master | Expert items + 5% chance of a diamond helmet or chestplate |
