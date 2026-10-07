# Armies mod — manual & integration test plan

**Scope:** Minecraft Kingdom: Armies (current monolith `villagers` JAR). Use this for release QA, playtest sessions, and bug reports.

**Related automated suites:** [stages-1-3-tests.md](stages-1-3-tests.md), [stage-1-tests.md](stage-1-tests.md), `ArmiesGameTests` (minimal quest/config smoke).

```bash
cd minecraft-mod
./gradlew runGameTestServer    # automated
./gradlew runClient            # manual
```

**Test world setup (recommended):**

| Setting | Why |
| --- | --- |
| Creative tab **Villagers** for blocks/items | Faster village build |
| `/villagers enlist` on first mess interaction (or command) | Threats + desertion grudges apply |
| Fresh overworld + note **spawn X/Z** | Camp distance rings are spawn-relative |
| Optional: lower `armiesQuestD1SoldierThreshold` to **5** in config for faster quest QA |
| Optional: lower `wanderingNecromancerGrudgeDelayDays` to **2** for necro QA |
| **F3** + `/data get` on bandit camp block entity | Site kind / UUID debugging |
| Second player or LAN for MP rows | Owner vs ally vs stranger |

Log each session: **seed**, **mod version/commit**, **config overrides**, **pass/fail** per section ID.

---

## 1. World generation & bandit structures

Generation runs **once per overworld** when the dimension first loads bandit logic (`BanditSiteGenerator.ensureGenerated`).

### 1.1 Generic sites

| ID | Steps | Expected |
| --- | --- | --- |
| S1 | New world, `/villagers locate list camp` or `/villagers locate camp` (or fly rings **800–4000** blocks from spawn) | **10** bandit **camps** + **3** **hideouts** (defaults `banditCampCount`, `banditHideoutCount`) |
| S2 | Inspect each camp | **Bandit camp** marker block, **2 chests** (N/E offsets), garrison spawns when chunk loads |
| S3 | Measure camp-to-camp distance | **≥700** blocks between site origins (`MIN_SITE_SEPARATION`) |
| S4 | Hideout sites | Caltrop patches + cage marker (MVP dressing); **6–10** bandits when bootstrapped |
| S5 | Clear a camp (kill leader + wipe or kill garrison) | Respawn after **`banditCampRespawnTicks`** (default **6000** ticks ≈ 5 min); chest loot **one-time** per clear rules |
| S6 | Reload world / restart server | Sites **do not** duplicate; SavedData stable |

### 1.2 Quest camps (Corvin / Garland)

| ID | Steps | Expected |
| --- | --- | --- |
| S7 | Find **CORVIN** site | **800–2200** blocks from **overworld spawn** |
| S8 | Find **GARLAND** site | **2200–4000** from spawn |
| S9 | Measure Corvin ↔ Garland | **≥1200** blocks apart |
| S10 | Garland terrain (visual) | Prefer **rougher** height variance vs Corvin placement (generator bias) |
| S11 | Corvin / Garland chests | Loot tier matches kind; quest bosses spawn with camp bootstrap |

### 1.3 Citadel shadow camps (integration)

| ID | Steps | Expected |
| --- | --- | --- |
| S12 | **Armies only**, no citadel anchor | **No** shadow ring camps |
| S13 | Call `CitadelAnchorSavedData.setCitadelCenter` (Citadel mod or debug) | **3** extra camps (`citadelShadowExtraCamps`) on ring |
| S14 | Ring radius | **~480 ± 24** blocks from citadel center (`citadelApproachDistance`, `citadelShadowRingJitter`) — citadel **barely visible** at typical 32-chunk view |
| S15 | Shadow camps not at spawn | Positions relative to **citadel**, not world spawn |

### 1.4 Prefab / structure debt (document only)

| ID | Steps | Expected |
| --- | --- | --- |
| S16 | Compare to `bandit-camp-prefabs.md` | MVP = marker + chests + hideout caltrops; **no** jigsaw NBT prefabs yet — note gaps, don’t fail on art |

---

## 2. Village claim, economy, enlistment

| ID | Steps | Expected |
| --- | --- | --- |
| V1 | Place **mess** + recruit first **soldier** in natural/player village | Village **claims**; owner UUID set |
| V2 | **Surveyor markers**, radius by population | Hamlet **32** → city **128** (see `soldiers-and-villages.md`) |
| V3 | Overlap second owner’s mess | Blocked or border rule per design |
| V4 | **Tax box** + mess stocked, skip days, open box | Back-tax collection (up to 30 days) |
| V5 | Soldier count vs tax coverage | **1 soldier covers 5** villagers (automated B3) |
| V6 | **Supply depot** weekly tick | Gear appears in depot or nearest **recruiter** within 48 blocks |
| V7 | **Recruiter box** | 27 slots; capacity **10** (B4) |
| V8 | `/villagers enlist` / first mess | Enlisted for nightly swarms |
| V9 | `/villagers optout` | No player-targeted swarm rolls; **structures still hostile** |
| V10 | Destroy all owner mess stations; rival places mess | Ownership transfer; home soldiers convert on return |

---

## 3. Soldier conversion, kit, ranks

| ID | Steps | Expected |
| --- | --- | --- |
| K1 | Unemployed villager + sword + chest + helmet + post bed + stocked mess | Converts to **soldier** |
| K2 | Trader with past soldier service | **Veteran** path if applicable |
| K3 | Trader never soldier | **Militia** only with weapon |
| K4 | Open soldier gear UI / kit slots | Helmet, chest, legs, boots, melee, ranged, arrows (max **16**) |
| K5 | Kill mobs as soldier | Ranks at **5 / 20 / 50** kills (Seasoned / Hero / Legend) — B2 |
| K6 | **Discharge** soldier | Gear drop rules; bed released |
| K7 | Pick up ground gear | Soldiers fill **empty allowed slots** from ground only (not chests) |

---

## 4. Combat — wake, targeting, hold ground

Constants from **`SoldierCombatConstants`** (verify in combat, not older design prose).

| ID | Steps | Expected |
| --- | --- | --- |
| C1 | Zombie **within 64 blocks**, **line of sight** | Soldier **wakes**, targets, fights (B7) |
| C2 | Zombie behind wall or **>64** | No wake until visible in range |
| C3 | **Hold ground** (awaken / rampart anchor active) | Engages hostiles within **48** of anchor; abandons chase beyond **64** from anchor |
| C4 | **Home bed** within **128** | Home bonus behavior; **no retreat** to fallback for retreat rule (M12/M13) |
| C5 | Low HP (**≤30%**), **outside 128** of post bed, **fallback** placed | Paths to **fallback** block |
| C6 | Enemies within **16** blocks | Post-bed healing / meal heal suppressed (out of combat rule) |
| C7 | **Bell** / **Awaken horn** | Soldiers alert; path toward **rampart** |
| C8 | **Mutiny** (high grumble, out of combat) | Message + desert (see desertion) |

---

## 5. Ranged combat — when they shoot, how often, ammo

**Implementation reference:** `SoldierCombatArms`, `SoldierRangedAttackGoal`, `SoldierRampartShooting`.

| Parameter | Code value | How to observe |
| --- | --- | --- |
| Bow cooldown | **22 ticks** (~1.1 s) between shots | Count shots/sec on stationary target at 12 blocks |
| Crossbow cooldown | **28 ticks** | Same with crossbow kit |
| Ranged band (open ground) | **8–32** blocks horizontal | Inside 8: prefers melee if sword present; beyond 32: move closer |
| Melee prefer | **≤6** blocks | Switches to sword/shield |
| Rampart min range | **2** blocks horizontal | Archers on wall shoot closer targets |
| Max range | **32** blocks | No shots beyond; pathing adjusts |
| Line of sight | Required | No shot through solid blocks |
| Arrows | From kit; **−1** per shot; bow **durability** −1 | Watch offhand count |
| Max arrows | **16** | Resupply when below max |

| ID | Steps | Expected |
| --- | --- | --- |
| R1 | Bow soldier vs zombie at **12** blocks, flat ground | Stays in band, **~1 shot per 22 ticks**, looks at target |
| R2 | Same at **4** blocks with sword + bow | **Melee** goal wins inside **6** blocks |
| R3 | **Bow-only** (no melee) at **4** blocks | Stays ranged; may stop navigation inside min distance |
| R4 | Block LOS with wall | Stops shooting; paths for LOS or rampart slot |
| R5 | **Rampart** + awaken | Prefers wall; **stops moving** when clear shot; shoots at **2–32** horizontal |
| R6 | Elevated archer (**Y > target + 2**) on rampart | Does not chase down; continues ranged |
| R7 | Empty arrows mid-fight | Stops ranged; **GuardDutyPathing** → arrow bin / recruiter |
| R8 | **Arrow bin** within resupply search | Paths to bin; refills to **16** |
| R9 | Missed arrows on ground within **6** blocks | **ArrowRecovery** picks up (when not at cap) |
| R10 | Crossbow kit | **28-tick** cadence; projectile speed **3.15** vs bow **1.6** (feel test) |

---

## 6. Pathfinding & post return

| ID | Steps | Expected |
| --- | --- | --- |
| P1 | Peaceful day, soldier with post bed + rampart | **GuardDutyPathing** → return to **rampart** |
| P2 | Needs arrows | Path to nearest **arrow bin** or recruiter with arrows |
| P3 | After combat ends | **PostReturnPathing** → rampart / mess / bed per behavior |
| P4 | **Combat doors** | Soldier opens doors on path (stress with enclosed post) |
| P5 | **Campaign** away from village | Campaign pathing; return home converts ownership if village flipped |
| P6 | Chunk unload mid-path | No duplicate soldiers; resume when loaded |
| P7 | Ladder / 1-block step village wall | Rampart archer path: `pathToShootPosition` finds shoot slot |
| P8 | **Caltrops** (hideout) | Slow/damage; soldiers path through without infinite stall |

---

## 7. Militia (contrast soldiers)

| ID | Steps | Expected |
| --- | --- | --- |
| M1 | Armed trader | Militia; **flees** when outnumbered / low HP (except within **128** of home bed) |
| M2 | Bow militia | No melee; backs away |
| M3 | 2 days no mess food | Drops weapon, returns to trade |
| M4 | 10 kills | Seasoned militia (+HP, no flee from outnumber) |
| M5 | Unemployed militia + full soldier reqs | Auto-upgrades to **soldier** |

---

## 8. Desertion, bandits, wandering necromancers

| ID | Steps | Expected |
| --- | --- | --- |
| D1 | **3 nights** no owned post bed | Desert roll (game test A desertion) |
| D2 | **7 days** no mess food (unstocked) | Desert roll |
| D3 | Outcome distribution (large sample) | ~**50%** villager reset, **45%** bandit, **5%** necro (config) |
| D4 | **Rookbreaker** title | **−10%** desert probability (reprieve rolls) |
| D5 | Bandit deserter | Keeps gear; **BanditCombat**; name tag **Bandit** unless named |
| D6 | Necro deserter | Keeps **personal name + kills** on tag; hood/robe |
| D7 | Necro grudge delay | **No strike** for **`wanderingNecromancerGrudgeDelayDays`** (default **30** days) |
| D8 | Ex-owner logs in after desert | After delay, strikes every **`wanderingNecromancerStrikeIntervalDays`** (default **2**) |
| D9 | Strike ambush | Necro **teleports** near owner; **dialog line** + evoker sound; undead spawn |
| D10 | Strike village | Swarm marches to **weakest** village (lowest soldiers/villager); dialog names village |
| D11 | `/villagers optout` | No necro grudge strikes |
| D12 | Kill wandering necro | **Phylactery shard** drop; removed from SavedData |

---

## 9. Bandit camps & combat (non-quest)

| ID | Steps | Expected |
| --- | --- | --- |
| B1 | Camp garrison | Bandits target soldiers/militia/players |
| B2 | Camp leader death | Leader drops gear; camp clear timer starts |
| B3 | Loot | Stays in **camp chests** (no auto-teleport to village) |
| B4 | **Halvek** quest assault | Separate from static camp kings until D1 fires |

---

## 10. Armies quest arc (full matrix)

Use **`armies-questline.md`**. Boss kills: **`armiesQuestBossPlayerKillOnly`** — non-player damage cannot finish bosses.

### D1

| ID | Steps | Expected |
| --- | --- | --- |
| Q1 | Reach **30** soldiers (**soldiers only**, not militia) in claim | Stage waits for **enter claim** |
| Q2 | Enter claim at ≥30 | **Halvek** assault (~18–22 bandits); any moon if `armiesQuestD1IgnoreMoon` |
| Q3 | Halvek to 1 HP via golem / fall damage | **Cannot** die |
| Q4 | Player kills Halvek | **Blood-stained Ledger**; O1 on read |
| Q5 | Second player owns different mess | **Per-owner** arc on owner UUID |

### O1

| ID | Steps | Expected |
| --- | --- | --- |
| Q6 | Read ledger | Chat **bearing** toward Corvin camp |
| Q7 | Clear Corvin (**player kill**) | Dialog line; **`armiesQuestD2Armed`** |
| Q8 | Corvin camp distance | Within **800–2200** spawn ring |

### D2

| ID | Steps | Expected |
| --- | --- | --- |
| Q9 | Enter **largest** village by soldier count with D2 armed | Garland assault (**bandits only**, no undead) |
| Q10 | Garland **≤30% HP** | Flees; **torn_map_half** |
| Q11 | Kill **≥65%** assault bandits (not Garland) | Same flee |
| Q12 | Break mess / all post beds | **Fail** state; retry after rebuild + re-enter |
| Q13 | Player “kills” Garland in D2 | **Cannot** full kill (flee immunity) |

### O2

| ID | Steps | Expected |
| --- | --- | --- |
| Q14 | Craft **ledger + torn map + compass** | **Rook's Trail Compass** (lodestone to Garland camp) |
| Q15 | Kill Garland (**player**) | **Rookbreaker** title; signet trophy; **−10%** desertion |
| Q16 | Revisit camps | Halvek/Corvin/Garland **do not** respawn |

---

## 11. Threats (parallel to quest)

| ID | Steps | Expected |
| --- | --- | --- |
| T1 | Enlisted player, nightly | **5%** swarm chance; max **2** swarms (see `SwarmService`) |
| T2 | New moon | **NewMoonRaidService** pressure (enlisted) |
| T3 | Swarm unload | March stored in SavedData when chunks load |

---

## 12. Multiplayer & permissions

| ID | Steps | Expected |
| --- | --- | --- |
| MP1 | Ally vs owner quest items | **Owner** advances stage; ally can DPS |
| MP2 | Stranger in village | Protection slowdown tiers by soldier count present |
| MP3 | Co-owner / ally book | **TBD** — document if unimplemented |

---

## 13. Regression smoke (every build)

1. `./gradlew runGameTestServer` — all green  
2. **C1** + **R1** + **V1** + **S1** (one camp found)  
3. **Q1–Q4** with lowered D1 threshold on test config  

---

## 14. Known gaps / do not file as bugs yet

From design vs code (see `stages-1-3-tests.md`, `soldiers-and-villages.md`):

- Recruiter villager POI AI, full trader tax loot tables, night stumble on rampart  
- 25-band roaming simulation, warlord probes, bandit alliance  
- Bandit camp **NBT prefabs** (jigsaw structures)  
- Design doc “switch melee at **4** blocks” vs code **6** (`MELEE_PREFER_MAX_DISTANCE`) — file only if gameplay feels wrong  
- Citadel shadow requires **external** citadel anchor API  

---

## Bug report template

```
Section ID:
Seed / commit:
Config changes:
Steps:
Expected:
Actual:
Screenshots / replay:
Logs (server tick if pathing):
```
