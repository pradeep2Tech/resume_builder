package com.resumebuilder.jdanalyzer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumebuilder.jdanalyzer.config.AiConfiguration.AiEnabledHolder;
import com.resumebuilder.jdanalyzer.model.ExtractedJdKeywords;
import com.resumebuilder.jdanalyzer.model.SkillsRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class JdKeywordExtractorService {

    private static final Logger log = LoggerFactory.getLogger(JdKeywordExtractorService.class);
    private static final Pattern JSON_BLOCK = Pattern.compile("```(?:json)?\\s*([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    private final ObjectMapper objectMapper;
    private final boolean aiEnabled;
    private final ChatClient chatClient;

    public JdKeywordExtractorService(
            ObjectProvider<ChatClient.Builder> chatClientBuilder,
            ObjectMapper objectMapper,
            AiEnabledHolder aiEnabledHolder) {
        this.objectMapper = objectMapper;
        this.aiEnabled = aiEnabledHolder.enabled() && chatClientBuilder.getIfAvailable() != null;
        this.chatClient = aiEnabled
                ? chatClientBuilder.getObject().build()
                : null;
    }

    public ExtractedJdKeywords extract(String jobDescription, SkillsRegistry registry) {
        if (aiEnabled && chatClient != null) {
            try {
                return extractWithAi(jobDescription);
            } catch (Exception e) {
                log.warn("AI extraction failed, falling back to rule-based extraction: {}", e.getMessage());
            }
        }
        return extractWithRules(jobDescription, registry);
    }

    public boolean isAiEnabled() {
        return aiEnabled;
    }

    private ExtractedJdKeywords extractWithAi(String jobDescription) throws Exception {
        String prompt = """
                Extract ATS-relevant keywords from this job description.
                Return ONLY valid JSON with this schema:
                {
                  "required": ["skill or keyword"],
                  "niceToHave": ["skill or keyword"],
                  "certifications": ["cert name"],
                  "softSkills": ["leadership, communication, etc."]
                }
                Rules:
                - required = must-have skills, tools, platforms, years-of-experience phrases
                - niceToHave = preferred/bonus skills only
                - Use concise canonical terms (e.g. Kubernetes not K8s unless JD only says K8s)
                - Do not invent skills not present in the JD

                Job description:
                %s
                """.formatted(jobDescription);

        String raw = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        String json = unwrapJson(raw);
        JsonNode root = objectMapper.readTree(json);
        return new ExtractedJdKeywords(
                readArray(root, "required"),
                readArray(root, "niceToHave"),
                readArray(root, "certifications"),
                readArray(root, "softSkills")
        );
    }

    private ExtractedJdKeywords extractWithRules(String jobDescription, SkillsRegistry registry) {
        String jdLower = jobDescription.toLowerCase(Locale.ROOT);
        Set<String> required = new LinkedHashSet<>();
        Set<String> niceToHave = new LinkedHashSet<>();

        for (String skill : registry.masterSkills()) {
            if (containsTerm(jdLower, skill)) {
                if (isNiceToHaveContext(jobDescription, skill)) {
                    niceToHave.add(skill);
                } else {
                    required.add(skill);
                }
            }
        }

        for (String alias : registry.aliases().keySet()) {
            if (containsTerm(jdLower, alias)) {
                String canonical = registry.resolveCanonical(alias);
                if (isNiceToHaveContext(jobDescription, alias)) {
                    niceToHave.add(canonical);
                } else {
                    required.add(canonical);
                }
            }
        }

        niceToHave.removeAll(required);
        return new ExtractedJdKeywords(
                new ArrayList<>(required),
                new ArrayList<>(niceToHave),
                List.of(),
                List.of()
        );
    }

    private boolean isNiceToHaveContext(String jobDescription, String term) {
        String lower = jobDescription.toLowerCase(Locale.ROOT);
        int index = lower.indexOf(term.toLowerCase(Locale.ROOT));
        if (index < 0) {
            return false;
        }
        int windowStart = Math.max(0, index - 120);
        String window = lower.substring(windowStart, index);
        return window.contains("nice to have")
                || window.contains("preferred")
                || window.contains("bonus")
                || window.contains("plus");
    }

    private boolean containsTerm(String jdLower, String term) {
        String normalized = term.toLowerCase(Locale.ROOT).trim();
        if (normalized.isEmpty()) {
            return false;
        }
        if (normalized.contains(" ")) {
            return jdLower.contains(normalized);
        }
        return Pattern.compile("\\b" + Pattern.quote(normalized) + "\\b", Pattern.CASE_INSENSITIVE)
                .matcher(jdLower)
                .find();
    }

    private List<String> readArray(JsonNode root, String field) {
        JsonNode node = root.path(field);
        if (!node.isArray()) {
            return List.of();
        }
        List<String> values = new ArrayList<>();
        node.forEach(item -> {
            if (item.isTextual() && !item.asText().isBlank()) {
                values.add(item.asText().trim());
            }
        });
        return values;
    }

    private String unwrapJson(String raw) {
        if (raw == null) {
            return "{}";
        }
        var matcher = JSON_BLOCK.matcher(raw.trim());
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        int start = raw.indexOf('{');
        int end = raw.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return raw.substring(start, end + 1);
        }
        return raw.trim();
    }
}
