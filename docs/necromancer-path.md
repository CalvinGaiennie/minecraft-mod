# Necromancer questline (player)

**Design in progress** — owner: you.

Pre–citadel optional **story arc** parallel to the Order (`acolyte-path.md`). Player-facing term is **necromancer** only (no separate “sorcerer” guild).

**Casual necromancy** (`necromancy.md` — wand, hearts, minions, stigma) is **not** the same as this questline. You can play as an active necromancer without ever committing; you can **opt out** of the quest and stay eligible for the Order.

Mechanical identity for committed players stays **`necromancy.md`**, plus quest **skills/tracks** (TBD). Commitment does not replace the power/weakness loop with generic mage fantasy.

## Design philosophy (vs Order — author note)

**Opposite of the acolyte path** (`acolyte-path.md`):

| | **Order (acolyte)** | **Necromancer path** (intake may say “sorcery”) |
| --- | --- | --- |
| **Shape** | **Work first** — basic, light, hard feats, escort; specialty **earned** through grind and masters. | **Decision first** — less upfront labor; the fork (**commit** / **opt out**) and living with the path matter more than long feat walls. |
| **Payoff** | **Reward after proof** — Maelor **locks** specialty; perks follow earned trust. | **Reward and weakness together** — commit brings power (quest, skills, story) **and** immediate, lasting costs (`necromancy.md`: hearts, stigma, no absolution, Order closed). |
| **Fantasy** | Brothers, Annals, reunite the Order. | The dark road finds you; you choose how deep to go. |

When we design necromancer chapters (§6+ in discussion plan), avoid mirroring acolyte feat checklists unless a beat explicitly needs one — favor **choices, consequences, and tradeoffs** over collection grinds.

## Relationship to other systems

| Topic | Rule |
| --- | --- |
| **Order acolyte path** | Stays available while intro quest is open or if you **opt out**. **Closes only on commit** (`acolyte-path.md`). |
| **Throne / citadel owner** | Unchanged — active necromancer sit rules, wand forfeit, relic ban, tree (`citadel-claim.md`, `necromancy.md`, `endgame.md`). |
| **Necromancer commitment** | End of **intro quest** — player chooses **commit**. **Absolution permanently unavailable.** Order closed. Main quest + skills unlock (TBD). |
| **Opt out** | End of **intro quest** — player refuses the binding. **Necromancer questline permanently unavailable.** Order stays open (through citadel claim at least). **Active necromancer** play and **absolution** still work under `necromancy.md`. |
| **Intro trigger** | When the player has lost **3 permanent hearts** to the wand (configurable threshold), a **necromancer NPC** seeks them out and offers the intro arc — **only if not near Order acolytes** (no refuge acolyte within **128** blocks default; no wand/hood/robe visit overlap). If the player is gear-clean at a refuge, intro waits until they leave Order space. **TBD:** ignoring the offer, repeat prompts, multiplayer. |
| **Timing** | Main arc emphasis **pre–citadel claim**; crypts, rogues, citadel pair (`endgame.md`, `threats-and-mobs.md`). |

## Intro arc → commit or opt out

```text
(Wand play anywhere — hearts, minions, optional absolution)
  → 3 permanent hearts lost (default) → necromancer NPC finds you → intro quest
  → final choice:
       COMMIT — questline continues, no absolution, Order closed, perks/skills (TBD)
       OPT OUT — questline locked out forever, Order still valid, casual necromancy unchanged
  → (if committed) main arc — mentor, citadel, brothers, endings (TBD)
```

**Design intent:** heart loss proves you are not dabbling once; the NPC offer is the **moral fork**. Commit aligns with **no absolution** (life sentence). Opt out separates **story** from **mechanics** so Order players are not punished for a wand phase they walk back from.

## Skills and affinities (not Order-shaped)

**Committed quest only** (not granted on opt out). **Not** a 4×4 mirror of Order specialties or citadel lectern jobs.

### Progression model **A** (chosen)

```text
Commit to necromancer questline
  → (TBD when) choose one **primary affinity**
  → mid main arc: unlock one **cross-track** (second affinity keystone)
```

- **Primary + one cross-track** — not collect-em-all, not four parallel citadel roles.
- **Class / affinity list deferred** — candidate themes from design notes include weather, animals/beasts, bard/dirge, enchant/potion (malefic), and phylactery/horcrux upgrades; names, count, and keystone items **TBD**.
- **Horcrux:** likely an upgrade path on **phylactery shards** (`necromancy.md`), not a separate “class” — confirm when affinities are picked.
- All affinities assume **committed** status and full **`necromancy.md`** penalties; tracks add style/tools, not a bypass of hearts, stigma, or relic ban.
- **Power ceiling TBD** when affinities are designed — must stay below Order **major** post-tree royal stations (`acolyte-path.md`).

### Feat gates (§8 parallel)

Committed players may get **per-affinity feat checks** when affinities are defined — not the same lists as Order Smith/Arcane/etc. **TBD** after affinity roster is chosen.

## Sections still to fill

- **Intro quest** — steps, NPC identity, commit/opt-out scene, what happens if player is already deep in Order training.
- **Main quest flow** — early game, mentor, citadel access, two brothers choice, endings (e.g. citadel to End — intake TBD).
- **Who / when** — soldiers near cult sites?, claim timing, committed vs opt-out on throne/relics (both still active necromancer rules if using wand).
- **NPCs** — roster; **two named bound necromancers** in citadel (`endgame.md`, names TBD).
- **Feat gates** — achievement-style gates (TBD; parallel to Order discussion).
- **Config keys** — e.g. `necromancerQuestHeartThreshold` default **3**, `necromancerIntroMinDistanceFromAcolyte` default **128**, intro NPC cooldown (TBD).

## Lore language

Historical chronicles may use older words (e.g. “sorcerer”); **player systems use necromancer**. Intake draft: `docs/notes/2026-10-05-design-notes-intake.md`.

## Existing mechanics (reference)

- Hood, robe, wand, minions, absolution (until commit), penalties — `necromancy.md`
- Citadel throne, barrier, relic ban — `citadel-claim.md`, `endgame.md`, `mod-split.md`

## Marketing one-liner (draft)

“Three hearts gone, something finds you — swear the dark road or walk away and leave the Order your path.”
