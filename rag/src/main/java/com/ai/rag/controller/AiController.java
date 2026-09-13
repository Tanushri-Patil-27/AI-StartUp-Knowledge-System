package com.ai.rag.controller;

import com.ai.rag.client.OrganizationClient;
import com.ai.rag.dto.AskQuestionRequest;
import com.ai.rag.dto.AskQuestionResponse;
import com.ai.rag.dto.OrganizationMemberAccessResponse;
import com.ai.rag.service.AiService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/ai")
public class AiController {


    private final AiService aiService;

    private final OrganizationClient organizationClient;


    public AiController(
            AiService aiService,
            OrganizationClient organizationClient) {

        this.aiService = aiService;
        this.organizationClient =
                organizationClient;
    }


    /**
     * Ask a question about the organization's
     * stored knowledge.
     *
     * POST /api/ai/ask
     */
    @PostMapping("/ask")
    public ResponseEntity<AskQuestionResponse> askQuestion(

            @Valid
            @RequestBody
            AskQuestionRequest request,

            Authentication authentication) {


        /*
         * Get currently authenticated user ID.
         *
         * JwtAuthenticationFilter sets userId
         * as authentication.getName().
         */
        Long currentUserId;

        try {

            currentUserId =
                    Long.valueOf(
                            authentication.getName()
                    );

        } catch (NumberFormatException e) {

            throw new IllegalStateException(
                    "JWT does not contain a valid userId. "
                    + "Update User Service JWT to include userId."
            );
        }


        /*
         * Check whether user belongs to
         * requested organization.
         */
        OrganizationMemberAccessResponse access =
                organizationClient.checkMemberAccess(
                        request.getOrganizationId(),
                        currentUserId
                );


        if (access == null
                || !access.isMember()) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            new AskQuestionResponse(
                                    "You do not have access to this organization.",
                                    request.getQuestion(),
                                    request.getOrganizationId(),
                                    new ArrayList<>()
                            )
                    );
        }


        /*
         * Ask AI service.
         *
         * AI service will:
         *
         * Question
         *      ↓
         * Qdrant similarity search
         *      ↓
         * Relevant chunks
         *      ↓
         * Context
         *      ↓
         * OpenAI
         *      ↓
         * Answer
         */
        String answer =
                aiService.askQuestion(
                        request.getQuestion(),
                        request.getOrganizationId()
                );


        /*
         * Build response.
         */
        AskQuestionResponse response =
                new AskQuestionResponse(
                        answer,
                        request.getQuestion(),
                        request.getOrganizationId(),
                        new ArrayList<>()
                );


        return ResponseEntity.ok(response);
    }
}