package com.ai.rag.dto;

import java.util.List;

public record AiAnswerResult(
        String answer,
        List<AnswerSourceDTO> sources
) {
}
