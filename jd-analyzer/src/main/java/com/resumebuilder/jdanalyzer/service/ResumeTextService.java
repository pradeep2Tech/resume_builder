package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.config.ResumeBuilderProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Service
public class ResumeTextService {

    private final ResumeBuilderProperties properties;
    private final DocumentTextExtractorService documentTextExtractor;

    public ResumeTextService(
            ResumeBuilderProperties properties,
            DocumentTextExtractorService documentTextExtractor) {
        this.properties = properties;
        this.documentTextExtractor = documentTextExtractor;
    }

    public String loadResumeText(String resumeText, String resumePath) {
        return loadResumeText(resumeText, resumePath, null);
    }

    public String loadResumeText(String resumeText, String resumePath, MultipartFile resumeFile) {
        if (resumeFile != null && !resumeFile.isEmpty()) {
            return stripFrontMatter(documentTextExtractor.extractFromUpload(resumeFile));
        }
        if (resumeText != null && !resumeText.isBlank()) {
            return stripFrontMatter(resumeText);
        }
        Path path = resolveResumePath(resumePath);
        return stripFrontMatter(documentTextExtractor.extractFromPath(path));
    }

    public String loadJobDescription(String jobDescriptionText, String jobDescriptionPath, MultipartFile jobDescriptionFile) {
        if (jobDescriptionFile != null && !jobDescriptionFile.isEmpty()) {
            return documentTextExtractor.extractFromUpload(jobDescriptionFile);
        }
        if (jobDescriptionPath != null && !jobDescriptionPath.isBlank()) {
            Path path = resolvePath(jobDescriptionPath);
            return documentTextExtractor.extractFromPath(path);
        }
        if (jobDescriptionText != null && !jobDescriptionText.isBlank()) {
            return jobDescriptionText.trim();
        }
        throw new IllegalArgumentException(
                "Job description is required: provide jobDescriptionFile, jobDescriptionPath, or jobDescription text");
    }

    public String loadDefaultResume() {
        return stripFrontMatter(documentTextExtractor.extractFromPath(properties.defaultResumePath()));
    }

    private Path resolveResumePath(String resumePath) {
        if (resumePath == null || resumePath.isBlank()) {
            return properties.defaultResumePath();
        }
        return resolvePath(resumePath);
    }

    private Path resolvePath(String filePath) {
        Path path = Path.of(filePath);
        if (path.isAbsolute()) {
            return path.normalize();
        }
        return properties.careerMasterRoot().resolve(filePath).normalize();
    }

    String stripFrontMatter(String content) {
        if (content.startsWith("---")) {
            int end = content.indexOf("---", 3);
            if (end > 0) {
                return content.substring(end + 3).trim();
            }
        }
        return content.trim();
    }
}
