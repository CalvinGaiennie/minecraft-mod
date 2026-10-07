# Armies storyline (player, Armies mod)

**Scope:** Main **optional** quest arc when **Minecraft Kingdom: Armies** is installed **without** Citadel. Citadel adds its own spine (`acolyte-path.md`, `necromancer-path.md`, `endgame.md`); this doc is the **kingdom** arc only.

**Design intent:** A few **big set-piece quests**, not a checklist grid. Mix **defense** (hold what you built) and **offense** (hunt named bandit kings). Emergent play (raids, swarms, deserters) continues between beats.

**Status:** **Author direction, 2026-10-07.** Counts, names, triggers, and rewards **TBD**.

---

## Shape: two defensive, two offensive

| # | Type | Working title | Fantasy |
| --- | --- | --- | --- |
| **D1** | Defensive | **Hold the claim** | Your first real kingdom moment: a **coordinated hit** on an **owned village** (band + raid pressure). Win by **keeping the mess up** and soldiers alive through one bad night. Teaches horns, havens, supply. |
| **D2** | Defensive | **Warlord probe** | After the realm grows (**Baron+**, TBD), a **named Warlord** (see `endgame.md`) strikes **one** village. Bigger than a normal new-moon raid, **not** a server-wipe. Goal: **repel** without losing the village economy. |
| **O1** | Offensive | **First crowned thief** | First **authored bandit king** (`threats-and-mobs.md`): intel from camp loot / village rumor → **outpost base** → kill the named king, recover loot + **lead on the network**. |
| **O2** | Offensive | **Break the ring** | Capstone **authored king** (strongest base, TBD roster size). Closes the **Armies-only** bandit-king thread; leaves room for Citadel fortresses if both mods are loaded later. |

**Order (proposed):** D1 early (first or second village). O1 mid (after Lord/Baron). D2 mid-late (Baron/Duke). O2 late (Duke/King). Exact gates **TBD**; should follow **kingdom titles** and enlistment, not grind walls.

---

## Authored bandit kings (Armies)

- **Separate** from Citadel’s **ten relic fortresses** (`endgame.md`).
- Each king: **fixed name**, **outpost-scale base**, **defenders inside** (pillager-outpost feel).
- **Intel chain** for O1→O2 lives here (drops, books, NPC lines), **TBD** detail.
- Payoff: **loot**, quest flags, maybe a **unique horn/item** (not a relic).

**Roster size:** **TBD** (author leaning: **3–5** kings total, with **2** as the “big” offensive beats O1/O2 and smaller optional sites **TBD**).

---

## Warlords (ongoing pressure, not a fifth mega-quest)

Warlords stay as **recurring kingdom pressure**, tuned **down** from early drafts:

- Trigger only at **Duke+** (or **Baron+**, TBD), not while learning the loop.
- **Every 6–8 new-moon cycles** (not 4–8), one **named** general targets **one** claimed village near an enlisted player.
- **Size cap:** at most a **medium** new-moon raid plus the warlord NPC (modest buffs), **no** extra sappers/ladders/siege engines beyond normal raids.
- **Not** a parallel citadel; losing should hurt **one** village, not delete the kingdom.

D2 is the **scripted intro** to warlords; later hits use the same rules.

---

## With Citadel installed

- This arc **still runs** unless we add explicit mutual exclusion later (**none now**).
- **No** relic fortresses in Armies-only logic; Citadel fortresses are **discovery/endgame**, not replacements for O2.
- Optional cross-links **TBD** (e.g. chronicle mentions a king who fled toward the great road).

---

## Still to decide

- Exact **trigger** conditions per beat (title, soldier count, quest flags).
- **Failure** rules (retry, permanent stain, village loss).
- **Multiplayer** which player owns quest state.
- Whether **D1** fires once per world or once per player.

Cross-links: `threats-and-mobs.md`, `endgame.md` (titles, warlords), `soldiers-and-villages.md`, `mod-split.md`.
