# Armies storyline (player, Armies mod)

**Scope:** Main **optional** quest arc when **Minecraft Kingdom: Armies** is installed **without** Citadel. Citadel adds its own spine (`acolyte-path.md`, `necromancer-path.md`, `endgame.md`); this doc is the **kingdom** arc only.

**Design intent:** **Four linked set pieces** with **named** antagonists (**first names** on quest bosses; **Garland** may use a **nickname** like **the Rook** in dialogue). Normal raids, swarms, and warlords run **beside** this arc.

**Status:** **Approved** by author (2026-10-07), except tuning numbers in playtest.

---

## Chain (order is fixed)

```text
D1 — village size trigger → scripted assault (any moon)
  → kill assault leader (player finish only) → notebook
O1 — hunt “the boss” from the notebook (player finish only) → middle-management twist
D2 — biggest village already under attack by the big leader → repel; he escapes
O2 — hunt and kill the big bandit leader (player finish only)
```

---

## D1 — **The muster that drew eyes**

**Trigger:** The **first** owned village where you reach **`armiesQuestD1SoldierThreshold`** living **soldiers** (ranked **Soldier** only; **militia does not count**) in the claim zone (default **30**, config). Fires **once per player UUID**.

**Start:** The assault begins **the moment you enter that village’s claim** while it still has **≥30** soldiers (any moon). If you hit 30 while **already inside** the zone, it starts **immediately**. No delay day. If you never enter after hitting 30 elsewhere, nothing happens until you **walk into** that village.

**Target village:** That **first 30-soldier** village (the claim where the count crossed the threshold). Fight happens **there**.

**Assault leader:** **Halvek**. ~**18–22** deserter bandits + Halvek (tune in playtest).

**Rules:**

- Halvek cannot drop below **1 HP** from non-player damage; **player** must land the killing blow (`armiesQuestBossPlayerKillOnly`).
- Drops **Blood-stained Ledger** (quest item, id **`bandit_ledger`**). First **right-click** or **read** sets quest stage **O1** and adds journal line.

---

## O1 — **What the ledger names**

**Start:** Player holds **Blood-stained Ledger** with O1 flag unset.

**Goal:** Ledger names **Corvin** and a bearing (flavor text only). **Quest compass** or **journal waypoint** to a **pre-placed outpost** (**Corvin’s camp**, SavedData, one site per world, distance rules **TBD** placement).

**Middle manager:** **Corvin** (not the big leader). Outpost: **8–12** bandits + Corvin.

**Rules:**

- Corvin: **player finish only**.
- **Death line:** *“You absolute mule. I’m not the Rook. I’m what keeps his books. Run home, lordling, before Garland turns your pretty village into ash.”*
- Sets flag **`armiesQuestD2Armed`**.

---

## D2 — **The big one hits home**

**Trigger:** **`armiesQuestD2Armed`** and player **enters** their **largest village** by **soldier count in zone** (soldiers only; tie-break: villager population, then mess UUID hash).

**Scene:** Village already in **authored assault**. Leader: **Garland** (known as **the Rook**). ~**24–30** attackers + Garland (tougher than D1, **not** warlord-sized).

**Goal:** Kill or rout waves until **attack phase ends** (timer cap **TBD**, or all minions dead except Garland).

**Success:** Mess **still placed** and **≥1** post bed unbroken. Garland **flees** at **30% HP** (immune, flee script). Drops **`torn_map_half`**.

**Fail:** Mess broken or no beds: assault ends; **retry** after **rebuilding mess** when you enter largest village again. **No** permanent village delete.

**Garland cannot be fully killed in D2.**

---

## O2 — **No more running**

**Start:** **`torn_map_half`** + journal line from a villager (proposed): *“He went to the broken tower beyond the birch swamp!”*

**Find:** Combine **`torn_map_half`** with **ledger** (crafting grid or anvil **TBD**) → compass unlock to **Garland’s camp** (SavedData outpost, stronger than Corvin’s).

**Goal:** Kill **Garland the Rook**. **Player finish only**.

**Rewards:**

- Large treasury chest (mixed loot, **no** relic).
- **Garland’s Signet** (slot **TBD**): **trophy only**, no stat perks.
- Permanent chat title **Rookbreaker** (from his nickname **the Rook**). While you hold this title: **−10% soldier desertion** in owned villages (`titleRookbreakerDesertionMultiplier` **0.90**, config). Same perk style as kingdom titles in `endgame.md` (title-gated, not item-gated).
- Arc **complete**; Halvek, Corvin, Garland **do not** respawn.

---

## Boss roster

| Role | Name | Beat |
| --- | --- | --- |
| Assault leader | **Halvek** | D1 |
| Middle management | **Corvin** | O1 |
| Big leader | **Garland** (nickname **the Rook**) | D2 escape → O2 kill |

Citadel **relic fortresses** use **different** characters (`endgame.md`).

---

## Multiplayer

- Quest progress on **mess owner player UUID** at D1 trigger.
- Allies can fight; **owner** advances quest items unless **party share** added later.
- **One** active arc per owner per world.

---

## Config

| Key | Default | Notes |
| --- | --- | --- |
| `armiesQuestD1SoldierThreshold` | **30** | **Soldiers** only, in village zone |
| `armiesQuestD1IgnoreMoon` | **true** | |
| `armiesQuestBossPlayerKillOnly` | **true** | |
| `titleRookbreakerDesertionMultiplier` | **0.90** | **−10%** desertion while **Rookbreaker** title active |

---

## Warlords (background)

Recurring **warlords** (`endgame.md`) are **not** D1, D2, or O2.

---

## With Citadel installed

Arc **still runs**. Garland is **not** a relic-fortress king.

Cross-links: `threats-and-mobs.md`, `endgame.md`, `config-and-recipes.md`, `mod-split.md`.
