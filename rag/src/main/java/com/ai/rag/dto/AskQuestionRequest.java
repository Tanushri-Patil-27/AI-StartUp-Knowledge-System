package com.ai.rag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AskQuestionRequest {

    @NotBlank(message = "Question is required")
    @Size(
            max = 2000,
            message = "Question cannot exceed 2000 characters"
    )
    private String question;


    public AskQuestionRequest() {
    }


    public AskQuestionRequest(
            String question) {

        this.question = question;
    }


    public String getQuestion() {
        return question;
    }


    public void setQuestion(String question) {
        this.question = question;
    }
}