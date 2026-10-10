package com.ai.rag.controller;

import com.ai.rag.config.JwtUserPrincipal;
import com.ai.rag.dto.AskQuestionRequest;
import com.ai.rag.dto.AskQuestionResponse;
import com.ai.rag.service.AiService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/ai")
public class AiController {


    private final AiService aiService;


    public AiController(
            AiService aiService) {

        this.aiService = aiService;
    }


    // =========================================================
    // ASK AI
    // =========================================================

    @PostMapping("/ask")
    public ResponseEntity<AskQuestionResponse> askQuestion(

            @Valid
            @RequestBody
            AskQuestionRequest request,

            Authentication authentication) {


        // =====================================================
        // GET JWT PRINCIPAL
        // =====================================================

        JwtUserPrincipal principal =
                (JwtUserPrincipal)
                        authentication.getPrincipal();


        // =====================================================
        // GET USER INFORMATION FROM JWT
        // =====================================================

        Long userId =
                principal.getUserId();

        Long organizationId =
                principal.getOrganizationId();

        String role =
                principal.getRole();


        // =====================================================
        // BASIC VALIDATION
        // =====================================================

        if (organizationId == null) {

            throw new IllegalStateException(
                    "JWT does not contain organizationId"
            );
        }


        if (userId == null) {

            throw new IllegalStateException(
                    "JWT does not contain userId"
            );
        }


        // =====================================================
        // ASK AI
        // =====================================================

        String answer =
                aiService.askQuestion(
                        request.getQuestion(),
                        organizationId
                );


        // =====================================================
        // RESPONSE
        // =====================================================

        AskQuestionResponse response =
                new AskQuestionResponse(
                        answer,
                        request.getQuestion(),
                        organizationId,
                        new ArrayList<>()
                );


        return ResponseEntity.ok(
                response
        );
    }
}