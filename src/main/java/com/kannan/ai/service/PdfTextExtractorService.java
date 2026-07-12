package com.kannan.ai.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PdfTextExtractorService {

    /**
     * Extracts all readable text from an uploaded PDF file.
     *
     * @param file the uploaded PDF (as MultipartFile)
     * @return the extracted text content, or an empty string if the PDF has no extractable text
     */
    public String extractText(MultipartFile file) {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {

            if (document.isEncrypted()) {
                throw new RuntimeException("Cannot extract text: PDF is encrypted/password-protected");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);

            String text = stripper.getText(document);
            return text != null ? text.trim() : "";

        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from PDF: " + file.getOriginalFilename(), e);
        }
    }
}
