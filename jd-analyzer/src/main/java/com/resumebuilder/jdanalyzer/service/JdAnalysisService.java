package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.model.ExtractedJdKeywords;
import com.resumebuilder.jdanalyzer.model.JdAnalysisRequest;
import com.resumebuilder.jdanalyzer.model.JdAnalysisResponse;
import com.resumebuilder.jdanalyzer.model.SkillsRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class JdAnalysisService {

    private final SkillsRegistryService skillsRegistryService;
    private final ResumeTextService resumeTextService;
    private final JdKeywordExtractorService keywordExtractorService;
    private final KeywordMatcherService keywordMatcherService;

    public JdAnalysisService(
            SkillsRegistryService skillsRegistryService,
            ResumeTextService resumeTextService,
            JdKeywordExtractorService keywordExtractorService,
            KeywordMatcherService keywordMatcherService) {
        this.skillsRegistryService = skillsRegistryService;
        this.resumeTextService = resumeTextService;
        this.keywordExtractorService = keywordExtractorService;
        this.keywordMatcherService = keywordMatcherService;
    }

    public JdAnalysisResponse analyze(JdAnalysisRequest request) {
        return analyze(request, null, null);
    }

    public JdAnalysisResponse analyze(
            JdAnalysisRequest request,
            MultipartFile jobDescriptionFile,
            MultipartFile resumeFile) {

        String jobDescription = resumeTextService.loadJobDescription(
                request.jobDescription(),
                jobDescriptionFile
        );

        SkillsRegistry registry = skillsRegistryService.load();
        String resumeText = resumeTextService.loadResumeText(
                request.resumeText(),
                request.resumePath(),
                resumeFile
        );
        ExtractedJdKeywords extracted = keywordExtractorService.extract(jobDescription, registry);

        return keywordMatcherService.match(extracted, resumeText, registry, "rule-based");
    }
}
