# Config and recipes

## Recipes

Recipes for blocks and items not covered in their own sections. All use vanilla materials.

| Block | Recipe |
|---|---|
| Mess station | 3 wooden slabs on top, barrel in the middle |
| Tax box | Chest surrounded by 4 iron ingots, gold ingot on top |
| Recruiter box | Chest + iron sword + any banner (shapeless) |
| Arrow bin | Barrel + arrow + string (shapeless) |
| Rampart | Stone bricks + stone brick wall + iron ingot (shapeless) |
| Fallback block | Any banner + cobblestone wall (shapeless) |
| Haven block | White banner + hay bale (shapeless) |

| Command item | Recipe |
|---|---|
| War horn (base for all horns) | 3 copper ingots in a V, leather in the middle |
| Awaken horn | War horn + yellow dye |
| Fallback horn | War horn + white dye |
| Charge horn | War horn + red dye |
| Guard chief horn | War horn + gold ingot |
| Lever horn | War horn + any of the 8 dye colors; add an iron ingot for the one-random-lever version |
| Soldier lever | Lever + matching dye |
| Soldier button | Any button + matching dye |

| Animal job block | Recipe |
|---|---|
| Feed trough (cattle) | 3 wooden slabs + hay bale + emerald block |
| Shearing post (sheep) | Fence + shears + emerald block |
| Nesting box (chicken) | Barrel + hay bale + emerald block |
| Slop bucket (pig) | Bucket + carrot + emerald block |
| Hitching post (horse) | Fence + lead + gold block |

| Necromancer item | Recipe |
|---|---|
| Horcrux compass | Compass + phylactery shard + soul sand + wither rose (shapeless). Full detail: `necromancy.md`. |

## Config settings

Every number a server owner might want to tune, with its default.

| Setting | Default |
|---|---|
| Max soldiers per player | No cap |
| Max minions per player | No cap |
| Soldier meals per day | 2 |
| Militia meals per day | 1 |
| Villagers covered per soldier | 5 |
| Tax collection interval (hamlet / small village / village / town / city) | 3 / 2 / 2 / 1 / 1 days |
| Trader tax odds | See Tax by trader level |
| Recruiter capacity | 10 |
| Post bed price for recruiters | 5 emeralds |
| Supply rate (blacksmith, fletcher, armorer) | 1 load per week |
| Hunger desertion chance (days 3-6) | 10% / 20% / 35% / 50%, forced on day 7 |
| Morale desertion chance | 10% (seasoned soldiers 5%) |
| Squad retreat threshold | 65% killed |
| Home bed distance | 128 blocks |
| Horn range outside a village | 256 blocks |
| Flare ranges (levels 1 / 2 / 3) | 32 / 64 / 128 blocks |
| Village bell trigger | 2/5 of defenders, minimum 3 enemies, 5-minute cooldown |
| Raid chance (new moon / nights either side) | 40% / 15% |
| Raid size odds (small / medium / large) | 60% / 30% / 10% |
| Swarm spawn chance per night | 5% |
| Swarm size cap (creeper cap) | 30 (8) |
| Runaway chance per villager per day | 0.1% |
| Wand permanent heart loss chance | 3% |
| Wand side effect chance | 5% |
| Necromancer recruiter interval multiplier | 3× |
| `necromancerWorldHavocEnabled` — chunk-wide flute havoc (e.g. mass animal dirge), large summons | **true** (set **false** on peaceful/co-op servers) |
| `necromancerHorcruxEnabled` — poison/pot immunity, pseudo-death instead of dying | **true** (set **false** to disable lich rules) |
| `necromancerHorcruxDestroyers` — ways to destroy a horcrux item | **dragons_well**, **ghast_fireball**, **wither**, **wither_skull**, **lightning**, **dragon_breath_bottle** (proposed; servers may extend) |
| `necromancerHorcruxPingRadiusChunks` — hunter “near jar” particle ping (**circular**, chunk-center distance) | **10** |
| `necromancerGodWizardMeleeBonus` — bonus melee damage with full cursed netherite set | **0.25** (+25%) |
| `necromancerRogueHorcruxChance` — rogue spawns with one horcrux in hidden chest | **0.04** (~4%) |
| `necromancerRogueSurfaceFraction` — rogue sites above ground (vs underground crypt) | **0.80** |
| `necromancerRogueSiteSpacing` — average distance between rogue sites (~**2×** pillager outpost spacing = **half** as many) | **TBD** (~**400–512** blocks; tune in playtest) |
| `necromancerHorcruxCompassEnabled` — craftable compass to nearest horcrux chunk (stackable, unlimited crafts) | **true** |
| `necromancerQuestHeartThreshold` — hearts lost before intro NPC | **3** |
| `necromancerIntroMinDistanceFromAcolyte` — blocks intro near refuges | **128** |
| Robe stigma fraction (non-necromancer in hood/robe) | 0.5 |
| Minion decay | 30 in-game days |
| Simplified AI distance | 48 blocks |
| Respect mobGriefing | On |
| Soldiers attack non-allies on sight | Off |
| Militia in natural villages | 2 |
| Cured zombie soldier odds (serve / refuse / desert) | 60% / 25% / 15% |
| Village protection (soldiers home: 0 / 1-9 / 10-24 / 25+) | Normal / 3x / 5x / 10x slower breaking for non-allies |
| Surveyor's markers allowed | 1 per 5 members (the first mess station also claims land like a marker) |
| Marker radius (hamlet / small village / village / town / city) | 32 / 48 / 64 / 96 / 128 blocks |
| Raid chance near spawn before the citadel falls (under 1,000 / 1,000-3,000 blocks) | 60% / 25% and 50% / 20% |
| Natural militia near spawn (under 1,000 / 1,000-3,000 blocks) | 4 / 3 |
| Arrow cap per soldier or militiaman | 16 |
| Siege ladder max height | 20 blocks |
| Camp block storage | 54 slots |
| Veteran defense radius | 32 blocks |
| Cleanup timers (stray arrows / mob drops / soldier drops) | 30 seconds / 5 minutes / 10 minutes |
| Well of Kings rate | 5 XP points per second |
| Golden age trade discount | 40% |
| Max royal guards | 10% of the soldier cap (server cap if set, otherwise current army), minimum 1 |
| Royal guard teleport blocked near enemies | 5 hostile mobs or enemy soldiers within 16 blocks |
| Non-ally block placing in a village with no soldiers home | 1 block per 2 seconds (none while any soldier is home) |
| Block placing in a village under attack | 1 block per second |
| Night penalties for unlit soldiers | 5% stumble chance per in-game hour (no other penalties) |
| Mutiny triggers | 1 grumble point per costly victory (40%+ losses) or friendly kill; mutiny chance per new point 2% / 8% / 15% / 25% / 35% at 1 / 2 / 3 / 4 / 5+ points; daily roll while upset of 1% per point, max 5% |
| Mutiny join chance (soldier / seasoned / hero and Legend) | See the join table under Mutiny |
| Dragon's Well breath fills per player per day | **0** = unlimited (default); set **>0** only if a server wants a daily cap |
| Kingstree sapling interval | 30 in-game days |
| Acolyte refuges per world | 17 (12 with Royal Annals copies, 1 Horn-keeper, 4 ordinary) |
| Acolyte specialties (NPC) | 16 brothers: 4 Arcane, 4 Alchemist, 4 Smith, 4 War Leader; plus Maelor |
| Acolyte HP (brother / War Leader / Maelor) | 30 / 50 / 30 |
| Acolyte soldier aura (brothers + Maelor) | 10% DR, 1 heart / 10s OOC within 16 blocks |
| Acolyte soldier aura (War Leader) | 15% DR, 1 heart / 8s OOC within 16 blocks |
| Acolyte escort regen (base + per nearby brother within 8 blocks) | ½ heart / 15s each, +½ heart / 15s per other brother |
| Acolyte zombie-villager cure range | 2 blocks, presence over a few seconds |
| Player acolyte — Maelor brothers minimum | TBD (`acolyteBrothersMinimum`) |
| Order quest — block if owner's soldiers within | 32 blocks (`acolyteSoldierBlockRadius`) |
| Player War Leader aura radius | 16 blocks (you, citadel allies, your soldiers/militia) |
| Player War Leader aura (pre-tree) | 15% DR, 1 heart / 8s OOC |
| Player War Leader aura (post-tree) | 20% DR, 1 heart / 6s OOC |
| Player War Leader escort regen bonus | +25% to brother escort regen within 16 blocks |
| Player Smith pre-tree — repair durability | +50% per material (`steadyHand`) |
| Player Smith pre-tree — anvil XP cost | −1 level, min 0 (`frugalRepair`) |
| Player Smith pre-tree — gear wear | −10% durability loss on worn gear (`hardWear`) |
| Player Smith pre-tree — smithing template | not consumed on player upgrades (`temperedUpgrade`) |
| Royal station — ask smith repair material discount | 40% |
| Royal station — ask smith repair XP discount | 50% |
| Royal station — major smith repair material discount | 70% |
| Royal station — major smith repair XP discount | 75% |
| Royal station — major Arcane enchant | 0 levels, 0 lapis; chosen level = vanilla max + 2 (gear or book) |
| Royal station — minor Arcane enchant | random roll, up to vanilla max + 1 (gear or book) |
| Royal station — major Alchemist duration multiplier | 3× (minor ask 2×; pre-tree alchemist 1.5× on own brews) |
| Abandoned kingdom threshold | 60 in-game days since the owner last logged in |
| King's Horn duration | 1 minute, once per in-game day |
| Sapper time on obsidian (diamond or netherite pickaxe) | 12 minutes per block |
| Swarms alive per player before no new one spawns | 2 |
| Bandit bands per world | 25 |
| Spectral king stats | 100 HP, 30% damage resistance, Legend-level melee damage, no knockback, plus his relic's power |
| Corrupted King stats | 300 HP, 40% damage resistance, Legend-level melee damage, double damage from behind |
| Hero legend chances (library shelves / village chest / bandit camp) | 30% per book / 3% / 5% |
