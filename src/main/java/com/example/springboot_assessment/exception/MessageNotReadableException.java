package com.example.springboot_assessment.exception;

public class MessageNotReadableException extends RuntimeException {

    public MessageNotReadableException() {
        super("Invalid Data. Please try again.");
    }
}
