package com.resumebuilder.jdanalyzer.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;

@Service
public class DocumentTextExtractorService {

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(
            ".pdf", ".docx", ".txt", ".md", ".markdown"
    );

    public String extractFromPath(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File not found: " + path);
        }
        String fileName = path.getFileName().toString();
        try {
            return extractByFileName(fileName, Files.readAllBytes(path));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read file: " + path, e);
        }
    }

    public String extractFromUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Uploaded file must have a filename");
        }
        validateExtension(fileName);
        try {
            return extractByFileName(fileName, file.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read uploaded file: " + fileName, e);
        }
    }

    public void validateExtension(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        boolean supported = SUPPORTED_EXTENSIONS.stream().anyMatch(lower::endsWith);
        if (!supported) {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + fileName + ". Supported: PDF, DOCX, TXT, MD");
        }
    }

    private String extractByFileName(String fileName, byte[] content) throws IOException {
        String lower = fileName.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".pdf")) {
            return extractPdf(content);
        }
        if (lower.endsWith(".docx")) {
            return extractDocx(content);
        }
        return normalizeText(new String(content, StandardCharsets.UTF_8));
    }

    private String extractDocx(byte[] content) throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(content);
             XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return normalizeText(extractor.getText());
        }
    }

    private String extractPdf(byte[] content) throws IOException {
        try (PDDocument document = Loader.loadPDF(content)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return normalizeText(stripper.getText(document));
        }
    }

    private String normalizeText(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\u0000", "")
                .replaceAll("\r\n", "\n")
                .trim();
    }
}
