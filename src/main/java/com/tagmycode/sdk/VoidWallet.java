package com.tagmycode.sdk;

import com.tagmycode.sdk.authentication.OauthToken;

public class VoidWallet implements IWallet {
    @Override
    public boolean saveOauthToken(OauthToken oauthToken) {
        return true;
    }
}
