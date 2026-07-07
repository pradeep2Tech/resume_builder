package com.resumebuilder.jdanalyzer.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JdAnalysisControllerUploadTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void analyzeUploadAcceptsJobDescriptionFile() throws Exception {
        String jd = """
                Principal System Architect

                Required skills:
                - Java 17 and Spring Boot
                - Kubernetes and Kafka
                - Microservices architecture
                - Domain-Driven Design
                - OpenTelemetry

                Nice to have: Terraform, AWS
                """;

        MockMultipartFile jdFile = new MockMultipartFile(
                "jobDescriptionFile",
                "architect-jd.txt",
                "text/plain",
                jd.getBytes()
        );

        mockMvc.perform(multipart("/api/v1/jd/analyze/upload")
                        .file(jdFile)
                        .param("resumePath", "cvs/pradeep-cv-2026-ats/resume.md"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchScore").isNumber())
                .andExpect(jsonPath("$.requiredKeywords").isArray())
                .andExpect(jsonPath("$.analysisMode").value("rule-based"));
    }
}
