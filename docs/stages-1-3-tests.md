# Stages 1–3 test plan

Run automated tests:

```bash
cd minecraft-mod
./gradlew runGameTestServer
```

Pass criteria: all registered GameTests pass (see [stages-1-4-completion.md](stages-1-4-completion.md)).

Manual tests use `./gradlew runClient`, Creative tab **Villagers**.

---

## Stage 1 (Core)

### Automated (A1–A11)

See [stage-1-tests.md](stage-1-tests.md) for the full A1–A11 table (conversion, mess, desertion, discharge, village overlap, muster roll, awaken horn).

### Manual (M1–M9)

See [stage-1-tests.md](stage-1-tests.md) for M1–M9 (mess stock/GUI, recruit gear, conversion, horn, muster roll, markers, rampart, discharge).

---

## Stage 2 (Economy + combat basics)

### Automated (B1–B10)

| ID | Test | Rule |
|----|------|------|
| B1 | `combat_homeBedRadius128` | Home post bed bonus radius is **128** blocks (not distance-from-soldier scan). |
| B2 | `ranks_seasonedAtFiveKills` | Rank thresholds: 5 seasoned, 20 hero, 50 legend. |
| B3 | `taxCoverage_fivePerSoldier` | Each soldier covers up to **5** taxable villagers. |
| B4 | `recruiter_capacityTen` | Recruiter box capacity constant is **10**. |
| B5 | `arrowBin_acceptsArrowsOnly` | Arrow bin accepts arrows only. |
| B6 | `veteran_defendUses128RadiusConstant` | Veteran defend radius is **128**. |
| B7 | `combat_wakeUses64RadiusConstant` | Hostile wake range is **64** with line of sight. |
| B8 | `zombieCure_oddsRoll` | Cured zombie soldier roll ~60% return to service (seeded bulk roll). |
| B9 | `fallback_blockRegistered` | Fallback block registers and places. |
| B10 | `veteran_engagesInsideHomeRadius` | Veteran with **veteran’s sword** at home bed engages; far from home does not. |

### Manual (M10–M18)

| ID | Steps | Expected |
|----|--------|----------|
| M10 | Recruit a soldier. Spawn zombie **within 64 blocks** in **open line of sight**. | Soldier **stops fleeing**, targets zombie, melee attacks (may take a few ticks). |
| M11 | Same soldier, zombie **behind wall** (>64 or no LOS). | No forced target until visible within 64. |
| M12 | Soldier **>128 blocks** from post bed, hurt below ~30% HP, place **Fallback block** nearby. | Soldier paths toward fallback (retreat). |
| M13 | Soldier **within 128** of post bed, low HP. | Does **not** retreat to fallback for that rule. |
| M14 | Place **Tax box**, mess + soldiers **in tax zone**, civilians in zone. Skip days without opening, then **open** the box. | Action bar shows days due + collection; bread/items appear (up to 30 days back-tax). |
| M15 | Open **Recruiter box** / **Supply depot** GUIs. | 27-slot recruiter / 9-slot depot GUIs open; weekly supply (near player) adds sword/bow/chestplate to the **nearest recruiter box within 48 blocks**, or the depot if no recruiter exists. |
| M16 | **Discharge** soldier. Place a **vanilla bed**; let veteran claim/sleep if needed. Give **Veteran’s sword**. | Fights hostiles within **128** of **that villager bed** only, not the post bed. |
| M17 | Kill monsters as soldier; watch name/rank after kills. | At 5+ kills, rank/health bump (Seasoned). |
| M18 | Soldier kills mobs repeatedly. | Melee weapon **loses durability** over time. |

**Not fully implemented yet (design doc):** recruiter villager job AI, emerald post-bed purchase, trader tax loot table rolls, tier-2/3 tax box upgrades, full gear replacement from recruiter, zombie death → zombie soldier entity, full 60/25/15 cure on actual zombie villager.

---

## Stage 3 (Defense)

### Automated

Covered partly by B5, B7, B9; bell/retreat/ranks exercised in code paths above.

### Manual (M19–M26)

| ID | Steps | Expected |
|----|--------|----------|
| M19 | Place **Arrow bin**, put arrows in GUI. | Only arrows allowed in slots. |
| M20 | Place **Fallback block** near village wall. | Low-HP soldier outside home radius paths here (M12). |
| M21 | Ring **vanilla bell** near soldiers (right-click bell). | Action bar: awaken count; soldiers get awaken flag (path to rampart). |
| M22 | Use **Awaken horn** vs bell. | Both alert soldiers within 64 of you / bell. |
| M23 | Soldier on **rampart** at night (no torch). | May still path normally; full **night stumble** not implemented. |
| M24 | Stock **mess**, soldier sleeps at post bed with **no enemies within 16 blocks**. | Heal over time / meal heal still apply. |
| M25 | Multiple soldiers, one visible zombie. | Each awakened soldier can acquire target independently. |
| M26 | **Veteran** >128 blocks from home with sword. | Acts as civilian (no attack goals). |

**Stage 3–4 MVP:** haven, royal guard post/armor, nightwatch helmet, village gate, camp block, campaign boots, blockade stub, mutiny stub, reassignment writ, caltrops/banner items. See [stages-1-4-completion.md](stages-1-4-completion.md).

**Stage 3–4 extras (see completion doc):** village protection, arrow pickup, beacon buffs, caltrops, enemy build block, banner on mess, visible armor. **Still deferred:** firework/crossbow flares, lever horns, full bow/melee switch, 8-wide castle gates, caltrop path avoidance.

---

## Quick smoke (all stages)

1. Mess + post bed + rampart + fallback + tax box + arrow bin within ~32 blocks.  
2. Stock mess, recruit via gear menu (helmet, chest, sword).  
3. Confirm rank name tag and conversion message.  
4. Spawn zombie in sight within 64 blocks → soldier fights.  
5. Ring bell → soldiers alert.  
6. `./gradlew runGameTestServer` → 21/21 pass.

Report failing **ID** (A*, B*, M*) in chat with what you saw.
