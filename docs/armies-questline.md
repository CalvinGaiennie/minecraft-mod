# Armies storyline (player, Armies mod)

**Scope:** Main **optional** quest arc when **Minecraft Kingdom: Armies** is installed **without** Citadel. Citadel adds its own spine (`acolyte-path.md`, `necromancer-path.md`, `endgame.md`); this doc is the **kingdom** arc only.

**Design intent:** **Four linked set pieces** with **named** antagonists. **Not** “a harder raid night”: each defensive beat is a **quest you deliberately start** (specific player action → specific attack with a **fixed name** and script). Normal raids, swarms, and warlords continue **beside** this arc, not as substitutes for D1/D2.

**Status:** **Author direction, 2026-10-07.** Antagonist names, exact triggers, and scene text **TBD**.

---

## Chain (order is fixed)

```text
D1 (named defensive, player-triggered)
  → O1 (offense: identify who hit you in D1)
    → D2 (named defensive; starts when O1 completes; yields intel for O2)
      → O2 (offense: capstone strike using D2 intel)
```

| Step | Type | Role |
| --- | --- | --- |
| **D1** | Defensive | **Authored assault** on your kingdom. You **cause** it by doing a **specific thing** (TBD: e.g. claim + mess ritual, tax milestone, horn at wrong place). Attacker has a **fixed name** and **custom wave** (not a random new-moon roll). |
| **O1** | Offensive | **Investigation:** find **who ordered or led** the D1 hit. Tracks, witnesses, camp intel → first **authored bandit king** base → kill or capture beat **TBD**. Confirms the link to D1’s name. |
| **D2** | Defensive | **Auto-starts when O1 completes.** A **second named** enemy (ally of O1’s target, or the same network) hits **one** village with a **scripted** siege (still outpost-scale pressure, **not** kingdom wipe). **Reward:** concrete **intel** (map, confession, item) required for **O2**. |
| **O2** | Offensive | **Capstone:** use D2 intel to locate and **break** the **ring leader’s** base (strongest authored king in this arc). Closes the Armies-only bandit-king thread. |

**Emergent threats** (deserter bands, swarms, routine new-moon raids, **toned-down warlords**) do **not** count as D1 or D2.

---

## D1 / D2 vs normal gameplay

| | **Quest defensive (D1, D2)** | **Normal mod pressure** |
| --- | --- | --- |
| Start | **Player action** or **quest flag** (O1 done → D2) | Time, enlistment, moon, title |
| Enemy | **Named** NPC + authored script | Generic raiders / bands |
| Purpose | Story beat | Ongoing difficulty |

Working titles only until names are chosen: **D1 attacker `[Name TBD]`**, **D2 attacker `[Name TBD]`**, **O1 king `[Name TBD]`**, **O2 ring leader `[Name TBD]`**.

---

## Authored bandit kings (Armies)

- **Separate** from Citadel’s **ten relic fortresses** (`endgame.md`).
- **O1** and **O2** use **outpost-scale bases** (pillager-outpost class), fixed names, defenders inside.
- **O1** payoff: identity of D1’s patron + quest progress.
- **O2** payoff: loot, arc closure, optional unique item (not a relic).

Smaller named lieutenants **TBD** (optional sites between O1 and O2).

---

## Warlords (background, not D1/D2)

**Warlords** stay as **optional recurring** kingdom pressure (`endgame.md`), **toned down** (slow timer, medium raid cap, one village). They are **not** the D1 or D2 set pieces unless a future rewrite explicitly merges them (**not planned**).

---

## With Citadel installed

- This arc **still runs** (no exclusion rule yet).
- Citadel **fortresses** remain Citadel’s endgame; **O2** does not require them.
- Cross-lore **TBD**.

---

## Still to decide

- **D1 trigger:** what specific player action starts it (and minimum kingdom state).
- **D1/D2 scripts:** force size, duration, fail/retry, which village is targeted.
- **O1 investigation UX:** journal, villager lines, physical clues.
- **Multiplayer:** quest owner per player vs per kingdom.
- **Four names** and whether D1 attacker appears again in D2 or only by reference.

Cross-links: `threats-and-mobs.md`, `endgame.md`, `soldiers-and-villages.md`, `mod-split.md`.
