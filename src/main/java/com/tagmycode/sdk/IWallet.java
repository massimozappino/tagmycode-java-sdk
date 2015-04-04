package com.tagmycode.sdk;

import com.tagmycode.sdk.authentication.OauthToken;

public interface IWallet {
    boolean saveOauthToken(OauthToken oauthToken);
}
