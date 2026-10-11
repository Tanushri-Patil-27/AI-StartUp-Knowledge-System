package com.ai.rag.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerSourceDTO {

    private String documentId;
    private String sourceFileName;
    private Integer chunkIndex;
}

