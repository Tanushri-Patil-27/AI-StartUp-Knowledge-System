package com.ai.rag.service;

public interface EmbeddingService {

    float[] generateEmbedding(String text);

    int getEmbeddingDimension();
}