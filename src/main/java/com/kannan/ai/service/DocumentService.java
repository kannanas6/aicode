package com.kannan.ai.service;


import com.kannan.ai.entity.Document;
import com.kannan.ai.entity.DocumentChunk;
import com.kannan.ai.model.DocumentChunkResponse;
import com.kannan.ai.model.DocumentResponse;
import com.kannan.ai.repository.DocumentChunkRepository;
import com.kannan.ai.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;
    private final PdfTextExtractorService pdfTextExtractorService;
    private final TextChunkService textChunkService;

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

            // 2. Split extracted text into overlapping chunks
            List<String> chunks = textChunkService.chunkText(extractedText);

            // 3. Build entity with both PDF bytes and extracted text
            Document document = Document.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(contentType)
                    .fileSize(file.getSize())
                    .fileData(file.getBytes())
                    .extractedText(extractedText)
                    .build();

            // 4. Save the document first so we have its generated id
            Document saved = documentRepository.save(document);

            // 5. Save all chunks, linked to the saved document's id
            List<DocumentChunk> documentChunks = new ArrayList<>();
            for (int i = 0; i < chunks.size(); i++) {
                documentChunks.add(DocumentChunk.builder()
                        .documentId(saved.getId())
                        .chunkIndex(i + 1)
                        .content(chunks.get(i))
                        .build());
            }
            documentChunkRepository.saveAll(documentChunks);

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

    public List<DocumentChunkResponse> getChunksByDocumentId(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new RuntimeException("Document not found with id: " + documentId);
        }
        return documentChunkRepository.findByDocumentIdOrderByChunkIndexAsc(documentId)
                .stream()
                .map(chunk -> DocumentChunkResponse.builder()
                        .chunkIndex(chunk.getChunkIndex())
                        .content(chunk.getContent())
                        .build())
                .toList();
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
