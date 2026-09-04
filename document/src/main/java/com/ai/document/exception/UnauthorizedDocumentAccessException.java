package com.ai.document.exception;

public class UnauthorizedDocumentAccessException extends RuntimeException {

    public UnauthorizedDocumentAccessException(String message) {
        super(message);
    }
}