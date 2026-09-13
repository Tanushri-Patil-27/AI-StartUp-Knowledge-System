package com.ai.rag.dto;

import java.util.ArrayList;
import java.util.List;

public class AskQuestionResponse {

    private String answer;

    private String question;

    private Long organizationId;

    private List<SourceDocumentResponse> sources;


    public AskQuestionResponse() {

        this.sources = new ArrayList<>();
    }


    public AskQuestionResponse(
            String answer,
            String question,
            Long organizationId,
            List<SourceDocumentResponse> sources) {

        this.answer = answer;
        this.question = question;
        this.organizationId = organizationId;
        this.sources = sources;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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


    public List<SourceDocumentResponse> getSources() {
        return sources;
    }

    public void setSources(
            List<SourceDocumentResponse> sources) {

        this.sources = sources;
    }
}