# Marketing site (sources)

Built output is published from **`site/`** at the repo root (Netlify `publish` directory).

| Role | Path |
| --- | --- |
| Templates + markdown | `docs/marketing/` (here) |
| Deployable HTML | `site/index.html`, `site/marketing/*`, `site/dev/**` |
| Netlify config | `netlify.toml` |

## Build

```bash
python3 docs/marketing/build_marketing.py
```

Commit **`site/`** after content changes so previews match production (Netlify also runs the build command on deploy).

Implementer mirror: **`docs/mod-goals.md`**.
