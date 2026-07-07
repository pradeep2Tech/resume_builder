package com.resumebuilder.jdanalyzer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    public record AiEnabledHolder(boolean enabled) {
    }

    @org.springframework.context.annotation.Bean
    public AiEnabledHolder aiEnabledHolder(@Value("${resume-builder.ai.enabled:false}") boolean enabled) {
        return new AiEnabledHolder(enabled);
    }
}
