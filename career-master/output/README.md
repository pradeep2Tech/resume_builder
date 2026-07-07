# Output

Generated artifacts from career-master prompts — not source of truth.

| Artifact | Typical prompt |
|----------|----------------|
| `linkedin-profile.md` | Generate LinkedIn About, headline, and experience from master data |
| `cover-letter-<company>.md` | Tailor cover letter to a specific JD |
| `jd-match-<slug>.md` | Skill match report against a job description |

## Rules

1. **Never edit master data here** — update `profile/`, `companies/`, or `projects/` instead.
2. Regenerate from master when facts change.
3. Name files with slug + date when useful: `linkedin-profile-2026-07.md`.

## Workflow

```
JD or prompt → read profile/ + companies/ + projects/ → write output/<artifact>.md
```

For CV variants, use `cvs/` (registered, reusable) rather than `output/`.
