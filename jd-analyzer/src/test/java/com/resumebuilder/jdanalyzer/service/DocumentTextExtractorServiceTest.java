package com.resumebuilder.jdanalyzer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentTextExtractorServiceTest {

    private final DocumentTextExtractorService extractor = new DocumentTextExtractorService();

    @TempDir
    Path tempDir;

    @Test
    void extractsPlainTextFile() throws Exception {
        Path file = tempDir.resolve("job-description.txt");
        Files.writeString(file, "Required: Java, Spring Boot, Kubernetes, Kafka.");

        String text = extractor.extractFromPath(file);

        assertThat(text).contains("Java");
        assertThat(text).contains("Kubernetes");
    }

    @Test
    void extractsMarkdownFile() throws Exception {
        Path file = tempDir.resolve("jd.md");
        Files.writeString(file, "# Role\n\nMust have **Golang** and Redis experience.");

        String text = extractor.extractFromPath(file);

        assertThat(text).contains("Golang");
        assertThat(text).contains("Redis");
    }
}
