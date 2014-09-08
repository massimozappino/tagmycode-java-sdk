package com.tagmycode.sdk.exception;

import com.tagmycode.sdk.exception.TagMyCodeException;

public class TagMyCodeJsonException extends TagMyCodeException {
    public TagMyCodeJsonException(Exception e) {
        super(e);
    }

    public TagMyCodeJsonException() {
        super("Unable to parse JSON");
    }
}
