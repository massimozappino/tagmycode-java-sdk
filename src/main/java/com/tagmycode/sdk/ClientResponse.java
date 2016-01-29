package com.tagmycode.sdk;

import org.scribe.model.Response;

public class ClientResponse {

    public static final String HEADER_X_RATE_LIMIT_LIMIT = "X-RateLimit-Limit";
    public static final String HEADER_X_RATE_LIMIT_REMAINING = "X-RateLimit-Remaining";
    public static final String HEADER_X_RATE_LIMIT_RESET = "X-RateLimit-Reset";
    public static final String HEADER_LAST_RESOURCE_UPDATE = "Last-Resource-Update";
    private Response response;
    private RateLimit rateLimit;

    public ClientResponse(Response response) {
        this.response = response;
        initRateLimit();
    }

    private void initRateLimit() {
        try {
            rateLimit = new RateLimit(Integer.parseInt(response.getHeader(HEADER_X_RATE_LIMIT_LIMIT)),
                    Integer.parseInt(response.getHeader(HEADER_X_RATE_LIMIT_REMAINING)),
                    Integer.parseInt(response.getHeader(HEADER_X_RATE_LIMIT_RESET)));
        } catch (NumberFormatException e) {
            rateLimit = null;
        }
    }

    public String getBody() {
        String body;
        try {
            body = response.getBody();
        } catch (IllegalArgumentException e) {
            body = null;
        }
        return body;
    }

    public int getHttpStatusCode() {
        return response.getCode();
    }

    public boolean isError() {
        return !response.isSuccessful();
    }

    public RateLimit getRateLimit() {
        return rateLimit;
    }

    public ErrorResponse gerErrorResponse() {
        return new ErrorResponse(getBody());
    }

    public String extractLastResourceUpdate() {
        return response.getHeader(HEADER_LAST_RESOURCE_UPDATE);
    }
}
