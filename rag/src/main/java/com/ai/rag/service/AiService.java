package com.ai.rag.service;

public interface AiService {

    String askQuestion(
            String question,
            Long organizationId
    );
}