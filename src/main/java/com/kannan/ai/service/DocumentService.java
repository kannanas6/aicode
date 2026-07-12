package com.kannan.ai.service;

import com.kannan.ai.entity.Document;
import com.kannan.ai.model.DocumentResponse;
import com.kannan.ai.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final PdfTextExtractorService pdfTextExtractorService;

    public DocumentResponse uploadDocument(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        try {
            // 1. Extract text from the PDF
            String extractedText = pdfTextExtractorService.extractText(file);
            System.out.println("Extracted text length: " + extractedText.length());
            // 2. Build entity with both PDF bytes and extracted text
            Document document = Document.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(contentType)
                    .fileSize(file.getSize())
                    .fileData(file.getBytes())
                    .extractedText(extractedText)
                    .build();

            // 3. Save PDF + extracted text together
            Document saved = documentRepository.save(document);
            return toResponse(saved);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read PDF file bytes", e);
        }
    }

    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Document not found with id: " + id);
        }
        documentRepository.deleteById(id);
    }

    private DocumentResponse toResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .fileName(document.getFileName())
                .fileType(document.getFileType())
                .fileSize(document.getFileSize())
                .extractedText(document.getExtractedText())
                .uploadedAt(document.getUploadedAt())
                .build();
    }
}
