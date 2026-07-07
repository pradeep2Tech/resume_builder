# Web UI

Vanilla HTML/CSS/JS — no build step. Served from Spring Boot static resources.

## Files

```
jd-analyzer/src/main/resources/static/
├── index.html   # Page structure
├── app.js       # API calls + DOM rendering
└── style.css    # Dark theme layout
```

Spring maps `/` → `index.html` via `WelcomePageHandlerMapping`.

## User flow

1. **On load** — parallel `GET /api/v1/profile` + `GET /api/v1/cvs`
2. **Header** — name, title, location; CV `<select>` populated from `/cvs`
3. **JD input** — two tabs:
   - **Upload** — drag-drop or browse (`jobDescriptionFile`)
   - **Paste** — textarea → `POST /api/v1/jd/analyze` JSON
4. **Analyze** — disables button, shows loading state
5. **Results** — score ring, 80% recommendation, keyword chips, projects, ATS flags

## API usage (app.js)

```javascript
const API = '/api/v1';

// Upload
FormData: jobDescriptionFile, resumePath

// Paste
POST /api/v1/jd/analyze
{ jobDescription, resumePath }
```

Errors: reads `ProblemDetail.detail` from 4xx responses.

## UI elements → response fields

| Element ID | Data field |
|------------|------------|
| `#score-value` | `matchScore` |
| `#score-count` | `matchedCount` / `requiredCount` |
| `#recommendation` | derived: ≥80% ok, else warn |
| `#matched-chips` | `matchedKeywords` |
| `#missing-chips` | `missingKeywords` |
| `#nice-matched-chips` | `matchedNiceToHave` |
| `#nice-missing-chips` | `missingNiceToHave` |
| `#projects-list` | `recommendedProjects` |
| `#ats-flags` | `atsFlags` |

## Styling notes

- CSS variables in `:root` (`--bg`, `--accent`, `--ok`, `--warn`, `--miss`)
- Score ring: `conic-gradient` with `--score` custom property
- Responsive: keyword grid collapses to 1 column on narrow screens

## Extending the UI

| Change | Edit |
|--------|------|
| New result section | `index.html` + `renderResults()` in `app.js` |
| New API field | extend `JdAnalysisResponse` + `renderResults()` |
| Styling | `style.css` only |
| New page | add HTML under `static/`; no router today |

No hot reload — restart Spring Boot or hard-refresh browser after static changes.
