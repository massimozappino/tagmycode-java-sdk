package com.tagmycode.sdk.authentication;

public class TagMyCodeApiProduction extends TagMyCodeApi {
    @Override
    public String getEndpointUrl() {
        return "https://api.tagmycode.com";
    }

    @Override
    public String getOauthBaseUrl() {
        return "https://tagmycode.com";
    }
}
