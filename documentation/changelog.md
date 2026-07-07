# Changelog (design decisions)

High-level history for AI context — avoid re-reading git log.

## 2026-07-07 — Web UI MVP

- Replaced curl/API-only workflow with browser UI at `http://localhost:8080`.
- Static frontend: `index.html`, `app.js`, `style.css` (no npm).
- Added `GET /api/v1/profile`, `GET /api/v1/cvs`.
- **Removed:** Spring AI, `AnalysisReportWriter`, markdown report output, `writeReport` flag.
- Analysis mode: **rule-based only** (`skills.md` matching).
- CV discovery: scan `cvs/*/resume.md` (not `cvs/index.md` table).

## 2026-07-07 — Development docs expanded

- `development.md`: prerequisites (verify Java/Maven), start/stop (Ctrl+C, kill port 8080), troubleshooting.

## 2026-07-07 — Documentation folder

- Added `documentation/` as canonical reference for humans and AI agents.
- Added `.cursor/rules/documentation-first.mdc` — scan docs before code exploration.

## Earlier — Initial jd-analyzer

- Spring Boot service comparing JD vs CV keywords.
- Multipart upload for PDF/DOCX/TXT/MD.
- Integration with `career-master/profile/skills.md` aliases.
- 80% match threshold defined in career-master workflow.

## Intentionally not built (yet)

- Multi-CV auto-ranking in one API call
- Auth / multi-user
- AI/LLM keyword extraction
- CV preview panel in UI
- Parsing `cvs/index.md` registry table in Java
- Markdown report export from UI
