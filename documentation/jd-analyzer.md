# jd-analyzer (Backend)

Spring Boot 4.0 · Java 21 · Spring AI 2.0 · Maven · port **8080**.

## REST API

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/health` | `{ status, service }` |
| GET | `/api/v1/profile` | `ProfileSummary` from career-summary.md |
| GET | `/api/v1/cvs` | List `CvSummary` (slug + path) |
| POST | `/api/v1/jd/analyze` | JSON: `{ jobDescription, resumePath? }` |
| POST | `/api/v1/jd/analyze/upload` | Multipart: `jobDescriptionFile`, `resumePath?` |
| POST | `/api/v1/skills/reload` | Reload skills.md cache |

Static UI at `/` (index.html).

## Request / response models

**JdAnalysisRequest** (internal):
- `jobDescription` — inline text
- `resumeText` — inline CV (rarely used)
- `resumePath` — relative to career-master; defaults to `default-resume`

**JdAnalysisResponse** (API + UI):
- `matchScore`, `requiredCount`, `matchedCount`
- `requiredKeywords`, `matchedKeywords`, `missingKeywords`
- `niceToHaveKeywords`, `matchedNiceToHave`, `missingNiceToHave`
- `synonymMatches[]` — `{ jdTerm, canonicalTerm, presentInResume }`
- `atsFlags[]`, `recommendedProjects[]`
- `analysisMode` — `"rule-based"` (default) or `"spring-ai"` (with `ai` profile + API key)

## Services

| Class | Role |
|-------|------|
| `ProfileService` | Parse `**Field:** value` from career-summary.md |
| `CvRegistryService` | Scan `cvs/*/resume.md` directories |
| `SkillsRegistryService` | Parse skills.md tables + alias section; cached |
| `DocumentTextExtractorService` | PDF (PDFBox), DOCX (POI), TXT, MD |
| `ResumeTextService` | Load JD/CV; strip YAML front matter |
| `JdKeywordExtractorService` | Rule-based keyword extraction from JD |
| `KeywordMatcherService` | Score + ATS flags + project recommendations |
| `JdAnalysisService` | Orchestrates analyze flow |

## Keyword extraction

**Default (rule-based):**

1. For each skill in `skills.md` master list → if term in JD → required or nice-to-have (context window: "preferred", "bonus", etc.).
2. Same for alias keys → map to canonical skill.
3. Deterministic and offline; no API key required.

**Optional (Spring AI 2.0):** activate profile `ai` and set `OPENAI_API_KEY`. JD keywords are extracted via ChatClient; falls back to rule-based on failure. Matching/scoring still uses `KeywordMatcherService` (same similarity logic).

## Project recommendations

`KeywordMatcherService.recommendProjects()` maps missing keywords to hardcoded paths, e.g.:

- ERP/K8s/Java → `projects/netcracker/erp-modernization.md`
- Loyalty/Kafka → `projects/entain/loyalty-platform.md`
- CRM/microservice → `projects/entain/crm-modernization.md`

Extend this method when adding new project mappings.

## Error handling

`GlobalExceptionHandler` returns RFC 7807 `ProblemDetail`:

- `IllegalArgumentException` → 400
- `MaxUploadSizeExceededException` → 413 (15 MB file / 20 MB request)

## Tests

```
src/test/java/.../
├── JdAnalyzerApplicationTests.java      # context load
├── service/KeywordMatcherServiceTest.java
├── service/ResumeTextServiceTest.java
├── service/SkillsRegistryServiceTest.java
├── service/DocumentTextExtractorServiceTest.java
└── web/JdAnalysisControllerUploadTest.java
```

Run: `cd jd-analyzer && mvn test`

## Dependencies (pom.xml)

- `spring-boot-starter-web`
- `spring-boot-starter-validation`
- `spring-ai-starter-model-openai` (BOM 2.0.0; disabled unless `ai` profile)
- `spring-boot-starter-webmvc-test` (test scope; Spring Boot 4 MockMvc)
- `pdfbox` 3.0.3
- `poi-ooxml` 5.3.0
