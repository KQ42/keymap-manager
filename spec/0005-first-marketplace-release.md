# 0005 — First Marketplace release

- **Status:** Accepted 2026-10-22 — the working plan towards the first public release
- **Source:** internal (project status assessment, 2026-10-22)
- **Date:** 2026-10-22

---

## 1. Goal

Publish **Keymap Manager** (`de.kq42.plugins.keymapmanager`) on the JetBrains
Marketplace. The plugin has **never been released**: every earlier version
(≤ 1.7.0) only ever existed as a locally built ZIP. "Release" in older commits and
in the 1.7.0 change-notes means "version bump", not "published".

## 2. Baseline (2026-10-22, at `2b295d2`)

- **Features:** done for the first release — specs 0001, 0003, 0004 shipped;
  backlog B2/B3 are post-release ideas.
- **Keymap:** `validate_keymap.py` → `PASS`; working copy = plugin resource copy.
- **Build:** `version = "1.7.0"`; `verifyPlugin` → *Compatible*. The existing ZIP
  (built 2026-08-05 20:56) predates `2b295d2` (every-key rebind capture) and is
  stale.
- **Git:** clean, in sync with `origin/main`, all commits signed.
- **Marketplace:** plugin ID unknown there (404); no plugin named "Keymap Manager"
  exists (checked 2026-10-22 — closest: "Keymap Nationalizer", "Keymap Exporter").

## 3. Open decisions

| # | Decision | Status |
|---|---|---|
| D1 | Vendor, and with it the **plugin ID** (permanent once published), `LICENSE` holder and source URL | **KQ42** — ID `de.kq42.plugins.keymapmanager` (revised 2026-10-22) |
| D2 | Version number of the first public release | **1.8.0** |
| D3 | Marketplace category (license: MIT, already in `LICENSE`) | **Keymap** |
| D4 | Author-sign the ZIP, or rely on Marketplace signing | **Marketplace signing** |

Resolved: **D0 name** — "Keymap Manager" is distinct on the Marketplace (§2).

## 4. Path to release

| # | Step | Depends on | Done |
|---|---|---|---|
| 1 | Resolve D1–D4 | — | ☑ 2026-10-22 |
| 2 | Apply D1: vendor, plugin ID, action ID, Java packages, `LICENSE` → KQ42 | D1 | ☑ 2026-10-22 |
| 3 | Apply D2: `version` in `build.gradle.kts`, ZIP names in README/INSTALL/CLAUDE.md | D2 | ☑ 2026-10-22 |
| 4 | Rewrite `<change-notes>` as a first-release feature summary (no "up to 1.6.1" fix story) | D2 | ☑ 2026-10-22 |
| 5 | Fix stale "↩ ⎋ ⌫ ⇥ Space" wording in README + `docs/marketplace-description.html` (only ↩ ⎋ ⇥ need radios since `2b295d2`) | — | ☑ 2026-10-22 |
| 6 | Housekeeping: spec 0003 → Implemented; BACKLOG pre-publish note → points here | — | ☑ 2026-10-22 |
| 7 | `buildPlugin` + `verifyPlugin` → *Compatible*; `validate_keymap.py` on the shipped copy | 2–5 | ☑ 2026-10-22 (re-run for KQ42) |
| 8 | Manual smoke test of the built ZIP (uninstall old copy first; rebind arrows/Space/⌫; ⌘V on MacBook Pro DE; import/export) | 7 | ☑ 2026-10-22 (Stefan, re-run for KQ42) |
| 9 | First upload via the web (manual moderation, ~2 business days); paste listing description | 8 | ☐ |
| 10 | Tag the released commit; flip this spec to Implemented | 9 | ☐ |

Post-release: backlog B3 (guided conflict resolution), B2 (compare keymaps).

## 5. Decision log

All resolved by Stefan, 2026-10-22:

- **D1 — KQ42** (revised the same day; first answer was "keep CIVA"). Vendor
  `KQ42`, email `stefan.huettemann@kq42.de`, URL
  `https://kq42.de/plugins/keymap-manager` (page source:
  `docs/website/plugins/keymap-manager/index.html`), `LICENSE` © KQ42. Because
  nothing is published yet, everything vendor-derived moved too: plugin ID
  `de.civa.plugins.keymapmanager` → `de.kq42.plugins.keymapmanager` (permanent
  once uploaded), action ID `de.civa.keymap.ShowConflictReport` →
  `de.kq42.keymap.ShowConflictReport` (users' keymaps store it, so it must not
  change after release), Java package `de.civa.keymap` → `de.kq42.keymap`, Gradle
  `group`. The source URL stays the personal GitHub repo. The vendor URL is
  live over HTTPS since 2026-10-22, together with `/impressum/` and `/datenschutz/`
  (site source `docs/website/`, upload `docs/website.sftp`).
- **D2 — 1.8.0.** Continues the internal numbering, with a minor bump over the
  1.7.0 ZIP for the every-key rebind capture (`2b295d2`). The change-notes are
  still rewritten as a first-release summary, since nobody saw 1.7.0.
- **D3 — category "Keymap".** The tag the platform keymaps and "Keymap
  Nationalizer" carry (verified via the Marketplace API).
- **D4 — no author signing.** Marketplace signs on its side; revisit for later
  releases if wanted.
