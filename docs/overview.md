# Overview

A Minecraft mod that turns villagers into soldiers and militia, backed by a village economy, horn commands, and new threats.

## Performance

Big fights stay playable by cutting the heaviest AI work, mostly invisibly to players.

- **Squad pathfinding:** soldiers moving together share one path calculated by a squad leader; the rest follow him.
- **Lazy idle AI:** soldiers who are guarding, sleeping, or standing at a rampart think about once every 2 seconds instead of 20 times a second, and wake fully the instant they take damage or something hostile comes within 16 blocks.
- **Shared target scanning:** squads look for enemies once a second and share the result, instead of every soldier scanning every tick.
- **Reduced AI far from players:** soldiers and minions more than 48 blocks from any player run simplified AI. They still move and fight, with cheaper calculations.
- **Travel while unloaded:** swarms and bandit bands travel as stored data in unloaded chunks and only fight when loaded (see Threats).
- **Particle setting:** a client option to reduce the mod's particles (angry villagers, soul particles, plumes).
- **Server cap:** server owners can set a max number of soldiers and minions per player; the default is no cap.
- **Block damage:** the mod respects vanilla's mobGriefing rule. With it off, fire arrows, door breaking, and bandit burning don't damage blocks.
- **Spread the work across ticks:** each soldier thinks on his own tick slot instead of all 100 at once, so the load is smooth rather than spiky.
- **React to events:** rosters, bed status, and the muster roll update when something happens (a death, a bed broken, a mess station placed), not by checking constantly.
- **Tax math once per collection:** coverage, reachability, and trader rolls run once per collection per village, with the results cached.
- **Swarms move as one unit:** one leader pathfinds and the rest follow, so a 30-mob swarm costs about as much pathfinding as one mob.
- **Limit what clients draw:** name tags, badges, and plumes only render within a short distance of the player.
- **Measure from day one:** use the spark profiler mod while building, set a performance target (for example, 100 soldiers fighting a 30-mob swarm stays under a fixed share of each tick), and test it after every build stage.
- **Cleanup timers:** arrows that missed vanish after 30 seconds (vanilla: 1 minute). Mob drops from swarms, raids, and bandits use vanilla's 5 minutes. Soldier and militia death drops last 10 minutes, so there's time to recover gear after a battle.

## Platform

The mod targets NeoForge on Minecraft 1.21.1, with Java 21.

- **Why NeoForge:** its built-in API covers much of what this mod needs (events, saved data on entities and worlds, inventories, config, networking), and it's where big content modpacks live.
- **Why 1.21.1:** it's the version official Create still supports (its 26.1 port is in progress), and Ice and Fire Community Edition has a stable release there, so the mod can join existing modpacks. NeoForge's docs for it are frozen but complete.
- **Later:** port to 26.1 once official Create does. NeoForge expects 26.1 to replace 1.21.1 as the stable version, so plan for Java 25 and the API changes between 1.21.2 and 26.1. A Fabric version can wait until the mod is finished.
- **Versions to pin:** Minecraft 1.21.1, NeoForge 21.1.x (pin the newest build when the project starts), Java 21, and the NeoForge 1.21.1 docs. Decided against 1.20.1: NeoForge only supports 1.20.2 and later, so 1.20.1 would mean Forge, with no data components and an older API, and official Create has moved its continued support to 1.21.1.
- **Known gap on 1.21.1:** NeoForge's data attachments don't sync to clients on their own in this version, so soldier and village data that clients need (ranks, kill counts, badges) goes through our own network packets.
- **Art:** custom art encouraged for blocks, items, banners, and structures. Soldiers and militia can reuse the vanilla villager model with normal armor and item rendering until custom models exist; shields can use vanilla banner patterns or custom art. Default looks from vanilla parts when no custom asset exists:

| Who | Clothing | Badge (vanilla level badge on the belt) | Label over head |
|---|---|---|---|
| Soldier | Plain villager of his biome, no profession clothes | Stone | "Soldier", or his name once he has a kill |
| Seasoned soldier | Same | Iron | "Seasoned" + name |
| Hero | Same | Gold | "Hero" + name |
| Legend | Same | Diamond | "Legend" + name |
| Militiaman | His own trade clothes (or plain if unemployed) | None | "Militia" |
| Recruiter | Same as his rank | Emerald | "Recruiter" + name |
| Bandit | Nitwit clothes | None | "Bandit" |

- **Structures:** the lore adds 17 acolyte refuges, 10 bandit fortresses, the End outpost, the Evoker Lord's castle, and the shrine sites. Custom art encouraged; limit scope with a few reusable templates: about four refuge layouts and two fortress layouts, plus single layouts for the outpost, castle, and shrines.

## Build order

Build in eight stages, testing each before starting the next. Every stage depends on the ones before it.

1. **Core:** soldiers and militia (roles, inventory slots, gear, food, desertion, honorable discharge), mess stations, post beds, ownership, village area with surveyor's markers and the surveyor's knife, the awaken horn, and the muster roll.
2. **Economy:** tax box and its tiers, tax coverage, recruiters, blacksmith, fletcher, and armorer supply, gear wear and death drops, ranks, veterans and the veteran's sword, and zombie soldiers.
3. **Defense:** ramparts, arrow bins, fallback and haven blocks, the remaining horns, levers and buttons, flares, the village bell, nightwatch and royal guard armor, royal guard posts, night fighting and carried lights, caltrops, castle gates, village protection, arrow recovery, and beacon effects on soldiers.
4. **War:** allies, campaigns, camp blocks, blockades, assaults, sappers and axemen, siege ladders, boat plans, plank bridges and bridge plans, laced rations, mutiny, no building in enemy land, wanted posters, the reassignment writ, and village banners.
5. **Threats:** new moon raids, swarms, bandits, bandit camps and cave hideouts, and vanilla mob changes.
6. **Extras:** necromancy with necromancer crypts and phylactery shards, animal farmers and wild ranches, mob spawners, Village Chronicles, the training dummy, advancements, the village map, and the kingdom map.
7. **Endgame:** bandit fortresses, kingdom titles, warlords, monuments, the Black Citadel with its chronicles, the Dragon's Well, the Phantom Roost, the royal enchanting table, brewing stand, and anvil, and the Blighted Tree.
8. **Lore:** the 12 kings' relics and their tree origins, the 17 acolyte refuges and the Order of the Well, the 10 bandit fortresses, the End outpost, the Evoker Lord's castle, Torvald's and Valen's shrines, the Royal Annals and other books, and the hero legends.
