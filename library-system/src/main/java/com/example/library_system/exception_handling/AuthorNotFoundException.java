package com.example.library_system.exception_handling;

public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(String exceptionInfoMessage) {
        super(exceptionInfoMessage);
    }
}