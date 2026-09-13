package com.ai.rag.service;

import java.util.List;

public interface ChunkingService {

    List<String> chunkText(String text);

    List<String> chunkText(
            String text,
            int chunkSize,
            int overlap
    );
}