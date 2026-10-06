# Stage 1 test plan

Use this list before moving to Stage 2. **Automated** tests run in CI/dev via GameTests; **manual** tests need `runClient`.

## Automated (GameTests)

Run:

```bash
cd minecraft-mod
./gradlew runGameTestServer
```

Pass criteria: `All N required tests passed :)`. Stages 2–3 add 10 more tests (21 total); see [stages-1-3-tests.md](stages-1-3-tests.md).

| ID | Test method | Rule |
|----|-------------|------|
| A1 | `soldierConversion_requiresAllRequirements` | Unemployed villager with helmet, chestplate, sword, free post bed, and stocked mess becomes a soldier with label data. |
| A2 | `soldierConversion_missingGearFails` | Missing weapon blocks conversion. |
| A3 | `soldierConversion_emptyMessFails` | Mess station with no soldier food blocks conversion. |
| A4 | `messStation_acceptsSoldierFood` | Mess accepts bread (and `hasSoldierFood()` is true). |
| A5 | `messStation_rejectsNonFood` | Non-meal items are not accepted via insert API. |
| A6 | `desertion_homelessThreeNights` | Homelessness deserts on night 3, not night 2. |
| A7 | `desertion_hungryDaySeven` | Hunger deserts on day 7 without food, not day 6. |
| A8 | `discharge_keepsKills` | Honorable discharge removes soldier data, keeps veteran kills/rank. |
| A9 | `village_noOverlappingClaims` | Second owner cannot claim overlapping hamlet radius. |
| A10 | `musterRoll_countsMatch` | Muster summary counts soldiers and kills near test origin. |
| A11 | `awakenHorn_setsAwakenFlag` | Awaken signal sets future `awakenUntilGameTime`. |

## Manual (in-game)

Prerequisites: `./gradlew runClient`, Creative, **Villagers** tab.

| ID | Steps | Expected |
|----|--------|----------|
| M1 | Place **Mess Station**. Right-click with **bread** (or other cooked meal). | One item added per click; station holds food. |
| M2 | Right-click mess with **empty hand**. | 9-slot **Mess Station** screen opens; only soldier meals can be placed in slots. |
| M3 | Place **Post Bed** within ~16 blocks of mess. Spawn unemployed villager. **Right-click with empty hand** → **Recruit gear** (labeled armor + melee slots; ranged/arrows optional). Minimum for conversion: helmet, chestplate, sword/axe in melee slot. Stock mess (M1). Wait ~30s. | Villager converts; name label shows soldier styling. |
| M4 | Repeat M3 but **do not** stock mess. | Villager stays a normal equipped villager (no conversion). |
| M5 | Use **Awaken Horn** near converted soldiers. | Action bar: count of soldiers alerted. |
| M6 | Use **Muster Roll** near soldiers. | Action bar: soldier count and total kills. |
| M7 | Place **Surveyor's Marker** and second player's mess in overlapping claim (second player or test with friend). | Overlapping mess/marker placement blocked when rules apply. |
| M8 | Place **Rampart** blocks; observe soldier pathing near walls (informal). | No crash; soldiers path normally. |
| M9 | Remove soldier's **sword** (discharge trigger). | Soldier data cleared; veteran record kept (no easy UI—check muster roll / behavior). |

## Not covered in Stage 1 (defer)

- Surveyor's knife, owner-only mess access, village persistence (`SavedData`), full daily meal AI loop, rampart pathing GameTests.

## After you test

Note any failing **ID** (A* or M*) in an issue or chat so we can add a GameTest or fix behavior.
