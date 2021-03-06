package support;

import com.tagmycode.sdk.authentication.TagMyCodeApi;

public class TagMyCodeApiStub extends TagMyCodeApi {
    @Override
    public String getDomain() {
        return "localhost";
    }

    @Override
    public String getEndpointUrl() {
        return "http://localhost:7777";
    }

    @Override
    public String getOauthBaseUrl() {
        return "http://localhost:7777";
    }

    @Override
    public boolean isDevelopment() {
        return false;
    }
}
