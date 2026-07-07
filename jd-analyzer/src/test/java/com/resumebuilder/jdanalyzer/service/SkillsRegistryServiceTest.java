package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.config.ResumeBuilderProperties;
import com.resumebuilder.jdanalyzer.model.SkillsRegistry;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class SkillsRegistryServiceTest {

    @Test
    void loadsSkillsAndAliasesFromCareerMaster() {
        ResumeBuilderProperties properties = new ResumeBuilderProperties();
        properties.setCareerMasterPath(
                Path.of("..", "career-master").toAbsolutePath().normalize().toString()
        );

        SkillsRegistryService service = new SkillsRegistryService(properties);
        SkillsRegistry registry = service.load();

        assertThat(registry.masterSkills()).isNotEmpty();
        assertThat(registry.masterSkills()).anyMatch(s -> s.toLowerCase().contains("java"));
        assertThat(registry.aliases()).containsKey("K8s");
        assertThat(registry.resolveCanonical("K8s")).isEqualTo("Kubernetes");
    }
}
