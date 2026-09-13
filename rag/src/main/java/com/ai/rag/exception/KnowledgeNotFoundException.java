package com.ai.rag.exception;

public class KnowledgeNotFoundException extends RuntimeException {

    public KnowledgeNotFoundException(String message) {
        super(message);
    }
}