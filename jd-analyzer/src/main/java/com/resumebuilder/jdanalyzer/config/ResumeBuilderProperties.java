package com.resumebuilder.jdanalyzer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "resume-builder")
public class ResumeBuilderProperties {

    private String careerMasterPath = "../career-master";
    private String outputSubdir = "output";
    private String skillsFile = "profile/skills.md";
    private String defaultResume = "cvs/pradeep-cv-2026-ats/resume.md";

    public Path careerMasterRoot() {
        return Path.of(careerMasterPath).toAbsolutePath().normalize();
    }

    public Path skillsPath() {
        return careerMasterRoot().resolve(skillsFile);
    }

    public Path outputDir() {
        return careerMasterRoot().resolve(outputSubdir);
    }

    public Path defaultResumePath() {
        return careerMasterRoot().resolve(defaultResume);
    }

    public String getCareerMasterPath() {
        return careerMasterPath;
    }

    public void setCareerMasterPath(String careerMasterPath) {
        this.careerMasterPath = careerMasterPath;
    }

    public String getOutputSubdir() {
        return outputSubdir;
    }

    public void setOutputSubdir(String outputSubdir) {
        this.outputSubdir = outputSubdir;
    }

    public String getSkillsFile() {
        return skillsFile;
    }

    public void setSkillsFile(String skillsFile) {
        this.skillsFile = skillsFile;
    }

    public String getDefaultResume() {
        return defaultResume;
    }

    public void setDefaultResume(String defaultResume) {
        this.defaultResume = defaultResume;
    }
}
