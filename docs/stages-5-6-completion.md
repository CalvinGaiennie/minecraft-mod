# Stages 5–6 completion (MVP)

## Stage 5, Threats (MVP)

- **Enlisted players:** placing a mess station enlists you; `/villagers optout` and `/villagers enlist`. Threats skip opted-out players.
- **New moon raids:** dark-moon window (3 nights per 28-day cycle), horn warning, pillager push toward nearby claimed village when enlisted player is present at night.
- **Swarms:** 5% nightly roll per enlisted player (max 2 active swarms nearby); zombie/skeleton groups march on stored target.
- **Bandits:** 45% of deserters become bandits (50% villager reset, 5% wandering necro, see `soldiers-and-villages.md`).

**Deferred to later polish:** raid dawn loot/leave, swarm merge caps, bandit camps/caves/worldgen, troubled youth, full vanilla mob rebalance.

## Stage 6, Extras (MVP)

- **Necromancy:** wand converts hostile mobs to owner minions; hood/robe (leather armor), phylactery shard, soul bottle, grave shroud, grave marker & cursed effigy blocks (placement markers for future AI).
- **Farmers / ranches:** `farmer_station` and `wild_ranch` job markers (logic hooks TBD; blocks registered for builds).
- **Mob spawner cap:** limited zombie spawns when player is near.
- **Training dummy:** right-click adds practice kills (cap 5) on nearby soldiers.
- **Chronicle & map:** village chronicle item; **village map** opens muster roll (soldier). **Kingdom map / kingdom table cut**: use Annals, relic compass, citadel burial maps (necro).
- **Bandit camp** / **necromancer crypt** markers.
- **Advancement:** `villagers:root` (mess station in inventory).

## Art

All mod blocks and items use **vanilla models/textures** via `scripts/generate_vanilla_assets.py` (regenerate after adding IDs). No purple-black missing models.

## Verification

- `./gradlew compileJava`, green
- `./gradlew runGameTestServer`, **48/48** GameTests (includes `Stage56GameTests`)

See also [stages-1-4-completion.md](stages-1-4-completion.md).
