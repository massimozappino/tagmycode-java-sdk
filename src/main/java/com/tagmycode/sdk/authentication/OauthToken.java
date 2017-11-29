package com.tagmycode.sdk.authentication;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.Token;

public class OauthToken implements Comparable<OauthToken> {
    private Token accessToken;
    private Token refreshToken;

    public OauthToken(String accessToken, String refreshToken) {
        this.accessToken = new Token(accessToken, "");
        this.refreshToken = new Token(refreshToken, "");
    }

    public Token getAccessToken() {
        return accessToken;
    }

    public Token getRefreshToken() {
        return refreshToken;
    }

    @Override
    public int compareTo(OauthToken oauthToken) {
        return concatenateTokens(this).compareTo(concatenateTokens(oauthToken));
    }

    private String concatenateTokens(OauthToken oauthToken) {
        return oauthToken.getAccessToken().getToken() + oauthToken.getRefreshToken().getToken();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }

        if (!(object instanceof OauthToken)) {
            return false;
        }

        return compareTo((OauthToken) object) == 0;
    }

    public String toJson() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("access_token", accessToken.getToken());
            jo.put("refresh_token", refreshToken.getToken());
        } catch (JSONException ignore) {
        }

        return jo.toString();
    }
}
