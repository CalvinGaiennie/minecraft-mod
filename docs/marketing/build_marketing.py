#!/usr/bin/env python3
"""Build marketing index.html and development-plan.html from templates + development-plan.md."""

from __future__ import annotations

import html
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent
REPO_ROOT = ROOT.parent.parent
SITE_DIR = REPO_ROOT / "site"
INDEX_TEMPLATE = ROOT / "index.template.html"
DEV_TEMPLATE = ROOT / "development.template.html"
PLAN_MD = ROOT / "development-plan.md"
INDEX_OUT = SITE_DIR / "index.html"
DEV_OUT = SITE_DIR / "development-plan.html"
MARKER = "<!-- DEVELOPMENT_PLAN -->"


def slugify(title: str) -> str:
    s = title.strip().lower()
    s = re.sub(r"\{#([a-z0-9-]+)\}\s*$", "", s).strip()
    s = re.sub(r"[^a-z0-9]+", "-", s).strip("-")
    return s or "section"


def inline_md(text: str) -> str:
    text = html.escape(text)
    text = re.sub(
        r"\[([^\]]+)\]\(([^)]+)\)",
        lambda m: f'<a href="{html.escape(m.group(2), quote=True)}">{m.group(1)}</a>',
        text,
    )
    text = re.sub(r"`([^`]+)`", r"<code>\1</code>", text)
    text = re.sub(r"\*\*([^*]+)\*\*", r"<strong>\1</strong>", text)
    text = re.sub(r"\*([^*]+)\*", r"<em>\1</em>", text)
    return text


def parse_table(lines: list[str]) -> str:
    if len(lines) < 2:
        return ""
    header = [inline_md(c.strip()) for c in lines[0].strip("|").split("|")]
    rows = []
    for line in lines[2:]:
        if not line.strip().startswith("|"):
            break
        cells = [inline_md(c.strip()) for c in line.strip("|").split("|")]
        rows.append(cells)
    thead = "<tr>" + "".join(f"<th>{c}</th>" for c in header) + "</tr>"
    tbody = ""
    for row in rows:
        tbody += "<tr>" + "".join(f"<td>{c}</td>" for c in row) + "</tr>"
    return f"<table><thead>{thead}</thead><tbody>{tbody}</tbody></table>"


def block_to_html(block: str, box_class: str | None) -> str:
    lines = block.strip().splitlines()
    out: list[str] = []
    i = 0
    if box_class:
        out.append(f'<div class="box {box_class}">')

    while i < len(lines):
        line = lines[i]
        if line.startswith("### "):
            title = line[4:].strip()
            out.append(f"<h3>{inline_md(title)}</h3>")
            i += 1
            continue
        if line.startswith("|"):
            table_lines = []
            while i < len(lines) and lines[i].strip().startswith("|"):
                table_lines.append(lines[i])
                i += 1
            out.append(parse_table(table_lines))
            continue
        if re.match(r"^\d+\.\s", line):
            out.append("<ol>")
            while i < len(lines) and re.match(r"^\d+\.\s", lines[i]):
                item = re.sub(r"^\d+\.\s+", "", lines[i])
                out.append(f"<li>{inline_md(item)}</li>")
                i += 1
            out.append("</ol>")
            continue
        if line.startswith("- "):
            out.append("<ul>")
            while i < len(lines) and lines[i].startswith("- "):
                out.append(f"<li>{inline_md(lines[i][2:])}</li>")
                i += 1
            out.append("</ul>")
            continue
        if line.strip():
            para: list[str] = []
            while i < len(lines) and lines[i].strip() and not lines[i].startswith(
                ("### ", "- ", "|")
            ) and not re.match(r"^\d+\.\s", lines[i]):
                para.append(lines[i].strip())
                i += 1
            out.append(f"<p>{inline_md(' '.join(para))}</p>")
            continue
        i += 1

    if box_class:
        out.append("</div>")
    return "\n".join(out)


def render_mermaid_block(code: str) -> str:
    # Author-controlled markdown only — mermaid needs raw `<br/>` etc. in labels.
    return f'<div class="mermaid-wrap"><div class="mermaid">\n{code.strip()}\n</div></div>'


def render_section_body(body: str) -> str:
    lines = body.splitlines()
    out: list[str] = []
    i = 0
    while i < len(lines):
        line = lines[i]
        if line.strip() == "```mermaid":
            i += 1
            mermaid_lines: list[str] = []
            while i < len(lines) and lines[i].strip() != "```":
                mermaid_lines.append(lines[i])
                i += 1
            if i < len(lines) and lines[i].strip() == "```":
                i += 1
            out.append(render_mermaid_block("\n".join(mermaid_lines)))
            continue
        if line.startswith("::: "):
            spec = line[4:].strip()
            i += 1
            inner: list[str] = []
            while i < len(lines) and lines[i].strip() != ":::":
                inner.append(lines[i])
                i += 1
            if i < len(lines) and lines[i].strip() == ":::":
                i += 1
            if spec == "two":
                chunks = re.split(r"\n(?=### )", "\n".join(inner).strip())
                out.append('<div class="two">')
                for chunk in chunks:
                    if not chunk.strip():
                        continue
                    out.append('<div class="box">')
                    out.append(block_to_html(chunk, None))
                    out.append("</div>")
                out.append("</div>")
            else:
                out.append(block_to_html("\n".join(inner), spec))
            continue
        if line.startswith("### ") or line.strip():
            chunk_lines: list[str] = []
            while i < len(lines) and not lines[i].startswith("::: "):
                if lines[i].strip() == "```mermaid":
                    break
                chunk_lines.append(lines[i])
                i += 1
            if chunk_lines:
                out.append(block_to_html("\n".join(chunk_lines), None))
            continue
        i += 1
    return "\n".join(out)


def render_plan_md(source: str) -> str:
    source = re.sub(r"<!--.*?-->", "", source, flags=re.DOTALL)
    parts: list[str] = ['<main class="dev-body">']

    sections = re.split(r"\n(?=## )", source.strip())
    for section in sections:
        if not section.strip():
            continue
        if not section.startswith("## "):
            continue
        first, _, body = section.partition("\n")
        raw = first[3:].strip()
        sid = slugify(raw)
        m = re.search(r"\{#([a-z0-9-]+)\}", raw)
        if m:
            sid = m.group(1)
            raw = re.sub(r"\s*\{#[^}]+\}\s*", "", raw)
        parts.append(f'<h2 id="{sid}">{inline_md(raw)}</h2>')
        parts.append(render_section_body(body))

    parts.append("</main>")
    return "\n".join(parts)


def main() -> int:
    if not INDEX_TEMPLATE.is_file():
        print(f"Missing {INDEX_TEMPLATE}", file=sys.stderr)
        return 1
    if not DEV_TEMPLATE.is_file():
        print(f"Missing {DEV_TEMPLATE}", file=sys.stderr)
        return 1
    if not PLAN_MD.is_file():
        print(f"Missing {PLAN_MD}", file=sys.stderr)
        return 1

    SITE_DIR.mkdir(parents=True, exist_ok=True)
    INDEX_OUT.write_text(INDEX_TEMPLATE.read_text(encoding="utf-8"), encoding="utf-8")
    print(f"Wrote {INDEX_OUT}")

    dev_template = DEV_TEMPLATE.read_text(encoding="utf-8")
    if MARKER not in dev_template:
        print(f"Marker {MARKER} not found in dev template", file=sys.stderr)
        return 1
    plan_html = render_plan_md(PLAN_MD.read_text(encoding="utf-8"))
    DEV_OUT.write_text(dev_template.replace(MARKER, plan_html), encoding="utf-8")
    print(f"Wrote {DEV_OUT}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
