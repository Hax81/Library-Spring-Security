package com.example.library_system.exception_handling;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException (String exceptionInfoMessage) {
        super (exceptionInfoMessage);
    }
}