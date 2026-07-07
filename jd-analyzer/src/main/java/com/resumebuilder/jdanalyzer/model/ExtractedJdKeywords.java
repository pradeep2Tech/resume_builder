package com.resumebuilder.jdanalyzer.model;

import java.util.List;

public record ExtractedJdKeywords(
        List<String> required,
        List<String> niceToHave,
        List<String> certifications,
        List<String> softSkills
) {
}
