package com.tagmycode.sdk;

import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.exception.TagMyCodeException;

public interface IOauthWallet {
    OauthToken loadOauthToken() throws TagMyCodeException;
    void saveOauthToken(OauthToken oauthToken) throws TagMyCodeException;
    void deleteOauthToken() throws TagMyCodeException;
}
