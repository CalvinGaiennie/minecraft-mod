# Citadel ownership and throne

Rules for first claim, usurpation, barrier, and owner mistakes. **Necromancer mod** owns SavedData; **soldier mod** optional (mess link, allies allowlist, kingdom perks).

## Corrupted King fight

- Non-players may **damage** the king; only a **player** killing blow may **kill** him (HP clamps until a player finishes).
- **Killing credit** + **Kingslayer** title: **player** last hit only (not soldiers/minions).
- **Contender credit:** player **personally** damaged the king at least once (your soldiers’ damage does **not** count for you).

## Credits (separate UUID flags, not teams)

| Flag | Earned by |
|------|-----------|
| **Sealwright** | First player to break the seal with Seal-Breaker on the gate (once per world) |
| **Killing credit** | Player killing blow on Corrupted King |
| **Contender credit** | Personal damage to Corrupted King during that fight |
| **Savior of the Tree** (title) | Player who **uses the cure** on the Blight Heart (`endgame.md`) — **any** player, not owner-only; once per world heal |

**Sealwright bonus** (when **anyone** completes a valid claim sit): title **Sealbreaker**; **Seal-Breaker** sword bound to Sealwright — one craft per world, relic persistence, homing to Sealwright’s inventory if not in **any** player inventory for **&gt;60s** (may be stolen while held).

## First claim (unclaimed citadel)

1. **Act:** occupy **Citadel Throne** **60 seconds** without dying or leaving seat (contestable PvP).
2. **Days 1–7** after king death: only players with **killing** and/or **contender** credit may complete the sit. The **7-day window starts when the Corrupted King dies**; bound brothers **alive or dead** do not pause or reset it (`necromancer-path.md` stall claim uses the same timer).
3. **Day 8+** if still unclaimed: **anyone** except rules below.
4. **Active necromancer:** cannot progress sit. **Blighted tree:** blocked + **chat message** (must absolve — only if player **never created a horcrux**; see `necromancy.md`). **Healthy tree:** **instant death** (no totem) + **broadcast within 1000 blocks** of Kingstree explaining why.
5. **Citadel ownership ban** (wand forfeit): cannot sit; **blighted:** **damage per tick** while attempting; **healthy tree:** instant death.
6. After claim: **Maelor roster homecoming** only if reunite completed pre-claim; otherwise acolytes must be **escorted** to citadel by any player (`endgame.md`). Restoration tracks; **only owner** may place **mess station** in citadel (soldier block, necro rule).

## Ender barrier (allowlist)

- Ward is **always active**; it **allows** only the **owner** and **allies** (soldier ally list when loaded).
- **No owner:** no allowlist → **anyone may enter** (ward does not exclude).
- **Seal-Breaker** opens the **first** seal only; it does **not** breach a healthy-tree owner barrier for theft.

## Usurpation (owner already registered)

- Owner **remains owner** until a successful **60s sit** transfers UUID (dying does not alone unclaim).
- **Trigger:** owner **dies inside citadel bounds**; death must be within the **last 10 in-game minutes**.
- **Killer:** **5 minutes** exclusive right to attempt throne sit; then **anyone** (subject to necro/ban rules).
- **Spectral kings** (Hall restored): defend **throne** from everyone except **registered owner** until usurp completes.
- **No** infiltration item, **no** garrison-clear requirement, **no** separate usurpation consumable in current plan.

## Owner uses wand (any use: convert or renew)

1. **Warning UI** before first use while owner: forfeits citadel, starts **7-day reclaim**, permanent **citadel ownership ban** if confirmed.
2. On confirm: **ownership cleared**; **restoration stays** (tree, Hall, world structures); **owner perks** (horn, Well, golden age binding, etc.) detach until new owner.
3. **7 days:** only original **killing/contender** credits may sit; **barrier has no owner** → anyone may enter citadel.
4. Banned player: **acolytes hostile on sight**; **weakness** while inside citadel; sit attempts as ban rules above; **never** own citadel again.

## Kingslayer perks

- **Well of Kings:** **+100% XP** rate (stacks on base Well rate) for **Kingslayer** only while they are **citadel owner** and **Savior of the Tree**. Base Well XP still works for any barrier-eligible player who is **not** an active necromancer (`endgame.md`).
- **Spectral kings:** if Hall restored, defend Kingslayer when **someone attacks them** and kings are nearby.
- **Spectral Thornwald:** when Kingslayer **below 3 hearts** (current), spawn **Thornwald Thornheart** to protect; ends when wearer **above 3 hearts** OR Thornwald dies; **no** trigger if **max HP &lt; 3** from permanent wand loss.

## Phantom Roost

- For **citadel owner** (allies optional via soldier): overworld phantoms in kingdom do not spawn except in **Roost** — caged room with **phantom spawner**, phantoms cannot escape. Owner farms **membranes** as citadel loot (not tied to necromancer crafting in citadel docs).
