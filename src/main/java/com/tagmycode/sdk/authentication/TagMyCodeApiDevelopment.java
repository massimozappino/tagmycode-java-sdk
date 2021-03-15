package com.tagmycode.sdk.authentication;

public class TagMyCodeApiDevelopment extends TagMyCodeApi {
    @Override
    public String getDomain() {
        return "tagmycode.test";
    }

    @Override
    public boolean isDevelopment() {
        return true;
    }
}
