package com.resumebuilder.jdanalyzer.model;

public record JdAnalysisRequest(
        String jobDescription,
        String resumeText,
        String resumePath
) {
}
