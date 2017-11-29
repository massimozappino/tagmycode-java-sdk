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

    @Test
    public void toJson() {
        assertEquals("{\"access_token\":\"access_token_value\",\"refresh_token\":\"refresh_token_value\"}",
                new OauthToken("access_token_value", "refresh_token_value").toJson());
    }
}