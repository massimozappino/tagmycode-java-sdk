package com.tagmycode.sdk.exception;

import com.tagmycode.sdk.ClientResponse;

public class TagMyCodeApiException extends TagMyCodeException {

    private int httpStatusCode;

    public TagMyCodeApiException() {
        super("Generic error");
        httpStatusCode = 500;
    }

    public TagMyCodeApiException(ClientResponse clientResponse) {
        super(clientResponse.gerErrorResponse().getMessage());
        httpStatusCode = clientResponse.getHttpStatusCode();
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
