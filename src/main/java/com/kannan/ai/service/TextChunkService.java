package com.kannan.ai.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkService {

    private static final int CHUNK_SIZE = 500;
    private static final int OVERLAP = 100;

    /**
     * Splits text into overlapping chunks.
     * Chunk size = 500 characters, overlap = 100 characters.
     * e.g. chunk1 = [0,500), chunk2 = [400,900), chunk3 = [800,1300) ...
     *
     * @param text the full extracted text
     * @return list of text chunks in order
     */
    public List<String> chunkText(String text) {
        List<String> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks;
        }

        int step = CHUNK_SIZE - OVERLAP; // how far the window advances each time (400)
        int length = text.length();
        int start = 0;

        while (start < length) {
            int end = Math.min(start + CHUNK_SIZE, length);
            chunks.add(text.substring(start, end));

            if (end == length) {
                break;
            }
            start += step;
        }

        return chunks;
    }
}
