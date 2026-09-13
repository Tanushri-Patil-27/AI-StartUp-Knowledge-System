package com.ai.rag.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingServiceImpl implements ChunkingService {

    private static final int DEFAULT_CHUNK_SIZE = 1000;

    private static final int DEFAULT_OVERLAP = 150;


    @Override
    public List<String> chunkText(String text) {

        return chunkText(
                text,
                DEFAULT_CHUNK_SIZE,
                DEFAULT_OVERLAP
        );
    }


    @Override
    public List<String> chunkText(
            String text,
            int chunkSize,
            int overlap) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        if (chunkSize <= 0) {
            throw new IllegalArgumentException(
                    "Chunk size must be greater than 0"
            );
        }

        if (overlap < 0 || overlap >= chunkSize) {
            throw new IllegalArgumentException(
                    "Overlap must be >= 0 and smaller than chunk size"
            );
        }


        String cleanedText = cleanText(text);

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < cleanedText.length()) {

            int end = Math.min(
                    start + chunkSize,
                    cleanedText.length()
            );

            /*
             * Try to end the chunk at a sentence or whitespace boundary.
             */
            if (end < cleanedText.length()) {

                int sentenceEnd =
                        findLastSentenceBoundary(
                                cleanedText,
                                start,
                                end
                        );

                if (sentenceEnd > start) {
                    end = sentenceEnd;
                } else {

                    int whitespaceIndex =
                            cleanedText.lastIndexOf(" ", end);

                    if (whitespaceIndex > start) {
                        end = whitespaceIndex;
                    }
                }
            }


            String chunk =
                    cleanedText.substring(start, end).trim();


            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }


            if (end >= cleanedText.length()) {
                break;
            }


            int nextStart = end - overlap;


            if (nextStart <= start) {
                nextStart = end;
            }

            start = nextStart;
        }


        return chunks;
    }


    private String cleanText(String text) {

        return text
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replaceAll("[ \t]+", " ")
                .replaceAll("\n{3,}", "\n\n")
                .trim();
    }


    private int findLastSentenceBoundary(
            String text,
            int start,
            int end) {

        int lastPeriod =
                text.lastIndexOf(".", end - 1);

        int lastQuestion =
                text.lastIndexOf("?", end - 1);

        int lastExclamation =
                text.lastIndexOf("!", end - 1);

        int boundary =
                Math.max(
                        lastPeriod,
                        Math.max(
                                lastQuestion,
                                lastExclamation
                        )
                );

        if (boundary > start) {
            return boundary + 1;
        }

        return -1;
    }
}