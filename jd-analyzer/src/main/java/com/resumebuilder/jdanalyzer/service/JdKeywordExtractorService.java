package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.model.ExtractedJdKeywords;
import com.resumebuilder.jdanalyzer.model.SkillsRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class JdKeywordExtractorService {

    public ExtractedJdKeywords extract(String jobDescription, SkillsRegistry registry) {
        return extractWithRules(jobDescription, registry);
    }

    private ExtractedJdKeywords extractWithRules(String jobDescription, SkillsRegistry registry) {
        String jdLower = jobDescription.toLowerCase(Locale.ROOT);
        Set<String> required = new LinkedHashSet<>();
        Set<String> niceToHave = new LinkedHashSet<>();

        for (String skill : registry.masterSkills()) {
            if (containsTerm(jdLower, skill)) {
                if (isNiceToHaveContext(jobDescription, skill)) {
                    niceToHave.add(skill);
                } else {
                    required.add(skill);
                }
            }
        }

        for (String alias : registry.aliases().keySet()) {
            if (containsTerm(jdLower, alias)) {
                String canonical = registry.resolveCanonical(alias);
                if (isNiceToHaveContext(jobDescription, alias)) {
                    niceToHave.add(canonical);
                } else {
                    required.add(canonical);
                }
            }
        }

        niceToHave.removeAll(required);
        return new ExtractedJdKeywords(
                new ArrayList<>(required),
                new ArrayList<>(niceToHave),
                List.of(),
                List.of()
        );
    }

    private boolean isNiceToHaveContext(String jobDescription, String term) {
        String lower = jobDescription.toLowerCase(Locale.ROOT);
        int index = lower.indexOf(term.toLowerCase(Locale.ROOT));
        if (index < 0) {
            return false;
        }
        int windowStart = Math.max(0, index - 120);
        String window = lower.substring(windowStart, index);
        return window.contains("nice to have")
                || window.contains("preferred")
                || window.contains("bonus")
                || window.contains("plus");
    }

    private boolean containsTerm(String jdLower, String term) {
        String normalized = term.toLowerCase(Locale.ROOT).trim();
        if (normalized.isEmpty()) {
            return false;
        }
        if (normalized.contains(" ")) {
            return jdLower.contains(normalized);
        }
        return Pattern.compile("\\b" + Pattern.quote(normalized) + "\\b", Pattern.CASE_INSENSITIVE)
                .matcher(jdLower)
                .find();
    }
}
