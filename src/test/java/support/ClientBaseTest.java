package support;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.tagmycode.sdk.Client;
import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.authentication.TagMyCodeApiProduction;
import com.tagmycode.sdk.exception.TagMyCodeException;
import org.junit.Before;
import org.junit.Rule;

public abstract class ClientBaseTest extends BaseTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(7777);
    protected Client client;

    @Before
    public void configureClient() throws TagMyCodeException {
        client = new Client(new TagMyCodeApiStub(), "key", "secret", new VoidOauthWallet());
        client.setOauthToken(new OauthToken("xxx", "yyy"));
    }

    protected Client getProductionClient() {
        return new Client(new TagMyCodeApiProduction(), "key", "secret", new VoidOauthWallet());

    }
}
