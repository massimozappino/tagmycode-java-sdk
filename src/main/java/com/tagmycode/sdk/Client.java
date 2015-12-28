package com.tagmycode.sdk;

import com.tagmycode.sdk.authentication.*;
import com.tagmycode.sdk.exception.TagMyCodeApiException;
import com.tagmycode.sdk.exception.TagMyCodeConnectionException;
import com.tagmycode.sdk.exception.TagMyCodeUnauthorizedException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;

import java.util.Map;

public class Client {
    private TagMyCodeServiceImpl service;
    private OauthToken oauthToken;
    public String endpointUrl;
    private IWallet wallet;

    public Client(TagMyCodeApi tagmycodeApi, String key, String secret, IWallet wallet) {
        this.wallet = wallet;
        if (tagmycodeApi.isSsl()) {
            new Ssl().disableSslVerification();
        }

        this.service = (TagMyCodeServiceImpl) new ServiceBuilder()
                .provider(tagmycodeApi)
                .apiKey(key)
                .apiSecret(secret)
                .build();
        endpointUrl = tagmycodeApi.getEndpointUrl();
        setOauthToken(null);
    }

    public void setWallet(IWallet wallet) {
        if (null == wallet) {
            wallet = new VoidWallet();
        }
        this.wallet = wallet;
    }

    public Client(String key, String secret, IWallet wallet) {
        this(new TagMyCodeApiProduction(), key, secret, wallet);
    }

    public Client(AbstractSecret secret, IWallet wallet) {
        this(secret.getConsumerId(), secret.getConsumerSecret(), wallet);
    }

    public void fetchOauthToken(String verificationCode) throws TagMyCodeConnectionException {
        Verifier verifier = new Verifier(verificationCode);
        try {
            setOauthToken(service.getOauthToken(verifier));
        } catch (OAuthException e) {
            throw new TagMyCodeConnectionException(e);
        }
    }

    public void refreshOauthToken() throws TagMyCodeUnauthorizedException {
        try {
            setOauthToken(service.getAccessTokenFromRefreshToken(oauthToken.getRefreshToken()));
        } catch (OAuthException e) {
            throw new TagMyCodeUnauthorizedException();
        }
    }

    public String getAuthorizationUrl() {
        return service.getAuthorizationUrl(null);
    }

    public void setOauthToken(OauthToken oauthToken) {
        if (isTokenValid(oauthToken)) {
            wallet.saveOauthToken(oauthToken);
            this.oauthToken = oauthToken;
        } else {
            this.oauthToken = new VoidOauthToken();
        }
    }

    public OauthToken getOauthToken() {
        return oauthToken;
    }

    private boolean isTokenValid(OauthToken token) {
        return (token != null)
                && !(token instanceof VoidOauthToken)
                && ((token.getAccessToken().getToken().length() != 0) || (token.getRefreshToken().getToken().length() != 0));
    }

    public ClientResponse sendRequest(String uri, Verb verb) throws TagMyCodeConnectionException, TagMyCodeApiException, TagMyCodeUnauthorizedException {
        return sendRequest(uri, verb, new ParamList());
    }

    public ClientResponse sendRequest(String uri, Verb verb, ParamList paramList) throws TagMyCodeConnectionException, TagMyCodeApiException, TagMyCodeUnauthorizedException {
        OAuthRequest request = signRequest(uri, verb, paramList);
        try {
            Response response = request.send();

            if (response.getCode() == 401) {
                refreshOauthToken();
                request = signRequest(uri, verb, paramList);
                response = request.send();
            }

            return getClientResponse(response);
        } catch (OAuthException ex) {
            throw new TagMyCodeConnectionException(ex);
        }
    }

    private OAuthRequest signRequest(String uri, Verb verb, ParamList paramList) {
        OAuthRequest request = createRequestObject(uri, verb, paramList);
        this.service.signRequest(oauthToken.getAccessToken(), request);
        return request;
    }

    private OAuthRequest createRequestObject(String uri, Verb verb, ParamList paramList) {
        String endpoint = this.endpointUrl + "/" + uri;
        OAuthRequest request = new OAuthRequest(verb, endpoint);

        for (Map.Entry<String, String> e : paramList.entrySet()) {
            if (verb == Verb.GET) {
                request.addQuerystringParameter(e.getKey(), e.getValue());
            } else {
                request.addBodyParameter(e.getKey(), e.getValue());
            }
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

    public void revokeAccess() {
        setOauthToken(null);
    }

}