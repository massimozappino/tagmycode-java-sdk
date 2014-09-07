package support;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.tagmycode.sdk.Client;
import com.tagmycode.sdk.authentication.OauthToken;
import org.junit.Before;
import org.junit.Rule;

public abstract class ClientBaseTest extends BaseTest {
    protected Client client;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(7777);

    @Before
    public void configureClient() {
        client = new Client(new TagMyCodeApiStub(), "key", "secret");
        client.setOauthToken(new OauthToken("xxx", "yyy"));
    }
}
