package com.github.kpteam.exception;

public class InterruptSendingException extends RuntimeException {

    public InterruptSendingException(String message, Exception exception) {
        super(message, exception);
    }
}
