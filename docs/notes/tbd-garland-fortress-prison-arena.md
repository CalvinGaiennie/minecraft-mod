# O2 — Garland prison arena (plot & structure)

**Status:** **Author decisions in progress** (2026-10-08). Wave schedule, gear ladder counts, warm-up count (~**7**), N, MP, mace-loss quest state, escape timer, and several fork penalties **deferred**.

---

## Surface arc (Acts 0–1)

- **O1** → sent **home** for **D2**.
- **D2:** capture a Garland soldier → **guide** to the castle if **≥ N soldiers** from camp (**N TBD**).
- **March:** guide leads the route; **no soft recruit target** and **no checkpoint “recruit more” prompts** (author **2026-10-08**).
- **Approach village** near castle → guide **escapes**.
- **Kidnap:** after escape, player enters a **specific authored trigger** (volume / block spot in NBT — **exact coords with structure**). Not chunk-wide or sleep-based.
- Army stays **outside** during prison (Act 2).

**No compass.** **`torn_map_half` not required** for O2 start (author **2026-10-08**; D2 drop may stay flavor only).

**Multiplayer:** owner-only prison vs party — **TBD**.

---

## Capture & gear (author, 2026-10-08)

- **No item wipe:** whatever you carry at capture is **stripped and stored** in a **chest in Garland’s quarters** (recover when you **take the castle** / win the arc).
- **No** torture visuals or other hard-no tone limits; **no** permanent item loss from prison mechanics.

---

## Act 2 — Prison wing (whole compound)

**Not** a single cell + one roommate only. Authored **prison wing**: **multiple cells**, yard / time **outside your cell**, and/or a **rumor mill** (NPC gossip hub for forks and prisoner payoffs — **design TBD**).

### Garland — plan & the tower

**First tower visit (Act 2):** **Garland in person** walks you to the tower, explains the plan, **sightline to your army** (“bad position”). (Author **2026-10-08**.)

- Player **barely kitted** in arena; emotional pressure from vista + later timed escape.

### Phase A — warm-ups (main arena)

- Same **main arena**; ~**7** warm-up bouts (exact count **TBD** when authored).
- **Gear ladder** slot count and joke/helpful ratio **TBD**.

### Phase B — optional forks (between fights)

| Option | Effect (author) |
| --- | --- |
| **Steal from a guard** | Risk/reward; if **caught**, guards **take something from you** (what item/category **TBD**). |
| **Throw a fight** | Penalty **similar to steal** (detail **TBD**); gear-ladder slot rule **TBD**. |
| **Beat up / kill a bad prisoner** | **Potion** reward; ties to **escape help** in Act 3 (see payoffs). |

**Prisoner payoffs (Act 2 → Act 3):** **Independent** — you can finish with **intel only**, **escape help only**, or **both** (two separate NPC arcs, not mutually exclusive).

| Arc | If helped |
| --- | --- |
| **Intel (A)** | Better rally / save more soldiers — numbers **TBD**. |
| **Escape (B)** | Easier/faster final egress — mechanic **TBD**. |

### Phase C — waves

- Schedule (mob vs gladiator mix) **TBD** (author to send later).
- **Death in a wave:** **keep inventory**; **respawn in cell**; **retry that fight** (no full ladder reset decided yet).
- Completing waves **unlocks mace champion**.

### Phase D — mace champion

- Must **die** (no yield).
- **Player lose (knockout):** drop **below 2 hearts** → **knockout only** (no death screen); **no** cell respawn for this bout.
- **Knockout path (author, 2026-10-08):** **One-way** — **no** rematch with the mace champion. Garland brings you to the **tower** to **watch** his men attack your army, then you **must try to escape** (timing/debuff **TBD**).
- **Failed escape:** Garland **releases you** once **≥ 95%** of **your marched soldiers** are dead — you rejoin a **crippled** force (surface fight / finish Garland **TBD** tuning).
- **Win path:** champion **dies** → tower watch → escape → timely return **helps** the surface fight (unchanged intent).

**Champion kill ≠ arc complete.** **Rookbreaker** title + **Garland’s Signet** = **player kill Garland the Rook on the surface** after prison (same as `armies-questline.md`), **not** the mace soldier alone.

---

## Act 3 — After mace **win**

1. **Tower** — forced watch while Garland’s men hit your army (losses **TBD**).
2. **Escape race** — stripped / debuffed; reach camp. **Time cap TBD** (depends on escape route terrain in NBT).
3. **Surface:** if you **return in time**, you get an **opportunity to help** your force win — **not** an auto-win; late arrival = weaker rally (**details TBD**).

**v1 scope:** **Full** prison arc in one implementation pass (author **2026-10-08**).

---

## NBT blockout zones

```text
[ PRISON WING — cells, yard/rumor mill, quest NPCs ]
        |
[ GARLAND TOWER — army vista ]  (scripted visit)
        |
[ GARLAND QUARTERS — stored player gear chest ]
        |
[ MAIN ARENA ] — ~7 warm-ups + waves + mace champion
        |
[ ESCAPE ROUTE ] — terrain sets timer TBD
        |
[ SURFACE / CAMP ] — optional help if timely; kill Garland for Rookbreaker
```

**Kidnap trigger:** marked **volume** on approach (paired with `garland_compound` NBT).

---

## Still open (short list)

- **N** (minimum soldiers to start march).
- **Garland in person** at first tower vs lieutenant.
- **Steal / throw fight** — what is taken on failure.
- **Wave list**, **gear ladder** count & joke ratio.
- **N**, wave schedule, gear ladder, **MP**, surface tuning after **95%** release.
- **Forced watch** loss %, **Prisoner B** escape mechanic, **MP** kidnapping.

---

## Doc links

- `armies-questline.md` — D2 guide, O2 rewards.
- `bandit-camp-prefabs.md` — **garland_compound** + prison interior NBT.
