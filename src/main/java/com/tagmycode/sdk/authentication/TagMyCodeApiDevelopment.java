package com.tagmycode.sdk.authentication;

public class TagMyCodeApiDevelopment extends TagMyCodeApi {
    @Override
    public String getEndpointUrl() {
        return "https://api.tagmycode.dev";
    }

    @Override
    public String getOauthBaseUrl() {
        return "https://tagmycode.dev";
    }
}
