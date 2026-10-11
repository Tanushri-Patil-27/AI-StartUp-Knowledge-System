
package com.ai.rag.controller;

import com.ai.rag.config.JwtUserPrincipal;
import com.ai.rag.dto.AskQuestionRequest;
import com.ai.rag.dto.AskQuestionResponse;
import com.ai.rag.service.AiService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public ResponseEntity<AskQuestionResponse> askQuestion(
            @Valid @RequestBody AskQuestionRequest request,
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal()
                instanceof String) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        if (!(authentication.getPrincipal()
                instanceof JwtUserPrincipal principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        Long userId = principal.getUserId();
        Long organizationId = principal.getOrganizationId();

        if (userId == null || organizationId == null) {
            throw new IllegalStateException(
                    "JWT does not contain userId or organizationId"
            );
        }

        String answer = aiService.askQuestion(
                request.getQuestion(),
                organizationId
        );

        AskQuestionResponse response = new AskQuestionResponse(
                answer,
                request.getQuestion(),
                organizationId,
                java.util.List.of()
        );

        return ResponseEntity.ok(response);
    }
}
