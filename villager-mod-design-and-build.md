# Villager mod: design and build notes (single file)

This one file holds the whole design for the villager mod, plus the plan for building it with Claude Code. It was made from the planning notes on 2026-10-04.

**For Claude Code:** read part 1 (Master build note) first. It says how to set up, what order to build in, and how to test. Parts 2 to 9 hold the rules. If you want separate files, split this one at the nine top-level headings named in the Contents (ignore the `# Villager mod` line inside the CLAUDE.md code block) into `docs/`, and name part 1 `docs/master-build-note.md`.

## Contents

1. Master build note
2. Overview
3. Soldiers and villages
4. War and defense
5. Necromancy
6. Threats and mobs
7. Endgame
8. Old kingdom lore
9. Config and recipes

---

# Master build note

Open this note first in Claude Code. It says what we're building, which technical decisions are made and why, how to set up, how to build in stages, how to test, and what's still open. The rule details live in the later parts of this file; this note points to them.

## 1. How to use these notes

- **Where the rules are (parts of this file):**
    - Soldiers and villages: roles, ownership, soldier behavior (ranks, desertion, mutiny, night fighting), and the village economy (taxes, recruiting, supply).
    - War and defense: defense blocks, camps, campaigns, assaults, sieges, traps, horns, flares, armor, items, and player tools.
    - Necromancy: hood, robe, wand, minions, risks, penalties, absolution, recipes, and crypts.
    - Threats and mobs: raids, swarms, bandits, cave hideouts, animal farmers, vanilla mob changes, and the effect on players who ignore the mod.
    - Endgame: bandit fortresses, kingdom titles, the Black Citadel, relics, acolytes, and the Blighted Tree.
    - Old kingdom lore: the twelve kings, deaths and relics, the Order of the Well, books, hero legends, landmarks, and the Village Chronicles.
    - Config and recipes: every crafting recipe and every tunable number.
    - Overview: performance rules, platform notes, and the build order.
- **"(proposed)" means a tunable default**, not a final number. Put every number in config and tune it in playtesting.
- **Read before building:** before each task, read the part for that system. Don't work from memory of the rules.
- **Custom art encouraged.** Original models and textures for blocks, items, and structures are welcome. Use vanilla stand-ins when art isn't ready yet; ask the user if a look isn't specified in the notes.
- **When the notes are silent or conflict, ask the user** instead of guessing. Write the answer into the notes or the decisions log at the end of this note.
- **Source of truth:** once this file is split into the repo's `docs/` folder, the repo copy is the source of truth. Change the docs file in the same commit as the code that changes the rule.

## 2. What we're building

A NeoForge mod for Minecraft 1.21.1 that turns villagers into soldiers and militia. They're backed by a village economy (mess stations, taxes, recruiters, supply), horn and flare commands, and campaigns, and they face new threats (new moon raids, roving swarms, bandits). Around that sit necromancy, animal farmers, and an endgame built on the Black Citadel and the lore of twelve old kings. The design is aimed at players who love it, and must not ruin normal play for players who ignore it (see the enlisted-player rule in Threats and mobs).

## 3. Decisions and reasons

| Decision | Choice | Why |
|---|---|---|
| Minecraft version | 1.21.1 | Official Create still supports 1.21.1 (its 26.1 port is in progress), and Ice and Fire Community Edition has a stable release there. NeoForge's docs for it are frozen but complete. |
| Why not 1.20.1 | Rejected | NeoForge only supports 1.20.2 and later, so 1.20.1 means Forge, with no data components and an older API. Create has moved its continued support to 1.21.1. |
| Why not 26.1.2 (for now) | Deferred | It's the better long-term target: NeoForge expects 26.1 to replace 1.21.1, its docs are maintained, and the game is unobfuscated. But official Create isn't on it yet. Revisit when Create ports. Expect Java 25 and API changes since 1.21.2. |
| Mod loader | NeoForge 21.1.x | Pin the newest 21.1.x build when the project starts. |
| Java | 21 | Set as the Gradle toolchain in the template. |
| Build tool | ModDevGradle from NeoForge's official 1.21.1 template | Simpler than NeoGradle, which mainly helps when one project holds several Minecraft versions. |
| Names | Mojang's official names plus Parchment | Parchment adds readable parameter names and Javadoc, which matters because villager and mob AI isn't covered in NeoForge's docs. |
| Editor | IntelliJ IDEA (free tier) | ModDevGradle creates run configs and debugs from IntelliJ. |
| Soldiers and militia | Real vanilla villagers with extra data (assumed) | The notes need soldiers to keep trades, beds, breeding rules, and to switch back to trading (see Veterans). A new entity type would lose that. Confirm with spike A before committing. |
| Per-entity data | NeoForge data attachments | Meant for custom data on entities we don't own. On 1.21.1 they don't sync to clients on their own, so we write sync packets for what clients need (ranks, kill counts, badges, labels). |
| World data | SavedData | NeoForge's documented way to store extra world-level data. Use it for exact placements and counts (17 refuges, 10 fortresses, the citadel). |
| Structures | Data-driven templates, reused | A few layouts (about 4 refuge layouts and 2 fortress layouts) plus single layouts for the outpost, castle, and shrines. |
| Testing | GameTests plus separate test instances | The template turns on GameTests for the mod's namespace. |
| Compatibility | No dependency on Create | Only test alongside Create and Ice and Fire CE in a separate instance later. |

All the tools are free: NeoForge (LGPL 2.1), ModDevGradle, Parchment, Java 21 (OpenJDK builds like Temurin), IntelliJ IDEA's free tier, Gradle, Git, and GitHub's free tier. The only paid item is Minecraft: Java Edition itself, if the user doesn't own it. Unverified: whether dev runs work with an offline test account.

## 4. Environment setup

1. **Install:** JDK 21, IntelliJ IDEA, Git, and Claude Code (quickstart: https://code.claude.com/docs/en/quickstart). Start Claude Code by running `claude` inside the project folder.
2. **Create the project:** copy NeoForge's official 1.21.1 ModDevGradle template (https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle) as a GitHub template or download it, then `git init`. Keep the template's Gradle wrapper and plugin versions.
3. **Set the mod id and name** in `gradle.properties` and the mod metadata. These are still undecided; ask the user.
4. **Turn on Parchment** in `gradle.properties` (the template has a parchment line; use the latest 1.21.1 value from https://parchmentmc.org/docs/getting-started). Reference: https://docs.neoforged.net/toolchain/docs/parchment/.
5. **Check the template first:** run `./gradlew build`, then `runClient` and confirm Minecraft opens with the empty mod. Do this before writing any mod code.
6. **Run configs:** `runClient`, `runServer`, `runData` (data generation), and the GameTest server. Dev runs use a `run` folder inside the project with its own mods, config, and worlds, so the user's launcher mods don't load.
7. **Git:** ignore `run/`, `build/`, and `.gradle/`. Commit `docs/` and `CLAUDE.md`.
8. **Troubleshooting:** a Java version other than 21 is the usual cause of build failures. On Linux, a Gradle cache symlinked to an NTFS partition can crash `runServer`.

## 5. Architecture: spikes to run first

The riskiest parts are hooks into vanilla code, so test them with small experiments before building features.

- **Spike A, villager behavior:** can we store soldier data on a vanilla villager as an attachment and change what he does (for example, walk to a rampart when idle)? Villager AI is built on vanilla's brain system (look at the villager's brain setup code). Try NeoForge events first; if they can't reach it, try a Mixin. Report which hook works, and what breaks with other mods.
- **Spike B, client sync:** send one attachment value (a rank) to clients with a network payload, show it in the name label, and keep it correct after a relog and when tracking starts.
- **Spike C, job blocks:** register a custom job block (the recruiter box) as a point of interest and profession, and confirm a villager can claim it.
- **Spike D, SavedData placement:** place one structure at an exact stored position in a new world, using ungenerated land only.
- **Spike E, unloaded travel:** store a swarm or horn order as data and make it resolve when a chunk loads, with no per-tick work while unloaded.

After each spike, write the result into the decisions log.

## 6. Build plan

The stages match the build order in the Overview part. Test each before starting the next, and commit when a rule's GameTest passes.

### Stage 1: Core

**Scope:** soldiers and militia (roles, inventory slots, gear, food, desertion, honorable discharge), mess stations, post beds, ownership, village area with surveyor's markers and the surveyor's knife, the awaken horn, and the muster roll. **Read:** Soldiers and villages, War and defense, Config and recipes.

**Slices, in order:**
1. Mess station, post bed, and post block exist with recipes.
2. Making a soldier by hand: a villager given a weapon, chestplate, and helmet, with a free post bed and a stocked mess station, becomes a soldier. Name and badge show on clients.
3. Daily routine: two meals, sleeping in the post bed, walking to ramparts.
4. Ownership and village area: first mess station claims land; surveyor's markers (1 per 5 members); no overlap.
5. Desertion, discharge, the awaken horn, and the muster roll.

**Done when (GameTests):** a villager becomes a soldier only when every requirement is met; a hungry soldier deserts on day 7; a soldier with no home bed for 3 nights deserts; taking a soldier's weapon discharges him and he keeps his kills; two players can't claim overlapping land; the muster roll counts match.

### Stage 2: Economy

**Scope:** tax box and its tiers, tax coverage, recruiters, blacksmith, fletcher, and armorer supply, gear wear and death drops, ranks, veterans and the veteran's sword, and zombie soldiers. **Read:** Soldiers and villages.

**Done when:** a village collects taxes only for villagers a soldier covers (5 each) who can reach the box; trader tax rolls match the table (use a seeded random in tests); collection intervals follow village size; a recruiter turns unemployed villagers into soldiers up to his capacity and buys post beds for 5 emeralds; supply adds 1 item a week; broken gear is replaced from issued gear; ranks, health, and damage match the tables; a veteran defends within 32 blocks; cured zombie soldiers follow the 60/25/15 odds.

### Stage 3: Defense

**Scope:** ramparts, arrow bins, fallback and haven blocks, the remaining horns, levers and buttons, flares, the village bell, nightwatch and royal guard armor, royal guard posts, night fighting and carried lights, caltrops, castle gates, village protection, arrow recovery, and beacon effects on soldiers. **Read:** War and defense, Soldiers and villages (night fighting).

### Stage 4: War

**Scope:** allies, campaigns, camp blocks, blockades, assaults, sappers and axemen, siege ladders, boat plans, plank bridges and bridge plans, laced rations, mutiny, no building in enemy land, wanted posters, the reassignment writ, and village banners. **Read:** War and defense, Soldiers and villages (mutiny, ownership).

### Stage 5: Threats

**Scope:** new moon raids, swarms, bandits, bandit camps and cave hideouts, and vanilla mob changes. **Read:** Threats and mobs. Build the enlisted-player rule here, so a player who ignores the mod isn't affected.

### Stage 6: Extras

**Scope:** necromancy with necromancer crypts and phylactery shards, animal farmers and wild ranches, mob spawners, Village Chronicles, the training dummy, advancements, the village map, and the kingdom map. **Read:** Necromancy, Threats and mobs, Old kingdom lore (chronicles), War and defense (items).

### Stage 7: Endgame

**Scope:** bandit fortresses, kingdom titles, warlords, monuments, the Black Citadel with its chronicles, the Dragon's Well, the Phantom Roost, the royal enchanting table, brewing stand, and anvil, and the Blighted Tree. **Read:** Endgame.

**Citadel flow, in order:**
1. Kill the Ender Dragon, craft the Seal-Breaker, and use it on the gate.
2. Defeat the garrison, the necromancers, and the Corrupted King. Whoever then places a mess station and a soldier claims it.
3. Healing the tree (kill the Blight Heart, then 7 in-game days) reforms the barrier under the owner's control and wakes the stations, Well of Kings, Roost, safe road, and training grounds.
4. Restoring the Hall (all 12 relics, the Crown last) raises the guardians and gives the King's Horn, High King title, the raid drop, and the golden age.

### Stage 8: Lore

**Scope:** the 12 kings' relics and their tree origins, the 17 acolyte refuges and the Order of the Well, the 10 bandit fortresses, the End outpost, the Evoker Lord's castle, Torvald's and Valen's shrines, the Royal Annals and other books, and the hero legends. **Read:** Old kingdom lore, Endgame.

## 7. Rules every system must follow

- **Config:** every number goes in config with the default from the Config and recipes part.
- **mobGriefing:** respect it for door breaking, fire arrows, bandit burning, siege gear, and sapper digging.
- **Enlisted players:** swarm rolls, mod raids, and runaways only happen around players who have made a soldier, claimed a village, or used a wand. Mod raids only hit claimed villages.
- **Unloaded chunks:** swarms, bandit bands, and horn orders keep moving as stored data and only fight, eat, and occupy villages in loaded chunks. No per-tick work while unloaded.
- **Performance:** squad pathfinding, lazy idle AI, shared target scanning, reduced AI far from players, work spread across ticks, and event-driven updates (see Performance in the Overview part). Use the spark profiler from day one. The target is 100 soldiers fighting a 30-mob swarm within a fixed share of each tick.
- **IDs, not names:** allies, wanted posters, and ownership store player IDs.
- **Exact counts:** 17 refuges and 10 fortresses come from SavedData, placed when the world is first loaded, and never in already-generated chunks.
- **Relics:** the 12 relics (the Crown included) can't be destroyed and never despawn; a relic in the void reappears on the last solid ground it touched.
- **Client data:** anything a client must show goes through our own sync packets (1.21.1 attachments don't sync themselves).

## 8. Testing

- **GameTests** cover each rule with a pass or fail. Name them after the rule, and run them with the template's GameTest run config. Use seeded randomness for anything with odds.
- **Manual checklists:** keep a short checklist per stage in `docs/` for what a player sees (labels, particles, chat messages).
- **Test worlds:** use a clean test instance with only our jar for normal testing. Later, make a second instance with Create and Ice and Fire CE for compatibility. Never put the test jar in a main modpack's folder.
- **Performance:** after every stage, profile the big-fight scenario with spark.

## 9. Open items and risks

- **Undecided:** mod id and name, license, repo name, and the user's operating system and editor setup.
- **Stand-in looks:** which vanilla textures stand in for horns, plans, writs, and other items.
- **Cross-part references:** some text says "see Mutiny" or "see Fletcher supply" without a link now that sections are in different parts.
- **Risks:** hooking the villager brain (spike A); Mixin conflicts with other mods; client sync; placing 27 structures reliably; unloaded-chunk travel; and the size of the rule set. Raise problems early and ask rather than quietly changing a rule.
- **Proposed numbers** are untested. Expect to tune raid odds, mutiny odds, tax rates, and spectral king stats in playtesting.

## 10. First prompts for Claude Code

Paste these in order, one per session.

1. **Setup:** "Read the Master build note, section 4. Set up the project from the NeoForge 1.21.1 ModDevGradle template, create CLAUDE.md from section 11, turn on Parchment, and confirm `build` and `runClient` work with the empty mod. Don't write mod code yet."
2. **Spike A:** "Read section 5, spike A, and the Soldiers and villages part (Villager roles). Store a rank on a vanilla villager as a data attachment and make a soldier walk to a rampart block when idle. Report which hook works."
3. **Spike B:** "Read section 5, spike B. Sync the rank to clients with a network payload and show it in the name label."
4. **Stage 1:** "Read the Soldiers and villages part and the Master build note, Stage 1. Build slice 1, then stop and show me the GameTest for it."
5. **After each slice:** "Run the GameTests, fix failures, commit, then start the next slice."

## 11. CLAUDE.md draft

Paste into the repo root as `CLAUDE.md`, then trim it to what's true.

```
# Villager mod

A NeoForge mod for Minecraft 1.21.1 that turns villagers into soldiers and militia, with a village economy, horns, campaigns, raids, swarms, bandits, necromancy, and a citadel endgame. Rules live in docs/ (one file per part). Read docs/master-build-note.md first.

## Stack
- Minecraft 1.21.1, NeoForge 21.1.x, Java 21, ModDevGradle, Mojang names + Parchment.
- NeoForge docs: https://docs.neoforged.net/docs/1.21.1/

## Commands
- Build: ./gradlew build
- Run client: ./gradlew runClient
- Run server: ./gradlew runServer
- Data generation: ./gradlew runData
- GameTests: the template's GameTest run config

## Rules
- Read the matching docs/ file before building a system. "(proposed)" numbers are tunable defaults.
- Every number goes in config.
- Custom art encouraged. Vanilla stand-ins are fine until art is ready.
- Soldiers and militia are vanilla villagers with data attachments. Don't add a new entity type without asking.
- NeoForge 1.21.1 attachments don't sync to clients; use our own payloads.
- Respect mobGriefing. Store player IDs, not names.
- Swarms, bands, and horn orders travel as stored data while unloaded.
- Write a GameTest for each rule. Commit when it passes.
- If a rule is unclear or conflicts, ask. Record the answer in docs/ in the same commit.
- Never put a test jar in a real modpack folder.
```

## 12. Links

- NeoForge docs for 1.21.1: https://docs.neoforged.net/docs/1.21.1/ (entities: /networking/entities/, attachments: /datastorage/attachments/, block entities: /blockentities/, items: /items/, mod files: /gettingstarted/modfiles/)
- ModDevGradle: https://docs.neoforged.net/toolchain/docs/plugins/mdg/
- Parchment: https://docs.neoforged.net/toolchain/docs/parchment/ and https://parchmentmc.org/docs/getting-started
- Template: https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle
- NeoForge versions: https://projects.neoforged.net/neoforged/neoforge
- Create's version status: https://wiki.createmod.net/users/development-status
- Claude Code quickstart: https://code.claude.com/docs/en/quickstart

## 13. Decisions log

Add one line per decision, newest last, with the date and the reason.

- **2026-10-04:** platform set to Minecraft 1.21.1, NeoForge 21.1.x, Java 21, ModDevGradle, Parchment. Reason: official Create supports 1.21.1 and the design is meant to join modpacks. Revisit for 26.1.2 once Create ports.
- **2026-10-04:** soldiers and militia are assumed to be vanilla villagers with data attachments. To be confirmed by spike A.
- **2026-10-04:** the notes were drafted in a planning chat and split into parts: Overview, Soldiers and villages, War and defense, Necromancy, Threats and mobs, Endgame, Old kingdom lore, Config and recipes, and this note.
- **2026-10-04:** custom art encouraged (blocks, items, structures); vanilla stand-ins OK until assets exist.

---

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

---

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
- **What still hurts friends:** explosions, lava, fire, and sneaking hits. These are the only ways a player can kill his own soldiers, so friendly kills (see Mutiny) are always on him.

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

---

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

- **What it is:** a single large ladder object that two soldiers carry together and stand up against an enemy wall in one motion. Its height fits the wall automatically, up to 25 blocks. It reuses the vanilla ladder texture on a tall, flat shape. Recipe: 8 ladders + 2 iron ingots. Soldiers and militia follow the same rules.
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

---

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
- **Phylactery shard:** dropped by rogue necromancers (1–2 each) and the **two named bound necromancers** in the citadel (`docs/endgame.md`). A player necromancer killed by another player drops 1, at most once per in-game day.
- **Bone whistle:** 2 phylactery shards + a bone + a goat horn. Blowing it summons 3 wolves that fight for the player for 3 minutes, once per in-game day; 20 uses (proposed).
- **Blessed incense:** a phylactery shard + glowstone dust + a honey bottle. Given to a cleric in the player's own village, it lets him cure zombie villagers and zombie soldiers within 32 blocks of his brewing stand over one day, with no weakness potion or golden apple. Zombie soldiers cured this way return as soldiers 90% of the time instead of 60% (proposed).

---

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
- **Targets:** everyone, including villagers, soldiers, golems, players, and bandits. Robed necromancers are immune.
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

---

# Endgame

Details still to decide: bandit fortresses, kingdom titles, warlords, monuments, and the Black Citadel with its acolytes and the Blighted Tree. The lore behind it is in the Old kingdom lore part.

- **Bandit fortresses:** rare, large fortified structures ruled by a Bandit King over several bands, with walls and a treasury. Taking one is a full campaign: camp, blockade, siege ladders, sappers, and assault. Ten fortresses per world, placed from saved world data like the refuges; six of them each hold one king's relic, and the other four are ordinary bandit fortresses with no relic. They sit in remote, hard-to-reach terrain at least 2,500 blocks from spawn and never appear on village maps, so players find them with the Royal Annals hints, the burial maps, or a relic compass. Only the six relic fortresses are marked by those clues; the other four have the same garrison and a normal treasury, and the relic compass ignores them. They're well defended: a Bandit King and 3 bands (about 30 bandits, 12 of them archers on the walls), with the strongest garrisons guarding the weapon relics (proposed).
- **Kingdom titles:** a player's title depends on how many villages he owns. A village only counts while at least one of his soldiers there is alive. Perks are a chat title, extra recruiter slots, and better odds on rare tax items (gold, diamonds, and netherite) only.

| Title | Villages (proposed) | Extra recruiter slots (proposed) | Rare tax item odds (proposed) |
|---|---|---|---|
| Lord | 1 | 0 | Normal |
| Baron | 3 | +1 | +10% |
| Duke | 6 | +2 | +20% |
| King | 10 | +3 | +30% |

- **Warlords:** once a kingdom reaches a certain size, a Warlord raid arrives every 4 to 8 new moon cycles (random within that range), bringing its own siege equipment and growing with the player's title.
- **Monuments:** when a hero or Legend dies, he drops a service record, a paper with his name and stats. Players can craft and place statues in several sizes at any time, but they look rough and unfinished until a service record is applied. Then the statue takes on that soldier's look, name, rank, and kill count, building toward a Hall of Heroes.

## The Black Citadel

A post-End boss fortress that uses the mod's war systems.

- **The structure:** one huge fortress per world, 1,000 to 2,000 blocks from spawn, with a great road running straight from spawn to its gates. Its walls are made of a block nothing can break or breach, about 40 blocks tall (too tall for siege ladders) and several blocks thick with lava inside. Beneath the citadel, a lava lake sits under an unbreakable foundation, so no one can dig in from below. Its gates are special citadel doors that only players can break; soldiers can't. An ender barrier seals the whole fortress until the Ender Dragon has been killed in that world and a player uses the Seal-Breaker on its gate (see Breaking the seal). The barrier is completely impassable: it surrounds the citadel on every side, above and below ground, can't be broken, dug under, flown over, or crossed with ender pearls or any other teleport.
- **The boss:** the Corrupted King, a cruel king raised from the dead by his necromancers, extremely hard to kill. He leads a garrison of about 100 defenders (at least 50; to be tuned for lag), undead soldiers and living militia, plus **two named bound necromancers** (see `docs/endgame.md`). The fortress is built so a small force can defend it well: high walls, few entrances, chokepoints, and archer towers. Attackers need a big army.
- **The fight:**
    1. **The siege:** the walls can't be breached or climbed, so the only ways in are the citadel doors, which players must break themselves while their army covers them, or the secret tunnel.
    2. **Starving them out (optional):** blockading the citadel makes its living garrison desert over time under the normal desertion rules.
    3. **The two bound necromancers:** kill both (`docs/endgame.md`); while either lives, fallen defenders keep rising.
    4. **The duel:** the Corrupted King has 300 HP (Wither-level), 40% damage resistance, and Legend-level melee damage, and takes double damage from behind (proposed).
- **The Corrupted King's Crown:** his reward, worn in the helmet slot. While worn, soldiers of the wearer who die within 32 blocks of him rise after 10 seconds as zombie soldiers that keep their gear and rank, fight on for 2 minutes, then crumble to dust, gone for good. Each fight in which it raises anyone adds one grumble point to its group (proposed). It's the 12th relic, belonging to the Corrupted King's own statue: placing it there lays him to rest and finishes restoring the Hall. The wearer can keep it as long as he likes, but the Hall isn't fully restored until he gives it up.
- **One crown per world:** the Corrupted King can only be defeated once, so there's a single crown for players to fight over. All 12 relics, the crown included, can't be destroyed and never despawn. If the crown is lost anyway, it returns to the citadel's throne. A lost relic stays wherever it was lost; if it falls into the void, it reappears on the last solid ground it touched. Players can track one down with a relic compass.
- **The citadel beacon:** a normal beacon placed deep in the keep, so its range mostly covers the defenders' positions rather than the approach. Its pyramid sits in a hidden room beneath the keep; destroying the pyramid shuts the beacon off. Once the Corrupted King falls, players can take the beacon and use it like any other.
- **The treasury:** a stocked treasury block, plus gold, diamond, and emerald blocks piled around the room.
- **The library:** full bookshelves, chests of strong enchanted books, the royal enchanting table (see Acolytes), plus the Royal Annals: 12 volumes, one for each king (the 12th is the Corrupted King's), each also kept as a copy in an acolyte's refuge (see Acolytes), telling his life, his famous relic and its power, and the battle where he died. Each volume hints at where that king's relic lies. Volumes can be copied the way vanilla written books are (the volume plus a book and quill), and copies work in the relic compass recipe. The library also holds two enchanted books impossible in vanilla, one copy each: Sharpness VII and Looting V (proposed levels). They work on any weapon that can take that enchantment. The Seal-Breaker holds only one enchantment, so putting one on it means choosing between them. Both books sit in a sealed vault that only opens once the Corrupted King is defeated, so nobody can sneak in through the tunnel and grab them early.
- **The alchemy room:** brewing stands, chests of strong potions, and the royal brewing stand (see Acolytes).
- **The Hall of the Twelve Kings:** statues of the old kingdom's 12 kings, the last of them the Corrupted King, all named in the Village Chronicles. Their relics are missing.
- **The story:** the citadel was once the seat of a great kingdom. Its twelfth and last king was cruel and weak, and when he died his necromancers raised him back to rule forever as the Corrupted King. To keep anyone from ever reaching him, they bound the citadel's seal to the life of the Ender Dragon, which is why the dragon has to die first. Chronicles about the old kings, the fallen heroes, and the sealing tell the full story, and tie to the statues in the hall.
- **The secret tunnel:** a hidden, half-collapsed tunnel runs from outside the walls into the lower keep. Only a super rare chronicle hints at where it starts.
- **The dungeon:** about 8 captives (proposed) the garrison took from nearby villages, locked in cells: mostly master-level traders, plus a few small-time local heroes from recent years. Freed traders go to the player's nearest village as normal villagers at their trade level, and the heroes join as named soldiers, seasoned soldiers or heroes at most. They have nothing to do with the old kingdom.
- **The royal armory:** racks and chests of full iron and diamond gear, enough to re-equip an army.
- **The war room:** burial maps marking where each fallen hero of the old kingdom died.
- **The banner of the old kingdom:** a unique village banner, no longer kept in the citadel. One of the 4 ordinary refuges has a 25% chance per world to hold it in its chest (proposed), so many worlds have none. It can't be copied, can only fly over one village at a time, and follows the relic rules, so it can't be destroyed. Soldiers carrying it on their shields never desert from morale.
- **Shadow over spawn:** until the citadel falls, new moon raids are more common near spawn, and villages near spawn get extra militia (which also means fewer normal trades there). Once the Corrupted King is defeated, raids everywhere return to normal rates.
- **The kings' relics:** the Hall holds statues of the old kingdom's 12 kings, each famous for a weapon or armor piece (the 12th, the Corrupted King, for his Crown). Bandit fortresses farther from spawn hold six of the relics, each sold there at the end of a long chain of thieves who couldn't use it. Four sit in acolyte refuges, saved from the treasury in the purge (see Acolytes), and the burial maps point to each relic's current place. The Thornheart Circlet lies in a necromancer outpost on an outer End island (proposed), taken by the necromancers who killed its king. The war room's burial maps reveal them once the citadel falls. Relics can be used by players and soldiers, have unique powers, and never break.
- **Restoring the citadel:** once the garrison, the necromancers, and the Corrupted King are all defeated, whoever places a mess station and a soldier in the citadel claims it. The owner then restores it in two steps, in either order: healing the tree and restoring the Hall. Placing a relic on its king's statue consumes it; it stays fixed to the statue for good, and the Crown goes last, on the Corrupted King's statue. The King's Horn has its own vault puzzle (opened by restoring the Tidespear statue) and can only summon kings whose statues are restored.
    1. **Healing the tree** (kill the Blight Heart, then 7 in-game days of recovery) gives:
        - **The citadel becomes his:** the ender barrier forms again under his control. Until then the barrier stays down, so anyone can attack the owner. Only he and his allies can pass, unless a challenger uses a Seal-Breaker on the gate, which lowers the barrier for 10 minutes for the wielder and his army (proposed). A challenger who kills every spectral king standing and every one of the owner's soldiers inside the walls can claim the citadel with a mess station and a soldier, taking over everything restoration gave. If he fails, the kings rise again after 3 in-game days (proposed).
        - **The stations wake:** the royal enchanting table, brewing stand, and anvil work with their acolytes.
        - **The Well of Kings:** works for the owner and his allies.
        - **The Phantom Roost:** phantoms no longer hunt him or his allies anywhere in his kingdom. The phantoms that would have spawned for them appear at night in the Roost, a caged chamber atop the keep, where the owner can farm membranes for necromancer hoods, robes, and repairs. Everyone else in his kingdom still gets phantoms as normal.
        - **A safe road:** hostile mobs can't spawn on or near the great road from spawn to the citadel.
        - **Royal training grounds:** permanent, unbreakable training dummies that only work inside the citadel. They don't give kills. A soldier who practices on them for 3 days becomes royal-trained for good, with +10% damage. For 30 in-game days after training he also never deserts from morale or joins a mutiny, and one more day on the dummies renews it (proposed). Royal-trained soldiers show "Royal" before their label (for example, "Royal Hero" + name), and their post bed has a gold trim.
    2. **Restoring the Hall** (placing all 12 relics, the Crown last) gives:
        - **The kings return:** each great king rises as a spectral guardian as his relic is placed, and defends the citadel forever. Each has 100 HP, 30% damage resistance, Legend-level melee damage, and no knockback (proposed), and fights with the power of his own relic (see the relics table): the Dawnbreaker king burns undead, the Stormcaller king calls lightning, the Long Hunt king shoots from range, the Kingsmaul king stuns, the Oathkeeper king shields his allies, the Last Mantle king survives one lethal hit, and so on. They fight the owner's enemies only and stay inside the citadel; the kings the horn summons are temporary copies.
        - **The King's Horn:** the horn lies in a sealed vault beneath the Hall of the Twelve Kings. It opens when a player uses a water bucket on the feet of the Tidespear king's statue, the same king in every world, but only once the Tidespear relic has been placed on that statue (proposed). This is a special action, not a placed block, so the citadel's building ban doesn't stop it. The Rhyme of the Twelve in the Horn-keeper's refuge chest (a normal book, copyable) holds a riddle that points to the Tidespear king, and his Royal Annals volume confirms it. Water at any other statue's feet does nothing. The horn only works for the citadel's owner. It summons one king, the best match for his main opponent, picked from the kings whose relic is on their statue, which may be only the Tidespear king at first. The king fights beside him anywhere in his kingdom for 1 minute, once per in-game day (proposed). The main opponent is the biggest enemy force within 64 blocks, checked in this order (proposed): ravagers and other heavy mobs call the Kingsmaul king; mostly archers and crossbowmen call the Long Hunt king; undead, minions, and zombie swarms call the Dawnbreaker king; any group of 10 or more calls the Stormcaller king; raiders and bandits call the Oathkeeper king; anything else calls the Tidespear king. If the best match's statue isn't restored, the Tidespear king comes instead. The summoned king only fights hostile mobs, raiders, bandits, and swarms, never players or soldiers. It can't be destroyed, and if it's lost it returns to its vault.
        - **High King:** a title above King, with +4 recruiter slots and +40% odds on rare tax items (proposed).
        - **The shadow lifts:** mod raids worldwide drop below normal (proposed: 20% on new moon night, 8% on the nights either side), and no swarms form anywhere in his kingdom.
        - **A golden age:** villagers in his kingdom breed faster and restock trades more often, and all villager trades there are cheaper for everyone (proposed: 40% cheaper, stacking with vanilla discounts down to vanilla's 1-emerald minimum).
- **The Well of Kings:** a source of unlimited experience inside the citadel. Standing in it gives XP straight to the player, not as orbs, so it can't be bottled or collected by machines (5 XP points per second; about 5 minutes to reach level 30 from zero). Only the citadel's owner and his allies can use it.
- **Relic compass:** an extremely expensive item that points to one relic wherever it is, including carried by a soldier, a bandit, or another player, then breaks once the compass holder picks that relic up. It's made with a special recipe that consumes full stacks: 8 stacks of ender pearls (128), a stack of diamond blocks (64), and the Royal Annals volume of the king whose relic it tracks (proposed).

| Distance from spawn | New moon raid chance | Nights either side | Militia in natural villages |
|---|---|---|---|
| Under 1,000 blocks | 60% | 25% | 4 |
| 1,000 to 3,000 blocks | 50% | 20% | 3 |
| Over 3,000 blocks | 40% (normal) | 15% (normal) | 2 (normal) |

**The 12 relics (proposed):**

| Relic | Type | Power |
|---|---|---|
| Dawnbreaker | Sword | Sets undead on fire and deals extra damage to them |
| Oathkeeper | Shield | Allies within 8 blocks of the holder take 20% less damage |
| The Long Hunt | Bow | Never uses arrows, and arrows pierce through enemies |
| The Thornheart Circlet | Helmet | Immune to knockback, with night vision, and poisonous plants, corruption, and laced or corrupted food glow within 32 blocks, even through walls (proposed) |
| Stormcaller | Axe | Chance to call lightning on hit; breaks wood twice as fast |
| The Corrupted King's Crown | Helmet | While worn, soldiers of the wearer who die within 32 blocks of him rise as zombie soldiers that keep their gear and fight on for 2 minutes, then crumble to dust; each fight it raises anyone in adds one grumble point (proposed) |
| The Kingsmaul | Mace | Smashes knock back and stun nearby enemies |
| Tidespear | Trident | Returns when thrown and pulls enemies toward the wielder |
| Ironroot Helm | Helmet | While worn: +5 recruit slots per recruiter, +25% trader tax odds, half the new moon raid chance, and no swarms in his villages (proposed) |
| The Last Mantle | Chestplate | Survives a lethal hit once per day |
| Greaves of the Long March | Leggings | Faster movement; players and soldiers wearing them never get hungry |
| Stormstriders | Boots | No fall damage and can walk on water |

**Relic strength:** every relic beats anything players can make, even fully enchanted netherite. Weapons deal more damage, armor protects better, and tools break blocks faster than the best possible netherite equivalent (proposed: about 25% better). Relics can't be enchanted, since their base stats already exceed the enchanted maximum.

**Breaking the seal:** killing the Ender Dragon only weakens the barrier. To open it, a player must also craft the Seal-Breaker and use it on the citadel's gate (proposed recipe):

1. **Ender core:** 8 eyes of ender around a bottle of dragon's breath. Two are needed, so 16 eyes of ender, a full stack of ender pearls.
2. **Nether core:** a nether star surrounded by 4 netherite ingots and 4 crying obsidian.
3. **Seal-Breaker:** both ender cores, the nether core, 4 shulker shells, 2 end crystals, and a dragon head.

The nether star means the Wither has to be beaten too, so opening the citadel takes the Ender Dragon, the Wither, End cities, and a lot of netherite.

**The Dragon's Well:** an old stone well just outside the citadel's gates, beyond the ender barrier, where the necromancers drew the dragon's essence to bind the seal. Once the Ender Dragon has died in that world, right-clicking the well with an empty bottle gives a bottle of dragon's breath, once per player per in-game day (proposed). This keeps the Seal-Breaker craftable for everyone after the first dragon kill.

**The Wellkeeper's Account:** a lore book on a lectern beside the well. It tells how the seal was bound to the dragon and hints at every Seal-Breaker ingredient. Players can copy it like a Royal Annals volume, and there is no other copy.

**Dragon heads can't be lost:** dragon heads follow the relic rules. They can't be destroyed and never despawn, and one that falls into the void reappears on the last solid ground it touched. A dragon compass points to the nearest dragon head not in a player's inventory, or to the nearest unlooted End ship if there are none. Recipe (proposed): the relic compass recipe at a quarter of the cost (2 stacks of ender pearls, 16 diamond blocks), with a bottle of dragon's breath in place of the Annals volume.

**The Seal-Breaker is a sword (proposed stats):**

- Using it on the gate opens the seal, and the sword isn't used up.
- About 15% stronger than a netherite sword and never breaks. It can hold only one enchantment, so the player has to choose which one.
- Deals double damage to undead, including the citadel's garrison and the Corrupted King, and stays useful against swarms and minions for the rest of the game.
- Breaks the citadel doors five times faster than other tools. Its undead bonus stacks with its enchantment, including Sharpness VII.

## Acolytes

Acolytes are the surviving veterans of Kaelen's Order of the Well, all men, who scattered when the necromancers took the citadel, each hiding something he refused to leave behind. They serve whoever tends the tree: once the citadel is claimed they return to run its royal enchanting table, brewing stand, and anvil, and they steady the soldiers around them.

- **Refuges:** 17 per world, placed from saved world data when the world is first loaded so the count is exact, between 500 and 4,000 blocks from spawn and at least 700 blocks apart (proposed). Each is a small, ordinary-looking hideout, not a temple: a hermit's hut, a cellar under a ruined house, a collapsed chapel, or a cave nook. Necromancers hunt the survivors, so most refuges have 0-6 undead nearby. Each has a chest with what its acolyte saved, so nothing valuable is carried by the acolyte himself.
- **Who they are:** all 17 are men, veterans of Kaelen's campaigns who belong to the Order of the Well (see the Old kingdom lore part). None alive remembers the earlier kings, only the Annals.
- **What they carry:** each acolyte saved something he refused to leave behind and hid it in a chest in his refuge. Of the 17:
    - 12 refuges each hold one volume of the Royal Annals in the chest, a different king each. These are copies, so the library's originals don't need to be unbreakable.
    - 1 is the Horn-keeper's refuge, with *The Rhyme of the Twelve* in its chest, a rhyme that hides the riddle about opening the King's Horn vault (see the Hall restoration list).
    - 4 have only ordinary loot in the chest. Two of them hold Thornwald's notes, one on the greaves and one on Valen's sword, and one may hold the old kingdom banner (25% chance per world).
- **Relics:** four relics sit in the chests of the refuges that hold those kings' Annals volumes, saved from the treasury in the purge: The Last Mantle (Edmund), the Ironroot Helm (Aldric), Stormstriders (Corwin), and Oathkeeper (Oswin). The rest lie in bandit fortresses and an End outpost (see the Old kingdom lore part). Anyone who clears the undead can take them.
- **Treatises:** six treatises, on base defense, campaigns and supply, night fighting, sieges and sappers, traps and spoiled food, and holding the citadel. Each is placed independently at a random one of the 10 bandit fortresses or the End outpost, so some places hold two and some none. Treatises are normal books that players can take and copy.
- **Chests:** chests are ordinary chests, so anyone who clears the undead can take the books, relic, or banner. Books can be copied; relics and the banner can't be destroyed and follow the relic rules.
- **Recruiting:** clear the refuge's undead, then talk to the acolyte. Any player can recruit an acolyte whatever his title, since they serve whoever tends the tree. He moves to one of that player's villages with a free bed and an acolyte's lectern (recipe proposed: lectern + book + gold ingot).
- **Homecoming:** when a player claims the citadel, all 17 acolytes, recruited ones included, leave wherever they are and walk to the citadel, arriving within 3 in-game days (proposed). Each brings his books and keeps them on a lectern beside him. Relics go to the treasury and the banner to the throne room. Whatever players already took from a chest stays with them, and a dead acolyte brings nothing. They serve the new owner, but can't use the citadel's stations until the tree is healed.
- **Three kinds:** 6 arcanists run the enchanting table, 6 alchemists run the brewing stand, and 5 smiths run the anvil, assigned at random per world.
- **Aura:** soldiers within 16 blocks of an acolyte take 10% less damage and heal 1 heart every 10 seconds out of combat (proposed). In campaign boots, an acolyte follows the army, stays near the camp block, and flees from combat. Healing and damage-reduction effects on soldiers (the aura, the Kingstree, Kingswood trees, and beacons) don't stack: only the strongest of each type applies (proposed).
- **Death:** a dead acolyte is never replaced. His chest stays where it is, so his death loses only the acolyte and his homecoming, not the books, relic, or banner in the chest.
- **Citadel stations:** the royal enchanting table (library), royal brewing stand (alchemy room), and royal anvil (royal armory) are built into the citadel. They're unbreakable and can't be crafted or placed. Each works for the citadel's owner and his allies while an acolyte of the matching kind stands at its lectern beside it, whatever the owner's kingdom title. All three stay dormant until the tree is healed. Acolytes beyond those three jobs have no job block yet (campaign help is one idea).
- **Royal enchanting:** works like a vanilla table, but can raise one enchantment on an item one level past vanilla's max (for example Protection V, Sharpness VI, Efficiency VI, Unbreaking IV) for 30 levels and 3 lapis (proposed). One boosted enchantment per item, so this stays below relics and the library's Sharpness VII.
- **Royal brewing:** level III strength, speed, and regeneration, double-length versions of any potion, and two potions only it can make (proposed): Potion of Resolve (splashed soldiers don't flee, retreat, or desert from morale for a day) and Rallying Splash (heals the thrower's soldiers within 8 blocks by 10 hearts).
- **Royal anvil:** reforges diamond gear into netherite without a smithing template, using a netherite ingot (proposed).

## The Blighted Tree

A great tree in the citadel's courtyard, poisoned by the necromancers. Healing it is the second half of restoring the citadel, alongside the Hall, in either order.

- **The blight:** the tree stands gray and leafless and can't be harmed. Only the citadel's owner can heal it, and only after the Corrupted King falls. Healing takes one step, then time:
    1. **Kill the rot:** a root cavern opens beneath the tree. At its heart, the Blight Heart, a stationary core, keeps raising minions until players destroy it. It glows through stone for anyone wearing the Thornheart Circlet, which helps but isn't required.
    2. **Let it recover:** once the Blight Heart is dead the poison is gone, and the tree heals itself over 7 in-game days (proposed), then blooms. No potion or watering is needed. The citadel's stations wake when it blooms.
- **The Kingstree:** healed, the tree blooms. The owner, his allies, and his soldiers within 48 blocks regenerate slowly, and soldiers there never stumble in the dark.
- **Saplings:** the Kingstree drops one Kingswood sapling at its base every 30 in-game days (proposed), with a chat message to the owner.
- **Kingswood trees:** a sapling grows into a small tree. Within 16 blocks, soldiers heal 1 heart every 10 seconds out of combat and villagers breed 25% faster (proposed). Its leaves never drop saplings and it yields 8-12 logs once, so Kingswood is finite. Anyone can chop a Kingswood tree, which makes them worth raiding.
- **Kingswood uses (proposed):** a Kingswood gate, a castle gate sappers and axemen can't break and players break twice as slowly as an iron gate; a Kingswood bow with 15% more arrow damage, for players and soldiers; and a Kingswood mess station whose meals heal 6 hearts instead of 3.

---

# Old kingdom lore

The Black Citadel was the seat of a kingdom of twelve kings, who ruled one after another around the god tree. Every great relic came from the tree, and the Royal Annals tell each king's life.

## The twelve kings

| King | Relic | Feats |
| --- | --- | --- |
| Edmund Last Mantle | The Last Mantle (chestplate) | Ruled the hill castle and planted the god tree. |
| Garrick Kingsmaul | The Kingsmaul (mace) | Hunted down and killed the bandit king, then built the outer wall around the tree. |
| Marek Tidespear | Tidespear (trident) | Sank the drowned fleet and took the ocean monument from its elder guardians. |
| Valen Dawnbreaker | Dawnbreaker (sword) | Killed the villager-made Wither and lit the citadel beacon from its nether star in a tower on Garrick's wall; Corwin later built the keep around it. |
| Corwin the Good | Stormstriders (boots) | Hunted the phantoms in their mountain roosts, caged them in the Roost atop the inner keep he built, and sealed the Warden. |
| Torvald of the Long March | Greaves of the Long March (leggings) | Laid the Great Road, marched through the Nether and returned with the secret of netherite, and killed the Evoker Lord to end the illager war. |
| Aldric the Founder | Ironroot Helm (helmet) | Made the walled hill a kingdom, wrote the first charter, and broke the Great Swarm, the last straw that won the villagers over. |
| Oswin Oathkeeper | Oathkeeper (shield) | Killed a famous necromancer, starting the feud, and kept Aldric's pact by driving the world's scariest necromancer away from his villagers. |
| Hakon Longhunt | The Long Hunt (bow) | Chased the scariest necromancer, made the End portal, killed him in the End, and took the last dragon egg home for safekeeping. |
| Kaelen Stormcaller | Stormcaller (axe) | Dug out the Well of Kings, and founded the Order of the Well and the Royal Annals. |
| Thornwald Thornheart, the Last Good King | Thornheart Circlet (helmet) | Hatched the dragon and carried it to the End in secret. |
| Maldric, the Corrupted King | The Corrupted King's Crown (helmet) | Signed the Black Treaty, which let the necromancers into the citadel. |

The kings in the middle, from Marek to Torvald, had a spotty relationship with the villagers and often had to win their favor. Aldric's charter and the Great Swarm finally settled it.

## Deaths and where the relics are

| King | Death | Relic now |
| --- | --- | --- |
| Edmund | A bandit king destroyed his castle trying to reach the tree, and Edmund died defending it. | Refuge: the tree spat the mantle out to his son Garrick the day he prayed beneath it before the hunt, and acolytes saved it from the treasury. |
| Garrick | Died in the last siege of the bandit empire's remnants. | Fortress: the mace was too heavy for anyone else, so it was looted and sold three times. |
| Marek | Died at home. | Fortress: he buried the trident at the tree when he returned, and necromancers later dug it up and sold it to bandits. |
| Valen | Held a border village's wall all night against a crypt's undead and died as the sun rose. | Fortress: the sword flared as he fell, and thieves later took it from the village. |
| Corwin | Died old and loved. | Refuge: acolytes saved the boots from the treasury. |
| Torvald | The army arrived too worn to fight, but the greaves kept him fresh, so he held a besieged town's gates alone and saved it, then died of his wounds. | Fortress: his comrades set out to carry the greaves home to the tree, and raiders ambushed them in a mountain pass. The greaves passed through a smuggler and a fence to a fortress. |
| Aldric | Died old in the keep. | Refuge: acolytes saved the helm from the treasury. |
| Oswin | Died shielding the tree's first sapling from the scariest necromancer. | Refuge: his friend Lord Harren took the shield from the dirt and failed to use it, and acolytes saved it. |
| Hakon | Died in a trapper's cabin in the far north, hunting the necromancer's remaining goons. | Fortress: the bow wouldn't draw for anyone else, so it was sold along a chain. |
| Kaelen | Died old in the acolytes' library. | Fortress: the necromancers looted the axe from the throne room in the purge. |
| Thornwald | Killed in the End by the necromancer's leftover goons, who knew they had to kill him before they could poison the tree. | End outpost: they took the circlet to a necromancer outpost on an outer island. |
| Maldric | Poisoned and raised by two disguised necromancers. | The Crown stays with him until a player kills him. |

**Oswin and Hakon:** Oswin had adopted the orphan Hakon as his hunting squire and was starting to treat him as heir, which threatened his friend Lord Harren, next in line for the throne. When the scariest necromancer came for the tree's first sapling, Harren hid and refused to cover Oswin. Oswin died, the sapling jumped out of the ground into Hakon's hand and became his bow, and Harren took the shield from the dirt. Hakon was one of the few witnesses. When Hakon came back from the End and confronted him, the shield wouldn't protect a coward from its own twig arrows. Harren is not one of the twelve.

**The fall:** two of the necromancer's goons returned from the End in disguise and beguiled Thornwald's son Maldric, ending the line of good kings. He signed the Black Treaty on their advice, the tree was poisoned and became the Blighted Tree, and after Maldric died the necromancers raised him and bound the citadel's seal to the dragon.

## How the god tree made each relic

- **The Last Mantle:** an old invincibility charm Edmund used to shield the sapling. The tree absorbed most of its magic when he died and spat it out to Garrick, so it now saves a wearer once a day.
- **The Kingsmaul:** the handle is a branch a bandit broke off the tree, and the head is cast from the bandit king's crown.
- **Tidespear:** Marek took it from a drowned pirate lord and killed the elder guardians with it. He buried it beside the tree, and the roots curled around it and gave it its pull.
- **Dawnbreaker:** before going after the Wither, Valen quenched the blade in the tree's golden sap, which hardened to amber that burns the undead like sunlight.
- **Stormstriders:** Corwin lined the boots with two leaves picked from the tree, and they saved him many times while he hunted the phantoms in their mountain roosts.
- **Greaves of the Long March:** Torvald made them from fruit picked from the tree, on his father's advice.
- **Ironroot Helm:** Aldric wove it from an iron-hard root of the tree during his long trek recruiting villagers against the Great Swarm.
- **Oathkeeper:** a shield of layered bark from the trunk. Oswin's name comes from keeping Aldric's pact and driving the scariest necromancer away from the new villagers.
- **The Long Hunt:** the tree's first sapling jumped out of the ground into Hakon's hand when Oswin died. His first shot marked the necromancer, which let him track him to the End.
- **Stormcaller:** lightning struck the tree the night Kaelen dug the well and fused its roots to glass. The axe head is that glass.
- **Thornheart Circlet:** it's woven from the tree's thorn sprouts, which prickle at poison. The necromancers had to kill Thornwald before they could poison the tree.
- **The Crown:** a branch from the first blighted bough, so its power over the dead is the corruption itself.

## The Order of the Well

- **Before the Order:** Marek, Corwin, and Aldric left manuscripts in the keep's library. Necromancers tried many times to steal pages and never got one.
- **Founding:** Kaelen decided to guard them more closely. He gathered 17 of his veterans, all men, who had seen what the tree did for the world: its sap closing wounds, its fruit feeding armies. They became the Order of the Well and vowed to guard the Annals, the Well, and the tree, and to serve whoever tends the tree, not whoever wears the crown.
- **Maelor:** the eldest founder and Kaelen's campaign scribe. Kaelen asked him to continue the Annals, and helped him write. He's the last founder alive, and the acolytes today trained under Kaelen or Thornwald.
- **The purge:** after the Black Treaty, the necromancers drove the Order out. Each member fled with one Annals volume so the story couldn't be erased, and four also carried relics out of the treasury.
- **Now:** they hide in refuges, still hunted, hoping someone will take the citadel and heal the tree.

## Books

| Book | Author | Where |
| --- | --- | --- |
| The Royal Annals, 12 volumes, one per king | I and II: Marek. III: Marek, finished by Corwin. IV and V: Corwin. VI and VII: Aldric. VIII to X: Maelor, at Kaelen's request with his help. XI and XII: Maelor, XI from court records and XII written in exile and left unfinished. | The library, plus a copy in each of 12 refuges. |
| The Whole Tale | Aldric, one narrative of everything up to his day. | The library. |
| The Catalogue of the Royal Library | Maelor. It indexes every book once kept in the library and marks each missing one "lost in the purge". | The library. |
| The Order of the Well | Maelor, the Order's history. | The refuge holding Kaelen's Annals volume. |
| The Wellkeeper's Account | The Order. | A lectern beside the Dragon's Well, with no copy. |
| The Rhyme of the Twelve | An old rhyme that hides the horn vault's riddle. | The Horn-keeper's refuge. |
| Thornwald's notes on the greaves | Thornwald, hunting the relics before he learned about the egg. They follow the ambush, the smuggler, and the fence, then break off at a torn page saying the egg is alive and he must go home. | An ordinary refuge. |
| Thornwald's notes on Valen's sword | Thornwald. They follow the thieves from Valen's village and the fence to a fortress, then stop mid-sentence at the egg. | An ordinary refuge. |
| Six treatises | Unsigned officers of the old kingdom. | Random bandit fortresses or the End outpost. |
| The 16 Village Chronicles | Various. | Village chests, bandit camps, outposts, and strongholds. |

## Hero legends

Eight short books about heroes who weren't kings. Each has a random chance to appear on the citadel's library shelves, in village chests, and in bandit camps (proposed: about 30% per book on the shelves, 3% per village chest, and 5% per bandit camp). Each ends with a small tip about the mod.

| Book | Story | Tip |
| --- | --- | --- |
| The Gardener of Ash Hill | When the bandit king torched Edmund's castle, a gardener hauled water up the hill all night to keep the sapling's roots wet. | Soldiers' fire arrows can set your own wooden walls and crops alight, so keep water near them. |
| The Mason's Daughter | A girl stopped the masons from cutting a root that blocked Garrick's foundation, so the wall curves around it to this day. | A sapper's progress on a block only resets if the block is broken or replaced, so defenders can repair a wall by replacing damaged blocks. |
| The Ferryman's Wager | A ferryman bet his boat and his life that he could get Garrick across the flooded river before the bandit king's army arrived. | A boat plan sends every soldier at a camp, camp guards included, so keep boats stocked in the camp block. |
| The Knight Who Would Not Kneel | When Lord Harren seized the regency, Sir Perrin refused to swear to him and followed Hakon through the End portal. Hakon was captured carrying the egg home, and Perrin broke him out and fought their way back to the portal. | A royal guard follows you everywhere like a tamed wolf, but won't teleport near enemies, so don't count on him to save you in a fight. |
| Hollis and the Pass of Crows | A shepherd followed the raiders who took Torvald's greaves for three days across the mountains, and lost the trail one day short of the smuggler's den. | Relics never break, and the relic compass finds one even in a thief's pack. |
| Wend the Thief-Taker | A bounty hunter traced the Kingsmaul's chain of thieves and took it back once, then lost it to the next buyer. | A wanted poster in your village makes your soldiers attack the listed players on sight. |
| The Copyist | After the Black Treaty, a young scribe copied Maelor's Annals page by page in secret, smuggling each one out in loaves of bread. | You can copy any book with a book and quill, and copies of Annals volumes work in the relic compass recipe. |
| Old Marrow and the Laced Bread | A healer saved Torvald's army from poisoned rations during the illager war. | Anyone can put soldier food into a mess station but only you can take it out, so check yours after a raid. |

## Landmarks

- **Torvald's shrine:** in the town he saved, a statue stands with an empty niche where his greaves once hung. Praying there gives a march blessing for a week, once per player: never hungry and faster.
- **Valen's wall:** the ruined wall of a border village, kept as a landmark, with a shrine. Praying there makes undead flee from you for a week, once per player.
- **The Evoker Lord's castle:** a ruined castle 4,000 blocks straight south of spawn (proposed), where Torvald killed the Evoker Lord. The distance is why his march earned its name. It's a ruin with a few illusioners and other illagers, and a little loot hidden for players who search hard.
- **The necromancer outpost:** a base on an outer End island, reachable only once the dragon is dead, holding the Thornheart Circlet.

## Village Chronicles

- **Village Chronicles:** books that explain the mod through lore stories, like the first Legend or the first necromancer. Each chronicle teaches one system (soldiers, taxes, horns, necromancy, and so on) through its story.
- **Where to find them:** fairly common in village chests, more often in big villages (towns and cities). Also found in bandit camps, pillager outposts, and strongholds.

**Planned chronicles:** each is an entertaining story that teaches one part of the mod.

| Title | Story | Teaches |
|---|---|---|
| From Stone to Diamond | A farm boy enlists, survives raid after raid, and rises from plain soldier to Legend | Ranks, kills, badges, rank health and damage, bed colors, names |
| The Recruiter's Handbook | An in-world manual written by a grumpy old recruiter, full of his complaints and advice | Recruiter boxes, post beds and post blocks, soldier requirements, issued gear, supply |
| The Honest Collector | A tax collector tries to run a fair village while bandits, nobles, and hungry soldiers all want a cut | Tax boxes and tiers, coverage, biome taxes, trader rolls, the tax box cap |
| The Necromancer of Ashfall | A desperate lord raises the dead to save his village, loses himself to the wand, and is finally defeated by a hero | Wand, charges, heart loss, minions, grave markers, effigies, necromancer penalties, absolution |
| Hold the Line | A baker grabs an axe to defend his shop and slowly becomes the village's toughest militiaman | Militia, the home bonus, fleeing, seasoned militia, veterans and the veteran's sword |
| Night of the Black Moon | One village's desperate night against a new moon raid, told by the boy who rang the bell | New moon raids, the village bell, the awaken horn, ramparts, arrow bins |
| Bread and Boots | A sergeant's campaign journal, from leaving home to starving out an enemy town | Campaign boots, camp blocks, food on the march, blockades, attack plans, assaults |
| The Deserter's Road | A hungry, homeless soldier abandons his post and falls in with a bandit band | Hunger, homelessness, morale, desertion, bandits, troubled youth |
| Horns of the Valley | A horn-maker explains each horn she crafts through the battles they decided | Horns, levers and buttons, flares, ranges, the village bell |
| The Long Herd | A wandering rancher drives his herd from village to village, looking for a home | Animal farmers, wild ranches, pens, feed, meat and leather taxes |
| Two Banners | Two rival lords claim neighboring villages, then must decide whether to fight or ally | Ownership, mess stations and claims, borders, allies and ranks, village banners |
| The Last Good King | Thornwald, the last good king, who hatched a dragon out of kindness and died in the End | Citadel backstory; names the kings in the Hall of the Twelve Kings |
| The Cruel Crown | Maldric, the last of the twelve kings, beguiled into the Black Treaty by two disguised advisers, then poisoned and raised by them | Who the Corrupted King is; hints at the necromancers' role in the fight |
| The Sealing | How, after Maldric's death, the necromancers bound the citadel's seal to the life of the Ender Dragon Thornwald hatched | Why the dragon must die first |
| A Spy's Letter | A captured spy's last report from inside the citadel (rare) | Tips: the beacon's hidden pyramid room, guard positions, and the citadel doors only players can break |
| The Mason's Confession | A dying mason confesses to a tunnel he dug to smuggle out his family (super rare) | Hints at where the secret tunnel starts |

---

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
| Siege ladder max height | 25 blocks |
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
| Dragon's Well | 1 bottle per player per in-game day |
| Kingstree sapling interval | 30 in-game days |
| Acolyte refuges per world | 17 (12 with Royal Annals copies, 1 Horn-keeper, 4 ordinary) |
| Abandoned kingdom threshold | 60 in-game days since the owner last logged in |
| King's Horn duration | 1 minute, once per in-game day |
| Sapper time on obsidian (diamond or netherite pickaxe) | 12 minutes per block |
| Swarms alive per player before no new one spawns | 2 |
| Bandit bands per world | 25 |
| Spectral king stats | 100 HP, 30% damage resistance, Legend-level melee damage, no knockback, plus his relic's power |
| Corrupted King stats | 300 HP, 40% damage resistance, Legend-level melee damage, double damage from behind |
| Hero legend chances (library shelves / village chest / bandit camp) | 30% per book / 3% / 5% |
