package com.ai.rag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AskQuestionRequest {

    @NotBlank(message = "Question is required")
    @Size(max = 2000, message = "Question cannot exceed 2000 characters")
    private String question;

    @NotNull(message = "Organization ID is required")
    private Long organizationId;


    public AskQuestionRequest() {
    }


    public AskQuestionRequest(
            String question,
            Long organizationId) {

        this.question = question;
        this.organizationId = organizationId;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}