# Master Build Note - Villagers Mod

## Overview
A NeoForge mod for Minecraft 1.21.1 that transforms villagers into soldiers and militia, with village economies, strategic mechanics, and an escalating endgame. Built on NeoForge 21.1, Java 21, ModDevGradle.

## Architecture Rules
- Every configurable number goes in config
- Soldiers and militia are vanilla villagers with data attachments (no new entity types without approval)
- NeoForge 1.21.1 attachments don't sync to clients; use custom payloads
- Respect mobGriefing gamerule
- Store player UUIDs, not names
- Swarms, bands, and orders persist while unloaded
- Write GameTest for each rule; commit when it passes
- Custom art allowed for blocks/items; UI/HUD minimal and clean

## Build Stages

### Stage 1: Village Infrastructure
**Slice 1: Basic Blocks** ✅ COMPLETE
- [x] Mess Station block (7 barrels in T-shape) - gathering point
- [x] Post Bed block (5 white beds) - sleeping point
- [x] Post Block (cobblestone slab + iron ingot) - structural element
- [x] Crafting recipes for all three blocks
- [x] Custom textures and Villagers creative tab
- [x] English translations

**Slice 2: Village Structures** ✅ COMPLETE
- [x] Barracks block (training area, oak planks + crafting table)
- [x] Watchtower block (command center, bricks)
- [x] Armory block (equipment storage, iron blocks + chest)
- [x] Village gate block (defensive structure, oak doors + iron bars)

**Slice 3: Village Detection**
- [ ] Village bounds detection (players place blocks to define territory)
- [ ] Village state tracking (name, owner, allegiance)
- [ ] Village persistence (NBT storage)

### Stage 2: Soldier System
**Slice 1: Soldier Conversion**
- [ ] Data attachment for soldiers (ranks, experience, loadout)
- [ ] Conversion mechanic (right-click villager with sword)
- [ ] Custom appearance/nametag for soldiers
- [ ] Soldier persistence through save/load

**Slice 2: Soldier AI**
- [ ] Combat behavior (target hostile mobs)
- [ ] Patrol mechanics (patrol nearby village)
- [ ] Guard duty (defend village structures)
- [ ] Unit cohesion (grouping behavior)

**Slice 3: Militia**
- [ ] Drafted villagers (weaker soldiers from regular villagers)
- [ ] Militia rank system (recruit → soldier → veteran)
- [ ] Militia morale system

### Stage 3: Village Economy
**Slice 1: Resource Management**
- [ ] Emerald currency system
- [ ] Cargo system (storage containers)
- [ ] Resource generation (gathering, farming)

**Slice 2: Trade System**
- [ ] Player-to-village trading (buy/sell)
- [ ] Village-to-village trading
- [ ] Market mechanics

**Slice 3: Upkeep**
- [ ] Soldier wages (emerald cost to maintain)
- [ ] Building maintenance costs
- [ ] Economic collapse risk

### Stage 4: Horns and Commands
**Slice 1: Horn Mechanics**
- [ ] Horn item crafting (quartz block + dark oak wood)
- [ ] Horn sound effects
- [ ] Horn range and listener detection

**Slice 2: Commands**
- [ ] Attack command (gather nearby soldiers)
- [ ] Retreat command
- [ ] Patrol command
- [ ] Rally point mechanic

**Slice 3: Horn Orders**
- [ ] Orders persist when soldiers unload
- [ ] Order chains (sequential commands)
- [ ] Group management

### Stage 5: Campaigns and Raids
**Slice 1: Raid Mechanics**
- [ ] Raid initiator (raid horn, bad omen, hostile structures)
- [ ] Raid waves (escalating difficulty)
- [ ] Village defense mechanics
- [ ] Raid end conditions (victory/defeat)

**Slice 2: Campaigns**
- [ ] Multi-part raid sequences
- [ ] Campaign objectives
- [ ] Reputation system (players gain/lose standing with villages)

**Slice 3: Advanced Raids**
- [ ] Siege mechanics (break defenses)
- [ ] Different raid types (pillagers, bandits, undead)

### Stage 6: Swarms and Bands
**Slice 1: Swarms**
- [ ] Coordinated movement of groups
- [ ] Formation tactics
- [ ] Swarm persistence data

**Slice 2: Bands**
- [ ] Mercenary bands (roving soldiers)
- [ ] Band allegiances and conflicts
- [ ] Hiring mechanics

**Slice 3: Territory Wars**
- [ ] Village-to-village conflicts
- [ ] Territory control
- [ ] Alliances and enemies

### Stage 7: Bandits and Hostiles
**Slice 1: Bandit Faction**
- [ ] Corrupted villagers (bandits)
- [ ] Bandit encampments
- [ ] Raiding mechanics (bandits attack villages)

**Slice 2: Bandit AI**
- [ ] Intelligent bandit strategies
- [ ] Bandit camps (generation and control)
- [ ] Bounty system

**Slice 3: Faction Wars**
- [ ] Player-aligned soldiers vs bandits
- [ ] Dynamic conflicts
- [ ] Casualties and village damage

### Stage 8: Necromancy System
**Slice 1: Undead Conversion**
- [ ] Dead soldier resurrection mechanics
- [ ] Skeleton warrior generation
- [ ] Necromancer entity (summoner)

**Slice 2: Undead Swarms**
- [ ] Coordinated undead groups
- [ ] Undead persistence
- [ ] Undead vs living conflicts

**Slice 3: Dark Magic**
- [ ] Necromancy crafting (grimoire, components)
- [ ] Corruption mechanics
- [ ] Curse system

### Stage 9: Citadel Endgame
**Slice 1: Citadel Generation**
- [ ] Citadel structure (massive defensive fortress)
- [ ] Generation mechanics (triggered by reaching critical mass)
- [ ] Citadel loyalty system

**Slice 2: Citadel Conflict**
- [ ] Siege on citadel (raid mechanics adapted)
- [ ] Citadel defense (automated turrets, powerful defenders)
- [ ] Endgame combat

**Slice 3: Citadel Conquest**
- [ ] Victory conditions (capture citadel)
- [ ] Post-game state (player governs territory)
- [ ] Scaling and balance at high player investment

## Current Status
- Stage 1, Slice 1: ✅ Complete (3 basic blocks with recipes)
- Stage 1, Slice 2: ✅ Complete (4 village structure blocks with recipes)
- Next: Stage 1, Slice 3 (Village Detection)

## Notes
- All (proposed) numbers/values are tunable via config
- Each slice should have at least one GameTest before commit
- Balance feedback from playtesting incorporated each stage
- Git tags mark each slice completion (v1.0.0-stage#-slice#)
