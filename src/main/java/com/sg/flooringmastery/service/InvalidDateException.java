package com.sg.flooringmastery.service;

public class InvalidDateException extends Exception {

    InvalidDateException(String message) {
        super(message);
    }

    InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
}