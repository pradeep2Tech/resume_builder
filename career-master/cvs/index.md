# CV Registry

Index of generated and curated CV variants. Used for JD matching (80% skills rule).

## Registry

| Slug | Target role | Primary skills | Companies emphasized | Created | Last used | Notes |
|------|-------------|----------------|----------------------|---------|-----------|-------|
| [pradeep-cv-2026-ats](pradeep-cv-2026-ats/resume.md) | System Architect / Engineering Leader | Java, Golang, Kubernetes, Kafka, NATS, Redis, Spring Boot, Microservices, DDD, OpenTelemetry, OpenSearch, PySpark | Netcracker, Entain, Yodlee, TCS | 2026-01 | — | ATS-format baseline CV; source: `pradeep_CV_2026_ATS.pdf` |

## Source documents (external)

| Document | Path | Purpose |
|----------|------|---------|
| Detailed CV (non-ATS) | `c:\Users\sach0725\Documents\me\pradeep\pradeep_cv_overall.doc` | Full project history (12 projects) |
| Early developer CV | `c:\Users\sach0725\Documents\me\pradeep\Pradeep_previous.doc` | Yodlee/TCS developer-era projects (8 projects) |
| LinkedIn export | `c:\Users\sach0725\Documents\me\pradeep\LinkeIn_Profile.pdf` | Profile summary & timeline |
| ATS CV 2026 | `c:\Users\sach0725\Documents\me\pradeep\pradeep_CV_2026_ATS.pdf` | Primary application CV |

## Matching procedure

When a JD is shared:

1. Extract **required** skills from JD.
2. For each CV row, count matches against **Primary skills** (use aliases from `profile/skills.md`).
3. Compute: `match % = matched / required × 100`.
4. **≥ 80%** → recommend that CV (highest match wins; ties by recency).
5. **< 80%** → generate new CV from master data; save under `cvs/<slug>/` and add row here.

## Suggested future CV variants

| Slug (to create) | When to use |
|------------------|-------------|
| `principal-engineer-gaming-loyalty` | Gaming / loyalty / Golang-heavy roles |
| `erp-modernization-architect` | ERP, Java 17, K8s, observability roles |
| `fintech-data-aggregation` | PFM, DAP, Selenium, text mining, API architect roles |
| `developer-fintech-yodlee` | Early-career tech lead; PFM, DAP, Issue Analyzer, rule engines |
| `engineering-manager` | People leadership + architecture hybrid roles |
