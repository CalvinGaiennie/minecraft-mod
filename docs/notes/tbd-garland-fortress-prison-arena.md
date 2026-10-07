# O2 — Garland prison arena (plot & structure, draft)

**Status:** **Author discussion draft.** Act 2 **detailed** below; surface march (guide, betrayer village) unchanged from prior revision.

---

## Surface arc (Acts 0–1) — summary

- **O1** → sent **home** for **D2**.
- **D2:** capture a Garland soldier → **guide** to the castle if **≥ N soldiers**; begs you to **recruit more** on the march.
- **Approach village** near castle → guide **escapes**.
- **Kidnap** soon after → prison interior begins (**Act 2**).

**No compass.** Army mustered for march.

---

## Act 2 — Prison (author detail, 2026-10-07)

### Opening — cell & fellow prisoner

- Player wakes in a **cell** with **another prisoner** (named NPC **TBD**).
- Prisoner **recaps**: the march, guide betrayal, capture, where the army was left.
- Establishes tone and **quest hub** for optional prisoner favors later.

### Garland — plan & the tower

- **Soon after**, Garland (in person or escorted **TBD**): explains **his plan**.
- Player taken to a **tower** (high room in compound NBT) with **clear sightline** to the approach — **literally see** the camp/army in a bad position (“your army is fucked”).
- Emotional anchor for timer/ambush pressure; motivates small fights and main arena.

### Phase A — warm-ups (main arena)

- All **warm-up** bouts use the **same main arena** (one blockout room; gates/cells open into it).
- Player **barely kitted**; opponents = **weak prisoner militia/soldiers** (broken kingdom gear).
- **Each successful warm-up** grants a **gear upgrade** for the **next** fight (see **gear ladder** below).

### Phase B — optional prisoner choices (between fights)

Not fetch quests only — **meaningful forks**, e.g.:

| Option | Effect (author) |
| --- | --- |
| **Steal from a guard** | Risk/reward item or potion; caught **TBD** penalty. |
| **Throw a fight** (intentional loss in arena) | **Removes one gear-upgrade opportunity** from the ladder (fixed slot **TBD**). |
| **Beat up / kill a bad prisoner** | Unlocks benefit or rep shift **TBD**; may close other options. |

Also **food / potions** from allies. None required to reach the **mace fight**; two prisoners **pay off in Act 3** if you helped them (**see below**).

### Phase C — main arena waves (earn the mace fight)

- Same **main arena**; escalation after warm-ups:
  - **Hostile mobs:** wolves, skeletons, zombies, spiders (**TBD** per wave).
  - **Prisoner gladiators:** varied **kits + buffs** (speed, strength, resistance **TBD**).
- **Each wave win → gear upgrade** for the next bout (**gear ladder**).
- **Death** in a wave → respawn **cell**, retry (**TBD** lose upgrade streak or not).
- Completing the wave schedule **unlocks** the **mace champion**.

### Gear ladder (after each successful fight)

- Upgrades are **scripted drops** or **Garland’s “generosity”** chest — quality **varies on purpose**:
  - **Joke tier:** almost broken shield, short wooden sword, cracked helm.
  - **Helpful tier:** real armor piece, decent weapon, useful potion.
- **Throwing a fight** consumes **one** upgrade slot (player never gets that tier’s roll).
- Playtest until warm-ups + waves feel **barely survivable** without every slot being helpful.

### Phase D — mace champion (final prison duel)

- **Garland’s soldier**, **strong mace**; tuned **very hard to win** (stats, knockback, regen **TBD**).
- **No mid-fight beat at 50% HP** on the champion — nothing special at half health.
- **Player lose condition:** if **you** drop **below 2 hearts** (under 4 HP), you **lose**; the fight **ends immediately** (no cell respawn for this bout).
- **Win** (champion dead **TBD** rules) → **Act 3 — tower + naked escape race** (below).
- **Lose** (below 2 hearts **TBD** death too?) → **bad end** **TBD** (no redemption arc); **not** the naked race.

**Garland boss kill / Rookbreaker** — likely after you **lead** saved soldiers **TBD**; not on champion alone.

---

## Act 3 — After you **win** the mace duel (author, 2026-10-07)

**No execution scene.** **No** “give up → cell → next-day rescue.”

1. **Tower again** — Garland brings you **back up** to the **tower** (same vista as Act 2 open).
2. **Forced watch** — His men **attack your army** on the ground; you **watch** (scripted losses / morale **TBD**).
3. **Naked escape race** — You are **stripped** (naked + nasty potion effects **TBD**, maybe **wooden sword** or nothing). You must **escape the fortress** and **reach your force in time** to **save who you can** and **lead them to turn the fight**.
4. **Timed pressure** (~**couple minutes** **TBD**) — Garland’s strike continues while you escape; army state when you arrive depends on **speed** + **prisoner intel**.

### Prisoner payoffs (Act 2 choices → Act 3 ease)

| Prisoner arc | If you helped them |
| --- | --- |
| **Prisoner A** | Gives **intel** during escape (weak point, which flank to hit, when to rally) → **save more soldiers** / better start to the surface battle **TBD**. |
| **Prisoner B** | **Direct help** on **final escape** (opens route, distracts guard, short ally **TBD**) → **easier / faster** egress; harder path if skipped. |

Fellow **cellmate** from opening may map to A or B **TBD**. Steal / throw fight / bad prisoner beats can still gate **which** prisoners trust you.

### Lose the mace duel

- **Below 2 hearts** → fight stops; **fail state** **TBD** (Garland mockery, army wiped or crippled without player-led turnaround — **not** the win-path naked race).

### Parallel

- First **tower visit** (Act 2) = threat preview; second **tower visit** (Act 3 win) = assault in progress + escape starts from **inside** fortress, not a friendly release.

---

## NBT blockout zones (updated)

```text
[ CELL BLOCK + fellow prisoner ]
        |
[ GARLAND TOWER — army vista ]  (scripted visit)
        |
[ PRISON WING ] — cells, quest NPCs, guard steal routes
        |
[ MAIN ARENA ] — warm-ups + mob/prisoner waves + mace champion
        |
[ TOWER ] — Act 2 preview + Act 3 forced watch (second visit)
        |
[ ESCAPE ROUTE ] — harder default; prisoner B shortcut if helped
        |
[ SURFACE / CAMP ] — rally survivors; lead counterattack; Garland **TBD**
```

**Surface:** army camp visible from tower; **naked race** from fortress → camp **after mace win only**.

---

## Open questions (when ready)

- **Champion mace** — does winning here count for quest, or only **Garland** on surface for **Rookbreaker**?
- **Mace loss** — army auto-wiped or recoverable on retry?
- **MP** — owner-only kidnapping; co-op prisoners **TBD**.

---

## Doc links

- `armies-questline.md` — D2 guide, O2 draft.
- `bandit-camp-prefabs.md` — **garland_compound** + prison/interior NBT.
