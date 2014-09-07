package com.tagmycode.sdk.authentication;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OauthTokenTest {
    @Test
    public void twoOauthTokensAreEquals() {
        OauthToken oauthToken1 = new OauthToken("123", "456");
        OauthToken oauthToken2 = new OauthToken("123", "456");
        assertEquals(oauthToken1, oauthToken2);
    }
}