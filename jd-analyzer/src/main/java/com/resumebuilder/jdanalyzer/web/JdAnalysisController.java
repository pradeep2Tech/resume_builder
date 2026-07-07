package com.resumebuilder.jdanalyzer.web;

import com.resumebuilder.jdanalyzer.model.JdAnalysisRequest;
import com.resumebuilder.jdanalyzer.model.JdAnalysisResponse;
import com.resumebuilder.jdanalyzer.service.JdAnalysisService;
import com.resumebuilder.jdanalyzer.service.SkillsRegistryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class JdAnalysisController {

    private final JdAnalysisService jdAnalysisService;
    private final SkillsRegistryService skillsRegistryService;

    public JdAnalysisController(JdAnalysisService jdAnalysisService, SkillsRegistryService skillsRegistryService) {
        this.jdAnalysisService = jdAnalysisService;
        this.skillsRegistryService = skillsRegistryService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "jd-analyzer");
    }

    /**
     * JSON endpoint for short JD text or server-side JD file path.
     */
    @PostMapping("/jd/analyze")
    public ResponseEntity<JdAnalysisResponse> analyze(@RequestBody AnalyzeRequestBody body) {
        JdAnalysisRequest request = toRequest(body);
        return ResponseEntity.ok(jdAnalysisService.analyze(request));
    }

    /**
     * Multipart endpoint for large JD files (PDF, DOCX, TXT, MD).
     * Attach jobDescriptionFile; optionally attach resumeFile or pass resumePath.
     */
    @PostMapping(value = "/jd/analyze/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JdAnalysisResponse> analyzeUpload(
            @RequestPart("jobDescriptionFile") MultipartFile jobDescriptionFile,
            @RequestPart(value = "resumeFile", required = false) MultipartFile resumeFile,
            @RequestParam(value = "resumePath", required = false) String resumePath,
            @RequestParam(value = "reportSlug", required = false) String reportSlug,
            @RequestParam(value = "writeReport", defaultValue = "false") boolean writeReport) {

        JdAnalysisRequest request = new JdAnalysisRequest(
                null,
                null,
                null,
                resumePath,
                reportSlug,
                writeReport
        );
        return ResponseEntity.ok(jdAnalysisService.analyze(request, jobDescriptionFile, resumeFile));
    }

    @PostMapping("/skills/reload")
    public Map<String, Object> reloadSkills() {
        skillsRegistryService.reload();
        var registry = skillsRegistryService.load();
        return Map.of(
                "status", "reloaded",
                "masterSkillCount", registry.masterSkills().size(),
                "aliasCount", registry.aliases().size()
        );
    }

    private JdAnalysisRequest toRequest(AnalyzeRequestBody body) {
        return new JdAnalysisRequest(
                body.jobDescription(),
                body.jobDescriptionPath(),
                body.resumeText(),
                body.resumePath(),
                body.reportSlug(),
                body.writeReport()
        );
    }

    public record AnalyzeRequestBody(
            String jobDescription,
            String jobDescriptionPath,
            String resumeText,
            String resumePath,
            String reportSlug,
            boolean writeReport
    ) {
    }
}
