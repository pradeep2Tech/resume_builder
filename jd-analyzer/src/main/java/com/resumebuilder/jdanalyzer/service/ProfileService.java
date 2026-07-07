package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.config.ResumeBuilderProperties;
import com.resumebuilder.jdanalyzer.model.ProfileSummary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProfileService {

    private static final Pattern FIELD = Pattern.compile("\\*\\*(.+?)\\*\\*:\\s*(.+)");

    private final ResumeBuilderProperties properties;

    public ProfileService(ResumeBuilderProperties properties) {
        this.properties = properties;
    }

    public ProfileSummary load() {
        Path summaryPath = properties.careerMasterRoot().resolve("profile/career-summary.md");
        try {
            String content = Files.readString(summaryPath);
            return new ProfileSummary(
                    field(content, "Name"),
                    field(content, "Title"),
                    field(content, "Location"),
                    properties.getDefaultResume()
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read career summary: " + summaryPath, e);
        }
    }

    private String field(String content, String label) {
        Matcher matcher = FIELD.matcher(content);
        while (matcher.find()) {
            if (label.equalsIgnoreCase(matcher.group(1).trim())) {
                return matcher.group(2).trim();
            }
        }
        return "";
    }
}
