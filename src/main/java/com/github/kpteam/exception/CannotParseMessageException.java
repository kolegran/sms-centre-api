package com.github.kpteam.exception;

public class CannotParseMessageException extends RuntimeException {

    public CannotParseMessageException(String message, Exception exception) {
        super(message, exception);
    }
}
