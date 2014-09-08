package com.tagmycode.sdk.authentication;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagMyCodeServiceImpl extends OAuth20ServiceImpl {

    private final TagMyCodeApi tagMyCodeApi;
    private final OAuthConfig tagMyCodeConfig;

    public TagMyCodeServiceImpl(TagMyCodeApi tagMyCodeApi, OAuthConfig tagMyCodeConfig) {
        super(tagMyCodeApi, tagMyCodeConfig);
        this.tagMyCodeApi = tagMyCodeApi;
        this.tagMyCodeConfig = tagMyCodeConfig;
    }


    public OauthToken getOauthToken(Verifier verifier) {
        OAuthRequest request = createBaseRequest();
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter("grant_type", "authorization_code");
        Response response = request.send();
        return createToken(response);
    }

    public OauthToken getAccessTokenFromRefreshToken(Token refreshToken) {
        OAuthRequest request = createBaseRequest();
        request.addBodyParameter("grant_type", "refresh_token");
        request.addBodyParameter("refresh_token", refreshToken.getToken());
        Response response = request.send();
        return createToken(response);
    }

    private OAuthRequest createBaseRequest() {
        OAuthRequest request = new OAuthRequest(tagMyCodeApi.getAccessTokenVerb(), tagMyCodeApi.getAccessTokenEndpoint());
        request.addBodyParameter(OAuthConstants.CLIENT_ID, tagMyCodeConfig.getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, tagMyCodeConfig.getApiSecret());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, tagMyCodeConfig.getCallback());

        if (tagMyCodeConfig.hasScope()) {
            request.addBodyParameter(OAuthConstants.SCOPE, tagMyCodeConfig.getScope());
        }
        return request;
    }

    private OauthToken createToken(Response response) {
        String accessToken = extractToken("access_token", response.getBody());
        String refreshToken = extractToken("refresh_token", response.getBody());

        return new OauthToken(accessToken, refreshToken);
    }

    private static String extractToken(String name, String responseBody) {
        try {
            Preconditions.checkEmptyString(name, "name String is incorrect!");
            Preconditions.checkEmptyString(responseBody, "Response body is incorrect. Can't extract a token from an empty string");
        } catch (IllegalArgumentException e) {
            throw new OAuthException(e.getMessage());
        }

        //{"access_token" : ""}
        String REGEX = "\"%s\"\\s*:\\s*\"([^\"]+)\"";
        REGEX = String.format(REGEX, name);

        Matcher matcher = Pattern.compile(REGEX).matcher(responseBody);
        if (matcher.find()) {
            return OAuthEncoder.decode(matcher.group(1));
        } else {
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + responseBody + "'", null);
        }
    }
}
