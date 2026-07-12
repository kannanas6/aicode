package com.kannan.ai.controller;


import com.kannan.ai.entity.Document;
import com.kannan.ai.model.DocumentChunkResponse;
import com.kannan.ai.model.DocumentResponse;
import com.kannan.ai.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // Upload a PDF -> stored in MySQL (ai_knowledge_assistant DB)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> uploadDocument(@RequestParam("file") MultipartFile file) {
        DocumentResponse response = documentService.uploadDocument(file);
        return ResponseEntity.ok(response);
    }

    // List metadata of all uploaded documents
    @GetMapping
    public ResponseEntity<List<DocumentResponse>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    // Download/view the actual PDF back from the DB by id
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(document.getFileData());
    }

    // Fetch just the extracted text content of a document
    @GetMapping("/{id}/text")
    public ResponseEntity<String> getExtractedText(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        return ResponseEntity.ok(document.getExtractedText());
    }

    // Fetch all chunks for a document
    @GetMapping("/{id}/chunks")
    public ResponseEntity<List<DocumentChunkResponse>> getChunks(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getChunksByDocumentId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
