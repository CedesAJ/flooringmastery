package com.sg.flooringmastery.service;

public class InvalidOrderException extends Exception {
    InvalidOrderException(String message) {
        super(message);
    }

    InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
