package com.sg.flooringmastery.service;

public class InvalidValidationException extends Exception{
    InvalidValidationException(String message) {
        super(message);
    }

    InvalidValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
