package com.tagmycode.sdk;

import com.tagmycode.sdk.authentication.*;
import com.tagmycode.sdk.exception.TagMyCodeApiException;
import com.tagmycode.sdk.exception.TagMyCodeConnectionException;
import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.exception.TagMyCodeUnauthorizedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scribe.builder.ServiceBuilder;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;

import java.util.Map;

public class Client {
    private final String endpointUrl;
    private final TagMyCodeServiceImpl service;
    private OauthToken oauthToken;
    private final TagMyCodeApi tagmycodeApi;
    private IOauthWallet wallet;
    private static final Logger logger = LogManager.getLogger(Client.class);

    public Client(TagMyCodeApi tagmycodeApi, String key, String secret, IOauthWallet wallet) {
        this.tagmycodeApi = tagmycodeApi;
        this.wallet = wallet;
        if (tagmycodeApi.isDevelopment()) {
            Ssl.disableSslVerification();
        }
        this.service = (TagMyCodeServiceImpl) new ServiceBuilder()
                .provider(tagmycodeApi)
                .apiKey(key)
                .apiSecret(secret)
                .callback("oob")
                .build();
        endpointUrl = tagmycodeApi.getEndpointUrl();
        // TODO remove try/catch
        try {
            setOauthToken(null);
        } catch (TagMyCodeException e) {
            e.printStackTrace();
        }
    }

    public Client(String key, String secret, IOauthWallet wallet) {
        this(new TagMyCodeApiProduction(), key, secret, wallet);
    }

    public Client(AbstractSecret secret, IOauthWallet wallet) {
        this(secret.getConsumerId(), secret.getConsumerSecret(), wallet);
    }

    public void fetchOauthToken(String verificationCode) throws TagMyCodeException {
        Verifier verifier = new Verifier(verificationCode);
        try {
            setOauthToken(service.getOauthToken(verifier));
        } catch (OAuthException e) {
            throw new TagMyCodeConnectionException(e);
        }
    }

    public void refreshOauthToken() throws TagMyCodeException {
        try {
            fetchAndSetRefreshToken(service.getAccessTokenFromRefreshToken(oauthToken.getRefreshToken()));
        } catch (OAuthException e) {
            String exceptionMessage = e.getMessage();
            String message = "Error fetching refresh token: " + exceptionMessage;
            if (responseIsUnauthorized(exceptionMessage)) {
                throw new TagMyCodeUnauthorizedException(message);
            } else {
                throw new TagMyCodeException(message);
            }
        }
    }

    protected void fetchAndSetRefreshToken(OauthToken accessTokenFromRefreshToken) throws TagMyCodeException {
        setOauthToken(accessTokenFromRefreshToken);
    }

    public String getAuthorizationUrl() {
        return service.getAuthorizationUrl(null);
    }

    public OauthToken getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(OauthToken oauthToken) throws TagMyCodeException {
        if (isTokenValid(oauthToken)) {
            wallet.saveOauthToken(oauthToken);
            this.oauthToken = oauthToken;
        } else {
            this.oauthToken = new VoidOauthToken();
        }
    }

    private boolean isTokenValid(OauthToken token) {
        return (token != null)
                && !(token instanceof VoidOauthToken)
                && ((token.getAccessToken().getToken().length() != 0)
                || (token.getRefreshToken().getToken().length() != 0));
    }

    public ClientResponse sendRequest(String uri, Verb verb) throws TagMyCodeException {
        return sendRequest(uri, verb, new ParamList(), new ParamList());
    }

    public ClientResponse sendRequest(String uri, Verb verb, ParamList paramList) throws TagMyCodeException {
        return sendRequest(uri, verb, paramList, new ParamList());
    }

    public ClientResponse sendRequest(String uri, Verb verb, ParamList paramList, ParamList headers) throws TagMyCodeException {
        OAuthRequest request = signRequest(uri, verb, paramList, headers);
        try {
            Response response = request.send();

            if (response.getCode() == 401) {
                refreshOauthToken();
                request = signRequest(uri, verb, paramList, headers);
                response = request.send();
            }

            return getClientResponse(response);
        } catch (OAuthException ex) {
            throw new TagMyCodeConnectionException(ex);
        }
    }

    private OAuthRequest signRequest(String uri, Verb verb, ParamList paramList, ParamList headers) {
        OAuthRequest request = createRequestObject(uri, verb, paramList, headers);

        this.service.signRequest(oauthToken.getAccessToken(), request);

        logRequest(request);
        return request;
    }

    private OAuthRequest createRequestObject(String uri, Verb verb, ParamList paramList, ParamList headers) {
        String endpoint = this.endpointUrl + "/" + uri;
        OAuthRequest request = new OAuthRequest(verb, endpoint);

        for (Map.Entry<String, String> e : paramList.entrySet()) {
            if (verb == Verb.GET) {
                request.addQuerystringParameter(e.getKey(), e.getValue());
            } else {
                request.addBodyParameter(e.getKey(), e.getValue());
            }
        }

        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return request;
    }

    private ClientResponse getClientResponse(Response response) throws TagMyCodeUnauthorizedException, TagMyCodeApiException {
        ClientResponse clientResponse = new ClientResponse(response);
        if (clientResponse.getHttpStatusCode() == 401) {
            throw new TagMyCodeUnauthorizedException();
        }
        if (clientResponse.isError()) {
            throw new TagMyCodeApiException(clientResponse);
        }
        return clientResponse;
    }

    public boolean isAuthenticated() {
        return isTokenValid(getOauthToken());
    }

    public TagMyCodeApi getTagmycodeApi() {
        return tagmycodeApi;
    }

    public IOauthWallet getWallet() {
        return wallet;
    }

    public void setWallet(IOauthWallet wallet) {
        this.wallet = wallet;
    }

    public OauthToken loadOauthToken() throws TagMyCodeException {
        OauthToken oauthToken = wallet.loadOauthToken();
        setOauthToken(oauthToken);
        return oauthToken;
    }

    public void revokeAccess() throws TagMyCodeException {
        setOauthToken(null);
        wallet.deleteOauthToken();
    }

    public boolean responseIsUnauthorized(String responseMessage) {
        return responseMessage.contains("refresh_token") || responseMessage.contains("invalid_grant");
    }

    private void logRequest(OAuthRequest request) {
        logger.debug(request.getVerb() + " " + request.getUrl() + "?" + request.getQueryStringParams().asFormUrlEncodedString());
        logger.debug("\tHEADERS: " + request.getHeaders().entrySet());
        logger.debug("\tBODY: " + request.getBodyContents());
        logger.debug("---");
    }
}