package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.config.ResumeBuilderProperties;
import com.resumebuilder.jdanalyzer.model.SkillsRegistry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SkillsRegistryService {

    private static final Pattern TABLE_ROW = Pattern.compile("^\\|\\s*([^|]+?)\\s*\\|");
    private static final Pattern ALIAS_ROW = Pattern.compile("^\\|\\s*([^|]+?)\\s*\\|\\s*([^|]+?)\\s*\\|");

    private final ResumeBuilderProperties properties;
    private volatile SkillsRegistry cachedRegistry;

    public SkillsRegistryService(ResumeBuilderProperties properties) {
        this.properties = properties;
    }

    public SkillsRegistry load() {
        SkillsRegistry registry = cachedRegistry;
        if (registry != null) {
            return registry;
        }
        synchronized (this) {
            if (cachedRegistry == null) {
                cachedRegistry = parseSkillsFile();
            }
            return cachedRegistry;
        }
    }

    public void reload() {
        synchronized (this) {
            cachedRegistry = parseSkillsFile();
        }
    }

    private SkillsRegistry parseSkillsFile() {
        try {
            List<String> lines = Files.readAllLines(properties.skillsPath());
            Set<String> masterSkills = new LinkedHashSet<>();
            Map<String, String> aliases = new LinkedHashMap<>();
            boolean inAliasSection = false;

            for (String line : lines) {
                if (line.startsWith("## JD matching aliases")) {
                    inAliasSection = true;
                    continue;
                }
                if (inAliasSection) {
                    parseAliasRow(line, aliases);
                    continue;
                }
                if (line.startsWith("|") && !line.contains("---")) {
                    parseSkillRow(line, masterSkills);
                }
            }
            addAliasKeysAsSearchTerms(aliases, masterSkills);
            return new SkillsRegistry(masterSkills, aliases);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read skills file: " + properties.skillsPath(), e);
        }
    }

    private void parseSkillRow(String line, Set<String> masterSkills) {
        Matcher matcher = TABLE_ROW.matcher(line);
        if (!matcher.find()) {
            return;
        }
        String firstCell = matcher.group(1).trim();
        if (firstCell.equalsIgnoreCase("Skill") || firstCell.equalsIgnoreCase("JD term")) {
            return;
        }
        for (String part : firstCell.split("/")) {
            String skill = part.trim();
            if (!skill.isEmpty()) {
                masterSkills.add(skill);
            }
        }
    }

    private void parseAliasRow(String line, Map<String, String> aliases) {
        Matcher matcher = ALIAS_ROW.matcher(line);
        if (!matcher.find()) {
            return;
        }
        String jdTerm = matcher.group(1).trim();
        String mapsTo = matcher.group(2).trim();
        if (jdTerm.equalsIgnoreCase("JD term") || jdTerm.startsWith("---")) {
            return;
        }
        aliases.put(jdTerm, mapsTo);
    }

    private void addAliasKeysAsSearchTerms(Map<String, String> aliases, Set<String> masterSkills) {
        for (String alias : aliases.keySet()) {
            masterSkills.add(alias);
        }
    }
}
