# Bandit structure NBT (authoring)

Drop structure block exports here, then tell the agent the filename + site kind.

| File (suggested name) | Site kind | Notes |
| --- | --- | --- |
| `camp_generic.nbt` | camp + citadel shadow | Marker at anchor |
| `hideout_cave.nbt` | hideout | Built for cave floor |
| **`corvin_tower.nbt`** | **corvin** | **Tower** on village anchor (priority) |
| **`garland_*.nbt`** + **`garland-manifest.json`** (**TBD**) | **garland** | **Multi-tile** site (mountains + fortress); 48³ per Structure Block save — see `docs/notes/garland-nbt-biome-and-terrain.md` |

In-game: Structure Block → Save. Exports often land under  
`run/saves/<world>/generated/minecraft/structures/`  
Copy the `.nbt` here and commit or @-mention in chat.

Put the **bandit_camp** marker block at the template origin (where the code places the anchor).

**Garland fortress + biome/terrain:** read `docs/notes/garland-nbt-biome-and-terrain.md` before a big sculpt — NBT stores **blocks you saved**, not the world’s biome; bake cliffs in the box or we lock spawn biome in code.
