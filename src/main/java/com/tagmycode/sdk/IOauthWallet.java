package com.tagmycode.sdk;

import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.exception.TagMyCodeException;

public interface IOauthWallet {
    void saveOauthToken(OauthToken oauthToken) throws TagMyCodeException;
}
