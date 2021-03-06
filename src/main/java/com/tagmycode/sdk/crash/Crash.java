package com.tagmycode.sdk.crash;

import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.model.User;
import lombok.Getter;
import org.json.JSONException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Crash {
    public static final String OPERATING_SYSTEM = "operating_system";
    public static final String JAVA_VERSION = "java_version";
    public static final String APP_ID = "app_id";
    public static final String APP_VERSION = "app_version";
    public static final String USER = "user";
    public static final String OAUTH_TOKEN = "oauth_token";
    public static final String THROWABLE = "throwable";
    public static final String THROWABLE_MESSAGE = "throwable_message";
    private String operatingSystem;
    private String javaVersion;
    private String appVersion;
    private User user;
    private Throwable throwable;
    private String appId;
    private OauthToken oauthToken;

    public Crash(String operatingSystem, String javaVersion, String appId, String appVersion, User user, OauthToken oauthToken, Throwable throwable) {
        this.operatingSystem = operatingSystem;
        this.javaVersion = javaVersion;
        this.appId = appId;
        this.appVersion = appVersion;
        this.user = user;
        this.oauthToken = oauthToken;
        this.throwable = throwable;
    }

    public static Crash create(String appId, String appVersion, User user, OauthToken oauthToken, Throwable throwable) {
        String operatingSystem = String.format("%s %s %s %s",
                System.getProperty("os.name"),
                System.getProperty("os.version"),
                System.getProperty("os.arch"),
                System.getProperty("sun.desktop")
        );

        return new Crash(operatingSystem, System.getProperty("java.version"), appId, appVersion, user, oauthToken, throwable);
    }

    private String extractStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(OPERATING_SYSTEM, getOperatingSystem());
        map.put(JAVA_VERSION, getJavaVersion());
        map.put(APP_VERSION, getAppVersion());
        map.put(APP_ID, getAppId());
        try {
            map.put(USER, getUser().toJson());
        } catch (JSONException e) {
            map.put(USER, "");
        }
        if (getOauthToken() != null) {
            map.put(OAUTH_TOKEN, getOauthToken().toJson());
        }
        map.put(THROWABLE, extractStackTrace(getThrowable()));
        map.put(THROWABLE_MESSAGE, getThrowable().getMessage());
        return map;
    }
}
