package com.example.mysite.exception;

public class IncorrectInputDataException extends RuntimeException {
    public IncorrectInputDataException(String msg) {
        super(msg);
    }
}
