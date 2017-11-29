package com.tagmycode.sdk.crash;

import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.model.User;
import org.junit.Before;
import org.junit.Test;
import support.ClientBaseTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class CrashClientTest extends ClientBaseTest {
    private CrashClient crashClient;

    @Before
    public void initTagMyCodeObject() {
        crashClient = new CrashClient(client);
    }

    @Test
    public void sendCrash() throws Exception {
        String url = "/crash.*";
        stubFor(post(urlMatching(url))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                ));
        Crash crash = Crash.create("app_id", "1.0.0", new User(), new OauthToken("access_token_value", "refresh_token_value"), new Throwable("throwable"));

        new CrashClient(client).sendCrash(crash);

        verify(postRequestedFor(urlMatching(url))
                .withRequestBody(containing("app_version=1.0.0&oauth_token=%7B%22access_token%22%3A%22access_token_value%22%2C%22refresh_token%22%3A%22refresh_token_value%22%7D&throwable=java.lang.Throwable")));
        crashClient.sendCrash(crash);
    }
}
