package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.model.ExtractedJdKeywords;
import com.resumebuilder.jdanalyzer.model.JdAnalysisResponse;
import com.resumebuilder.jdanalyzer.model.SkillsRegistry;
import com.resumebuilder.jdanalyzer.model.SynonymMatch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class KeywordMatcherService {

    public JdAnalysisResponse match(
            ExtractedJdKeywords extracted,
            String resumeText,
            SkillsRegistry registry,
            String analysisMode) {

        String resumeLower = resumeText.toLowerCase(Locale.ROOT);

        List<String> required = normalizeTerms(extracted.required(), registry);
        List<String> niceToHave = normalizeTerms(extracted.niceToHave(), registry);

        List<String> matchedRequired = new ArrayList<>();
        List<String> missingRequired = new ArrayList<>();
        for (String keyword : required) {
            if (isPresent(resumeLower, keyword, registry)) {
                matchedRequired.add(keyword);
            } else {
                missingRequired.add(keyword);
            }
        }

        List<String> matchedNice = new ArrayList<>();
        List<String> missingNice = new ArrayList<>();
        for (String keyword : niceToHave) {
            if (isPresent(resumeLower, keyword, registry)) {
                matchedNice.add(keyword);
            } else {
                missingNice.add(keyword);
            }
        }

        List<SynonymMatch> synonymMatches = buildSynonymMatches(registry, resumeLower);
        List<String> atsFlags = detectAtsFlags(resumeText);
        List<String> recommendedProjects = recommendProjects(missingRequired, registry);

        int requiredCount = required.size();
        int matchedCount = matchedRequired.size();
        int matchScore = requiredCount == 0 ? 100 : (matchedCount * 100) / requiredCount;

        return new JdAnalysisResponse(
                matchScore,
                requiredCount,
                matchedCount,
                required,
                matchedRequired,
                missingRequired,
                niceToHave,
                matchedNice,
                missingNice,
                synonymMatches,
                atsFlags,
                recommendedProjects,
                analysisMode
        );
    }

    private List<String> normalizeTerms(List<String> terms, SkillsRegistry registry) {
        Set<String> normalized = new LinkedHashSet<>();
        for (String term : terms) {
            if (term == null || term.isBlank()) {
                continue;
            }
            normalized.add(registry.resolveCanonical(term.trim()));
        }
        return new ArrayList<>(normalized);
    }

    private boolean isPresent(String resumeLower, String keyword, SkillsRegistry registry) {
        if (registry.containsAliasOrSkill(resumeLower, keyword)) {
            return true;
        }
        String canonical = registry.resolveCanonical(keyword);
        return resumeLower.contains(canonical.toLowerCase(Locale.ROOT));
    }

    private List<SynonymMatch> buildSynonymMatches(SkillsRegistry registry, String resumeLower) {
        List<SynonymMatch> matches = new ArrayList<>();
        registry.aliases().forEach((jdTerm, mapsTo) -> {
            String canonical = registry.resolveCanonical(jdTerm);
            boolean present = resumeLower.contains(jdTerm.toLowerCase(Locale.ROOT))
                    || resumeLower.contains(canonical.toLowerCase(Locale.ROOT));
            matches.add(new SynonymMatch(jdTerm, canonical, present));
        });
        return matches;
    }

    private List<String> detectAtsFlags(String resumeText) {
        List<String> flags = new ArrayList<>();
        String lower = resumeText.toLowerCase(Locale.ROOT);

        if (resumeText.contains("|") && resumeText.contains("---")) {
            flags.add("Markdown tables detected — ensure exported PDF/DOCX uses plain text layout for ATS");
        }
        if (lower.contains("graphic") || lower.contains("chart")) {
            flags.add("Graphics/charts may reduce ATS parse accuracy");
        }
        if (!lower.contains("experience") && !lower.contains("employment")) {
            flags.add("Missing standard Experience section heading");
        }
        if (!lower.contains("skill")) {
            flags.add("Missing explicit Skills section — consider adding keyword-rich skills block");
        }
        if (flags.isEmpty()) {
            flags.add("Resume structure looks ATS-friendly (standard sections, text-based content)");
        }
        return flags;
    }

    private List<String> recommendProjects(List<String> missingKeywords, SkillsRegistry registry) {
        List<String> projects = new ArrayList<>();
        Set<String> missingLower = new LinkedHashSet<>();
        missingKeywords.forEach(k -> missingLower.add(k.toLowerCase(Locale.ROOT)));

        registry.aliases().forEach((jdTerm, mapsTo) -> {
            String lowerMaps = mapsTo.toLowerCase(Locale.ROOT);
            for (String missing : missingLower) {
                if (lowerMaps.contains(missing) || jdTerm.toLowerCase(Locale.ROOT).contains(missing)) {
                    if (lowerMaps.contains("project")) {
                        projects.add(mapsTo);
                    }
                }
            }
        });

        if (missingLower.stream().anyMatch(k -> k.contains("erp") || k.contains("kubernetes") || k.contains("java"))) {
            projects.add("projects/netcracker/erp-modernization.md");
        }
        if (missingLower.stream().anyMatch(k -> k.contains("loyalty") || k.contains("reward") || k.contains("kafka"))) {
            projects.add("projects/entain/loyalty-platform.md");
        }
        if (missingLower.stream().anyMatch(k -> k.contains("crm") || k.contains("microservice"))) {
            projects.add("projects/entain/crm-modernization.md");
        }
        if (missingLower.stream().anyMatch(k -> k.contains("pfm") || k.contains("aggregation") || k.contains("selenium"))) {
            projects.add("projects/envestnet-yodlee/dap-aggregation.md");
        }
        if (missingLower.stream().anyMatch(k -> k.contains("spring ai") || k.contains("agent"))) {
            projects.add("projects/netcracker/spring-ai.md");
        }

        return projects.stream().distinct().toList();
    }
}
