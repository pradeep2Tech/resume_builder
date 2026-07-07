package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.config.ResumeBuilderProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResumeTextServiceTest {

    private final ResumeTextService service = new ResumeTextService(
            new ResumeBuilderProperties(),
            new DocumentTextExtractorService()
    );

    @Test
    void stripsYamlFrontMatter() {
        String input = """
                ---
                slug: test
                ---
                # Resume body
                Java developer
                """;
        String result = service.stripFrontMatter(input);
        assertThat(result).startsWith("# Resume body");
        assertThat(result).contains("Java developer");
        assertThat(result).doesNotContain("slug:");
    }
}
