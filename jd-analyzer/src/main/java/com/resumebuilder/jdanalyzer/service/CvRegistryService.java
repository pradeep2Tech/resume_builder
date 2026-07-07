package com.resumebuilder.jdanalyzer.service;

import com.resumebuilder.jdanalyzer.config.ResumeBuilderProperties;
import com.resumebuilder.jdanalyzer.model.CvSummary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CvRegistryService {

    private final ResumeBuilderProperties properties;

    public CvRegistryService(ResumeBuilderProperties properties) {
        this.properties = properties;
    }

    public List<CvSummary> listCvs() {
        Path cvsRoot = properties.careerMasterRoot().resolve("cvs");
        if (!Files.isDirectory(cvsRoot)) {
            return List.of();
        }

        List<CvSummary> cvs = new ArrayList<>();
        try (Stream<Path> dirs = Files.list(cvsRoot)) {
            dirs.filter(Files::isDirectory)
                    .sorted(Comparator.comparing(Path::getFileName))
                    .forEach(dir -> {
                        Path resume = dir.resolve("resume.md");
                        if (Files.isRegularFile(resume)) {
                            String slug = dir.getFileName().toString();
                            String relative = "cvs/" + slug + "/resume.md";
                            cvs.add(new CvSummary(slug, relative));
                        }
                    });
        } catch (IOException e) {
            throw new IllegalStateException("Failed to scan CV registry: " + cvsRoot, e);
        }
        return cvs;
    }
}
