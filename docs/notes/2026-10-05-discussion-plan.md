# Section-by-section discussion plan — 2026-10-05

**Intake file:** [`2026-10-05-design-notes-intake.md`](./2026-10-05-design-notes-intake.md)

**Rule:** Do not start the next section until the current section’s **decisions are written** into the main project docs (and the intake or this file records *what changed where*).

---

## Process (each section)

1. **Read** the section in the intake file (only that section).
2. **Discuss** — open questions, conflicts with existing lore, scope (MVP vs later).
3. **Decide** — you call it; “TBD” stays TBD with a one-line note if needed.
4. **Merge** — update the target doc(s) below; add a **Decision log** row at the bottom of this file.
5. **Checkpoint** — you say “next section” (or we stay on the same section if something’s unresolved).

---

## Terminology gate — **closed 2026-10-05**

**Decision:** **Order vs necromancer path** only. No player-facing “sorcerer” guild. **Model 2:** necromancer **prologue → lock** (full `necromancy.md` identity at lock; exact step TBD). Optional **skills / small class tracks** for necromancers — **not** a 4×4 acolyte mirror. Chronicle lore may still say “sorcerer” historically.

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
| **7** | Necromancer **skills / tracks** (weather, animals, bard, enchant/potion, horcruxes — not Order mirror) | `necromancer-path.md`, `necromancy.md`, `config-and-recipes.md` | After path shape is clear. |
| **8** | **Acolyte feat gates** (Smith / Brewer / Enchanter / Warlord + Maelor collections) | `acolyte-path.md`, `config-and-recipes.md` | Parallel track; can follow 1 or run after 6. |
| **9** | **Other notes:** orphanage, warlord 1000 candles, deserter → sorcerer % | `soldiers-and-villages.md`, `threats-and-mobs.md`, `acolyte-path.md` | World systems, not core brothers plot. |
| **10** | **Citadel layout / features** | `endgame.md`, `citadel-claim.md`, `villager-mod-design-and-build.md` | Structure list vs what’s already documented. |

---

## Known conflicts to resolve in discussion (not prescriptive)

- **Hakon** already “made the End portal” and killed **the** necromancer in the End — your draft has **overworld sorceress builds portal**, **king with bow kills sorcerer**. Merge or retcon carefully in §3–4.
- **Two citadel bosses** are documented as Black Treaty **advisers** (Maldric era), not necessarily “immortal brothers” — §5 decides if brothers = those two, reincarnations, or rename/refactor.
- **Sorcerer ending** (citadel moves to End, overworld crater + mega portal) may override or extend current **post-claim** flow — flag in §6 vs `citadel-claim.md`.

---

## Decision log

| Date | Section | Decision summary | Docs updated |
|------|---------|------------------|--------------|
| 2026-10-05 | **0** | Player path = **necromancer** only; no sorcerer guild. Prologue → **lock** = full necromancer mechanics. Skills/tracks later, not acolyte-shaped. | `necromancer-path.md`, `necromancy.md`, intake header |
| 2026-10-05 | **1** | **3 hearts lost** → intro quest → **commit** or **opt out**. Acolytes reject **wand/hood/robe** within 32b; intro does not fire near refuges (≥128b). | `acolyte-path.md`, `necromancer-path.md`, `necromancy.md`, `integration.md` |
| 2026-10-06 | **7** (partial) | Progression **model A**: one **primary affinity** + one **cross-track** mid arc. Which affinities / when you pick primary — **deferred**. | `necromancer-path.md` |
| 2026-10-06 | **8** (partial) | Quest flow: basic → brother **light** → Maelor **light ×3** → pick **hard** one → **lock** → Annals + escort. Feat numbers **TBD**. | `acolyte-path.md` |
| 2026-10-06 | **8** | **Closed:** basic (clear + necromancer + oath); light×4 (one task each, counts toward hard); hard×4 (Smith/Alchemist/Arcane books+proofs TBD/WL gear); flow + homecoming A + Friend of the Order + Annals on claim. | `acolyte-path.md`, `endgame.md`, `citadel-claim.md` |
| 2026-10-06 | **6 / path** | Necromancer path **design philosophy:** opposite acolyte — decision + reward/**weakness** together, not grind-then-reward (`necromancer-path.md`). |

---

## Work order (author override)

**Deferred for later:** §2, §3, §4, §5, §6, §8.

**Active sequence:** **§7 → §9 → §10**, then return to deferred sections.

---

## Current status

**Next up (when you return):** deferred **§2–6**, **§7** affinities, or **§9** / **§10**. **§8 + Order flow** treated as decided in docs.
