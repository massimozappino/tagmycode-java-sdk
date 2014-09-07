package com.tagmycode.sdk.authentication;

public class TagMyCodeApiDevelopment extends TagMyCodeApi {
    @Override
    public String getEndpointUrl() {
        return "https://api.tagmycode.local";
    }

    @Override
    public String getOauthBaseUrl() {
        return "https://tagmycode.local";
    }
}
