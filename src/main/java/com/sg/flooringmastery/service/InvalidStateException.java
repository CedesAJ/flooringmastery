package com.sg.flooringmastery.service;

public class InvalidStateException extends Exception {
    InvalidStateException(String message) {
        super(message);
    }

    InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }
}

