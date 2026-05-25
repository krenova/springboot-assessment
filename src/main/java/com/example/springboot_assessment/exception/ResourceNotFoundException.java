package com.example.springboot_assessment.exception;

public class ResourceNotFoundException extends Throwable {

    public ResourceNotFoundException(String message) {
        super(String.format("%s Please try again.",message));
    }
}
