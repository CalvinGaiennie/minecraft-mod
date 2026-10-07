# War and defense

## Defense blocks

These blocks tell soldiers and villagers where to stand guard or retreat.

| Block | What it does |
|---|---|
| Arrow bin | Restocks arrows for soldiers and militiamen. When turned on, the nearest one with a bow guards it. Holds up to 1 stack of arrows, refilled by players or nearby fletchers. A fletcher adds arrows at the same rate he supplies recruiter boxes (one load a week, see Fletcher supply) |
| Rampart | A single block that marks a guard spot. Soldiers pathfind here when not eating or sleeping.  |
| Fallback block | Soldiers go to the nearest one when the fallback horn blows |
| Haven block | Attracts retreating non-soldier villagers |
| Castle gate | A thick door, 4 blocks wide and 8 tall, meant to be placed in pairs for an 8-wide gateway. Opens and closes with redstone, so it works with levers, buttons, and lever horns. Comes in wood (axemen can break it during assaults) and iron (only sappers can). Players break castle gates about as slowly as obsidian, using an axe for wood gates or an iron pickaxe or better for iron gates (village protection slows non-allies further). For soldiers, each gate breaks as one piece and takes a long time: about 3 minutes for a wood gate and 6 minutes for an iron gate (proposed), with permanent progress like other sapper work. Recipes (proposed): wood gate is 6 logs, 2 iron blocks, and a chain in the center (about 19 iron each); iron gate is 8 iron blocks around an obsidian block (72 iron each, 144 for a pair) |

## Housing and field blocks

These blocks are where soldiers live at home and on campaign.

| Block | What it does |
|---|---|
| Post bed | Soldiers sleep here, treat it as home, and fully heal when they sleep out of combat. Players can craft them (any bed and an iron ingot, shapeless) and place them anywhere; recruiters buy theirs and place them on post blocks. Post beds don't count as beds for villager breeding. A post bed's color shows its owner's rank, using vanilla bed colors: white when empty, gray for a soldier, light gray for a seasoned soldier, yellow for a hero, cyan for a Legend, and green for a recruiter. Right-clicking it shows the owner's name, rank, kills, and whether he's home, away on campaign, or missing |
| Camp block | Home base for soldiers on campaign: a field mess and field bed in one (see Campaigns) |

### Campaigns

Soldiers in campaign boots leave the village with the player and live off a camp block until they come home.

- **Starting:** soldiers wearing campaign boots follow the player. Removing the boots sends a soldier home to his post bed.
- **Cost at home:** soldiers on campaign don't count toward tax coverage and leave the village less defended. Their post beds and recruit slots stay reserved while they're away.
- **Food:** soldiers still eat 2 meals a day. If the camp block runs out, the normal hunger rules apply (see Desertion).
- **Portals:** booted soldiers follow the player through Nether and End portals, so camps can be set up in other dimensions.

### Camp block

- **Hold point:** once placed, booted soldiers stop following the player and stay within about 16 blocks of it, fighting anything that comes close. It stays until the player breaks it or enemies destroy it.
- **Field mess:** stores food, siege ladders, boats, and arrows, with as much room as a double chest (54 slots). Soldiers eat from it daily.
- **Field bed:** soldiers sleep at the camp and fully heal out of combat, like a post bed.
- **No soldier cap:** a player can bring as many booted soldiers as they want.
- **One camp per player:** placing a new one replaces the old one, and every booted soldier moves to the new camp. Breaking it is how you move camp.
- **Enemies can destroy it:** bandits, raiders, and hostile mobs can attack it. When it breaks, it drops its stored food, and soldiers go back to following the player if he's within 128 blocks; otherwise they march home to their post beds.
- **Horns:** on campaign, the fallback horn sends soldiers to the camp block.
- **Not a village:** no recruiting, taxes, or rank bonuses at a camp, and raids and swarms don't target it specifically.

Recipe, crafting grid top to bottom:

|  |  |  |
|---|---|---|
| Wool | Wool | Wool |
| Log | Campfire | Log |
| Log | Barrel | Log |

### Blockades

A camp near an enemy village with enough soldiers starves it out.

- **Starting a blockade:** a camp within 64 blocks of another player's village border besieges it while it holds at least 15 soldiers.
- **Effects on the enemy village:** its tax box stops collecting, its recruiters can't recruit, and its recruiters can't restock mess stations from the tax box.
- **Ending a blockade:** the siege ends when the camp is destroyed or drops below 15 soldiers.

### Assaults

The player sets a target with an attack plan, then launches the assault with the charge horn.

- **Attack plan (item):** the player writes target coordinates and how many siege ladders to bring into it, then uses it on a camp block. It's consumed, and every soldier at that camp sets those coordinates as the target for their next charge. Recipe: paper, ink sac, and feather, shapeless.
- **Launching:** blowing the charge horn sends the camp's soldiers to the target. They attack enemy combatants within 32 blocks of the target: soldiers, militia, iron golems, hostile players, and bandits.
- **Civilians are spared:** soldiers don't kill traders or other non-combat villagers.
- **Timing:** assaults work day or night, but only on the player's order.
- **Hurt soldiers:** below 30% health, they fall back to the camp block.
- **Ending:** the assault ends when no enemies are within 128 blocks of the target for 60 seconds, or when the fallback horn blows. Survivors return to camp.
- **Doors:** during an assault, soldiers can break wooden doors.
- **Blockades hold:** soldiers away on an assault still count toward their camp's blockade.
- **Camp guards:** sneak-right-click a soldier to keep him back as a camp guard. Guards ignore attack plans and charge horns until you sneak-right-click them again. Guards wear a red plume on their helmet and show a "Camp Guard" name tag when you look at them.
- **Breaking mess stations:** soldiers on campaign attack and break enemy mess stations they find. Destroying every mess station in a village ends its owner's claim. This follows mobGriefing: with it off, players have to break them by hand.

### Pickaxe soldiers

- A player can give a soldier a pickaxe, which goes in his bow slot instead of a bow. It only counts when given directly by a player; soldiers never pick up pickaxes on their own.
- During an assault, pickaxe soldiers can break through iron doors as well as wooden ones.

### Siege ladders

- **What it is:** a single large ladder object that two soldiers carry together and stand up against an enemy wall in one motion. Its height fits the wall automatically, up to **20 blocks**. It reuses the vanilla ladder texture on a tall, flat shape. Recipe: 8 ladders + 2 iron ingots. Soldiers and militia follow the same rules.
- **Supply:** siege ladders are stored in the camp block. The attack plan says how many to bring, and up to that many are taken out of the camp block for the assault, only as many as the player stocked. Each is carried by a pair of soldiers.
- **Climbing:** vanilla mobs only climb ladders they bump into; they don't plan routes with them. So soldiers get a simple scripted action instead: walk to the siege ladder's base, climb straight up, and step off at the top.
- **Counterplay:** defenders can push a standing siege ladder over by hitting its top a few times, dropping anyone climbing it, or break it entirely. Standing one up follows mobGriefing.
- **Carrying:** the two carriers walk slower and can't attack while carrying.
- **Automatic use:** carriers stand the ladder up against any wall 2 or more blocks high that's between them and an enemy whenever they can't pathfind any closer.

### Boat plans

- Soldiers only carry boats while carrying out a boat plan. Boats are stored in the camp block.
- **Boat plan (item):** the player writes target coordinates into it and uses it on a camp block. It's consumed. Recipe (proposed): paper, ink sac, and any boat, shapeless.
- **What happens:** every soldier at that camp, camp guards included, grabs a boat, as long as the camp has boats left, and walks to the shore. Each waits up to 10 seconds for another soldier to climb in if one is within 20 blocks, then rows to the target coordinates. Soldiers without a boat ride as passengers when there's room.
- **Landing:** at the target, soldiers get out and drop their boats on the ground as items, then hold position there until given new orders.

### Sappers

- During an assault, pickaxe soldiers dig through walls that block the way to the target. They mine very slowly (dirt 30 seconds, stone 60 seconds, stone bricks and deepslate 90 seconds; obsidian and similar blocks take about 12 minutes each, and only with a diamond or netherite pickaxe (proposed)). Sappers can't mine wood.
- **Progress is permanent:** each block remembers how much it's been mined and shows vanilla's cracking effect. If a sapper dies, another can pick up where he left off. Progress only resets if the block is broken or replaced, so defenders can repair a wall by replacing damaged blocks.
- Wall digging follows mobGriefing.
- **Axemen:** during an assault, soldiers with axes break anything made of wood at 60 seconds per block, with the same permanent progress.
- **Beacons:** soldiers and militia get beacon effects like players do. Haste speeds up sappers and axemen, and strength, speed, resistance, and regeneration work on them too.

### Plank bridges

- **What it is:** a portable span two soldiers carry like a siege ladder and lay across a creek, moat, or ditch up to 4 blocks wide (proposed). Once laid it becomes a row of temporary bridge blocks, so soldiers and players path over it normally. Recipe (proposed): 6 wooden slabs, 2 chains, and an iron ingot.
- **Supply:** stored in the camp block. The attack plan says how many bridges to bring, the same way it does siege ladders.
- **Automatic use:** carriers lay a bridge whenever a gap of water or air up to 4 blocks wide is all that stands between them and an enemy or their target. Out of combat, soldiers lay one when their path crosses such a gap.
- **Bridge plan (item):** written with target coordinates and used on a camp block, like a boat plan. Soldiers carry bridges to the nearest crossing, lay them, and hold there. Recipe (proposed): paper, ink sac, and a wooden slab (shapeless).
- **Counterplay:** defenders knock a bridge into the water by hitting its far end 3 times, or break it. Laying one follows mobGriefing.
- **At home:** players can lay bridges by hand on their own land, and soldiers use them in their daily routine.

### Traps

Caltrops can only be placed on the player's own land or unclaimed land, never in enemy land or the citadel.

- **Caltrops:** a floor layer, like carpet. Anything that steps on it takes 1 heart and is slowed for 3 seconds (the slow is a built-in stat, not a potion effect, so endermen ignore it), and it hurts everyone, the owner, his villagers, and golems included; each caltrop breaks after 8 hits. The owner's and his allies' soldiers path around them, as do enemy seasoned soldiers and above. Plain enemy soldiers, militia, bandits, and hostile mobs walk straight in. Recipe (proposed): 4 iron nuggets and a flint make 4.

### Laced rations

Spoiled food lets a player weaken an enemy army without a fight.

- **Lacing:** any soldier food + a fermented spider eye (shapeless) makes a laced version that looks the same. Only the player who made it sees "Laced" on it. Mushroom stew, rabbit stew, and beetroot soup can be laced the same way to give to players.
- **Filling enemy messes:** anyone can right-click any mess station with soldier food (or a laced version) to add one item per click, like filling a composter. Nothing else can be put in, and only the owner and co-owners can open it to see or take food. Soldiers who see a non-ally put food in attack him, as with tax box theft.
- **Soldiers who eat it:** lose 5 hearts over 10 seconds (never below 1 heart) and are weakened for 3 minutes. Each has a 25% chance to fall sick: he stays in his post bed for a day and only fights if attacked (proposed).
- **Getting caught:** each sick soldier has a 50% chance to expose the station. The owner gets a chat warning, and soldiers skip that station until a player empties it.
- **Players:** laced stew gives Poison II for 8 seconds, Nausea for 10 seconds, and Hunger.
- **Bandits eat too:** occupying bandits take their daily food from the mess, so lacing your own occupied village thins out the band.

- **Potion lacing:** any soldier food + any drinkable potion (shapeless) makes food that gives that potion's effect at half its duration. It looks like normal food, and only its maker sees what's in it. Harmful potions (Poison, Weakness, Slowness, Harming) weaken enemy soldiers, but Harming never drops one below 1 heart. Helpful potions work too, so a player can feed his own army Strength or Regeneration before a big fight. Potion-laced food goes into mess stations like any other soldier food.

## Horns and signals

Horns only work on soldiers you own, or whose owner made you an officer or co-owner. Any horn blow sends nearby non-soldier, non-militia villagers, including other players' villagers, to their bed or the nearest haven block.

**Range:** a horn blown in a village reaches every soldier registered to that village (by post bed or mess station) and every villager who lives there, however far away they are. Anyone not registered to the village, such as soldiers on campaign, reacts only within 256 blocks of the player.

- **Loaded soldiers** react right away.
- **Unloaded soldiers** pick up the order when their chunk loads. If enough time has passed for them to march there, they appear at their destination.
- This keeps lag low, since faraway soldiers only act once they load.

| Horn | Effect |
|---|---|
| Awaken | All soldiers and militia go to the nearest arrow bin or rampart |
| Fallback | Soldiers retreat to fallback blocks, even if sleeping. Soldiers on campaign go to their camp block instead. Nearby non-soldier villagers go to the nearest haven block |
| Charge | Soldiers pursue enemies: all players not on the owner's ally list, hostile mobs, and soldiers or militia not in their group |
| Guard chief | All soldiers rush to the player and guard him. Works on only 10% of militia |

### Levers and buttons

- Soldiers pull a soldier's lever, or push a soldier's button, on a horn blast.
- Lever horns come in 8 pairs, one per dye color. Soldiers pull their matching lever when that horn sounds.
- Two horn levels: one triggers all levers of a color, the other one random lever of that color.
- Flares work like horns but act where they burst, not where the player stands (see Flares).

### Flares

Flares let the player point at a spot from a distance. They can be fired by hand or from a crossbow, and only affect soldiers the player owns or commands as an officer or co-owner. Like fireworks, they come in three levels set by gunpowder; higher levels reach more soldiers and cover a bigger area:

| Flare | Effect | Recipe |
|---|---|---|
| Rally flare | Soldiers walk to where it bursts and hold that spot. They only fight if attacked or approached | Firework rocket + white dye |
| Strike flare | Soldiers attack enemies around where it bursts | Firework rocket + red dye |

| Level | Gunpowder in the rocket | Soldiers respond within | Strike area |
|---|---|---|---|
| 1 | 1 | 32 blocks | 8 blocks |
| 2 | 2 | 64 blocks | 16 blocks |
| 3 | 3 | 128 blocks | 32 blocks |

## Gear and special items

Armor changes what a villager does; items give the player extra control.

### Armor

| Gear | Who it affects | Effect on the wearer | Recipe |
|---|---|---|---|
| Nightwatch armor | Soldiers and militia | Sleeps in his post bed during the day, patrols between ramparts and arrow bins at night | Any helmet or chestplate + black dye (shapeless); keeps its armor value |
| Royal guard armor | Soldiers | Follows the player everywhere like a tamed wolf, teleporting to him if more than 16 blocks behind (with limits, see below). Never stays at a camp | Any helmet or chestplate + gold ingot (shapeless); keeps its armor value, adds gold trim |
| Campaign boots | Soldiers | Follows the player until a camp block is placed, then stays there | Any boots + compass (shapeless) |

- **Feeding royal guards:** the player can hand-feed them by right-clicking with soldier food, and they also eat at any mess station they pass.

- **Royal guard limit:** a player can have royal guards up to 10% of his soldier cap, rounded down, minimum 1 (proposed). The cap is the server's max soldiers per player if set, otherwise his current number of soldiers. Guards over the limit stop following and go home to their post beds, newest first.
- **Teleporting:** a royal guard only teleports on safe ground. He never teleports into, within, or out of another player's village area (unless that player lists him as an ally), a bandit fortress, or the Black Citadel unless the player owns it, and never across the ender barrier. He also won't teleport while 5 or more hostile mobs or enemy soldiers are within 16 blocks of the player or of himself (proposed). He walks instead, so guards can't be used to drop an escort into a fight or an enemy base.
- **Royal guard post (block):** marks a spot for one royal guard to hold. Right-click the post with an empty hand, then right-click a royal guard within 10 seconds to assign him; repeat to release him. A posted guard stops following the player and stays within 3 blocks of the post, leaving only to eat and sleep. When the village is under attack (a raid, an enemy army in the village area, a non-ally player who has hit a villager or soldier there in the last 5 minutes, a blockade, or the bell rung in the last 5 minutes), he fights anywhere in the village like a normal soldier, then returns once it's quiet. Posted guards still count toward the limit. Recipe (proposed): rampart + gold ingot (shapeless).

### Items

- **Wanted poster:** hung on a wall in the player's village. His soldiers anywhere in that village attack the listed players on sight. Right-clicking it opens a text box, like editing a sign, to type up to 4 player names. It saves player IDs, so changing a name doesn't clear a record. Recipe: paper, ink sac, and iron nugget.
- **Village map:** sold by cartographers and found in bandit camp loot. Shows the nearest village the player hasn't visited, like a vanilla explorer map.
- **Fire arrows:** see Soldier behavior.

- **Kingdom map (item):** shows every village a player owns in one place. Right-clicking opens a screen with a top-down dot map of his kingdom and a list beside it. Each village shows as its banner with its name, size, and soldier count; allied villages show in a second color, and his camp is marked. Villages under attack flash, and the list flags hungry or homeless soldiers, full tax boxes, and blockades. The map always shows the kingdom of whoever made it, so a stolen one is real intelligence for the thief.
- **Getting one:** when a player claims his second village, he receives the chronicle *The Royal Surveyor*. From then on, every cartographer in his villages offers one extra trade: a royal charter for 16 emeralds, a village map, and a compass (proposed). A royal charter, a cartography table, and a gold block (shapeless) make a kingdom table. A kingdom table turns an empty map and a gold ingot into a kingdom map; copies work like vanilla map copies.

## Player tools

Tools that make the army easier to manage and easier to care about.

- **Muster roll (item):** shows a village's status at a glance. Use it anywhere in a village you own or command, or right-click a mess station with an empty hand. It lists total members, villagers by job and trade level (for example, 3 master librarians), soldiers by rank, militia, days of food left at the current rate, open post beds, recruiter slots in use, arrows in bins, tax box fill, any hungry or homeless soldiers, and whether the village is under blockade. Recipe: book + iron ingot.
- **Names:** a soldier gets a name after his first real kill; practice on a training dummy doesn't count. His name tag then shows his name, rank, and kill count.
- **Village banners:** the owner right-clicks the village's mess station with any banner to set its design. His soldiers carry it on their shields, and it flies over his camps, so armies are easy to tell apart.
- **Training dummy (block):** idle soldiers practice on it. Every 2 days of practice counts as 1 kill, up to 5, so practice alone can make a seasoned soldier but nothing higher. Recipe: armor stand + hay bale + stick.
- **Guided start:** the first time a player makes a soldier, he gets the Village Chronicle about soldiers, mess stations, beds, and horns.
- **Advancements:** a mod advancement tree that shows players what's possible: first soldier, first named soldier, first seasoned soldier, first hero, first Legend, survive a new moon raid, start a blockade, break a blockade, become a necromancer, drink the potion of absolution.
- **Village bell:** ringing a village's bell works like the awaken horn for that village: its soldiers and militia go to the nearest arrow bin or rampart. Soldiers who see a non-ally ring it attack him, as with tax box theft. Soldiers ring it themselves when they spot a raid captain, a bandit leader, or another player's army, or when one soldier can see at least 3 enemies and at least 2/5 as many enemies as the village has defenders (soldiers and militia within 64 blocks of the bell). Enemies only count if he has line of sight to them. When it triggers, the nearest soldier within 64 blocks of the bell runs to ring it, nightwatch soldiers first. Soldiers can ring a village's bell at most once every 5 minutes.

- **Reassignment writ (item):** moves a soldier's home to another post bed, in another village or elsewhere in the same one. Right-click the soldier with it, then right-click a free post bed. The writ holds his name until it's used on a bed or on another soldier. The soldier keeps his gear, rank, and kills, and his old post bed becomes free at once, so it never needs to be touched. He claims the new bed instantly, wherever he is, and stays where he is; the new bed is simply his home from now on. Anyone not in combat can be reassigned. Soldiers on campaign and camp guards keep their duty. Recipe (proposed): paper, ink sac, and an iron ingot (shapeless).
