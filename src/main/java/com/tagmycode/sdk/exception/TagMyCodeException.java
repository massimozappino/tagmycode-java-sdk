package com.tagmycode.sdk.exception;

public class TagMyCodeException extends Exception {

    public TagMyCodeException(String message) {
        super(message);
    }

    public TagMyCodeException(Exception e) {
        super(e.getMessage());
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
