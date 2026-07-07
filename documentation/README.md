# Resume Builder — Documentation Index

**Read this folder first** before exploring source code or making changes. These docs are the canonical project reference (token-efficient).

## Quick routing

| Question / task | Read first |
|-----------------|------------|
| What is this project? How do pieces fit? | [architecture.md](architecture.md) |
| career-master layout, CV rules, 80% matching | [career-master.md](career-master.md) |
| REST API, Java services, analysis flow | [jd-analyzer.md](jd-analyzer.md) |
| Web UI (upload, results, static files) | [web-ui.md](web-ui.md) |
| Run, test, config, env vars | [development.md](development.md) |
| **Start / stop app, prerequisites** | [development.md](development.md) — Prerequisites, Start, Stop |
| What changed recently / design decisions | [changelog.md](changelog.md) |

## Project motto

> Profile from `career-master` → upload JD in UI → show match results in browser.

Secondary (Cursor / markdown workflow): find best existing CV (≥80% skills match) before generating a new one.

## Repo layout (top level)

```
resume_builder/
├── documentation/       ← you are here (read first)
├── career-master/       ← source of truth (profile, projects, CVs)
├── jd-analyzer/         ← Spring Boot app + web UI
├── .cursor/rules/       ← AI rules (career-master, documentation-first)
├── .gitignore
└── README.md
```

## AI agent workflow

1. **Scan** `documentation/README.md` and open the doc(s) from the routing table above.
2. **Only if docs are insufficient**, read specific source files listed in those docs.
3. **Update docs** when you change architecture, API, config, or workflows (same PR/commit as code).
4. **Do not** duplicate long explanations in chat — point to doc paths.

## Human workflow

```bash
cd jd-analyzer && mvn spring-boot:run
# → http://localhost:8080
```
