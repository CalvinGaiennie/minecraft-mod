"""Site information architecture for marketing + dev HTML export."""

from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parent.parent.parent
DOCS_DIR = REPO_ROOT / "docs"

# Single marketing page → site/index.html
MARKETING_SOURCE = "pages/marketing.md"


@dataclass(frozen=True)
class DevDoc:
    relpath: str  # under docs/
    title: str
    split_h2_min: int = 2  # split into subpages when >= this many ## sections
    url_slug: str | None = None  # output folder under site/dev/{group}/


@dataclass(frozen=True)
class DevGroup:
    id: str
    title: str
    docs: tuple[DevDoc, ...]


DEV_GROUPS: tuple[DevGroup, ...] = (
    DevGroup(
        "start",
        "Start",
        (
            DevDoc("overview.md", "Overview"),
            DevDoc("mod-goals.md", "Mod goals"),
            DevDoc("mod-split.md", "Mod split"),
            DevDoc("integration.md", "Integration"),
            DevDoc("marketing/development-plan.md", "Development plan", split_h2_min=2),
            DevDoc("compatibility-planned-mods.md", "Compatibility targets"),
            DevDoc(
                "marketing/README.md",
                "Marketing site (README)",
                url_slug="marketing-readme",
            ),
        ),
    ),
    DevGroup(
        "notes",
        "Notes",
        (
            DevDoc("notes/2026-10-05-discussion-plan.md", "Discussion plan"),
            DevDoc("notes/2026-10-05-design-notes-intake.md", "Design intake"),
            DevDoc(
                "notes/tbd-garland-fortress-prison-arena.md",
                "Garland fortress & prison (TBD)",
                url_slug="tbd-garland-fortress-prison-arena",
            ),
            DevDoc(
                "notes/garland-nbt-biome-and-terrain.md",
                "Garland NBT, biome & terrain",
                url_slug="garland-nbt-biome-and-terrain",
            ),
            DevDoc(
                "notes/tbd-citadel-biome-placement.md",
                "Citadel biome placement (TBD)",
                url_slug="tbd-citadel-biome-placement",
            ),
        ),
    ),
    DevGroup(
        "citadel",
        "Citadel",
        (
            DevDoc("citadel-mod-plan.md", "Citadel mod plan", split_h2_min=2),
            DevDoc("endgame.md", "Endgame", split_h2_min=2),
            DevDoc("citadel-claim.md", "Citadel claim"),
            DevDoc("citadel-layout.md", "Citadel layout", split_h2_min=2),
            DevDoc("citadel-defenders.md", "Citadel defenders", split_h2_min=2),
        ),
    ),
    DevGroup(
        "order",
        "Order",
        (DevDoc("acolyte-path.md", "Acolyte path", split_h2_min=2),),
    ),
    DevGroup(
        "necromancer",
        "Necromancer",
        (
            DevDoc("necromancer-path.md", "Necromancer path", split_h2_min=2),
            DevDoc("necromancy.md", "Necromancy"),
        ),
    ),
    DevGroup(
        "armies",
        "Armies",
        (
            DevDoc("armies-questline.md", "Armies storyline"),
            DevDoc("soldiers-and-villages.md", "Soldiers & villages", split_h2_min=2),
            DevDoc("war-and-defense.md", "War & defense", split_h2_min=2),
            DevDoc("threats-and-mobs.md", "Threats & mobs", split_h2_min=2),
            DevDoc("armies-manual-test-plan.md", "Armies manual test plan", split_h2_min=2),
            DevDoc(
                "bandit-camp-prefabs.md",
                "Bandit camp prefabs",
                url_slug="bandit-camp-prefabs",
            ),
        ),
    ),
    DevGroup(
        "build",
        "Build",
        (
            DevDoc("config-and-recipes.md", "Config & recipes"),
            DevDoc("master-build-note.md", "Master build note", split_h2_min=2),
            DevDoc("stages-1-4-completion.md", "Stages 1–4"),
            DevDoc("stages-5-6-completion.md", "Stages 5–6"),
            DevDoc("stages-1-3-tests.md", "Stages 1–3 tests"),
            DevDoc("stage-1-tests.md", "Stage 1 tests"),
        ),
    ),
    DevGroup(
        "lore",
        "Lore",
        (DevDoc("old-kingdom-lore.md", "Old kingdom lore", split_h2_min=2),),
    ),
)

# Short lede on dev group hub pages (site/dev/{id}/index.html).
GROUP_BLURBS: dict[str, str] = {
    "start": "Mod goals, split, integration, and the development plan.",
    "notes": "Dated discussion plans and design intake notes.",
    "citadel": "Endgame throne, citadel claim, layout, and defenders.",
    "order": "Order of the Well: player acolyte questline and specialty tracks.",
    "necromancer": (
        "Player necromancer questline (commit vs opt out), casual wand play, "
        "horcruxes, flutes, and how the path relates to the Order."
    ),
    "armies": "Kingdom questline, soldiers, villages, war, defense, and threats.",
    "build": "Implementation notes for builders and mod structure.",
    "lore": "Old kingdom background and narrative canon.",
}


def doc_slug(relpath: str) -> str:
    name = Path(relpath).name
    if name.endswith(".md"):
        name = name[:-3]
    return name.replace("_", "-")


def dev_doc_url_slug(doc: DevDoc) -> str:
    return doc.url_slug if doc.url_slug else doc_slug(doc.relpath)
