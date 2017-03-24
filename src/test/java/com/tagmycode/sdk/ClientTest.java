package com.tagmycode.sdk;


import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.authentication.VoidOauthToken;
import com.tagmycode.sdk.exception.TagMyCodeApiException;
import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.exception.TagMyCodeUnauthorizedException;
import org.junit.Test;
import org.mockito.Mockito;
import org.scribe.model.Verb;
import support.ClientBaseTest;
import support.TagMyCodeApiStub;
import support.VoidOauthWallet;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClientTest extends ClientBaseTest {

    @Test(expected = IllegalArgumentException.class)
    public void TagMyCodeRestClientShouldFailWithEmptyKeyAndSecret() {
        new Client(new TagMyCodeApiStub(), "", "", new VoidOauthWallet());
    }

    @Test
    public void simpleConstructorIsForProduction() {
        Client clientSimple = new Client("consumer_id", "consumer_secret", new VoidOauthWallet());
        assertTrue(clientSimple.getAuthorizationUrl().contains("https://tagmycode.com/oauth2/authorize"));
    }

    @Test
    public void newClientHasAlwaysOauthToken() throws Exception {
        stubFor(get(urlMatching("/fake_request.*"))
                .willReturn(aResponse().withStatus(200)));
        Client newClient = new Client(new TagMyCodeApiStub(), "123", "456", new VoidOauthWallet());
        try {
            newClient.sendRequest("fake_request", Verb.GET);
        } catch (NullPointerException e) {
            fail("Expected a valid OauthToken");
        }
        OauthToken oauthToken = newClient.getOauthToken();
        assertTrue("void object OauthToken", oauthToken instanceof VoidOauthToken);
    }

    @Test
    public void constructorWithSecretObject() {
        Client clientSimple = new Client(new AbstractSecret() {
            @Override
            public String getConsumerId() {
                return "consumer_id";
            }

            @Override
            public String getConsumerSecret() {
                return "consumer_secret";
            }
        }, new VoidOauthWallet());
        assertTrue(clientSimple.getAuthorizationUrl().contains("https://tagmycode.com/oauth2/authorize"));
    }

    @Test
    public void emptyOauthToken() throws TagMyCodeException {
        client.setOauthToken(new OauthToken("", ""));
        assertFalse(client.isAuthenticated());
    }

    @Test
    public void revokeAccess() throws TagMyCodeException {
        IOauthWallet wallet = createWallet();
        client.setWallet(wallet);

        OauthToken oauthToken = new OauthToken("1", "2");
        client.setOauthToken(oauthToken);

        assertSame(oauthToken, client.getOauthToken());
        client.revokeAccess();
        assertTrue(client.getOauthToken() instanceof VoidOauthToken);
        Mockito.verify(wallet, times(1)).deleteOauthToken();
    }

    @Test
    public void testRateLimitObjectFromResponse() {
        ClientResponse cr = new ClientResponse(createResponseMock());

        RateLimit rateLimit = cr.getRateLimit();
        assertEquals(100, rateLimit.getLimit());
        assertEquals(50, rateLimit.getRemaining());
        assertEquals(1387110772, rateLimit.getReset());
    }

    @Test
    public void httpStatusErrorThrownApiError() throws Exception {
        stubFor(get(urlMatching("/languages.*"))
                .willReturn(aResponse()
                        .withStatus(410)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[" + resourceGenerate.getResourceReader().readFile("language.json") + "]"
                        )));
        try {
            client.sendRequest("languages/", Verb.GET);
            fail("should throw exception");
        } catch (TagMyCodeException e) {
            assertTrue(e instanceof TagMyCodeApiException);
        }
    }


    @Test
    public void validAccessTokenIsAuthenticated() throws TagMyCodeException {
        client.setOauthToken(new OauthToken("1", "1"));
        assertTrue(client.isAuthenticated());
    }

    @Test
    public void nullAccessTokenIsNotAuthenticated() throws TagMyCodeException {
        client.setOauthToken(null);
        assertFalse(client.isAuthenticated());
    }

    @Test
    public void defaultAccessTokenIsNotAuthenticated() {
        Client defaultClient = new Client(new TagMyCodeApiStub(), "key", "secret", new VoidOauthWallet());

        assertFalse(defaultClient.isAuthenticated());
    }

    @Test
    public void voidAccessTokenIsNotAuthenticated() throws TagMyCodeException {
        client.setOauthToken(new OauthToken("", ""));
        assertFalse(client.isAuthenticated());
    }

    @Test
    public void nullOauthTokenIsVoidOauthTokenInstance() throws TagMyCodeException {
        client.setOauthToken(null);
        assertTrue(client.getOauthToken() instanceof VoidOauthToken);
    }

    @Test
    public void fetchTokensShouldWorkCorrectly() throws TagMyCodeException {
        String accessTokenString = "token123";
        String refreshTokenString = "refreshToken";
        createStubForOauth(accessTokenString, refreshTokenString);

        client.fetchOauthToken("123");
        OauthToken oauthToken = client.getOauthToken();

        assertEquals(new OauthToken(accessTokenString, refreshTokenString), oauthToken);
        assertEquals(oauthToken, client.getOauthToken());
    }

    @Test
    public void refreshOauthTokenReceiveNewAccessToken() throws Exception {
        stubFor(post(urlMatching("/oauth2/token.*"))
                .withRequestBody((matching(".*refresh_token.*")))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(createStringResponseForToken("abc", "xyz")
                        )));
        assertEquals(new OauthToken("xxx", "yyy"), client.getOauthToken());

        client.refreshOauthToken();

        assertEquals(new OauthToken("abc", "xyz"), client.getOauthToken());
    }

    @Test
    public void refreshOauthTokenWithInvalidTokenThrowsException() throws Exception {
        stubFor(post(urlMatching("/oauth2/token.*"))
                .withRequestBody((matching(".*refresh_token.*")))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "text/plain")
                        .withBody("{\"error\":\"invalid_grant\",\"error_description\":\"Invalid refresh token\"}"
                        )));
        assertEquals(new OauthToken("xxx", "yyy"), client.getOauthToken());

        try {
            client.refreshOauthToken();
            fail("Expected exception");
        } catch (TagMyCodeException ignore) {
        }
        assertEquals(new OauthToken("xxx", "yyy"), client.getOauthToken());
    }

    @Test
    public void expiredAccessTokenShouldBeRefreshed() throws Exception {
        stubFor(get(urlMatching("/account.*"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(""
                        )));

        stubFor(post(urlMatching("/oauth2/token.*"))
                .withRequestBody((matching(".*refresh_token.*")))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(createStringResponseForToken("abc", "xyz")
                        )));
        Client clientSpy = spy(client);
        clientSpy.setOauthToken(new OauthToken("123", "456"));
        try {
            new TagMyCode(clientSpy).fetchAccount();
            fail("Expected exception");
        } catch (TagMyCodeUnauthorizedException ignore) {
        }
        Mockito.verify(clientSpy).refreshOauthToken();
    }

    @Test
    public void failedRefreshTokenThrownTagMyCodeUnauthorizedException() throws TagMyCodeException {
        stubFor(get(urlMatching("/account.*"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(""
                        )));

        stubFor(post(urlMatching("/oauth2/token.*"))
                .withRequestBody((matching(".*refresh_token.*")))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "text/plain")
                        .withBody("{}"
                        )));
        try {
            new TagMyCode(client).fetchAccount();
            fail("Expected exception");
        } catch (TagMyCodeException ignored) {
        }
    }

    @Test
    public void walletInvokedOnSetOauthToken() throws TagMyCodeException {
        Client clientSpy = spy(client);
        IOauthWallet walletMock = mock(IOauthWallet.class);
        clientSpy.setWallet(walletMock);
        OauthToken oauthToken = new OauthToken("123456", "123456");

        clientSpy.setOauthToken(oauthToken);

        Mockito.verify(walletMock, times(1)).saveOauthToken(oauthToken);
    }

    @Test
    public void walletNotInvokedWithNullOauthToken() throws TagMyCodeException {
        Client clientSpy = spy(client);
        IOauthWallet walletMock = mock(IOauthWallet.class);
        clientSpy.setWallet(walletMock);

        clientSpy.setOauthToken(null);

        Mockito.verify(walletMock, times(0)).saveOauthToken(null);
    }

    @Test
    public void getWallet() throws Exception {
        assertSame(wallet, client.getWallet());
    }

    @Test
    public void loadAccessToken() throws Exception {
        IOauthWallet iOauthWallet = createWallet();

        OauthToken dummyOauthToken = new VoidOauthToken();
        when(iOauthWallet.loadOauthToken()).thenReturn(dummyOauthToken);

        client.setWallet(iOauthWallet);
        Client spyClient = spy(client);

        OauthToken loadedToken = spyClient.loadOauthToken();

        assertEquals(dummyOauthToken, loadedToken);
        Mockito.verify(iOauthWallet, times(1)).loadOauthToken();
        Mockito.verify(spyClient, times(1)).setOauthToken(dummyOauthToken);
    }

    protected void createStubForOauth(String accessTokenString, String refreshTokenString) {
        stubFor(post(urlMatching("/oauth2/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/plain")
                        .withBody(createStringResponseForToken(accessTokenString, refreshTokenString)
                        )));
    }

    private String createStringResponseForToken(String accessTokenString, String refreshTokenString) {
        return "{\"access_token\":\"" + accessTokenString + "\",\"refresh_token\":\"" + refreshTokenString + "\"}";
    }
}