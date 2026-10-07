#!/usr/bin/env python3
"""Generate blockstates, block models, and item models using vanilla Minecraft art."""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "src/main/resources/assets/villagers"

# block_id -> minecraft texture for cube_all (block/block)
BLOCKS = {
    "mess_station": "minecraft:block/barrel_top",
    "post_block": "minecraft:block/stone_bricks",
    "rampart": "minecraft:block/stone_bricks",
    "village_marker": "minecraft:block/lodestone_top",
    "fallback_block": "minecraft:block/yellow_wool",
    "tax_box": "minecraft:block/oak_planks",
    "arrow_bin": "minecraft:block/target_top",
    "recruiter_box": "minecraft:block/iron_block",
    "supply_depot": "minecraft:block/copper_block",
    "haven_block": "minecraft:block/blue_wool",
    "royal_guard_post": "minecraft:block/polished_blackstone_bricks",
    "camp_block": "minecraft:block/white_wool",
    "caltrop": "minecraft:block/iron_block",
    "bandit_camp": "minecraft:block/red_wool",
    "necromancer_crypt": "minecraft:block/deepslate_bricks",
    "wild_ranch": "minecraft:block/hay_block_side",
    "farmer_station": "minecraft:block/composter_side",
    "mob_spawner_cap": "minecraft:block/spawner",
    "training_dummy": "minecraft:block/hay_block_top",
    "grave_marker": "minecraft:block/crying_obsidian",
    "cursed_effigy": "minecraft:block/soul_sand",
}

# item_id -> (parent, texture) for item/models
ITEMS = {
    "mess_station": ("minecraft:item/barrel", None),
    "post_block": ("minecraft:item/stone_bricks", None),
    "rampart": ("minecraft:item/stone_bricks", None),
    "village_marker": ("minecraft:item/lodestone", None),
    "fallback_block": ("minecraft:item/yellow_wool", None),
    "tax_box": ("minecraft:item/oak_planks", None),
    "arrow_bin": ("minecraft:item/target", None),
    "recruiter_box": ("minecraft:item/iron_ingot", None),
    "supply_depot": ("minecraft:item/copper_ingot", None),
    "haven_block": ("minecraft:item/blue_wool", None),
    "royal_guard_post": ("minecraft:item/polished_blackstone", None),
    "camp_block": ("minecraft:item/white_wool", None),
    "caltrops": ("villagers:block/caltrop", None),
    "bandit_camp": ("minecraft:item/red_wool", None),
    "necromancer_crypt": ("minecraft:item/deepslate_bricks", None),
    "wild_ranch": ("minecraft:item/hay_block", None),
    "farmer_station": ("minecraft:item/composter", None),
    "mob_spawner_cap": ("minecraft:item/iron_bars", None),
    "training_dummy": ("minecraft:item/hay_block", None),
    "grave_marker": ("minecraft:item/crying_obsidian", None),
    "cursed_effigy": ("minecraft:item/soul_sand", None),
    "veteran_sword": ("minecraft:item/iron_sword", None),
    "nightwatch_helmet": ("minecraft:item/iron_helmet", None),
    "royal_guard_chestplate": ("minecraft:item/iron_chestplate", None),
    "campaign_boots": ("minecraft:item/iron_boots", None),
    "reassignment_writ": ("minecraft:item/paper", None),
    "village_banner": ("minecraft:item/white_banner", None),
    "awaken_horn": ("minecraft:item/goat_horn", None),
    "muster_roll": ("minecraft:item/book", None),
    "necromancer_wand": ("minecraft:item/blaze_rod", None),
    "phylactery_shard": ("minecraft:item/amethyst_shard", None),
    "necromancer_hood": ("minecraft:item/leather_helmet", None),
    "necromancer_robe": ("minecraft:item/leather_chestplate", None),
    "soul_bottle": ("minecraft:item/experience_bottle", None),
    "village_chronicle": ("minecraft:item/writable_book", None),
    "village_map": ("minecraft:item/map", None),
    "grave_shroud": ("minecraft:item/chainmail_helmet", None),
    "bandit_ledger": ("minecraft:item/writable_book", None),
    "torn_map_half": ("minecraft:item/map", None),
    "garland_signet": ("minecraft:item/gold_nugget", None),
    "garland_quest_compass": ("minecraft:item/compass_16", None),
}


def write(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8")


def block_model(tex: str) -> str:
    return (
        '{\n  "parent": "minecraft:block/cube_all",\n  "textures": {\n    '
        f'"all": "{tex}"\n  }}\n'
    )


def main() -> None:
    for bid, tex in BLOCKS.items():
        write(ROOT / f"blockstates/{bid}.json", '{\n  "variants": {\n    "": { "model": "villagers:block/' + bid + '" }\n  }\n}\n')
        write(ROOT / f"models/block/{bid}.json", block_model(tex))
        if bid not in ("caltrop",):
            parent, _ = ITEMS.get(bid, (f"villagers:block/{bid}", None))
            if parent.startswith("villagers:"):
                write(ROOT / f"models/item/{bid}.json", f'{{"parent": "{parent}"}}\n')
            else:
                write(ROOT / f"models/item/{bid}.json", f'{{"parent": "{parent}"}}\n')

    for iid, (parent, _) in ITEMS.items():
        if iid in BLOCKS:
            continue
        write(ROOT / f"models/item/{iid}.json", f'{{"parent": "{parent}"}}\n')

    print(f"Wrote assets under {ROOT}")


if __name__ == "__main__":
    main()
