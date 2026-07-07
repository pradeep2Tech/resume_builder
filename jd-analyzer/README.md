# JD Analyzer

Spring Boot service that compares a **job description** against your **CV** and reports ATS keywords: matched, missing, nice-to-have gaps, and project recommendations.

Integrates with `career-master/` — reads `profile/skills.md` aliases and writes reports to `career-master/output/`.

## Prerequisites

- Java 17+
- Maven 3.9+
- (Optional) `OPENAI_API_KEY` for Spring AI extraction

## Run (rule-based, no API key)

```bash
cd jd-analyzer
mvn spring-boot:run
```

## Run with Spring AI

```bash
export OPENAI_API_KEY=sk-...
mvn spring-boot:run -Dspring-boot.run.profiles=ai
```

On Windows PowerShell:

```powershell
$env:OPENAI_API_KEY = "sk-..."
mvn spring-boot:run "-Dspring-boot.run.profiles=ai"
```

## API

### Health

```http
GET http://localhost:8080/api/v1/health
```

### Analyze JD vs CV — **file upload (recommended for large JDs)**

```http
POST http://localhost:8080/api/v1/jd/analyze/upload
Content-Type: multipart/form-data
```

| Part / param | Required | Description |
|--------------|----------|-------------|
| `jobDescriptionFile` | **Yes** | JD file: PDF, DOCX, TXT, or MD (up to 15 MB) |
| `resumeFile` | No | CV file upload (PDF, DOCX, TXT, MD) |
| `resumePath` | No | CV path under `career-master/` (used if no `resumeFile`) |
| `reportSlug` | No | Used in output filename |
| `writeReport` | No | `true` to write markdown report |

**PowerShell example:**

```powershell
$jdPath = "C:\path\to\job-description.pdf"
$cvPath = "C:\Users\sach0725\projects\resume_builder\career-master\cvs\pradeep-cv-2026-ats\resume.md"

$form = @{
    jobDescriptionFile = Get-Item $jdPath
    resumePath         = "cvs/pradeep-cv-2026-ats/resume.md"
    reportSlug         = "target-company-architect"
    writeReport        = "true"
}

Invoke-RestMethod -Uri "http://localhost:8080/api/v1/jd/analyze/upload" -Method Post -Form $form
```

**curl example:**

```bash
curl -X POST http://localhost:8080/api/v1/jd/analyze/upload \
  -F "jobDescriptionFile=@/path/to/job-description.docx" \
  -F "resumePath=cvs/pradeep-cv-2026-ats/resume.md" \
  -F "reportSlug=acme-architect" \
  -F "writeReport=true"
```

Upload both JD and CV files:

```bash
curl -X POST http://localhost:8080/api/v1/jd/analyze/upload \
  -F "jobDescriptionFile=@jd.pdf" \
  -F "resumeFile=@my-cv.pdf" \
  -F "writeReport=true"
```

### Analyze JD vs CV — JSON (short JD text or server-side path)

```http
POST http://localhost:8080/api/v1/jd/analyze
Content-Type: application/json

{
  "jobDescriptionPath": "output/sample-jd.txt",
  "resumePath": "cvs/pradeep-cv-2026-ats/resume.md",
  "reportSlug": "netcracker-architect",
  "writeReport": true
}
```

Provide **one of** `jobDescription`, `jobDescriptionPath`, or use the upload endpoint.

| Field | Required | Description |
|-------|----------|-------------|
| `jobDescription` | One of JD sources | Inline JD text (fine for short JDs) |
| `jobDescriptionPath` | One of JD sources | Path relative to `career-master/` or absolute |
| `resumeText` | No | Inline CV text (overrides `resumePath`) |
| `resumePath` | No | Path relative to `career-master/` or absolute; defaults to ATS CV |
| `reportSlug` | No | Used in output filename |
| `writeReport` | No | Write markdown report to `career-master/output/` |

### Response

```json
{
  "matchScore": 78,
  "requiredCount": 12,
  "matchedCount": 9,
  "requiredKeywords": ["Java", "Kubernetes", "Kafka"],
  "matchedKeywords": ["Java", "Kafka"],
  "missingKeywords": ["Terraform"],
  "niceToHaveKeywords": ["GraphQL"],
  "analysisMode": "rule-based",
  "reportPath": "C:\\...\\career-master\\output\\jd-match-netcracker-architect-2026-07-07.md"
}
```

### Reload skills

```http
POST http://localhost:8080/api/v1/skills/reload
```

Call after editing `career-master/profile/skills.md`.

## Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `resume-builder.career-master-path` | `../career-master` | Root of career master data |
| `resume-builder.default-resume` | `cvs/pradeep-cv-2026-ats/resume.md` | Default CV when none specified |
| `resume-builder.ai.enabled` | `false` | Enable Spring AI (use `ai` profile) |
| `CAREER_MASTER_PATH` | — | Env override for career-master root |
| `server.servlet.multipart.max-file-size` | `15MB` | Max JD/CV upload size |
| `server.servlet.multipart.max-request-size` | `20MB` | Max total multipart request |

## Analysis modes

| Mode | When | Behavior |
|------|------|----------|
| `rule-based` | Default (no API key) | Matches JD against `skills.md` master list + aliases |
| `spring-ai` | `ai` profile + `OPENAI_API_KEY` | LLM extracts required/nice-to-have keywords, then same matcher |

Rule-based mode works offline and is deterministic. Spring AI gives better extraction for unstructured JDs.

## Example curl

```bash
curl -s -X POST http://localhost:8080/api/v1/jd/analyze \
  -H "Content-Type: application/json" \
  -d "{\"jobDescription\":\"Principal Engineer with Java 17, Spring Boot, Kubernetes, Kafka, DDD, microservices architecture.\",\"writeReport\":true,\"reportSlug\":\"sample-jd\"}"
```

## Build & test

```bash
mvn test
mvn package
java -jar target/jd-analyzer-1.0.0-SNAPSHOT.jar
```

## Project layout

```
jd-analyzer/
├── pom.xml
└── src/main/java/com/resumebuilder/jdanalyzer/
    ├── config/          # career-master paths, AI toggle
    ├── model/           # request/response DTOs
    ├── service/         # skills parser, matcher, report writer
    └── web/             # REST controllers
```
