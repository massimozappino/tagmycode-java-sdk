package com.tagmycode.sdk.exception;

public class TagMyCodeUnauthorizedException extends TagMyCodeException {

    public TagMyCodeUnauthorizedException() {
        super("Unauthorized");
    }

    public TagMyCodeUnauthorizedException(String message) {
        super(message);
    }
}
