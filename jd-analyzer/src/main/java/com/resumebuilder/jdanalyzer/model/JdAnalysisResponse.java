package com.resumebuilder.jdanalyzer.model;

import java.util.List;

public record JdAnalysisResponse(
        int matchScore,
        int requiredCount,
        int matchedCount,
        List<String> requiredKeywords,
        List<String> matchedKeywords,
        List<String> missingKeywords,
        List<String> niceToHaveKeywords,
        List<String> matchedNiceToHave,
        List<String> missingNiceToHave,
        List<SynonymMatch> synonymMatches,
        List<String> atsFlags,
        List<String> recommendedProjects,
        String analysisMode,
        String reportPath
) {
}
