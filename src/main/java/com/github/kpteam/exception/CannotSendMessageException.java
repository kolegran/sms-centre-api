package com.github.kpteam.exception;

public class CannotSendMessageException extends RuntimeException {

    public CannotSendMessageException(String message, Exception exception) {
        super(message, exception);
    }
}
