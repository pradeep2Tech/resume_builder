package com.resumebuilder.jdanalyzer.web;

import com.resumebuilder.jdanalyzer.model.CvSummary;
import com.resumebuilder.jdanalyzer.model.JdAnalysisRequest;
import com.resumebuilder.jdanalyzer.model.JdAnalysisResponse;
import com.resumebuilder.jdanalyzer.model.ProfileSummary;
import com.resumebuilder.jdanalyzer.service.CvRegistryService;
import com.resumebuilder.jdanalyzer.service.JdAnalysisService;
import com.resumebuilder.jdanalyzer.service.ProfileService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class JdAnalysisController {

    private final JdAnalysisService jdAnalysisService;
    private final ProfileService profileService;
    private final CvRegistryService cvRegistryService;
    private final SkillsRegistryService skillsRegistryService;

    public JdAnalysisController(
            JdAnalysisService jdAnalysisService,
            ProfileService profileService,
            CvRegistryService cvRegistryService,
            SkillsRegistryService skillsRegistryService) {
        this.jdAnalysisService = jdAnalysisService;
        this.profileService = profileService;
        this.cvRegistryService = cvRegistryService;
        this.skillsRegistryService = skillsRegistryService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "resume-builder");
    }

    @GetMapping("/profile")
    public ProfileSummary profile() {
        return profileService.load();
    }

    @GetMapping("/cvs")
    public List<CvSummary> cvs() {
        return cvRegistryService.listCvs();
    }

    @PostMapping("/jd/analyze")
    public ResponseEntity<JdAnalysisResponse> analyze(@RequestBody AnalyzeRequestBody body) {
        JdAnalysisRequest request = new JdAnalysisRequest(
                body.jobDescription(),
                null,
                body.resumePath()
        );
        return ResponseEntity.ok(jdAnalysisService.analyze(request));
    }

    @PostMapping(value = "/jd/analyze/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JdAnalysisResponse> analyzeUpload(
            @RequestPart("jobDescriptionFile") MultipartFile jobDescriptionFile,
            @RequestParam(value = "resumePath", required = false) String resumePath) {

        JdAnalysisRequest request = new JdAnalysisRequest(null, null, resumePath);
        return ResponseEntity.ok(jdAnalysisService.analyze(request, jobDescriptionFile, null));
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

    public record AnalyzeRequestBody(String jobDescription, String resumePath) {
    }
}
