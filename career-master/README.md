# Career Master

Single source of truth for profile, experience, projects, and generated CVs.

## Motto

> Share a job description → find the best existing CV (≥80% skills match) → generate a new CV only when nothing fits.

## Layout

```
career-master/
├── README.md                 ← you are here
├── profile/                  ← who you are (stable facts)
│   ├── career-summary.md
│   ├── skills.md
│   ├── certifications.md
│   ├── awards.md
│   └── education.md
├── companies/                ← employment history (one file per employer)
│   ├── tcs.md
│   ├── envestnet-yodlee.md
│   ├── entain.md
│   └── netcracker.md
├── projects/                 ← deliverables grouped by employer
│   ├── tcs/
│   ├── envestnet-yodlee/
│   ├── entain/
│   └── netcracker/
├── cvs/                      ← generated & curated CV variants
│   ├── index.md
│   └── <slug>/resume.md
└── output/                   ← prompt-generated artifacts (LinkedIn, cover letters, JD reports)
```

## Workflow (for humans & AI)

1. **Maintain master data** in `profile/`, `companies/`, and `projects/<employer>/`. Update once; reuse everywhere.
2. **Paste a job description** (JD) when applying or tailoring.
3. **Search `cvs/`** — read `cvs/index.md` and compare JD skills against each CV's tagged skills.
4. **If best match ≥ 80%** → reuse or lightly tailor that CV.
5. **If best match < 80%** → generate a new CV from master data, save under `cvs/<slug>/`, and register in `cvs/index.md`.
6. **For LinkedIn, cover letters, JD analysis** → write to `output/` (ephemeral); do not treat as master data.

## Skill matching (80% rule)

- Extract required & nice-to-have skills from the JD.
- Compare against the CV's `skills` front matter (or `profile/skills.md`).
- **Match %** = (matched required skills / total required skills) × 100.
- Nice-to-have skills are bonus only; they do not lower the percentage if missing.
- Treat synonyms consistently (e.g. `K8s` = `Kubernetes`, `AWS` = `Amazon Web Services`).

## File conventions

| Area | Convention |
|------|------------|
| Companies | `companies/<employer-slug>.md` — reverse chronological within file |
| Projects | `projects/<employer-slug>/<project-slug>.md` — link to company + tech stack |
| CVs | `cvs/<slug>/resume.md` + optional `resume.pdf` |
| Output | `output/<artifact-slug>.md` — generated; not master data |
| Dates | `YYYY-MM` or `YYYY-MM — YYYY-MM` |
| Skills | Master list in `profile/skills.md`; tag in project files as needed |

## Quick start

1. Fill in `profile/*.md` with your real details.
2. Add one file per employer under `companies/`.
3. Document key projects under `projects/<employer>/`.
4. When you have a tailored CV, add it under `cvs/` and register in `cvs/index.md`.
5. Generate LinkedIn or other artifacts into `output/` from prompts.

## Project index

### Netcracker
| Project | Period |
|---------|--------|
| [erp-modernization](projects/netcracker/erp-modernization.md) | 2025 — Present |
| [platform-engineering](projects/netcracker/platform-engineering.md) | 2025 — Present |
| [spring-ai](projects/netcracker/spring-ai.md) | 2024 — Present |

### Entain
| Project | Period |
|---------|--------|
| [loyalty-platform](projects/entain/loyalty-platform.md) | 2018 — 2025 |
| [crm-modernization](projects/entain/crm-modernization.md) | 2015 — 2025 |

### Envestnet Yodlee
| Project | Period |
|---------|--------|
| [pfm](projects/envestnet-yodlee/pfm.md) | Dec 2011 — Aug 2013 |
| [issue-analyzer](projects/envestnet-yodlee/issue-analyzer.md) | Aug 2013 — May 2014 |
| [dap-aggregation](projects/envestnet-yodlee/dap-aggregation.md) | May 2014 — Nov 2014 |
| [my-monitor](projects/envestnet-yodlee/my-monitor.md) | Nov 2014 — Jan 2015 |
| [dap-restructure](projects/envestnet-yodlee/dap-restructure.md) | Jan 2015 — Nov 2015 |

### TCS
| Project | Period |
|---------|--------|
| [etreasury](projects/tcs/etreasury.md) | Aug 2007 — Jul 2008 |
| [ngt-treasury](projects/tcs/ngt-treasury.md) | Aug 2008 — May 2010 |
| [amex-relationship-center](projects/tcs/amex-relationship-center.md) | Jun 2010 — Dec 2011 |
