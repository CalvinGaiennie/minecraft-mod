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

**Trigger:** The **first** owned village where you reach **`armiesQuestD1SoldierThreshold`** living **soldiers** (ranked **Soldier** only; **militia does not count**) in the claim zone (default **15**, config). Fires **once per player UUID**.

**Start:** The assault begins **the moment you enter that village’s claim** while it still has **≥15** soldiers (any moon). If you hit 15 while **already inside** the zone, it starts **immediately**. No delay day. If you never enter after hitting 15 elsewhere, nothing happens until you **walk into** that village.

**Target village:** That **first 30-soldier** village (the claim where the count crossed the threshold). Fight happens **there**.

**Assault leader:** **Halvek**. ~**18–22** deserter bandits + Halvek (tune in playtest).

**Rules:**

- Halvek cannot drop below **1 HP** from non-player damage; **player** must land the killing blow (`armiesQuestBossPlayerKillOnly`).
- Drops **Blood-stained Ledger** (quest item, id **`bandit_ledger`**). First **right-click** or **read** sets quest stage **O1** and adds journal line.

---

## O1 — **What the ledger names**

**Start:** Player holds **Blood-stained Ledger** with O1 flag unset.

**Goal:** Ledger names **Corvin** and a bearing (flavor text only). Chat **waypoint** to **Corvin’s camp** (SavedData, one site per world).

**Camp placement (author):** **Corvin** **800–2200** blocks from spawn; **Garland** **2200–4000**; **≥1200** blocks apart. Generic camps **800–4000**. Long marches are intentional — bring **campaign/camp** support; loot is sized to **outfit soldiers** (owner distributes). **Future:** biome landmarks (ravine, pass, broken tower); Garland prefers **rough terrain** (height variance). Prefab plan: `bandit-camp-prefabs.md`.

**Middle manager:** **Corvin** (not the big leader). Outpost: **8–12** bandits + Corvin. Chests use **bandit camp loot** (`threats-and-mobs.md`) with a **modest bump** to **ores and valuables** (extra **diamonds, emeralds**, iron/gold ingots and blocks) — bookkeeper’s cut, not hideout-tier enchants. No extra unique drop beyond the quest beat.

**Rules:**

- Corvin: **player finish only**.
- **Death line:** *“You absolute mule. I’m not the Rook. I’m what keeps his books. Run home, lordling, before Garland turns your pretty village into ash.”*
- Sets flag **`armiesQuestD2Armed`** — player is **sent home** to face D2 (not a march to Garland yet).

---

## D2 — **The big one hits home**

**Trigger:** **`armiesQuestD2Armed`** and player **enters** their **largest village** by **soldier count in zone** (soldiers only; tie-break: villager population, then mess UUID hash).

**Scene:** Village already in **authored assault**. Leader: **Garland** (known as **the Rook**). ~**24–30** **bandit** attackers + Garland (**no undead**). Tougher than D1, **not** warlord-sized.

**Goal:** Drive Garland off — **not** a wipe of every bandit.

**Success:** Mess **still placed** and **≥1** post bed unbroken. Garland **flees** when **either**: HP **≤30%**, **or** **≥65%** of his assault bandits are dead (`armiesQuestD2GarlandFleeBanditLossFraction` **0.65**). Drops **`torn_map_half`**. **No** timer cap. **Bandits only** (no undead).

**Fail:** Mess broken or no beds: assault ends; **retry** after **rebuilding mess** when you enter largest village again. **No** permanent village delete.

**Garland cannot be fully killed in D2.**

**Capture & guide (author draft, not implemented):** During the assault, player may **capture** one Garland soldier alive. He offers to lead the owner to **the Rook’s castle** only if the expedition musters **≥ N soldiers** from **camp** (**N TBD**). **No** extra “recruit to target X” on the march (author **2026-10-08**). Route passes a **village near the castle**; the guide **escapes** there. **Kidnap** when the owner enters an **authored trigger spot** near the compound (**prison arc** — `notes/tbd-garland-fortress-prison-arena.md`). **`torn_map_half`** **not** required to start O2.

---

## O2 — **No more running**

**Start:** After **O1** (Corvin dead) and **D2** (captured guide path); **`torn_map_half` optional** (not O2 gate). Quest stage **O2 armed** (**TBD** exact flag).

**Find Garland (no compass):** Captured **guide** leads the march after D2; **≥ N soldiers** required to **start** (**N TBD**). **No** Rook's Trail Compass (**remove** craft if still in data).

**Kidnap gate:** After **guide escapes**, entering the **marked kidnap volume** at the fortress approach. Army remains **outside** during prison.

**Goal (surface):** Kill **Garland the Rook** (**player finish**). **Rookbreaker** title + **signet** on **Garland** kill, **not** the prison mace champion.

### Draft — Fortress approach & prison arena (not implemented)

**Discussion doc:** [`notes/tbd-garland-fortress-prison-arena.md`](notes/tbd-garland-fortress-prison-arena.md).

**Prison flow (author, see full doc):** **Prison wing** → **Garland** walks you to **tower** → **~7 warm-ups** + waves **TBD** → forks → **mace champion** (must **die** to **win** branch). **Knockout** under 2 hearts → **one-way** (no mace rematch) → **tower watch** + **escape**; **failed escape** → released when **≥ 95%** of your soldiers dead. **Capture:** gear in **Garland’s quarters** chest. **Rookbreaker** on **Garland** surface kill only. **MP** **TBD**.

**Rewards:**

- **Garland’s treasury:** **hideout-tier or better** blocks and enchanted gear (still **no** relic), on top of normal outpost chests — enough to feel like the Rook’s main stash; tune above hideouts in playtest.
- **Garland’s Signet**: **trophy only** (normal inventory item), no stat perks.
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
- Allies can fight; **owner** advances quest items. **Camp chest loot** stays at the site (take it from the chests like a normal clear).
- **One** active arc per owner per world.

---

## Config

| Key | Default | Notes |
| --- | --- | --- |
| `armiesQuestD1SoldierThreshold` | **15** | **Soldiers** only, in village zone |
| `armiesQuestD1IgnoreMoon` | **true** | |
| `armiesQuestBossPlayerKillOnly` | **true** | |
| `titleRookbreakerDesertionMultiplier` | **0.90** | **−10%** desertion while **Rookbreaker** title active |
| `corvinCampMinDistance` / `corvinCampMaxDistance` | **800** / **2200** | Corvin camp ring |
| `garlandCampMinDistance` / `garlandCampMaxDistance` | **2200** / **4000** | Garland camp ring |
| `questCampMinSeparation` | **1200** | Corvin vs Garland |
| `citadelShadowExtraCamps` | **3** | Extra camps on citadel approach ring |
| `armiesQuestD2GarlandFleeBanditLossFraction` | **0.65** | D2 flee when this share of bandits dead |
| `citadelApproachDistance` | **480** | Shadow camp ring (~just inside max view distance so citadel is barely visible) |
| `citadelShadowRingJitter` | **24** | ± blocks on that ring |
| `wanderingNecromancerStrikeIntervalDays` | **2** | Grudge strike cadence |

**Authored camp kings (beyond Halvek assault):** **Corvin** and **Garland** only.

---

## Warlords (background)

Recurring **warlords** (`endgame.md`) are **not** D1, D2, or O2. First probe **any time after Baron title** (not gated on finishing the Garland arc).

---

## With Citadel installed

Arc **still runs**. Garland is **not** a relic-fortress king.

Cross-links: `threats-and-mobs.md`, `endgame.md`, `config-and-recipes.md`, `mod-split.md`.
