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
    private final AnalysisReportWriter reportWriter;

    public JdAnalysisService(
            SkillsRegistryService skillsRegistryService,
            ResumeTextService resumeTextService,
            JdKeywordExtractorService keywordExtractorService,
            KeywordMatcherService keywordMatcherService,
            AnalysisReportWriter reportWriter) {
        this.skillsRegistryService = skillsRegistryService;
        this.resumeTextService = resumeTextService;
        this.keywordExtractorService = keywordExtractorService;
        this.keywordMatcherService = keywordMatcherService;
        this.reportWriter = reportWriter;
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
                request.jobDescriptionPath(),
                jobDescriptionFile
        );

        SkillsRegistry registry = skillsRegistryService.load();
        String resumeText = resumeTextService.loadResumeText(
                request.resumeText(),
                request.resumePath(),
                resumeFile
        );
        ExtractedJdKeywords extracted = keywordExtractorService.extract(jobDescription, registry);

        String analysisMode = keywordExtractorService.isAiEnabled() ? "spring-ai" : "rule-based";
        String reportPath = null;

        JdAnalysisResponse response = keywordMatcherService.match(
                extracted, resumeText, registry, analysisMode, null);

        if (request.writeReport()) {
            reportPath = reportWriter.write(response, request.reportSlug());
            response = new JdAnalysisResponse(
                    response.matchScore(),
                    response.requiredCount(),
                    response.matchedCount(),
                    response.requiredKeywords(),
                    response.matchedKeywords(),
                    response.missingKeywords(),
                    response.niceToHaveKeywords(),
                    response.matchedNiceToHave(),
                    response.missingNiceToHave(),
                    response.synonymMatches(),
                    response.atsFlags(),
                    response.recommendedProjects(),
                    response.analysisMode(),
                    reportPath
            );
        }

        return response;
    }
}
