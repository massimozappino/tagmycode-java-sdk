package support;

import com.tagmycode.sdk.IOauthWallet;
import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.exception.TagMyCodeException;

public class VoidOauthWallet implements IOauthWallet {
    @Override
    public OauthToken loadOauthToken() throws TagMyCodeException {
        return null;
    }

    @Override
    public void saveOauthToken(OauthToken oauthToken) {

    }

    @Override
    public void deleteOauthToken() throws TagMyCodeException {

    }
}
