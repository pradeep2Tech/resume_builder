package com.resumebuilder.jdanalyzer.model;

import java.util.List;

public record JdAnalysisRequest(
        String jobDescription,
        String jobDescriptionPath,
        String resumeText,
        String resumePath,
        String reportSlug,
        boolean writeReport
) {
}
