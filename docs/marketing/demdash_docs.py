#!/usr/bin/env python3
"""Replace U+2014 dash characters in canon markdown with plain punctuation."""

from __future__ import annotations

import re
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
DOCS = REPO / "docs"


def demdash(text: str) -> str:
    # Table placeholders (not prose)
    text = re.sub(r"\|\s*—\s*\|", "| - |", text)
    # Bold label then em dash (lists, table cells)
    text = re.sub(r"(\*\*[^*]+\*\*)\s*—\s*", r"\1: ", text)
    # Remaining em dashes (with or without spaces)
    text = re.sub(r"\s*—\s*", ", ", text)
    text = text.replace("—", ", ")
    # Cleanup
    text = re.sub(r",\s*,+", ", ", text)
    text = re.sub(r"\s+,", ",", text)
    text = re.sub(r",\s+\.", ".", text)
    return text


def main() -> None:
    paths = list(DOCS.rglob("*.md"))
    paths.append(REPO / "site" / "README.md")
    changed = 0
    for path in paths:
        if not path.is_file():
            continue
        old = path.read_text(encoding="utf-8")
        new = demdash(old)
        if new != old:
            path.write_text(new, encoding="utf-8")
            changed += 1
            print(path.relative_to(REPO))
    print(f"Updated {changed} files")


if __name__ == "__main__":
    main()
