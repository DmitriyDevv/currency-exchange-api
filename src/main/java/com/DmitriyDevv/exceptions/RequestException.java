package com.DmitriyDevv.exceptions;

public class RequestException extends RuntimeException {
    private final String message;
    private final int code;

    public RequestException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
