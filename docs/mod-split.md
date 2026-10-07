# Mod split (soldier vs necromancer/endgame)

Two optional NeoForge mods from this repo long-term. **Soldier** = villages, economy, war, threats, farmers. **Necromancer** = wand, citadel, relics, acolytes, fortress endgame. Neither hard-depends on the other; **integration** enhances both.

## Mod packaging (plain language)

**Brand:** **Minecraft Kingdom** (two optional mods in one series).

| Jar | Mod ID | Display name |
|-----|----------|----------------|
| Armies | `minecraft_kingdom_armies` | **Minecraft Kingdom: Armies** |
| Citadel | `minecraft_kingdom_citadel` | **Minecraft Kingdom: Citadel** |

Short IDs (`mk_armies` / `mk_citadel`) are fine for code if the long IDs feel heavy.

**Current monolith** (until Gradle split): `villagers` / **Villagers Mod**: `gradle.properties` unchanged.

- **One repo, two Gradle subprojects** (future): two JARs, shared docs, e.g. `minecraft_kingdom_armies-1.0.0` / `minecraft_kingdom_citadel-1.0.0`.
- **Naming note:** “Minecraft Kingdoms” (plural) is an existing **modpack** on CurseForge/Modrinth; “Minecraft: Kingdom” is a **resource pack**. This series is **Minecraft Kingdom** (singular) + subtitle, distinct enough for listings, but use consistent spelling in descriptions to reduce search confusion.
- **Save data** splits by domain: village claims / enlistment → soldier; citadel owner, credits, ban list → necromancer.
- **Players** install one or both; modpacks list soldier alone, or both with optional dependency metadata.

See `integration.md` for cross-mod hooks (no third API jar required at first).

## Soldier mod (default home for…)

Village military loop, mess/tax/recruiters/supply, defense blocks, horns/camps/campaigns/blockades, **wanted posters** (planned, see `war-and-defense.md`), training dummy, animal farmers, village **map** (muster roll), enlisted threats (raids, swarms, bandits), **authored bandit kings** (Armies-only story sites, `threats-and-mobs.md`). **No** ten relic fortresses (Citadel mod). **Warlords:** TBD (`endgame.md`).

**Cut from soldier plan (not deferred):** full siege assault kit (sappers, ladders, boat/bridge plans, laced rations as MVP scope).

**Still deferred on soldier side:** allies co-owner list, full recruiter POI, client kit sync polish.

## Necromancer / endgame mod

Wand, hood/robe, minions, crypts, phylactery, bone whistle, mob spawner crafting, citadel + throne rules (`citadel-claim.md`), 17 refuges, player acolyte questline (`acolyte-path.md`), necromancer questline TBD (`necromancer-path.md`), **10 bandit fortresses** (Citadel-only structures, named kings, six with relics), relics/Annals/compass, acolytes, Well, Roost, King’s Horn, golden age **owner** side.

**Bandits:** roaming deserter bands = **Armies**. **Fortress** sites and relic rooms = **Citadel**. With **both** mods, fortress defenders use soldier bandit entities when possible; Citadel-only fallback mobs **TBD**.

**Cut:** kingdom table block, **blessed incense**, separate **kingdom map** item (use Annals, relic compass, citadel war-room burial maps instead).

**Maps:** **Village map** (soldier) opens muster roll / owned villages. No second map item in necro until a clear purpose exists.

## Both mods (integration)

| Feature | Soldier | Necromancer |
|---------|---------|-------------|
| Kingdom titles (Lord→King) | Village + living soldier counts | - |
| High King / golden age | Villages in kingdom | Citadel owner |
| Ironroot / Oathkeeper / Crown (full) | Recruits, tax, soldiers, raids | Relic items + wear rules |
| Citadel mess placement | Block | Owner-only rule in citadel |
| Royal training grounds | Royal soldier tag | Citadel blocks |
| `citadelFallen` flag | Raid/militia near spawn | Set on Corrupted King death |
| Phantom Roost access | Ally UUIDs if loaded | Owner + cage spawner |
| Enlistment | Mess + `/enlist` |, (wand does not enlist) |

## Relics (wear + solo fallbacks)

- **Active necromancers cannot wear any of the 12 relics.**
- With **both** mods: use full design in `endgame.md` where it applies to soldiers/villages.
- **Necro-only** (no soldier mod) simplified effects:
  - **Ironroot Helm:** −25% damage from undead while worn.
  - **Oathkeeper:** −20% damage taken ( wearer only ).
  - **Corrupted Crown:** once per day, fatal hit while worn → survive at 1 HP + short knockback pulse (no raised soldiers).
  - **Greaves of the Long March:** wearer never loses hunger.
  - Other combat relics: player gear as documented.

## Config (proposed defaults from design chat)

| Setting | Default |
|---------|---------|
| Necromancer recruit interval multiplier | **3×** (vs normal) |
| Robe stigma fraction (non-necromancer in hood/robe) | **0.5** of necromancer penalties |
| Citadel ban: sit damage on blighted tree | **Per tick** |
| Citadel ban: sit on healthy tree | **Instant death** (no totem) |
| Spectral Thornwald | Despawn when wearer **above 3 hearts** OR Thornwald **dies**; no trigger if max HP **&lt; 3** from wand heart loss |

## Doc index

- `citadel-claim.md`, throne, credits, barrier, usurp, wand forfeit
- `integration.md`, events and optional hooks
- `necromancy.md`, wand, robes, absolution
- `endgame.md`, citadel content, relics, acolytes, Roost
