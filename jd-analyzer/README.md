# Resume Builder

Web app that matches a **job description** against your **career-master CV** and shows keyword gaps in the browser.

## Quick start

```bash
cd jd-analyzer
mvn spring-boot:run
```

Open **http://localhost:8080**

1. Profile loads from `career-master/profile/career-summary.md`
2. Upload a JD (PDF, DOCX, TXT, MD) or paste text
3. Results show match %, matched/missing keywords, and project suggestions

## Prerequisites

- Java 17+
- Maven 3.9+
- `career-master/` at `../career-master` (or set `CAREER_MASTER_PATH`)

**Install verification, start/stop, troubleshooting:** [documentation/development.md](../documentation/development.md)

## Start / stop

```bash
cd jd-analyzer
mvn spring-boot:run    # start → http://localhost:8080
# Ctrl+C in same terminal to stop
```

JAR mode: `mvn package -DskipTests` then `java -jar target/jd-analyzer-1.0.0-SNAPSHOT.jar`

## API

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/profile` | Name, title, default CV path |
| GET | `/api/v1/cvs` | List CVs under `career-master/cvs/` |
| POST | `/api/v1/jd/analyze` | JSON body: `{ "jobDescription": "...", "resumePath": "..." }` |
| POST | `/api/v1/jd/analyze/upload` | Multipart: `jobDescriptionFile`, optional `resumePath` |
| GET | `/api/v1/health` | Health check |

## Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `resume-builder.career-master-path` | `../career-master` | Root of career master data |
| `resume-builder.default-resume` | `cvs/pradeep-cv-2026-ats/resume.md` | Default CV |
| `CAREER_MASTER_PATH` | — | Env override for career-master root |

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
├── src/main/java/.../     # Spring Boot services + REST API
└── src/main/resources/
    ├── application.yml
    └── static/            # Web UI (index.html, app.js, style.css)
```
