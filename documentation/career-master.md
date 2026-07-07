# career-master

Markdown **source of truth**. The app reads from here; do not treat `output/` as master data.

## Directory map

```
career-master/
├── profile/              # Stable facts
│   ├── career-summary.md # Name, title, contact (parsed by ProfileService)
│   ├── skills.md         # Master skill list + JD alias table (parsed by SkillsRegistryService)
│   ├── certifications.md
│   ├── awards.md
│   └── education.md
├── companies/            # One file per employer (tcs, envestnet-yodlee, entain, netcracker)
├── projects/<employer>/  # Project write-ups per employer
├── cvs/
│   ├── index.md          # Human/AI registry table (not parsed by Java yet)
│   └── <slug>/resume.md  # CV variants (scanned by CvRegistryService)
└── output/               # Generated artifacts — gitignored except README.md
```

## 80% CV matching rule

Used by UI and Cursor rule (`.cursor/rules/career-master.mdc`):

1. Extract **required** skills from JD.
2. Compare against CV / skills registry.
3. `match % = matched required / total required × 100`.
4. Nice-to-have is bonus only.
5. **≥ 80%** → reuse existing CV; **< 80%** → tailor or create new CV from master data.

Synonyms live in `profile/skills.md` under `## JD matching aliases`.

## CV conventions

- Path: `cvs/<slug>/resume.md`
- Optional YAML front matter (stripped before matching).
- Register new CVs in `cvs/index.md` for human/AI tracking.
- Java auto-discovers CVs by scanning `cvs/*/resume.md` (ignores `index.md`).

## Cursor / AI content rules

When generating CVs or career content (see `.cursor/rules/career-master.mdc`):

- Never invent employers, dates, or metrics — use `[TBD]` or ask user.
- Master data edits go in `profile/`, `companies/`, `projects/` only.
- LinkedIn, cover letters, JD reports → `output/` (ephemeral).
- New CV → `cvs/<slug>/resume.md` + row in `cvs/index.md`.

## Employers & projects (index)

| Employer | Projects folder |
|----------|-----------------|
| Netcracker | `projects/netcracker/` — erp-modernization, platform-engineering, spring-ai |
| Entain | `projects/entain/` — loyalty-platform, crm-modernization |
| Envestnet Yodlee | `projects/envestnet-yodlee/` — pfm, issue-analyzer, dap-*, my-monitor |
| TCS | `projects/tcs/` — etreasury, ngt-treasury, amex-relationship-center |

Full timeline: see `career-master/README.md`.

## Files the app reads

| File | Consumer |
|------|----------|
| `profile/career-summary.md` | `ProfileService` |
| `profile/skills.md` | `SkillsRegistryService` |
| `cvs/*/resume.md` | `CvRegistryService`, `ResumeTextService` |

After editing `skills.md`, call `POST /api/v1/skills/reload` or restart the app.
