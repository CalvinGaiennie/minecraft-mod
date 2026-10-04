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
