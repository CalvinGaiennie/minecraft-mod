# Planned modpack compatibility (1.21.1 NeoForge)

**Short answer:** Yes — **compatibility is a design goal**, not a guarantee on day one. The project **targets 1.21.1 NeoForge** specifically so it can sit beside **Create** and **Ice and Fire: Community Edition** in modpacks (`overview.md`, `master-build-note.md`). There is **no hard dependency** on any of these mods.

**How we stay compatible**

- **Optional integration only** — no `@Mod` dependency; soft checks via `ModList` / events (`integration.md`).
- **Mixins disabled** in the current template (reduces cross-mod bytecode conflicts); villager work uses attachments + events first.
- **Soldiers = vanilla villagers** — not a custom mob type that fights other mods’ entity registries.
- **Enlisted-player rule** — mod raids/swarm rolls can skip players who opt out, so packs aren’t forced into our threat loop.
- **Cut siege MVP** — no custom ladder/sapper assault AI that would fight **Medieval Siege Machines** for the same job.
- **Separate test instance** — master build note: test our JAR with Create + IAF CE before claiming “modpack ready.”

**Verdict by mod**

| Mod | 1.21.1 NeoForge | Compatibility expectation | Main risks to test |
|-----|-----------------|---------------------------|-------------------|
| [Create](https://modrinth.com/mod/create) | Yes (official) | **High** — platform choice was partly for Create modpacks | Village protection vs contraption breaking; claim borders vs machines |
| Ice and Fire: Community Edition | Yes (project target; use pack’s CE build) | **Medium–high** — dragons/endgame parallel to Citadel, not merged | Soldier targeting on IAF mobs; structures vs citadel/refuge placement; dragon vs custom raids |
| [Epic Knights: Shields, Armor and Weapons](https://modrinth.com/mod/epic-knights-shields-armor-and-weapons) | Yes | **High** — player gear; soldiers keep their own kits | Low technical conflict; balance is gameplay |
| [Small Ships](https://modrinth.com/mod/small-ships) | Yes | **High** — naval layer orthogonal to village war | Low; our boat-assault content is cut from scope |
| [Medieval Siege Machines](https://modrinth.com/mod/medieval-siege-machines) (catapult/trebuchet/etc.) | Yes | **Medium** — player siege vs village claims | Explosions/projectiles vs village protection; `mobGriefing` |
| [Corpse](https://modrinth.com/mod/corpse) | Yes (1.21.1 NeoForge builds) | **High** — acolyte questline | Player/acolyte death + loot retention; quest items must not soft-lock (`acolyte-path.md`) |

**What we will *not* claim without testing**

- “Officially compatible” or CurseForge “compatible with X” badges until a **fixed mod list + version pins** pass smoke tests (load, 30 min SP, claim village, citadel flag, Create contraption in claim, IAF mob near soldiers).
- Perfect AI with every mod that replaces villager brains (if we add Mixins later, retest all of the above).

**Marketing language (safe)**

- “Built for **1.21.1 NeoForge** modpacks.”
- “**Play-tested target:** Create, Ice and Fire: Community Edition, Epic Knights, Small Ships, Medieval Siege Machines.”
- “**No required mods** — works alone; other mods enhance the sandbox.”

**Ice and Fire CE link:** use the exact file your pack pins (often CurseForge **Ice and Fire: Community Edition** by the community port team). Modrinth mirrors vary; verify `game_versions` includes **1.21.1** and **NeoForge** on the download you ship.
