#!/usr/bin/env python3
"""Build static marketing + full dev doc site from markdown."""

from __future__ import annotations

import html
import re
import shutil
import sys
from dataclasses import dataclass
from pathlib import Path

from site_structure import (
    DEV_GROUPS,
    DOCS_DIR,
    MARKETING_PAGES,
    REPO_ROOT,
    doc_slug,
)

ROOT = Path(__file__).resolve().parent
SITE_DIR = REPO_ROOT / "site"
ASSETS_DIR = SITE_DIR / "assets"
LAYOUT = ROOT / "layout.template.html"
CSS_SRC = ROOT / "site.css"
MARKETING_SRC = ROOT / "pages"


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


def render_mermaid_block(code: str) -> str:
    return f'<div class="mermaid-wrap"><div class="mermaid">\n{code.strip()}\n</div></div>'


def block_to_html(block: str, box_class: str | None) -> str:
    lines = block.strip().splitlines()
    out: list[str] = []
    i = 0
    if box_class:
        out.append(f'<div class="box {box_class}">')

    while i < len(lines):
        line = lines[i]
        if line.startswith("#### "):
            out.append(f"<h4>{inline_md(line[5:])}</h4>")
            i += 1
            continue
        if line.startswith("### "):
            out.append(f"<h3>{inline_md(line[4:])}</h3>")
            i += 1
            continue
        if line.startswith("## "):
            out.append(f"<h2 id=\"{slugify(line[3:])}\">{inline_md(line[3:])}</h2>")
            i += 1
            continue
        if line.startswith("# "):
            out.append(f"<h1>{inline_md(line[2:])}</h1>")
            i += 1
            continue
        if line.strip() == "```mermaid":
            i += 1
            mermaid_lines: list[str] = []
            while i < len(lines) and lines[i].strip() != "```":
                mermaid_lines.append(lines[i])
                i += 1
            if i < len(lines):
                i += 1
            out.append(render_mermaid_block("\n".join(mermaid_lines)))
            continue
        if line.strip().startswith("```"):
            lang = line.strip()[3:].strip()
            i += 1
            code_lines: list[str] = []
            while i < len(lines) and not lines[i].strip().startswith("```"):
                code_lines.append(lines[i])
                i += 1
            if i < len(lines):
                i += 1
            code = html.escape("\n".join(code_lines))
            cls = f' class="language-{html.escape(lang)}"' if lang else ""
            out.append(f"<pre><code{cls}>{code}</code></pre>")
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
        if line.startswith(">"):
            quote: list[str] = []
            while i < len(lines) and lines[i].startswith(">"):
                quote.append(lines[i].lstrip("> ").strip())
                i += 1
            out.append(f"<blockquote><p>{inline_md(' '.join(quote))}</p></blockquote>")
            continue
        if line.strip():
            para: list[str] = []
            while i < len(lines) and lines[i].strip() and not lines[i].startswith(
                ("#", "- ", "|", ">", "```")
            ) and not re.match(r"^\d+\.\s", lines[i]):
                para.append(lines[i].strip())
                i += 1
            out.append(f"<p>{inline_md(' '.join(para))}</p>")
            continue
        i += 1

    if box_class:
        out.append("</div>")
    return "\n".join(out)


def render_markdown(source: str, *, skip_leading_h1: bool = False) -> str:
    source = re.sub(r"<!--.*?-->", "", source, flags=re.DOTALL)
    lines = source.splitlines()
    out: list[str] = []
    i = 0
    skipped_h1 = False
    while i < len(lines):
        line = lines[i]
        if line.startswith("::: "):
            spec = line[4:].strip()
            i += 1
            inner: list[str] = []
            while i < len(lines) and lines[i].strip() != ":::":
                inner.append(lines[i])
                i += 1
            if i < len(lines):
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
        if line.startswith("# ") and skip_leading_h1 and not skipped_h1:
            skipped_h1 = True
            i += 1
            continue
        if line.strip() == "```mermaid":
            i += 1
            mermaid_lines = []
            while i < len(lines) and lines[i].strip() != "```":
                mermaid_lines.append(lines[i])
                i += 1
            if i < len(lines):
                i += 1
            out.append(render_mermaid_block("\n".join(mermaid_lines)))
            continue
        chunk_lines: list[str] = []
        while i < len(lines) and not lines[i].startswith("::: "):
            if lines[i].strip() == "```mermaid":
                break
            chunk_lines.append(lines[i])
            i += 1
        if chunk_lines:
            out.append(block_to_html("\n".join(chunk_lines), None))
    return "\n".join(out)


@dataclass
class DocSection:
    sid: str
    title: str
    body: str


@dataclass
class ParsedDoc:
    title: str
    intro: str
    sections: list[DocSection]


def parse_document(source: str) -> ParsedDoc:
    source = re.sub(r"<!--.*?-->", "", source, flags=re.DOTALL).strip()
    lines = source.splitlines()
    title = "Document"
    intro_lines: list[str] = []
    sections: list[DocSection] = []
    current_title: str | None = None
    current_body: list[str] = []
    i = 0
    while i < len(lines):
        line = lines[i]
        if line.startswith("# ") and title == "Document":
            title = line[2:].strip()
            i += 1
            continue
        if line.startswith("## "):
            if current_title is not None:
                body = "\n".join(current_body).strip()
                sections.append(DocSection(slugify(current_title), current_title, body))
            elif intro_lines or current_body:
                intro_lines.extend(current_body)
            current_title = re.sub(r"\s*\{#[^}]+\}\s*", "", line[3:]).strip()
            current_body = []
            i += 1
            continue
        if current_title is None:
            intro_lines.append(line)
        else:
            current_body.append(line)
        i += 1
    if current_title is not None:
        sections.append(
            DocSection(slugify(current_title), current_title, "\n".join(current_body).strip())
        )
    intro = "\n".join(intro_lines).strip()
    return ParsedDoc(title=title, intro=intro, sections=sections)


def rel_path(from_file: Path, to_file: Path) -> str:
    import os

    return os.path.relpath(to_file, from_file.parent).replace("\\", "/")


def h3_sidebar_items(body: str) -> list[tuple[str, str]]:
    items: list[tuple[str, str]] = []
    for line in body.splitlines():
        if line.startswith("### "):
            t = line[4:].strip()
            items.append((slugify(t), t))
    return items


@dataclass
class PageSpec:
    out_path: Path
    page_title: str
    scope: str  # marketing | dev
    main_active: str
    sub_nav_html: str
    sidebar_html: str
    content_html: str
    footer_html: str
    needs_mermaid: bool


def build_sub_nav_marketing(active_slug: str, page_path: Path) -> str:
    links = []
    for slug, label, _ in MARKETING_PAGES:
        if slug == "home":
            href = rel_path(page_path, SITE_DIR / "index.html")
        else:
            href = rel_path(page_path, SITE_DIR / "marketing" / f"{slug}.html")
        cls = "active"
        extra = ""
        if slug == "armies":
            extra = " armies"
        elif slug == "citadel":
            extra = " citadel"
        ac = f' class="{extra.strip()} active"' if slug == active_slug else (
            f' class="{extra.strip()}"' if extra else ""
        )
        links.append(f'<a href="{href}"{ac}>{label}</a>')
    return f'<nav class="sub-nav scope-marketing" aria-label="Marketing">{"".join(links)}</nav>'


def build_sub_nav_dev(active_group: str, page_path: Path) -> str:
    links = []
    for group in DEV_GROUPS:
        href = rel_path(page_path, SITE_DIR / "dev" / group.id / "index.html")
        ac = ' class="active"' if group.id == active_group else ""
        links.append(f'<a href="{href}"{ac}>{group.title}</a>')
    return f'<nav class="sub-nav scope-dev" aria-label="Dev sections">{"".join(links)}</nav>'


def render_layout(spec: PageSpec) -> str:
    layout = LAYOUT.read_text(encoding="utf-8")
    styles = rel_path(spec.out_path, ASSETS_DIR / "site.css")
    root = rel_path(spec.out_path, SITE_DIR / "index.html")
    mkt = rel_path(spec.out_path, SITE_DIR / "index.html")
    dev = rel_path(spec.out_path, SITE_DIR / "dev" / "index.html")
    mermaid_head = ""
    mermaid_script = ""
    if spec.needs_mermaid:
        mermaid_script = """  <script type="module">
    import mermaid from "https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs";
    mermaid.initialize({ startOnLoad: true, theme: "neutral", securityLevel: "loose" });
  </script>"""
    return (
        layout.replace("{{PAGE_TITLE}}", html.escape(spec.page_title))
        .replace("{{STYLES_HREF}}", styles)
        .replace("{{ROOT_HREF}}", root)
        .replace("{{MARKETING_HOME_HREF}}", mkt)
        .replace("{{DEV_HOME_HREF}}", dev)
        .replace(
            "{{MAIN_MARKETING_ACTIVE}}",
            "active" if spec.main_active == "marketing" else "",
        )
        .replace("{{MAIN_DEV_ACTIVE}}", "active" if spec.main_active == "dev" else "")
        .replace("{{SUB_NAV}}", spec.sub_nav_html)
        .replace("{{SIDEBAR}}", spec.sidebar_html)
        .replace("{{CONTENT}}", spec.content_html)
        .replace("{{FOOTER}}", spec.footer_html)
        .replace("{{BODY_CLASS}}", f"scope-{spec.scope}")
        .replace(
            "{{DEV_TAG}}",
            '<span class="dev-tag">· design canon</span>'
            if spec.scope == "dev"
            else "",
        )
        .replace("{{MERMAID_HEAD}}", mermaid_head)
        .replace("{{MERMAID_SCRIPT}}", mermaid_script)
    )


def sidebar_html(
    page_path: Path,
    doc_pages: list[tuple[str, str, Path]],
    current_sid: str,
    h3_items: list[tuple[str, str]] | None,
) -> str:
    if not doc_pages and not h3_items:
        return ""
    parts = ['<aside class="doc-sidebar" aria-label="On this page">']
    if doc_pages:
        parts.append('<p class="sidebar-label">Sections</p><ul>')
        for sid, title, path in doc_pages:
            href = rel_path(page_path, path)
            ac = ' class="active"' if sid == current_sid else ""
            parts.append(f'<li><a href="{href}"{ac}>{html.escape(title)}</a></li>')
        parts.append("</ul>")
    if h3_items:
        parts.append('<p class="sidebar-label">Subsections</p><ul>')
        for sid, title in h3_items:
            parts.append(
                f'<li><a href="#{html.escape(sid)}">{html.escape(title)}</a></li>'
            )
        parts.append("</ul>")
    parts.append("</aside>")
    shell = '<div class="page-shell has-sidebar">'
    return shell + "".join(parts)


def write_page(spec: PageSpec) -> None:
    spec.out_path.parent.mkdir(parents=True, exist_ok=True)
    body = render_layout(spec)
    if spec.sidebar_html.startswith('<div class="page-shell has-sidebar">'):
        aside = spec.sidebar_html.replace('<div class="page-shell has-sidebar">', "", 1)
        body = body.replace(
            '<div class="page-shell">',
            f'<div class="page-shell has-sidebar">{aside}',
            1,
        )
    spec.out_path.write_text(body, encoding="utf-8")


def build_marketing() -> None:
    for slug, label, relpath in MARKETING_PAGES:
        md = (MARKETING_SRC / Path(relpath).name).read_text(encoding="utf-8")
        parsed = parse_document(md)
        if slug == "home":
            out = SITE_DIR / "index.html"
        else:
            out = SITE_DIR / "marketing" / f"{slug}.html"
        content = render_markdown(md, skip_leading_h1=True)
        if slug == "home":
            content = f"<h1>{html.escape(parsed.title)}</h1>\n{content}"
        else:
            content = f"<h1>{html.escape(parsed.title)}</h1>\n{content}"
        spec = PageSpec(
            out_path=out,
            page_title=parsed.title,
            scope="marketing",
            main_active="marketing",
            sub_nav_html=build_sub_nav_marketing(slug, out),
            sidebar_html="",
            content_html=content,
            footer_html=(
                "Minecraft Kingdom: Armies & Citadel · "
                '<a href="' + rel_path(out, SITE_DIR / "dev" / "index.html") + '">Dev docs</a> · WIP'
            ),
            needs_mermaid=False,
        )
        write_page(spec)
        print(f"Wrote {out}")


def section_toc_html(sections: list[DocSection], base_path: Path, page_path: Path) -> str:
    if not sections:
        return ""
    items = []
    for sec in sections:
        href = rel_path(page_path, base_path / f"{sec.sid}.html")
        items.append(f'<li><a href="{href}">{html.escape(sec.title)}</a></li>')
    return (
        '<div class="section-toc"><h2>In this document</h2><ul>'
        + "".join(items)
        + "</ul></div>"
    )


def build_dev_doc(group_id: str, doc: "DevDoc") -> list[Path]:
    from site_structure import DevDoc  # noqa: F811

    src = DOCS_DIR / doc.relpath
    text = src.read_text(encoding="utf-8")
    parsed = parse_document(text)
    dslug = doc_slug(doc.relpath)
    base_dir = SITE_DIR / "dev" / group_id / dslug
    written: list[Path] = []

    split = len(parsed.sections) >= doc.split_h2_min
    if not split:
        out = base_dir / "index.html"
        body = render_markdown(text, skip_leading_h1=True)
        h3s = h3_sidebar_items(text)
        content = (
            f'<p class="source">Source: <code>docs/{html.escape(doc.relpath)}</code></p>'
            f"<h1>{html.escape(parsed.title)}</h1>\n{body}"
        )
        sidebar = sidebar_html(out, [], "index", h3s if len(h3s) >= 4 else None)
        spec = PageSpec(
            out_path=out,
            page_title=parsed.title,
            scope="dev",
            main_active="dev",
            sub_nav_html=build_sub_nav_dev(group_id, out),
            sidebar_html=sidebar,
            content_html=content,
            footer_html=f'Source: <code>docs/{html.escape(doc.relpath)}</code>',
            needs_mermaid="```mermaid" in text,
        )
        write_page(spec)
        written.append(out)
        print(f"Wrote {out}")
        return written

    pages_meta: list[tuple[str, str, Path]] = []
    index_out = base_dir / "index.html"
    pages_meta.append(("index", "Overview", index_out))

    intro_html = render_markdown(parsed.intro) if parsed.intro else ""
    index_content = (
        f'<p class="source">Source: <code>docs/{html.escape(doc.relpath)}</code></p>'
        f"<h1>{html.escape(parsed.title)}</h1>\n"
        f"{intro_html}\n"
        f"{section_toc_html(parsed.sections, base_dir, index_out)}"
    )
    sidebar = sidebar_html(index_out, pages_meta, "index", None)
    write_page(
        PageSpec(
            out_path=index_out,
            page_title=parsed.title,
            scope="dev",
            main_active="dev",
            sub_nav_html=build_sub_nav_dev(group_id, index_out),
            sidebar_html=sidebar,
            content_html=index_content,
            footer_html=f'Source: <code>docs/{html.escape(doc.relpath)}</code>',
            needs_mermaid="```mermaid" in text,
        )
    )
    written.append(index_out)
    print(f"Wrote {index_out}")

    for sec in parsed.sections:
        out = base_dir / f"{sec.sid}.html"
        pages_meta.append((sec.sid, sec.title, out))
        body = render_markdown(sec.body)
        h3s = h3_sidebar_items(sec.body)
        all_pages = [("index", "Overview", index_out)] + [
            (s.sid, s.title, base_dir / f"{s.sid}.html") for s in parsed.sections
        ]
        sidebar = sidebar_html(out, all_pages, sec.sid, h3s if len(h3s) >= 3 else None)
        content = (
            f'<p class="source">Source: <code>docs/{html.escape(doc.relpath)}</code>'
            f" · <a href=\"{rel_path(out, index_out)}\">{html.escape(parsed.title)}</a></p>"
            f"<h1>{html.escape(sec.title)}</h1>\n{body}"
        )
        write_page(
            PageSpec(
                out_path=out,
                page_title=f"{parsed.title} — {sec.title}",
                scope="dev",
                main_active="dev",
                sub_nav_html=build_sub_nav_dev(group_id, out),
                sidebar_html=sidebar,
                content_html=content,
                footer_html=f'Source: <code>docs/{html.escape(doc.relpath)}</code>',
                needs_mermaid="```mermaid" in sec.body,
            )
        )
        written.append(out)
        print(f"Wrote {out}")
    return written


def build_dev_group_index(group_id: str, title: str, doc_links: list[tuple[str, Path]]) -> None:
    out = SITE_DIR / "dev" / group_id / "index.html"
    items = []
    for label, path in sorted(doc_links, key=lambda x: x[0].lower()):
        items.append(
            f'<li><a href="{rel_path(out, path)}">{html.escape(label)}</a></li>'
        )
    content = (
        f'<div class="dev-hub"><h1>{html.escape(title)}</h1>'
        f"<p>Canon markdown exported from <code>docs/</code>.</p>"
        f"<ul>{''.join(items)}</ul></div>"
    )
    write_page(
        PageSpec(
            out_path=out,
            page_title=title,
            scope="dev",
            main_active="dev",
            sub_nav_html=build_sub_nav_dev(group_id, out),
            sidebar_html="",
            content_html=content,
            footer_html="Full design canon · regenerated from markdown",
            needs_mermaid=False,
        )
    )
    print(f"Wrote {out}")


def build_dev_root() -> None:
    out = SITE_DIR / "dev" / "index.html"
    sections = []
    for group in DEV_GROUPS:
        ghref = rel_path(out, SITE_DIR / "dev" / group.id / "index.html")
        doc_items = []
        for doc in group.docs:
            dslug = doc_slug(doc.relpath)
            dh = rel_path(out, SITE_DIR / "dev" / group.id / dslug / "index.html")
            doc_items.append(f'<li><a href="{dh}">{html.escape(doc.title)}</a></li>')
        sections.append(
            f"<section><h2><a href=\"{ghref}\">{html.escape(group.title)}</a></h2>"
            f"<ul>{''.join(doc_items)}</ul></section>"
        )
    content = (
        "<div class=\"dev-hub\"><h1>Development documentation</h1>"
        "<p>Every merged design doc and dated note under <code>docs/</code>, "
        "exported for reading in the browser. Edit markdown in the repo; run "
        "<code>python3 docs/marketing/build_marketing.py</code> to refresh.</p>"
        + "".join(sections)
        + "</div>"
    )
    write_page(
        PageSpec(
            out_path=out,
            page_title="Dev documentation",
            scope="dev",
            main_active="dev",
            sub_nav_html=build_sub_nav_dev("", out),
            sidebar_html="",
            content_html=content,
            footer_html="Not a substitute for implementer markdown — keep docs/ authoritative",
            needs_mermaid=False,
        )
    )
    print(f"Wrote {out}")


def build_dev() -> None:
    group_doc_index: dict[str, list[tuple[str, Path]]] = {
        g.id: [] for g in DEV_GROUPS
    }
    for group in DEV_GROUPS:
        for doc in group.docs:
            paths = build_dev_doc(group.id, doc)
            if paths:
                group_doc_index[group.id].append((doc.title, paths[0]))
    for group in DEV_GROUPS:
        links = group_doc_index[group.id]
        if links:
            build_dev_group_index(group.id, group.title, links)
    build_dev_root()


def cleanup_legacy() -> None:
    legacy = SITE_DIR / "development-plan.html"
    if legacy.is_file():
        legacy.unlink()
        print(f"Removed legacy {legacy}")


def write_redirects() -> None:
    redirects = SITE_DIR / "_redirects"
    redirects.write_text(
        "/development-plan.html /dev/start/development-plan/index.html 302\n",
        encoding="utf-8",
    )
    print(f"Wrote {redirects}")


def main() -> int:
    if not LAYOUT.is_file():
        print(f"Missing {LAYOUT}", file=sys.stderr)
        return 1
    SITE_DIR.mkdir(parents=True, exist_ok=True)
    ASSETS_DIR.mkdir(parents=True, exist_ok=True)
    shutil.copy2(CSS_SRC, ASSETS_DIR / "site.css")

    cleanup_legacy()
    build_marketing()
    build_dev()
    write_redirects()
    cleanup_wrong_group_dirs()
    return 0


def cleanup_wrong_group_dirs() -> None:
    """Remove mistaken Title-case group folders from an old build bug."""
    dev = SITE_DIR / "dev"
    if not dev.is_dir():
        return
    valid = {g.id for g in DEV_GROUPS}
    for child in dev.iterdir():
        if child.is_dir() and child.name not in valid and child.name != "":
            if child.name[0].isupper():
                shutil.rmtree(child)
                print(f"Removed mistaken {child}")


if __name__ == "__main__":
    sys.path.insert(0, str(ROOT))
    raise SystemExit(main())
