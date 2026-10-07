# Armies storyline (player, Armies mod)

**Scope:** Main **optional** quest arc when **Minecraft Kingdom: Armies** is installed **without** Citadel. Citadel adds its own spine (`acolyte-path.md`, `necromancer-path.md`, `endgame.md`); this doc is the **kingdom** arc only.

**Design intent:** **Four linked set pieces** with **named** antagonists. Normal raids, swarms, and warlords run **beside** this arc, not as substitutes.

**Status:** Story **structure locked** (author 2026-10-07). **Proposed** names, dialogue, and rules below are **awaiting author approval** (2026-10-07 draft).

---

## Chain (order is fixed)

```text
D1 — village size trigger → scripted assault (any moon)
  → kill assault leader (player finish only) → notebook
O1 — hunt “the boss” from the notebook (player finish only) → middle-management twist
D2 — biggest village already under attack by the real big leader → repel; he escapes
O2 — hunt and kill the big bandit leader (player finish only)
```

---

## D1 — **The muster that drew eyes**

**Trigger:** One **owned village** reaches **`armiesQuestD1SoldierThreshold`** living **soldiers** (ranked **Soldier** role only; **militia does not count**) in that village’s tax/claim zone (default **30**, config). Fires **once per player UUID** who owns that mess.

**Not** a new-moon raid: attack schedules **within 1–2 in-game days** after threshold (config **`armiesQuestD1DelayDays`**, default **1**), ignoring moon.

**Target village:** The village whose claim **first reached** the threshold (mess UUID + claim id **snapshotted** at trigger). If the mess moves later, **D1 still hits the snapshotted village** (forces you to defend where you built the army).

**Assault leader (proposed):** **Halvek**, title **Sergeant of the Red Ledger**. ~**18–22** deserter bandits + Halvek (tune in playtest).

**Rules:**

- Halvek cannot drop below **1 HP** from non-player damage; **player** must land the killing blow (`armiesQuestBossPlayerKillOnly`).
- Drops **Blood-stained Ledger** (quest item, id **`bandit_ledger`** proposed). First **right-click** or **pickup + read** sets quest stage **O1** and adds journal line.

---

## O1 — **What the ledger names**

**Start:** Player holds **Blood-stained Ledger** with O1 flag unset.

**Goal:** Ledger text names **Captain Corvin Slate** and a bearing (“**three days east of the last oak on the old cart road**”, flavor only). Gameplay: **quest compass** or **journal waypoint** to a **pre-placed outpost** (`Rook's Fingers`, SavedData, one site per world, **512–1200** blocks from spawn, **TBD** exact placement rules).

**Middle manager (proposed):** **Corvin Slate**, **Captain** (not king). Outpost: **8–12** bandits + Corvin.

**Rules:**

- Corvin: **player finish only**.
- **Death line (proposed):** *“You absolute mule. I’m not the Rook. I’m what keeps his books. Run home, lordling, before Garland turns your pretty village into ash.”*
- Sets flag **`armiesQuestD2Armed`**. No instant teleport home.

---

## D2 — **The big one hits home**

**Trigger:** **`armiesQuestD2Armed`** and player **enters** their **largest village** by **soldier count in zone** (same soldier-only count; tie-break: higher villager population, then lower mess UUID hash).

**Scene:** Village already in **authored assault**. Leader: **Garland Rook** (display **Garland “the Rook”**). ~**24–30** attackers + Garland (tougher than D1, **not** warlord-sized).

**Goal:** Kill or rout waves until **attack phase ends** (timer **20 min** real-time cap **TBD**, or all minions dead except Garland).

**Success:** Mess **still placed** and **≥1** post bed unbroken. Garland **flees** at **30% HP** (immune, smoke/teleport toward wilderness). Drops **`torn_map_half`** (quest item).

**Fail (proposed):** Mess broken or no beds: assault ends; **retry** when player re-enters largest village after **rebuilding mess** (no 7-day lock). **No** permanent village delete.

**Garland cannot be fully killed in D2.**

---

## O2 — **No more running**

**Start:** **`torn_map_half`** + journal entry from a **fleeing villager line** (proposed): *“He went to the broken tower beyond the birch swamp!”*

**Find:** Combine **`torn_map_half`** with **ledger** (crafting grid or anvil **TBD**) → **`rook_hideout_map`** OR compass unlock to **Garland's Roost** (second SavedData outpost, stronger than Corvin’s).

**Goal:** Kill **Garland Rook**. **Player finish only**.

**Rewards (proposed):**

- Large treasury chest (mixed loot, **no** relic).
- **Rook's Signet** (ring/trinket slot **TBD**): flavor item, +title chat prefix **“Rookbreaker”**, optional small perk **−5% soldier desertion** in owned villages (`rookSignetDesertionMultiplier` **0.95**, config).
- Marks arc **complete**; Halvek/Corvin/Garland **do not** respawn.

---

## Boss roster (proposed names)

| Role | Name | Beat |
| --- | --- | --- |
| Assault leader | **Halvek** (Sergeant of the Red Ledger) | D1 |
| Middle management | **Corvin Slate** (Captain) | O1 |
| Big leader | **Garland “the Rook”** | D2 escape → O2 kill |

Citadel **relic fortresses** use **different** kings (`endgame.md`). These three are **Armies-only** characters.

---

## Multiplayer (proposed)

- Quest progress stored on **mess owner player UUID** (who owned the village at D1 trigger).
- **Allies** can help fight; **only owner** can advance quest items (ledger read, map combine) unless we add **party share** later (**TBD**).
- **One** active arc per owner per world.

---

## Config (proposed)

| Key | Default | Notes |
| --- | --- | --- |
| `armiesQuestD1SoldierThreshold` | **30** | **Soldiers** only, in village zone |
| `armiesQuestD1IgnoreMoon` | **true** | |
| `armiesQuestD1DelayDays` | **1** | After threshold before assault |
| `armiesQuestBossPlayerKillOnly` | **true** | |
| `rookSignetDesertionMultiplier` | **0.95** | If signet equipped/owned |

---

## Warlords (background)

Recurring **warlords** (`endgame.md`) are **not** D1, D2, or O2.

---

## With Citadel installed

Arc **still runs**. Garland is **not** a relic-fortress king.

---

## Approval checklist (author)

- [ ] Names: Halvek, Corvin Slate, Garland the Rook  
- [ ] Soldier-only threshold; militia excluded  
- [ ] D1 village snapshot vs mess move  
- [ ] Corvin death line  
- [ ] D2 fail/retry rules; Garland flee at 30%  
- [ ] O2 find flow: torn map + ledger → roost  
- [ ] Reward: Rook's Signet + Rookbreaker title  
- [ ] Per-owner UUID quest state  

Cross-links: `threats-and-mobs.md`, `endgame.md`, `config-and-recipes.md`, `mod-split.md`.
