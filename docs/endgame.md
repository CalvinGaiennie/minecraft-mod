# Endgame

Details still to decide: bandit fortresses, kingdom titles, warlords, monuments, and the Black Citadel with its acolytes and the Blighted Tree. The lore behind it is in the Old kingdom lore part.

- **Bandit fortresses:** rare, large fortified structures ruled by a Bandit King over several bands, with walls and a treasury. Taking one is a full campaign: camp, blockade, siege ladders, sappers, and assault. Ten fortresses per world, placed from saved world data like the refuges; six of them each hold one king's relic, and the other four are ordinary bandit fortresses with no relic. They sit in remote, hard-to-reach terrain at least 2,500 blocks from spawn and never appear on village maps, so players find them with the Royal Annals hints, the burial maps, or a relic compass. Only the six relic fortresses are marked by those clues; the other four have the same garrison and a normal treasury, and the relic compass ignores them. They're well defended: a Bandit King and 3 bands (about 30 bandits, 12 of them archers on the walls), with the strongest garrisons guarding the weapon relics (proposed).
- **Kingdom titles:** a player's title depends on how many villages he owns. A village only counts while at least one of his soldiers there is alive. Perks are a chat title, extra recruiter slots, and better odds on rare tax items (gold, diamonds, and netherite) only.

| Title | Villages (proposed) | Extra recruiter slots (proposed) | Rare tax item odds (proposed) |
|---|---|---|---|
| Lord | 1 | 0 | Normal |
| Baron | 3 | +1 | +10% |
| Duke | 6 | +2 | +20% |
| King | 10 | +3 | +30% |

- **Warlords:** once a kingdom reaches a certain size, a Warlord raid arrives every 4 to 8 new moon cycles (random within that range), bringing its own siege equipment and growing with the player's title.
- **Monuments:** when a hero or Legend dies, he drops a service record, a paper with his name and stats. Players can craft and place statues in several sizes at any time, but they look rough and unfinished until a service record is applied. Then the statue takes on that soldier's look, name, rank, and kill count. With **Armies** at the citadel, use **scattered plinths** in courts and yards (`citadel-layout.md`); without Armies, monuments stay kingdom-wide only.

## The Black Citadel

A post-End boss fortress that uses the mod's war systems. **Layout:** `citadel-layout.md`. **Defenders:** `citadel-defenders.md`.

- **The structure:** one huge fortress per world, 1,000 to 2,000 blocks from spawn, with a great road running straight from spawn to its gates (**road is outside** citadel bounds — `citadel-layout.md`). **Outer walls** are **unbreakable**, ~**40** blocks tall (too tall for siege ladders), thick solid stone/brick (**no lava** in walls or underfoot). **Main citadel floors** are **level with** ground outside the gate; **basements** extend **up to 30 blocks down** from that plane, then an **unbreakable bottom** (`citadel-layout.md`). **Citadel bounds** = everything **inside the outer wall** (Kingstree, outdoor Hall, paddock, courts — not the great road). Its gates are special citadel doors that only **players** break; soldiers can't. An **ender barrier** seals the bounded volume until the Ender Dragon has died and a player uses the Seal-Breaker on the gate (see Breaking the seal). The barrier is impassable until opened — can't be broken, dug under, flown over, or pearl-skipped.
- **The boss:** the Corrupted King, a cruel king raised from the dead by his necromancers, extremely hard to kill. **Defenders:** **three boss NPCs** (king + **two bound necromancers**) plus **citadel undead spawners** (`citadel-defenders.md`) — no living militia, works without Armies. **Difficulty** comes from **boss behavior**, **spawner count/type/placement**, and **layout** (chokepoints, funnels, gate gauntlets) more than stat bloat; solo still expects multiple failures without strong gear and quest tools. High walls, few entrances.
- **The two bound necromancers:** exactly **two** named necromancer **boss NPCs** in the citadel — the pair from the **Black Treaty** who poisoned Maldric, raised him, and bound the seal (`old-kingdom-lore.md`). **Names TBD**; they are reserved as **story characters** for the necromancer questline (`necromancer-path.md`) and Village Chronicles / boss lore later.
    - Fixed roles in the keep (spawn points TBD with structure layout).
    - **Adds** come from nearby **spawner zones** and/or short-range summons (**TBD** — keep fights readable).
    - **While either lives**, the pair keeps raising fallen defenders (**optional** — config) and/or **spawners stay active**; **both must be killed** before rez stops and spawners shut down (`citadel-defenders.md`).
    - **Once killed** during the Corrupted King arc, they **do not respawn** in that world (unlike rogue crypt necromancers).
    - Drop **phylactery shards** (1–2 each) per `necromancy.md`.
- **The fight:**
    1. **The siege:** walls can't be breached or climbed (**one main assault entrance** + optional secret tunnel — `citadel-layout.md`). Players break **outer citadel doors** under **ranged overwatch**; **inner grand gate stays closed** in **defensive mode** until the king falls (mockup). Trap corridors (dispenser debuffs, lava), hazards, and spawners cover the push — **spawner blocks can be mined mid-assault** where reachable; plan **milk, potions, food, blocks, ranged**, and strong armor, not just melee DPS (`citadel-defenders.md` **Siege prep & effects**).
    2. **Starving them out:** **N/A** — garrison is **all undead** (no blockade desertion; see `citadel-defenders.md`).
    3. **The two bound necromancers:** kill **both** before the king; while either lives, fallen defenders keep rising.
    4. **The duel:** the Corrupted King has 300 HP (Wither-level), 40% damage resistance, and Legend-level melee damage, and takes double damage from behind (proposed).
- **The Corrupted King's Crown:** his reward, worn in the helmet slot. While worn, soldiers of the wearer who die within 32 blocks of him rise after 10 seconds as zombie soldiers that keep their gear and rank, fight on for 2 minutes, then crumble to dust, gone for good. Each fight in which it raises anyone adds one grumble point to its group (proposed). It's the 12th relic, belonging to the Corrupted King's own statue: placing it there lays him to rest and finishes restoring the Hall. The wearer can keep it as long as he likes, but the Hall isn't fully restored until he gives it up.
- **One crown per world:** the Corrupted King can only be defeated once, so there's a single crown for players to fight over. All 12 relics, the crown included, can't be destroyed and never despawn. If the crown is lost anyway, it returns to the citadel's throne. A lost relic stays wherever it was lost; if it falls into the void, it reappears on the last solid ground it touched. Players can track one down with a relic compass.
- **The citadel beacon:** a normal beacon in a hidden basement room; buffs defenders while intact. **Not** a protected block — **guards** in the room are the defense. Attackers may **break the pyramid during the assault** to drop the buff. After the king falls, take beacon + pyramid like any loot (`citadel-defenders.md`).
- **The treasury:** a stocked treasury block, plus gold, diamond, and emerald blocks piled around the room.
- **The library:** full bookshelves, chests of strong enchanted books, the royal enchanting table (see Acolytes), plus the Royal Annals: 12 volumes, one for each king (the 12th is the Corrupted King's), each also kept as a copy in an acolyte's refuge (see Acolytes), telling his life, his famous relic and its power, and the battle where he died. Each volume hints at where that king's relic lies. Volumes can be copied the way vanilla written books are (the volume plus a book and quill), and copies work in the relic compass recipe. The library also holds two enchanted books impossible in vanilla, one copy each: Sharpness VII and Looting V (proposed levels). They work on any weapon that can take that enchantment. The Seal-Breaker holds only one enchantment, so putting one on it means choosing between them. Both books sit in a sealed vault that only opens once the Corrupted King is defeated, so nobody can sneak in through the tunnel and grab them early.
- **The alchemy room:** brewing stands, chests of strong potions, and the royal brewing stand (see Acolytes).
- **The Hall of the Twelve Kings:** an **outdoor** courtyard of statues (`citadel-layout.md`) — the old kingdom's 12 kings, the last of them the Corrupted King, all named in the Village Chronicles. Their relics are missing.
- **The story:** the citadel was once the seat of a great kingdom. Its twelfth and last king was cruel and weak, and when he died his necromancers raised him back to rule forever as the Corrupted King. To keep anyone from ever reaching him, they bound the citadel's seal to the life of the Ender Dragon, which is why the dragon has to die first. Chronicles about the old kings, the fallen heroes, and the sealing tell the full story, and tie to the statues in the hall.
- **The secret tunnel:** a hidden, half-collapsed tunnel runs from outside the walls into the lower keep. Only a super rare chronicle hints at where it starts.
- **The dungeon:** about **8 captives** the garrison took from nearby villages (traders + local heroes — freed to nearest village / your army), **plus two imprisoned Order brothers** (always a **War Leader** and one **Arcane, Alchemist, or Smith** — **random per world**). Purge lore: the king ordered them to **hold the inner ward**; necromancers caged them instead of killing them. A **player-flipped lever** opens the Order cells during the assault; then **escort to Maelor** for reunite like any brother (`acolyte-path.md`, `citadel-layout.md`). Annals copies stay in **refuge chests only**, not on trapped brothers.
- **The royal armory:** racks and chests of full iron and diamond gear, enough to re-equip an army.
- **The war room:** burial maps marking where each fallen hero of the old kingdom died.
- **The banner of the old kingdom:** a unique village banner, no longer kept in the citadel. One of the 4 ordinary refuges has a 25% chance per world to hold it in its chest (proposed), so many worlds have none. It can't be copied, can only fly over one village at a time, and follows the relic rules, so it can't be destroyed. Soldiers carrying it on their shields never desert from morale.
- **Shadow over spawn:** until the citadel falls, new moon raids are more common near spawn, and villages near spawn get extra militia (which also means fewer normal trades there). Once the Corrupted King is defeated, raids everywhere return to normal rates.
- **The kings' relics:** the Hall holds statues of the old kingdom's 12 kings, each famous for a weapon or armor piece (the 12th, the Corrupted King, for his Crown). Bandit fortresses farther from spawn hold six of the relics, each sold there at the end of a long chain of thieves who couldn't use it. Four sit in acolyte refuges, saved from the treasury in the purge (see Acolytes), and the burial maps point to each relic's current place. The Thornheart Circlet lies in a necromancer outpost on an outer End island (proposed), taken by the necromancers who killed its king. The war room's burial maps reveal them once the citadel falls. Relics can be used by players and soldiers, have unique powers, and never break.
- **Claiming and owning the citadel:** see **`docs/citadel-claim.md`** (throne 60s sit, credits, barrier allowlist, usurp, wand forfeit). After claim, restore in two steps, in either order: healing the tree and restoring the Hall. Placing a relic on its king's statue consumes it; it stays fixed to the statue for good, and the Crown goes last, on the Corrupted King's statue. The King's Horn has its own vault puzzle (opened by restoring the Tidespear statue) and can only summon kings whose statues are restored.
- **Active necromancers cannot wear any relic.** With only the necromancer mod loaded, simplified relic rules are in **`docs/mod-split.md`**.
    1. **Healing the tree** (kill the Blight Heart, then 7 in-game days of recovery) gives:
        - **The citadel becomes his:** the ender barrier uses an **owner + allies allowlist** (see citadel-claim). Until the tree is healed, the citadel is vulnerable to attack; barrier behavior follows unclaimed vs owned rules in citadel-claim.
        - **The stations wake:** the royal enchanting table, brewing stand, and anvil work with their acolytes.
        - **The Well of Kings:** works for the owner and his allies.
        - **The Phantom Roost:** in the owner's kingdom, overworld phantoms only spawn in the **Roost** — a caged chamber atop the keep with a **phantom spawner**; phantoms cannot escape. The owner farms **phantom membranes** there. Everyone else in the kingdom still gets normal phantom spawns.
        - **A safe road:** hostile mobs can't spawn on or near the great road from spawn to the citadel.
        - **Royal training grounds:** permanent, unbreakable training dummies that only work inside the citadel. They don't give kills. A soldier who practices on them for 3 days becomes royal-trained for good, with +10% damage. For 30 in-game days after training he also never deserts from morale or joins a mutiny, and one more day on the dummies renews it (proposed). Royal-trained soldiers show "Royal" before their label (for example, "Royal Hero" + name), and their post bed has a gold trim.
    2. **Restoring the Hall** (placing all 12 relics, the Crown last) gives:
        - **The kings return:** each great king rises as a spectral guardian as his relic is placed, and defends the citadel forever. Each has 100 HP, 30% damage resistance, Legend-level melee damage, and no knockback (proposed), and fights with the power of his own relic (see the relics table): the Dawnbreaker king burns undead, the Stormcaller king calls lightning, the Long Hunt king shoots from range, the Kingsmaul king stuns, the Oathkeeper king shields his allies, the Last Mantle king survives one lethal hit, and so on. They fight the owner's enemies only and stay inside the citadel; the kings the horn summons are temporary copies.
        - **The King's Horn:** the horn lies in a sealed vault beneath the Hall of the Twelve Kings. It opens when a player uses a water bucket on the feet of the Tidespear king's statue, the same king in every world, but only once the Tidespear relic has been placed on that statue (proposed). This is a special action, not a placed block, so the citadel's building ban doesn't stop it. The Rhyme of the Twelve in the Horn-keeper's refuge chest (a normal book, copyable) holds a riddle that points to the Tidespear king, and his Royal Annals volume confirms it. Water at any other statue's feet does nothing. The horn only works for the citadel's owner. It summons one king, the best match for his main opponent, picked from the kings whose relic is on their statue, which may be only the Tidespear king at first. The king fights beside him anywhere in his kingdom for 1 minute, once per in-game day (proposed). The main opponent is the biggest enemy force within 64 blocks, checked in this order (proposed): ravagers and other heavy mobs call the Kingsmaul king; mostly archers and crossbowmen call the Long Hunt king; undead, minions, and zombie swarms call the Dawnbreaker king; any group of 10 or more calls the Stormcaller king; raiders and bandits call the Oathkeeper king; anything else calls the Tidespear king. If the best match's statue isn't restored, the Tidespear king comes instead. The summoned king only fights hostile mobs, raiders, bandits, and swarms, never players or soldiers. It can't be destroyed, and if it's lost it returns to its vault.
        - **High King:** a title above King, with +4 recruiter slots and +40% odds on rare tax items (proposed).
        - **The shadow lifts:** mod raids worldwide drop below normal (proposed: 20% on new moon night, 8% on the nights either side), and no swarms form anywhere in his kingdom.
        - **A golden age:** villagers in his kingdom breed faster and restock trades more often, and all villager trades there are cheaper for everyone (proposed: 40% cheaper, stacking with vanilla discounts down to vanilla's 1-emerald minimum).
- **The Well of Kings:** a source of unlimited experience inside the citadel. Standing in it gives XP straight to the player, not as orbs, so it can't be bottled or collected by machines (5 XP points per second; about 5 minutes to reach level 30 from zero). Only the citadel's owner and his allies can use it.
- **Relic compass:** an extremely expensive item that points to one relic wherever it is, including carried by a soldier, a bandit, or another player, then breaks once the compass holder picks that relic up. It's made with a special recipe that consumes full stacks: 8 stacks of ender pearls (128), a stack of diamond blocks (64), and the Royal Annals volume of the king whose relic it tracks (proposed).

| Distance from spawn | New moon raid chance | Nights either side | Militia in natural villages |
|---|---|---|---|
| Under 1,000 blocks | 60% | 25% | 4 |
| 1,000 to 3,000 blocks | 50% | 20% | 3 |
| Over 3,000 blocks | 40% (normal) | 15% (normal) | 2 (normal) |

**The 12 relics (proposed):**

| Relic | Type | Power |
|---|---|---|
| Dawnbreaker | Sword | Sets undead on fire and deals extra damage to them |
| Oathkeeper | Shield | With soldier mod: allies within 8 blocks take 20% less damage. Necro-only: holder takes 20% less damage |
| The Long Hunt | Bow | Never uses arrows, and arrows pierce through enemies |
| The Thornheart Circlet | Helmet | Immune to knockback, with night vision, and poisonous plants, corruption, and laced or corrupted food glow within 32 blocks, even through walls (proposed) |
| Stormcaller | Axe | Chance to call lightning on hit; breaks wood twice as fast |
| The Corrupted King's Crown | Helmet | With soldier mod: soldiers of the wearer who die within 32 blocks rise as zombie soldiers (2 minutes, then dust; grumble point). Necro-only: once per day, fatal hit → survive at 1 HP + knockback pulse. Necromancers cannot wear relics |
| The Kingsmaul | Mace | Smashes knock back and stun nearby enemies |
| Tidespear | Trident | Returns when thrown and pulls enemies toward the wielder |
| Ironroot Helm | Helmet | With soldier mod: +5 recruit slots per recruiter, +25% trader tax odds, half the new moon raid chance, and no swarms in his villages (proposed). Necro-only: −25% damage from undead while worn |
| The Last Mantle | Chestplate | Survives a lethal hit once per day |
| Greaves of the Long March | Leggings | Faster movement; wearer never gets hungry (soldier mod: same for soldiers wearing them) |
| Stormstriders | Boots | No fall damage and can walk on water |

**Relic strength:** every relic beats anything players can make, even fully enchanted netherite. Weapons deal more damage, armor protects better, and tools break blocks faster than the best possible netherite equivalent (proposed: about 25% better). Relics can't be enchanted, since their base stats already exceed the enchanted maximum.

**Breaking the seal:** killing the Ender Dragon only weakens the barrier. To open it, a player must also craft the Seal-Breaker and use it on the citadel's gate (proposed recipe):

1. **Ender core:** 8 eyes of ender around a bottle of dragon's breath. Two are needed, so 16 eyes of ender, a full stack of ender pearls.
2. **Nether core:** a nether star surrounded by 4 netherite ingots and 4 crying obsidian.
3. **Seal-Breaker:** both ender cores, the nether core, 4 shulker shells, 2 end crystals, and a dragon head.

The nether star means the Wither has to be beaten too, so opening the citadel takes the Ender Dragon, the Wither, End cities, and a lot of netherite.

**The Dragon's Well:** an old stone well just outside the citadel's gates, beyond the ender barrier, where the necromancers drew the dragon's essence to bind the seal. **Easy to miss** until you know the citadel approach — no map marker; **Wellkeeper's Account** and Annals/chronicle hints are the main way in. **Two roles:** (1) right-click with an **empty bottle** for **dragon's breath** — **no per-player daily cap** (**TBD** whether breath fill requires the Ender Dragon dead in that world; horcrux disposal does **not**); (2) **destroy horcruxes** by **throwing the item into the well** — works **before and after** the dragon is dead (`necromancer-path.md`). Usual hunter finish; ties lich hunts to the **great road / citadel approach**. Seal-Breaker crafting still needs breath bottles (16 eyes in recipe).

**The Wellkeeper's Account:** a lore book on a lectern beside the well. It tells how the seal was bound to the dragon, hints at every Seal-Breaker ingredient, and **TBD** mentions **wild liches** — throw their **horcrux** into the well to unmake it (`necromancy.md`, `necromancer-path.md`). Players can copy it like a Royal Annals volume, and there is no other copy.

**Dragon heads can't be lost:** dragon heads follow the relic rules. They can't be destroyed and never despawn, and one that falls into the void reappears on the last solid ground it touched. A dragon compass points to the nearest dragon head not in a player's inventory, or to the nearest unlooted End ship if there are none. Recipe (proposed): the relic compass recipe at a quarter of the cost (2 stacks of ender pearls, 16 diamond blocks), with a bottle of dragon's breath in place of the Annals volume.

**The Seal-Breaker is a sword (proposed stats):**

- Using it on the gate opens the seal, and the sword isn't used up.
- About 15% stronger than a netherite sword and never breaks. It can hold only one enchantment, so the player has to choose which one.
- Deals double damage to undead, including the citadel's garrison and the Corrupted King, and stays useful against swarms and minions for the rest of the game.
- Breaks the citadel doors five times faster than other tools. Its undead bonus stacks with its enchantment, including Sharpness VII.

## Acolytes

Acolytes are the surviving veterans of Kaelen's Order of the Well, all men, who scattered when the necromancers took the citadel, each hiding something he refused to leave behind. They serve whoever tends the tree: once the citadel is claimed they return to run its royal enchanting table, brewing stand, and anvil, and they steady the soldiers around them.

**Player path:** guild-style class and pre-claim questline — **`docs/acolyte-path.md`**. Does not block throne or necromancy.

- **Refuges:** **15 overworld hideouts** per world (includes **Maelor**), placed from saved world data — between 500 and 4,000 blocks from spawn and at least 700 blocks apart (proposed). **Two other brothers** exist only in the **citadel dungeon** (not overworld refuges). Each hideout is a small, ordinary-looking shelter; necromancers hunt survivors, so most have 0–6 undead nearby. Chest holds what that acolyte saved (`citadel-layout.md`).
- **Who they are:** all 17 are men, veterans of Kaelen's campaigns who belong to the Order of the Well (see the Old kingdom lore part). None alive remembers the earlier kings, only the Annals.
- **What they carry:** each acolyte saved something he refused to leave behind and hid it in a chest in his refuge. Of the 17:
    - 12 refuges each hold one volume of the Royal Annals in the chest, a different king each. These are copies, so the library's originals don't need to be unbreakable.
    - 1 is the Horn-keeper's refuge, with *The Rhyme of the Twelve* in its chest, a rhyme that hides the riddle about opening the King's Horn vault (see the Hall restoration list).
    - 4 have only ordinary loot in the chest. Two of them hold Thornwald's notes, one on the greaves and one on Valen's sword, and one may hold the old kingdom banner (25% chance per world).
- **Relics:** four relics sit in the chests of the refuges that hold those kings' Annals volumes, saved from the treasury in the purge: The Last Mantle (Edmund), the Ironroot Helm (Aldric), Stormstriders (Corwin), and Oathkeeper (Oswin). The rest lie in bandit fortresses and an End outpost (see the Old kingdom lore part). Once the Annals chest is open (see **Annals chests** below), anyone who cleared the undead can take them.
- **Treatises:** six treatises, on base defense, campaigns and supply, night fighting, sieges and sappers, traps and spoiled food, and holding the citadel. Each is placed independently at a random one of the 10 bandit fortresses or the End outpost, so some places hold two and some none. Treatises are normal books that players can take and copy.
- **Chests:** non-Annals refuge chests are ordinary: anyone who clears the undead can take the contents. Books can be copied; relics and the banner can't be destroyed and follow the relic rules.
- **Annals chests (twelve refuges):** while the citadel is **unclaimed**, these chests are **unbreakable and locked** until **Maelor speaks them all open** (acolyte quest) **or** the **first citadel claim** in that world if still locked (`acolyte-path.md`). After open, treat like other chests for anything still inside.
- **Recruiting:** clear the refuge's undead, then talk to the acolyte. Any player can recruit an acolyte whatever his title, since they serve whoever tends the tree. He moves to one of that player's villages with a free bed and an acolyte's lectern (recipe proposed: lectern + book + gold ingot).
- **Homecoming (Order roster — model A):** when a player **claims** the citadel, **only** **Maelor** and acolytes on **Maelor’s reunite roster** (delivered to Maelor’s refuge **before claim** by a player who **locked** specialty pre-claim — `acolyte-path.md`) walk to the citadel, arriving within **3 in-game days** (proposed). Each brings his books and keeps them on a lectern beside him. Relics go to the treasury and the banner to the throne room. Whatever players already took from a chest stays with them; a dead acolyte brings nothing. They serve the new owner but can't use the citadel's stations until the tree is healed.
- **Claim with no roster:** if **no** pre-claim Maelor reunite happened, **no** acolytes auto homecome on claim (including **recruited** acolytes at villages — they stay put until escorted). The citadel starts **without** Order staff until players bring brothers in (below).
- **Citadel escort (any player, after claim or before):** any player who has **cleared** a refuge and **escorts** an acolyte to **citadel bounds** (same follow/handoff rules as Maelor escort — no soldiers within 32 blocks, no necromancer kit — `acolyte-path.md`) registers that brother at the citadel. **Owner** and **allies** need this to fill lecterns; **non-owners** earn **escort titles** (below). **Recruited** acolytes must be escorted from their village bed to the citadel the same way.
- **Escort title (non-owner):** when the **citadel owner** is another player, any escort who **successfully delivers** at least one acolyte to the citadel earns the chat title **Friend of the Order** (persists per player per world — TBD if tier upgrades for more deliveries). **Owner** does not receive this title for escorting their own staff.
- **Order closed after claim:** players cannot **start or finish** the player acolyte questline after claim (`acolyte-path.md`). Post-claim escort is **logistics only** — no specialty lock, no perks.
- **Four specialties (+ Maelor):** **Maelor** is one of the 17 (proposed: Kaelen’s Annals refuge). The other **16** split **four per specialty** at world gen: **Arcane** (royal enchanting lectern), **Alchemist** (royal brewing), **Smith** (royal anvil), **War Leader** (field/campaign — no royal lectern). See `acolyte-path.md` for the player class using the same four names.
- **Health and combat (proposed):**
    - **Brothers** (Arcane, Alchemist, Smith, War Leader): **30 HP** — veteran tier, same max health as a seasoned soldier (`soldiers-and-villages.md`). Light armor (proposed: iron chest + boots). **Default:** follow the player or path to orders (escort, homecoming, recruited village). **Fight if needed** when threatened; they are not pacifists.
    - **War Leaders:** **50 HP** — hero tier, **20% damage resistance**, stronger soldier aura (below). **Only War Leaders** attach to **campaign boots** / camp blocks with Armies; other specialties stay home or escort but do not join campaign deployment.
    - **Maelor:** **30 HP** veteran body; **does not go on campaign** before or after the citadel is his home. Once at the **citadel** (after homecoming), he **stays there** — no campaign boots for Maelor.
- **Soldier aura (all brothers + Maelor at citadel):** soldiers within **16 blocks** take less damage and heal out of combat. **Arcane / Alchemist / Smith / Maelor:** **10%** less damage, **1 heart** every **10** seconds OOC. **War Leader:** **15%** less damage, **1 heart** every **8** seconds OOC. Does not stack with Kingstree, Kingswood, or beacons — **strongest only** per effect type.
- **Escort regen (brothers only):** each acolyte on an escort or traveling together has **constant slow regen**: **½ heart** every **15** seconds (base). For each **other brother** within **8** blocks, **+½ heart** per 15 seconds (stacks with the group — a full escort survives travel much better). Applies to acolytes, not players.
- **Zombie villagers:** an acolyte who **walks up to** a zombie villager (within **2** blocks) **uncures** it over a few seconds from **presence alone** — no golden apple (proposed: same final result as a normal cure). Maelor at the citadel counts; useful after swarms or necromancer raids near villages when Armies is loaded.
- **Maelor’s breadth:** at the citadel, Maelor has the **passive benefits of every specialty** (soldier aura at the Arcane/Alchemist rate unless a War Leader is closer; zombie-villager presence cure; he can **stand in at any royal lectern** if the matching brother is dead — Arcane, Alchemist, or Smith station — proposed). He does not replace War Leader campaign rules.
- **Death:** a dead acolyte is never replaced. His chest stays where it is, so his death loses only the acolyte and his homecoming, not the books, relic, or banner in the chest.
- **Citadel stations:** the **royal enchanting table** (library), **royal brewing stand** (alchemy room), and **royal anvil / forge** (royal armory) are built into the citadel. Unbreakable, not craftable or movable. All three stay **dormant** until the **Kingstree blooms**. Each station needs an **Arcane, Alchemist, or Smith** NPC at its **lectern** beside it (or **Maelor** filling in for a dead brother). **War Leaders** do not man lecterns.
- **Two ways to use a station (players):**
    1. **Ask the acolyte (minor)** — any **citadel owner or ally** requests service at the lectern. The brother runs the station; you skip **coal**, **blaze powder**, and **lapis** (enchant).
    2. **Use the block yourself (major)** — only a player whose **locked specialty matches** that station (**Arcane / Alchemist / Smith**) uses the job block directly. **War Leader** has no royal station (`acolyte-path.md`).
- **Power ladder (design rule):** **pre-tree** specialty perks → **minor (ask)** → **major (specialty at block)** → **library vault books** (Sharp VII / Looting V) → **king relics**. Each step is stronger than the last. **Relics** are unique king weapons/armor: stronger than anything you can enchant, and you **cannot** put enchants on them (`endgame.md` relic table).
- **Who / when (all station tiers):**

    | Tier | Who | When / where |
    | --- | --- | --- |
    | **Pre-tree player perks** | You, after **Maelor locked** your specialty | **Anywhere**, before or after citadel — **not** tied to tree or lectern (`acolyte-path.md`) |
    | **Minor (ask acolyte)** | **Citadel owner or ally** — any specialty, no need to match | **Inside citadel**, **after Kingstree blooms**, matching **NPC at lectern** (or Maelor) |
    | **Major (use block)** | You with **locked Arcane / Alchemist / Smith** (must match station) | Same as minor — citadel, tree bloomed, NPC at lectern; crown rank irrelevant |

- **Fuel / reagents (brew & enchant):** **Ask (minor)** and **major (specialty at block):** **no coal**, **no blaze powder**, **no lapis** on enchant/brew — acolyte station or your specialty waiver. You still bring potion ingredients (nether wart, etc.) and the item or **book** to enchant.

- **Minor — ask acolyte:**
    - **Enchant (who/when):** owner or ally, citadel, tree bloomed, Arcane (or Maelor) at lectern. Works on **gear or enchanted books** — same as a normal table visit. The acolyte **rolls random** enchants; rolls may reach **vanilla max + 1** (e.g. Sharp **VI** when tables normally stop at **V**). You **don’t choose** the enchant. **No lapis.**
    - **Brew (who/when):** owner or ally, citadel, tree bloomed, Alchemist at lectern. **Double duration**; tier I/II only; **no** Resolve / Rallying. **No blaze, no coal.**
    - **Smith (who/when):** owner or ally, citadel, tree bloomed, Smith at lectern. Repairs **40% less** material, **50% less** XP (min **0**).

- **Major — locked specialty at block:**
    - **Enchant / Arcane (who/when):** **you** are **locked Arcane**; citadel; tree bloomed; lectern manned. **Gear or books.** You **pick one** enchant — **no random roll**. Level = **vanilla max + 2** for that enchant (the “extra extra” above minor’s +1 random). **0 XP, 0 lapis.** One chosen enchant per visit per item/book. Examples (Java vanilla max → major pick):

        | Enchant (examples) | Vanilla max | Major may pick |
        | --- | --- | --- |
        | Sharpness, Efficiency, Smite, Bane | V | **VII** |
        | Protection, Power, Feather Falling, blast/fire/proj prot | IV | **VI** |
        | Unbreaking, Fortune, Looting | III | **V** |
        | (Other sensible combat/tool enchants) | (varies) | **vanilla max + 2** |

        **Looting V** and **Sharp VII** **books** in the library **vault** remain unique loot (Seal-Breaker choice, etc.); major can **apply** those levels on gear/books at the table but does **not** grant the vault’s one-time books. Still **below relic** items (no relic enchantment, raw stats beat crafted).

    - **Brew / Alchemist (who/when):** **you** are **locked Alchemist**; citadel; tree bloomed; lectern manned. **No blaze, no coal.** **Strength III, Speed III, Regeneration III**; **3× duration** on anything you brew there. Two **major-only** splash potions (not on ask/minor):
        - **Resolve (splash):** if **Armies** is loaded — **your soldiers** in the splash ignore morale flee, retreat, and desert for **1 in-game day**. If **Armies is not loaded** — morale rules do not exist; instead, **players and citadel allies** in the splash get **Resistance II** and **Regeneration II** for **10 minutes** (citadel / solo endgame sustain).
        - **Rallying Splash:** if **Armies** is loaded — **your soldiers** in the splash heal **10 hearts**. If **Armies is not loaded** — **players and citadel allies** in the splash heal **10 hearts**.
    - **Smith (who/when):** **you** are **locked Smith**; citadel; tree bloomed; lectern manned. (**1**) Diamond → netherite, one ingot, **no** template. (**2**) Royal repairs **70% / 75%** off mat/XP. (**3**) **Once per day:** full repair, **half** material, **0** XP. Pre-tree Smith perks stay on **world** anvils/smithing tables only.

## The Blighted Tree

A great tree in the citadel's courtyard, poisoned by the necromancers. Healing it is the second half of restoring the citadel, alongside the Hall, in either order.

- **The blight:** the tree stands gray and leafless and can't be harmed. Only the citadel's owner can heal it, and only after the Corrupted King falls. Healing takes one step, then time:
    1. **Kill the rot:** a root cavern opens beneath the tree. At its heart, the Blight Heart, a stationary core, keeps raising minions until players destroy it. It glows through stone for anyone wearing the Thornheart Circlet, which helps but isn't required.
    2. **Let it recover:** once the Blight Heart is dead the poison is gone, and the tree heals itself over 7 in-game days (proposed), then blooms. No potion or watering is needed. The citadel's stations wake when it blooms.
- **The Kingstree:** healed, the tree blooms. The owner, his allies, and his soldiers within 48 blocks regenerate slowly, and soldiers there never stumble in the dark.
- **Saplings:** the Kingstree drops one Kingswood sapling at its base every 30 in-game days (proposed), with a chat message to the owner.
- **Kingswood trees:** a sapling grows into a small tree. Within 16 blocks, soldiers heal 1 heart every 10 seconds out of combat and villagers breed 25% faster (proposed). Its leaves never drop saplings and it yields 8-12 logs once, so Kingswood is finite. Anyone can chop a Kingswood tree, which makes them worth raiding.
- **Kingswood uses (proposed):** a Kingswood gate, a castle gate sappers and axemen can't break and players break twice as slowly as an iron gate; a Kingswood bow with 15% more arrow damage, for players and soldiers; and a Kingswood mess station whose meals heal 6 hearts instead of 3.
