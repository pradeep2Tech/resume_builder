package com.resumebuilder.jdanalyzer.model;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record SkillsRegistry(
        Set<String> masterSkills,
        Map<String, String> aliases
) {

    public SkillsRegistry {
        masterSkills = Set.copyOf(new LinkedHashSet<>(masterSkills));
        aliases = Map.copyOf(new LinkedHashMap<>(aliases));
    }

    public String resolveCanonical(String term) {
        if (term == null || term.isBlank()) {
            return term;
        }
        String trimmed = term.trim();
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(trimmed)) {
                return entry.getValue().split(",")[0].trim();
            }
        }
        for (String skill : masterSkills) {
            if (skill.equalsIgnoreCase(trimmed)) {
                return skill;
            }
        }
        return trimmed;
    }

    public boolean containsAliasOrSkill(String text, String term) {
        if (text == null || term == null || term.isBlank()) {
            return false;
        }
        String lowerText = text.toLowerCase();
        if (lowerText.contains(term.toLowerCase())) {
            return true;
        }
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(term) && lowerText.contains(entry.getKey().toLowerCase())) {
                return true;
            }
            String mapped = entry.getValue();
            for (String part : mapped.split(",")) {
                if (part.trim().equalsIgnoreCase(term) && lowerText.contains(part.trim().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> findSkillsInText(String text) {
        return masterSkills.stream()
                .filter(skill -> containsAliasOrSkill(text, skill))
                .sorted()
                .toList();
    }
}
