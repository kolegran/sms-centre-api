package com.github.kpteam.exception;

public class CharsetEncodingException extends RuntimeException {

    public CharsetEncodingException(String message, Exception exception) {
        super(message, exception);
    }
}
