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
- **Source of truth:** the repo's `docs/` folder is the source of truth. Change the docs file in the same commit as the code that changes the rule. **Mod split:** `docs/mod-split.md`; **citadel ownership:** `docs/citadel-claim.md`; **cross-mod hooks:** `docs/integration.md`.

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

**Done when (GameTests):** a villager becomes a soldier only when every requirement is met; a hungry soldier deserts on day 7; a soldier with no home bed for 3 nights deserts; taking a soldier's weapon discharges him and he keeps his kills; two players can't claim overlapping land; the muster roll counts match. Full checklist: `docs/stage-1-tests.md`.

### Stage 2: Economy

**Scope:** tax box and its tiers, tax coverage, recruiters, blacksmith, fletcher, and armorer supply, gear wear and death drops, ranks, veterans and the veteran's sword, and zombie soldiers. **Read:** Soldiers and villages.

**Done when:** a village collects taxes only for villagers a soldier covers (5 each) who can reach the box; trader tax rolls match the table (use a seeded random in tests); collection intervals follow village size; a recruiter turns unemployed villagers into soldiers up to his capacity and buys post beds for 5 emeralds; supply adds 1 item a week; broken gear is replaced from issued gear; ranks, health, and damage match the tables; a veteran defends within 32 blocks; cured zombie soldiers follow the 60/25/15 odds.

### Stage 3: Defense

**Scope:** ramparts, arrow bins, fallback and haven blocks, the remaining horns, levers and buttons, flares, the village bell, nightwatch and royal guard armor, royal guard posts, night fighting and carried lights, caltrops, castle gates, village protection, arrow recovery, and beacon effects on soldiers. **Read:** War and defense, Soldiers and villages (night fighting).

### Stage 4: War

**Scope:** allies, campaigns, camp blocks, blockades, assaults, sappers and axemen, siege ladders, boat plans, plank bridges and bridge plans, laced rations, mutiny, no building in enemy land, wanted posters, the reassignment writ, and village banners. **Read:** War and defense, Soldiers and villages (mutiny, ownership).

**Stages 1–4 shipped (MVP):** see `docs/stages-1-4-completion.md`. Notable plan changes from the original design text:

- **Tax:** collected on **tax box open**, not on a world scan timer; **no pathfind** requirement; **128+** village zone with surveyor markers and population-based claim growth.
- **Combat / AI:** militia, rampart claims, crossbow, doors, hold ground, performance stagger (not full squad pathfinding spec).
- **Ranks:** **Sergeant** tier at 12 kills; veteran defend **128** blocks.
- **Stage 3–4 gaps closed in code:** caltrops (block), village protection + explosion cancel, arrow pickup, beacon buffs, banner on mess, enemy build block, **visible soldier armor** (kit attachment sync + villager armor render layer).
- **Still deferred past Stage 4 (soldier mod):** **wanted posters**, allies co-owner list, full recruiter POI, client attachment sync.
- **Cut from scope (not planned MVP):** siege assault kit (sappers, ladders, boat/bridge plans, laced rations).
- **Mod split:** see `docs/mod-split.md`, `docs/citadel-claim.md`, `docs/integration.md`.

### Stage 5: Threats

**Scope:** new moon raids, swarms, bandits, bandit camps and cave hideouts, and vanilla mob changes. **Read:** Threats and mobs. Build the enlisted-player rule here, so a player who ignores the mod isn't affected.

### Stage 6: Extras

**Scope:** necromancy with necromancer crypts and phylactery shards, animal farmers and wild ranches, mob spawners, Village Chronicles, the training dummy, advancements, the **village map** (soldier). **Read:** Necromancy, Threats and mobs, Old kingdom lore (chronicles), War and defense (items). Necro/endgame split: `docs/mod-split.md`.

**Stages 5–6 shipped (MVP):** see `docs/stages-5-6-completion.md`. Highlights:

- **Enlistment:** mess-station claim + `/villagers enlist|optout`; threat ticks respect enlisted/opt-out state where wired.
- **Threats:** new-moon raid window, nightly swarm spawn hooks, bandit data + deserter conversion stubs, bandit camp marker block.
- **Stage 6 blocks/items:** crypt, ranch, farmer station, spawner cap (BE tick), training dummy (practice kills), grave/effigy markers, necromancy gear + village map/chronicle, root advancement.
- **Art:** run `python3 scripts/generate_vanilla_assets.py` after adding IDs — all registered blocks/items use vanilla-parent models/textures (no missing purple-black cubes).
- **Still deferred past Stage 6:** full raid waves, swarm merge AI, farmer job AI, grave/effigy behavior, bandit camps as structures, citadel/endgame structures (necro mod).

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
- **2026-10-04:** Stage 1 GameTests green (8/8). GameTest templates live at `data/<modid>/structure/<name>.nbt` (e.g. `villagers:empty`).
