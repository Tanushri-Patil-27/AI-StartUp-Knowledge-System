package com.example.OrganizationService.exception;



public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
