package com.tagmycode.sdk;

import org.scribe.model.Response;

public class ClientResponse {

    private Response response;
    private RateLimit rateLimit;

    public ClientResponse(Response response) {
        this.response = response;
        initRateLimit();
    }

    private void initRateLimit() {
        try {
            rateLimit = new RateLimit(Integer.parseInt(response.getHeader("X-RateLimit-Limit")),
                    Integer.parseInt(response.getHeader("X-RateLimit-Remaining")),
                    Integer.parseInt(response.getHeader("X-RateLimit-Reset")));
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

    public String getLastUpdate() {
        return response.getHeader("Last-Update");
    }
}
