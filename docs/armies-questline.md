# Armies storyline (player, Armies mod)

**Scope:** Main **optional** quest arc when **Minecraft Kingdom: Armies** is installed **without** Citadel. Citadel adds its own spine (`acolyte-path.md`, `necromancer-path.md`, `endgame.md`); this doc is the **kingdom** arc only.

**Design intent:** **Four linked set pieces** with **named** antagonists. Normal raids, swarms, and warlords run **beside** this arc, not as substitutes.

**Status:** **Author direction, 2026-10-07.** Display names and dialogue **TBD**; structure below is **locked** unless author revises.

---

## Chain (order is fixed)

```text
D1 — village size trigger → scripted assault (any moon)
  → kill assault leader (player finish only) → notebook
O1 — hunt “the boss” from the notebook (player finish only) → middle-management twist
D2 — biggest village already under attack by the real big leader → repel; he escapes
O2 — hunt and kill the big bandit leader (player finish only, TBD)
```

---

## D1 — **The muster that drew eyes**

**Trigger:** One **owned village** reaches **`armiesQuestD1SoldierThreshold`** living soldiers in that village’s claim (default **30**, config). Fires **once per player** (or once per world, **TBD** multiplayer).

**Not** a new-moon raid: the attack **starts on its own schedule** when the threshold is met (ignore moon phase for this beat).

**Assault:** **Named attack leader** **`[Name TBD]`** leads an **authored wave** against **that village** (or the village that crossed the threshold, **TBD** if player moved mess).

**Rules:**

- The **assault leader** cannot be **finished off** except by a **direct player kill** (soldiers may soften him; last hit must be player). Exact immunity / regen while non-player damage **TBD** implementation.
- On death he drops **`[Notebook item TBD]`** (quest item). Looting it **starts O1**.

---

## O1 — **What the notebook names**

**Start:** Player has the **notebook** from D1’s leader.

**Goal:** Track the **boss the notebook points at** to an **outpost base**, fight through, kill **`[Middle management name TBD]`**.

**Rules:**

- **`[Middle management]`** is **player finish only** (same last-hit rule as D1 leader).
- On death: **dialogue** (paraphrase locked): he **laughs**, says you’re a **fool**, he’s **not** the big leader, only **middle management**, and you’d better **run home** before there’s **nothing left to run back to**.
- That line **arms D2**: next time the player **enters their largest village** (by soldier count or population, **TBD** tie-break), **D2** is active.

**Fantasy:** Player thought O1 was the capstone kill; it’s the **misdirect**.

---

## D2 — **The big one hits home**

**Trigger:** After O1 completes, when the player **arrives at their biggest village**, the village is **already in an active authored attack** led by **`[Big bandit leader name TBD]`** (not a random band merge).

**Goal:** **Push off** the attackers and **save the village** (mess intact, **TBD** fail conditions).

**Outcome on success:** The **big leader escapes** (cannot be killed during D2; despawn / flee script **TBD**). That escape **starts O2**.

**Not** moon-gated; this is a **quest state**, not routine pressure.

---

## O2 — **No more running**

**Start:** Big leader **escaped D2**.

**Goal:** **Find** his hideout (intel from D2 scene, notebook epilogue, survivor line, **TBD**), assault **outpost-scale base**, **kill `[Big bandit leader name TBD]`**.

**Rules:**

- **Player finish only** for the big leader (**TBD**, same as prior bosses).
- Completing O2 **closes** the Armies-only bandit-king arc (rewards **TBD**).

---

## Three bosses (names TBD)

| Role | Beat | Player-only finish |
| --- | --- | --- |
| Assault leader | D1 | Yes |
| Middle management | O1 | Yes |
| Big bandit leader | D2 escapes → O2 kill | O2 yes |

Citadel **relic fortresses** remain separate (`endgame.md`).

---

## Config (proposed)

| Key | Default | Notes |
| --- | --- | --- |
| `armiesQuestD1SoldierThreshold` | **30** | Same-village living soldiers |
| `armiesQuestD1IgnoreMoon` | **true** | D1 scheduling |
| `armiesQuestBossPlayerKillOnly` | **true** | Quest bosses need player last hit |

---

## Warlords (background)

Recurring **warlords** (`endgame.md`) are **not** D1, D2, or O2. Toned-down ambient pressure only.

---

## With Citadel installed

Arc **still runs** unless author adds exclusion later. No relic requirement for O2.

---

## Still to decide

- **Four display names** and notebook item id.
- **D1/D2 target village** rules if mess moves.
- **Multiplayer:** quest state per UUID vs per kingdom.
- **Failure:** D2 village lost, retry, or permanent scar.
- **O2 discovery** UX after D2 escape.

Cross-links: `threats-and-mobs.md`, `endgame.md`, `config-and-recipes.md`, `mod-split.md`.
