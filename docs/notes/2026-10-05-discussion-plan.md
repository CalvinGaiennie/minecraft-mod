# Section-by-section discussion plan, 2026-10-05

**Intake file:** [`2026-10-05-design-notes-intake.md`](./2026-10-05-design-notes-intake.md)

**Rule:** Do not start the next section until the current section’s **decisions are written** into the main project docs (and the intake or this file records *what changed where*).

---

## Process (each section)

1. **Read** the section in the intake file (only that section).
2. **Discuss**: open questions, conflicts with existing lore, scope (MVP vs later).
3. **Decide**: you call it; “TBD” stays TBD with a one-line note if needed.
4. **Merge**: update the target doc(s) below; add a **Decision log** row at the bottom of this file.
5. **Checkpoint**: you say “next section” (or we stay on the same section if something’s unresolved).

---

## Terminology gate, **closed 2026-10-05**

**Decision:** **Order vs necromancer path** only. No player-facing “sorcerer” guild. **Model 2:** necromancer **prologue → lock** (full `necromancy.md` identity at lock; exact step TBD). Optional **skills / small class tracks** for necromancers, **not** a 4×4 acolyte mirror. Chronicle lore may still say “sorcerer” historically.

---

## Section order

| # | Section (intake) | Primary merge targets | Why this order |
|---|------------------|----------------------|----------------|
| **0** | Terminology gate | (above) | Avoid rewriting lore twice. |
| **1** | Pre-citadel fork: acolyte **vs** necromancer; **Rule 1** (raised dead → acolytes know) | `acolyte-path.md`, `necromancer-path.md`, `integration.md` | **Merged 2026-10-05** (lock step TBD). |
| **2** | Minecraft lore tie-ins (End, Nether, mobs, warden, ancient city, wither, golems, guardians) | `old-kingdom-lore.md`, new stub `docs/minecraft-lore-bridges.md` (optional) | Big vision; separate from citadel fight so we don’t block path rules. |
| **3** | Founding-era story: tree king, first portal, two mothers, two brothers, End as one land, brothers’ war | `old-kingdom-lore.md` | Reconcile with **Oswin / Hakon / Harren** table already in lore. |
| **4** | 11th king, last dragon egg, second brother kills 11th → overworld | `old-kingdom-lore.md`, `endgame.md` | Links to Thornwald / Maldric / dragon seal. |
| **5** | Citadel fall: poison, undead 12th, **two brothers trapped**, seal logic (dragon + both alive?) | `old-kingdom-lore.md`, `endgame.md`, `necromancer-path.md` | Must align with **two named citadel necromancers** (brothers = those two?). |
| **6** | **Player sorcerer questline:** early game, mentor (second brother’s mother?), world sorcerers, citadel access, brother choice, endings (castle → End, giant portal) | `necromancer-path.md`, `citadel-claim.md`, `endgame.md` | Depends on 3–5. |
| **7** | Necromancer **skills / tracks** (weather, animals, bard, enchant/potion, horcruxes, not Order mirror) | `necromancer-path.md`, `necromancy.md`, `config-and-recipes.md` | After path shape is clear. |
| **8** | **Acolyte feat gates** (Smith / Brewer / Enchanter / Warlord + Maelor collections) | `acolyte-path.md`, `config-and-recipes.md` | Parallel track; can follow 1 or run after 6. |
| **9** | **Other notes:** orphanage, warlord 1000 candles, deserter → sorcerer % | `soldiers-and-villages.md`, `threats-and-mobs.md`, `acolyte-path.md` | World systems, not core brothers plot. |
| **10** | **Citadel layout / features** | `citadel-layout.md`, `endgame.md`, `citadel-claim.md`, `villager-mod-design-and-build.md` | Structure list vs what’s already documented. |

---

## Known conflicts to resolve in discussion (not prescriptive)

- **Hakon** already “made the End portal” and killed **the** necromancer in the End, your draft has **overworld sorceress builds portal**, **king with bow kills sorcerer**. Merge or retcon carefully in §3–4.
- **Two citadel bosses** are documented as Black Treaty **advisers** (Maldric era), not necessarily “immortal brothers”, §5 decides if brothers = those two, reincarnations, or rename/refactor.
- **Sorcerer ending** (citadel moves to End, overworld crater + mega portal) may override or extend current **post-claim** flow, flag in §6 vs `citadel-claim.md`.

---

## Decision log

| Date | Section | Decision summary | Docs updated |
|------|---------|------------------|--------------|
| 2026-10-05 | **0** | Player path = **necromancer** only; no sorcerer guild. Prologue → **lock** = full necromancer mechanics. Skills/tracks later, not acolyte-shaped. | `necromancer-path.md`, `necromancy.md`, intake header |
| 2026-10-05 | **1** | **3 hearts lost** → intro quest → **commit** or **opt out**. Acolytes reject **wand/hood/robe** within 32b; intro does not fire near refuges (≥128b). | `acolyte-path.md`, `necromancer-path.md`, `necromancy.md`, `integration.md` |
| 2026-10-06 | **7** (partial) | Progression **model A**: one **primary affinity** + one **cross-track** mid arc. Which affinities / when you pick primary, **deferred**. | `necromancer-path.md` |
| 2026-10-06 | **8** (partial) | Quest flow: basic → brother **light** → Maelor **light ×3** → pick **hard** one → **lock** → Annals + escort. Feat numbers **TBD**. | `acolyte-path.md` |
| 2026-10-06 | **8** | **Closed:** basic (clear + necromancer + oath); light×4 (one task each, counts toward hard); hard×4 (Smith/Alchemist/Arcane books+proofs TBD/WL gear); flow + homecoming A + Friend of the Order + Annals on claim. | `acolyte-path.md`, `endgame.md`, `citadel-claim.md` |
| 2026-10-06 | **6 / path** | Necromancer path **design philosophy:** opposite acolyte, decision + reward/**weakness** together, not grind-then-reward (`necromancer-path.md`). |
| 2026-10-06 | **§10** (partial) | Layout + **assault defensive mode**: one main entrance, closable inner gate, trap/narrow corridors, MC hazards (web/water/ice/lava); spawners + bosses; spawner mix incl. ravagers (few). Mockup for counts/triggers. **`citadel-layout.md`**, **`citadel-defenders.md`**. |
| 2026-10-06 | **§7** (partial) | Progression = **quest items + sacrifices**. **Flutes/pipes** (havoc OK, **`necromancerWorldHavocEnabled`**). Horcrux: **relics + kill** (acolyte, **player**, or **defined necromancer roster TBD**). Flute lockout on horcrux **deferred**. Hunt/steal/destroy power items. **`necromancer-path.md`**, **`necromancy.md`**, **`config-and-recipes.md`**. |
| 2026-10-06 | **§7** (horcrux) | **Hotbar** shard + power item → **kill** valid target → horcrux; **−1 heart** each; **many** horcruxes heart-limited; **chest-stored**. **Immune** poison/harm pots; pseudo-death 3m no clicks (**teleport TBD**). **Destroy horcrux item** = vanilla **hard** hits (ghast, wither, **TBD** whitelist). Binding = no unequip, not vanish on death. Chunk hints **TBD**. |
| 2026-10-06 | **§7** (wild lich) | **Subset** of crypt rogues have **horcrux in chest**: on-ramp for **destroy** (and lore for **create**). Count/placement **TBD**. **`necromancy.md`**, **`threats-and-mobs.md`**, **`necromancer-path.md`**. |
| 2026-10-06 | **§7** | Pseudo-death: **no teleport** on trigger. | `necromancer-path.md`, `necromancy.md` |
| 2026-10-06 | **§6** | **Started**: main arc outline + open questions in `necromancer-path.md`; lore brothers **§3–5** still deferred. | `necromancer-path.md`, this plan |
| 2026-10-06 | **§6** (intro) | **Largest town ~350b**; **great road through town** (**spawners**). Basement gate; **horcrux 4–8 chunks** away. Tombstone **She smote him.** Well off-road copse. | `necromancer-path.md`, `endgame.md`, `config-and-recipes.md` |
| 2026-10-06 | **§6** (intro flow) | **Horcrux destroy → kill rogue → plate → Well.** No plate = chastise. Plate = **follow-up quest** → **commit/opt out** (not at Well). | `necromancer-path.md` |
| 2026-10-06 | **§6** (commit) | Takes plate; ingredient list; hand-in = **commit** → **plate + Seal-Breaker**. **Son = evil second brother** (bound). Lie: **open seal**, kill **king + first brother**, **spare son**. | `necromancer-path.md` |
| 2026-10-06 | **§6** (End / mother / stall / tree) | End spell **teleports** player to End immediately. **Mother** only if **son killed**: spawns **at player**, dialogue, fight; **wrath** = kills **first** then you. **Stall** = brother **swarms** from citadel. **7-day** sit timer from **king death** only. **Blight Heart:** **mine under tree**; Corwin says check **under tree**. | `necromancer-path.md`, `endgame.md`, `citadel-claim.md` |
| 2026-10-06 | **§9** (partial) | **No** War Leader **1000 candles**. **Orphanages:** largest vanilla village **style**; **top 10%** villages by size. **Deserter → wandering necromancer:** ambush **player** with swarm **or** hit **one of their villages**. | `soldiers-and-villages.md`, `threats-and-mobs.md`, `acolyte-path.md`, `config-and-recipes.md` |
| 2026-10-06 | **§9** (desert + orphans) | Soldier desert outcomes **50%** villager / **45%** bandit / **5%** wandering necro. Orphans: **+4 HP**, **+15%** dmg, **1.5×** hunger/morale desert only. | `soldiers-and-villages.md`, `config-and-recipes.md`, `threats-and-mobs.md` |
| 2026-10-06 | **§9** (orphan + wander) | Orphanage: vanilla gen only, **≥21 beds** at gen. Wandering necro: **ex-owner**, **50/50** ambush/village, strike every **2** days, **permanent** death, **shard** loot. | same + `threats-and-mobs.md` |
| 2026-10-06 | **§7** (partial) | **8** End tyrants on **outer End** (not dragon island); home-bound aggro; **6** use **flutes** in fight; rogue sites = cave crypt / surface crypt / taken village / dark tower; god wizard **full set from mother at commit**; rot **1 heart / 15s**; flute stats closed. | `necromancer-path.md`, `endgame.md`, `config-and-recipes.md` |
| 2026-10-07 | **Bandit split (author)** | **10 fortresses = Citadel mod only** (named kings; relic forts in lore books). **Armies** gets separate **authored bandit kings** + quest TBD. Forts = outpost-style bases, anytime; loot-focused. Opt-out skips **player-hunt** threats only; structures stay hostile. Warlords **undecided**. Bandit grey morality **future TBD**. | `endgame.md`, `mod-split.md`, `threats-and-mobs.md` |
| 2026-10-07 | **Armies arc + warlords** | **Armies-only** spine: **2 defensive** (hold claim, warlord probe) + **2 offensive** (first king, break the ring). Warlords **stay**, **toned down** (slow timer, medium raid cap, one village). | `armies-questline.md`, `endgame.md` |
| 2026-10-07 | **Armies arc (revision)** | **D1/D2** = **named scripted defensives** (player triggers D1); **not** generic raids. **O1** = find who hit you in **D1**. **D2** starts after **O1**; intel for **O2**. Warlords separate from D1/D2. | `armies-questline.md` |
| 2026-10-07 | **Armies arc (story)** | **D1:** **30** soldiers one village → attack (no moon). Leader **player-kill** → **notebook** → **O1**. **O1:** middle manager **player-kill**, taunt, rush home. **D2:** biggest village under **big leader**; repel, he **escapes**. **O2:** hunt and kill big leader. | `armies-questline.md` |
| 2026-10-07 | **Armies arc (approved)** | Names **Halvek / Corvin / Garland** only. D1 requires owner **in village zone**; **−10%** desertion signet (**0.90**). | `armies-questline.md` |
| 2026-10-07 | **Armies arc (tweak)** | D1 starts **on enter** first **30**-soldier village; **Garland the Rook** nickname; **Rookbreaker** title **−10%** desertion (not signet). | `armies-questline.md` |

---

## Work order (author override)

**Deferred for later:** §2, §3, §4, §5, §6, §8.

**Active sequence:** **§9** → **§7** leftovers, then **§10** / deferred lore **§2–5**. (**§6** author direction merged.)

---

## Current status

**§10:** author decisions merged; close remaining **TBD** rows in `citadel-layout.md` when answered.

**Active:** **§7** (necromancer quest items, flutes, god wizard armor, horcrux). **§9** closed. **§10** next after §7.
