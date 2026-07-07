package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.model.ExtractedJdKeywords;
import com.resumebuilder.jdanalyzer.model.JdAnalysisResponse;
import com.resumebuilder.jdanalyzer.model.SkillsRegistry;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class KeywordMatcherServiceTest {

    private final KeywordMatcherService matcher = new KeywordMatcherService();

    @Test
    void computesMatchScoreFromRequiredKeywords() {
        SkillsRegistry registry = new SkillsRegistry(
                Set.of("Java", "Kubernetes", "Kafka"),
                Map.of("K8s", "Kubernetes")
        );

        ExtractedJdKeywords extracted = new ExtractedJdKeywords(
                List.of("Java", "Kubernetes", "Kafka"),
                List.of("Redis"),
                List.of(),
                List.of()
        );

        String resume = "Senior Java architect with Kafka experience on Kubernetes clusters.";

        JdAnalysisResponse response = matcher.match(extracted, resume, registry, "test", null);

        assertThat(response.matchScore()).isEqualTo(100);
        assertThat(response.missingKeywords()).isEmpty();
        assertThat(response.matchedKeywords()).contains("Java", "Kubernetes", "Kafka");
    }

    @Test
    void reportsMissingKeywords() {
        SkillsRegistry registry = new SkillsRegistry(
                new LinkedHashSet<>(List.of("Java", "Terraform", "AWS")),
                new LinkedHashMap<>()
        );

        ExtractedJdKeywords extracted = new ExtractedJdKeywords(
                List.of("Java", "Terraform", "AWS"),
                List.of(),
                List.of(),
                List.of()
        );

        JdAnalysisResponse response = matcher.match(
                extracted,
                "Java microservices developer",
                registry,
                "test",
                null
        );

        assertThat(response.matchScore()).isEqualTo(33);
        assertThat(response.missingKeywords()).contains("Terraform", "AWS");
    }
}
