# Development Guide

## Prerequisites

Install and verify before running the app.

| Requirement | Version | Verify |
|-------------|---------|--------|
| **Java JDK** | 21 (required) | `java -version` |
| **Maven** | 3.9+ | `mvn -version` |
| **career-master/** | Must exist and be readable | See layout below |
| **Network** | Port **8080** free on localhost | See [Check port](#check-port-8080) |

No database, Docker, or Node.js required. OpenAI API key only needed for optional `ai` profile.

### Verify Java

```powershell
java -version
```

Expected: `openjdk version "21"` or higher.

If missing: install [Eclipse Temurin JDK 21](https://adoptium.net/) and ensure `JAVA_HOME` / `PATH` are set.

### Verify Maven

```powershell
mvn -version
```

Expected: Apache Maven 3.9.x and Java 17+ in the output.

If missing: install from [maven.apache.org](https://maven.apache.org/download.cgi) or use `choco install maven` on Windows.

### career-master layout

Default path: `resume_builder/career-master/` (sibling to `jd-analyzer/`).

Required files for the app to start:

| File | Purpose |
|------|---------|
| `profile/career-summary.md` | Profile header in UI |
| `profile/skills.md` | Keyword matching |
| `cvs/<slug>/resume.md` | At least one CV (default: `cvs/pradeep-cv-2026-ats/resume.md`) |

If `career-master` is elsewhere, set `CAREER_MASTER_PATH` before starting (see [Configuration](#configuration)).

---

## Start the application

All commands run from the **`jd-analyzer`** directory.

### Option A — Development (recommended)

```powershell
cd c:\Users\sach0725\projects\resume_builder\jd-analyzer
mvn spring-boot:run
```

- Compiles on the fly
- Logs appear in the terminal
- Stop with **Ctrl+C** in the same terminal

### Option B — JAR (after build)

```powershell
cd c:\Users\sach0725\projects\resume_builder\jd-analyzer
mvn package -DskipTests
java -jar target\jd-analyzer-1.0.0-SNAPSHOT.jar
```

Use this to run the packaged app without Maven on each start.

### Option C — Custom career-master path

```powershell
cd jd-analyzer
$env:CAREER_MASTER_PATH = "C:\path\to\career-master"
mvn spring-boot:run
```

### Option D — AI keyword extraction (optional)

```powershell
cd jd-analyzer
$env:OPENAI_API_KEY = "<your-key>"
mvn spring-boot:run -Dspring-boot.run.profiles=ai
```

Uses Spring AI 2.0 for JD keyword extraction; matching scores still use rule-based `KeywordMatcherService`.

### Confirm it is running

1. **Browser:** open **http://localhost:8080** — profile name and CV dropdown should load.
2. **Health API:**
   ```powershell
   Invoke-RestMethod http://localhost:8080/api/v1/health
   ```
   Expected: `status = UP`, `service = resume-builder`

Startup log line to look for:

```
Started JdAnalyzerApplication in X seconds
Adding welcome page: class path resource [static/index.html]
```

---

## Stop the application

### If started in a terminal (Option A or B)

Press **Ctrl+C** in that terminal window.

Wait until the process exits (Maven may print `BUILD SUCCESS` after Spring Boot shuts down).

### If the terminal was closed but the app is still running

**Windows PowerShell** — find and stop process on port 8080:

```powershell
# Find PID listening on 8080
Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object OwningProcess

# Stop by PID (replace 12345 with actual PID)
Stop-Process -Id 12345 -Force
```

One-liner:

```powershell
Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue |
  ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }
```

**Alternative (any OS)** — kill Java process by name (stops all Java apps; use with care):

```powershell
# Windows
taskkill /F /IM java.exe

# Linux / macOS
pkill -f jd-analyzer
```

### Check port 8080

```powershell
Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
```

No output = port is free.

---

## Build & test

```powershell
cd jd-analyzer
mvn test          # unit + integration tests
mvn package       # produces target\jd-analyzer-1.0.0-SNAPSHOT.jar
```

---

## Configuration

File: `jd-analyzer/src/main/resources/application.yml`

| Key | Default | Notes |
|-----|---------|-------|
| `server.port` | 8080 | Change if 8080 is in use |
| `resume-builder.career-master-path` | `../career-master` | Relative to `jd-analyzer/` working dir |
| `resume-builder.default-resume` | `cvs/pradeep-cv-2026-ats/resume.md` | |
| `resume-builder.skills-file` | `profile/skills.md` | |
| `server.servlet.multipart.max-file-size` | 15MB | JD upload limit |

Local overrides: `application-local.yml` in `src/main/resources/` (gitignored).

### Change port (if 8080 is busy)

In `application-local.yml`:

```yaml
server:
  port: 8081
```

Then open **http://localhost:8081**.

---

## Troubleshooting

| Symptom | Likely cause | Fix |
|---------|--------------|-----|
| `Failed to read career summary` | Wrong `CAREER_MASTER_PATH` | Set env var or fix `career-master-path` in yml |
| Port 8080 already in use | Another app or old instance | [Stop the application](#if-the-terminal-was-closed-but-the-app-is-still-running) or change port |
| Profile shows "Loading profile…" | Server not running or wrong URL | Start app; check `http://localhost:8080/api/v1/health` |
| `java` / `mvn` not recognized | JDK/Maven not on PATH | Install and restart terminal |
| Upload fails 413 | JD file > 15 MB | Use smaller file or paste text |

---

## Git ignore highlights

Root `.gitignore`:

- `target/`, `*.class`, IDE files
- `.env`, secrets, `application-local.*`
- `career-master/output/*` (except `README.md`)
- `.cursor/*` except `.cursor/rules/`

---

## Cursor rules

| Rule file | Scope |
|-----------|-------|
| `.cursor/rules/documentation-first.mdc` | Always — read `documentation/` first |
| `.cursor/rules/career-master.mdc` | `career-master/**` — CV/JD workflow |

---

## Documentation maintenance

When you change any of the following, **update the matching doc** in `documentation/`:

| Change | Update |
|--------|--------|
| New API endpoint | `jd-analyzer.md`, `web-ui.md` if UI uses it |
| New career-master folder convention | `career-master.md` |
| Architecture / removed feature | `architecture.md`, `changelog.md` |
| Run/config/prerequisites | `development.md` |

---

## Common tasks

| Task | Where |
|------|-------|
| Add skill / alias | `career-master/profile/skills.md` → `POST /api/v1/skills/reload` or restart |
| Add CV variant | `career-master/cvs/<slug>/resume.md` → auto appears in UI dropdown |
| Change match logic | `KeywordMatcherService.java` |
| Change JD parsing | `JdKeywordExtractorService.java` |
| Map missing skill → project | `KeywordMatcherService.recommendProjects()` |

---

## IDE

`.vscode/settings.json` — Java build config disabled. Safe to commit.
