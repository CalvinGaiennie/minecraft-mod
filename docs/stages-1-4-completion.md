# Stages 1–4 completion

Automated: `./gradlew runGameTestServer`, expect **48** GameTests passing (Stages 1–6 registration smoke tests included).

## Stage 1, Core (complete)

- Soldiers and militia roles, kit slots, conversion, desertion, discharge, full kit death drops
- Mess stations, post beds, ramparts, ownership via mess + **Surveyor's markers** (owner stored on marker BE)
- Awaken horn, village bell alert, muster roll (soldiers + kills **per owned village**, not civilian counts)
- Village claims in `SavedData`; hamlet radius 32 at first mess, **population tiers** refresh on tax open (32→128)

## Stage 2, Economy (complete, plan updates)

| Design doc (original) | Shipped behavior |
|---|---|
| Tax every N days near players | **Tax runs when a player opens a tax box** |
| Villagers must pathfind to box | **Zone-only**: anyone in the tax zone can pay |
| 48-block coverage | **Village tax zone**: min **128** blocks; mess + owner surveyor markers; soldiers in zone count |
| 5 villagers per soldier | Unchanged |
| Blockade skips collection | Unchanged |
| Back-tax | Up to **30 days** since last open (`LastTaxDay` on tax box) |

Also: recruiter/supply weekly tick, gear replacement, ranks (incl. **Sergeant** @ 12 kills), veterans, zombie cure odds, combat (bow/crossbow, doors, rampart claims, hold ground). **Kit armor** syncs to clients and renders on villager models.

## Stage 3, Defense (complete MVP)

- Blocks: fallback, **haven**, **royal guard post**, arrow bin, rampart
- **Caltrop** floor block: damage + slow, 8 hits then breaks; place on own/unclaimed land only
- Nightwatch helmet + patrol; royal guard chest + post pathing
- **Village protection**: non-owners break blocks slower (3× / 5× / 10× by home soldiers present); non-owner explosions don't break blocks in claim
- **Arrow recovery**: soldiers pick up nearby ground arrows
- **Beacon buffs**: soldiers/militia near active beacons get tiered speed/haste/resistance/strength
- Alerts: awaken horn (64), vanilla bell (64). **Flares** (firework/crossbow command flares) remain a later defense feature per design doc, not a standalone item yet.

## Stage 4, War (complete MVP)

- **Camp block** + **campaign boots** (follow player, hold at camp, camp meals, retreat to camp)
- **CampaignState** SavedData; **blockade** when 15+ on-campaign soldiers near enemy village (64)
- **Mutiny**: 5+ grumble while out of combat → desert
- **Reassignment writ**: bind soldier, assign new post bed
- **Village banner**: register on mess (any banner item or default white); stored on mess BE
- **Enemy land**: non-owners cannot place blocks inside another player's claim

## Explicitly deferred to Stage 5+

Full design-doc scope not in 1–4 MVP:

- **Wanted posters** (soldier mod, planned; see `war-and-defense.md`)
- **Allies** co-owner list (protection uses owner UUID only today)
- Recruiter villager profession POI, emerald bed purchase, tax box tier upgrades

**Cut from MVP scope (not deferred):** siege assault kit (sappers, ladders, boat/bridge plans, laced rations).
- Client kit sync packets, zombie soldier world events
- Caltrop AI pathing avoidance, banner shield rendering, 8-wide castle gate blocks
- Squad pathfinding, lazy idle AI at full spec (partial stagger/tick opts only)

Manual checklists: [stage-1-tests.md](stage-1-tests.md), [stages-1-3-tests.md](stages-1-3-tests.md).
