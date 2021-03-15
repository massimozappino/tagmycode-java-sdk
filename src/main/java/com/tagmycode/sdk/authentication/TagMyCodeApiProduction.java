package com.tagmycode.sdk.authentication;

public class TagMyCodeApiProduction extends TagMyCodeApi {

    @Override
    public String getDomain() {
        return "tagmycode.com";
    }

    @Override
    public boolean isDevelopment() {
        return false;
    }
}
