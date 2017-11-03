package com.tagmycode.sdk.crash;

import com.tagmycode.sdk.model.User;
import org.json.JSONException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CrashTest {

    @Test
    public void create() throws JSONException {
        Crash crash = Crash.create("0000", "1.1.1", new User(), new Exception("message"));
        assertTrue(crash.getOperatingSystem().length() > 0);
        assertTrue(crash.getJavaVersion().contains("1."));
        assertEquals("0000", crash.getAppId());
        assertEquals("1.1.1", crash.getAppVersion());
        assertEquals("{\"id\":0}", crash.getUser().toJson());
        assertEquals("message", crash.getThrowable().getMessage());
    }

    @Test
    public void toMap() throws JSONException {
        Crash crash = new Crash("OS", "1.8", "plugin_id",
                "2.0.0", new User().setUsername("username"), new Exception("Error message"));

        Map<String, String> map = crash.toMap();
        assertEquals("OS", map.get(Crash.OPERATING_SYSTEM));
        assertEquals("1.8", map.get(Crash.JAVA_VERSION));
        assertEquals("2.0.0", map.get(Crash.APP_VERSION));
        assertEquals("plugin_id", map.get(Crash.APP_ID));
        assertEquals("{\"id\":0,\"username\":\"username\"}", map.get(Crash.USER));
        String[] stackTrace = map.get(Crash.THROWABLE).split("\r\n|\r|\n");
        assertTrue(stackTrace.length > 10);
        assertEquals("java.lang.Exception: Error message", stackTrace[0]);
        assertEquals("java.lang.Exception: Error message", stackTrace[0]);
        assertEquals("Error message", map.get(Crash.THROWABLE_MESSAGE));
    }

}