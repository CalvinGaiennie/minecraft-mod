# Armies storyline (player, Armies mod)

**Scope:** Main **optional** quest arc when **Minecraft Kingdom: Armies** is installed **without** Citadel. Citadel adds its own spine (`acolyte-path.md`, `necromancer-path.md`, `endgame.md`); this doc is the **kingdom** arc only.

**Design intent:** **Four linked set pieces** with **named** antagonists ( **first names only**, no surnames or fancy titles on NPCs). Normal raids, swarms, and warlords run **beside** this arc.

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

**Trigger:** One **owned village** reaches **`armiesQuestD1SoldierThreshold`** living **soldiers** (ranked **Soldier** role only; **militia does not count**) in that village’s tax/claim zone (default **30**, config). Fires **once per player UUID** who owns that mess.

**Not** a new-moon raid. After threshold, wait **`armiesQuestD1DelayDays`** (default **1** in-game day), then start the assault **only when the owning player is inside that village’s claim zone**. If you are elsewhere, the quest **waits** until you come home. **You must be there** for the attack to run and to defend it.

**Target village:** The village whose zone **hit 30 soldiers** (same claim as that mess). The assault happens **there**, not at a mess you relocated afterward.

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
- **Death line:** *“You absolute mule. I’m not Garland. I’m what keeps his books. Run home, lordling, before Garland turns your pretty village into ash.”*
- Sets flag **`armiesQuestD2Armed`**.

---

## D2 — **The big one hits home**

**Trigger:** **`armiesQuestD2Armed`** and player **enters** their **largest village** by **soldier count in zone** (soldiers only; tie-break: villager population, then mess UUID hash).

**Scene:** Village already in **authored assault**. Leader: **Garland**. ~**24–30** attackers + Garland (tougher than D1, **not** warlord-sized).

**Goal:** Kill or rout waves until **attack phase ends** (timer cap **TBD**, or all minions dead except Garland).

**Success:** Mess **still placed** and **≥1** post bed unbroken. Garland **flees** at **30% HP** (immune, flee script). Drops **`torn_map_half`**.

**Fail:** Mess broken or no beds: assault ends; **retry** after **rebuilding mess** when you enter largest village again. **No** permanent village delete.

**Garland cannot be fully killed in D2.**

---

## O2 — **No more running**

**Start:** **`torn_map_half`** + journal line from a villager (proposed): *“He went to the broken tower beyond the birch swamp!”*

**Find:** Combine **`torn_map_half`** with **ledger** (crafting grid or anvil **TBD**) → compass unlock to **Garland’s camp** (SavedData outpost, stronger than Corvin’s).

**Goal:** Kill **Garland**. **Player finish only**.

**Rewards:**

- Large treasury chest (mixed loot, **no** relic).
- **Garland’s Signet** (slot **TBD**): **−10% soldier desertion** in owned villages while carried or owned (`rookSignetDesertionMultiplier` **0.90**, config name kept for compatibility).
- Optional chat title **Rookbreaker** (**TBD** if author wants it removed).
- Arc **complete**; Halvek, Corvin, Garland **do not** respawn.

---

## Boss roster

| Role | Name | Beat |
| --- | --- | --- |
| Assault leader | **Halvek** | D1 |
| Middle management | **Corvin** | O1 |
| Big leader | **Garland** | D2 escape → O2 kill |

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
| `armiesQuestD1DelayDays` | **1** | After threshold; assault waits until owner **in zone** |
| `armiesQuestBossPlayerKillOnly` | **true** | |
| `rookSignetDesertionMultiplier` | **0.90** | **−10%** desertion (Garland’s Signet) |

---

## Warlords (background)

Recurring **warlords** (`endgame.md`) are **not** D1, D2, or O2.

---

## With Citadel installed

Arc **still runs**. Garland is **not** a relic-fortress king.

Cross-links: `threats-and-mobs.md`, `endgame.md`, `config-and-recipes.md`, `mod-split.md`.
