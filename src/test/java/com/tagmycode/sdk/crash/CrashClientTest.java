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
                .withRequestBody(equalTo("app_version=1.0.0&oauth_token=%7B%22access_token%22%3A%22access_token_value%22%2C%22refresh_token%22%3A%22refresh_token_value%22%7D&throwable=java.lang.Throwable%3A%20throwable%0A%09at%20com.tagmycode.sdk.crash.CrashClientTest.sendCrash%28CrashClientTest.java%3A27%29%0A%09at%20sun.reflect.NativeMethodAccessorImpl.invoke0%28Native%20Method%29%0A%09at%20sun.reflect.NativeMethodAccessorImpl.invoke%28NativeMethodAccessorImpl.java%3A62%29%0A%09at%20sun.reflect.DelegatingMethodAccessorImpl.invoke%28DelegatingMethodAccessorImpl.java%3A43%29%0A%09at%20java.lang.reflect.Method.invoke%28Method.java%3A498%29%0A%09at%20org.junit.runners.model.FrameworkMethod%241.runReflectiveCall%28FrameworkMethod.java%3A47%29%0A%09at%20org.junit.internal.runners.model.ReflectiveCallable.run%28ReflectiveCallable.java%3A12%29%0A%09at%20org.junit.runners.model.FrameworkMethod.invokeExplosively%28FrameworkMethod.java%3A44%29%0A%09at%20org.junit.internal.runners.statements.InvokeMethod.evaluate%28InvokeMethod.java%3A17%29%0A%09at%20org.junit.internal.runners.statements.RunBefores.evaluate%28RunBefores.java%3A26%29%0A%09at%20com.github.tomakehurst.wiremock.junit.WireMockRule%241.evaluate%28WireMockRule.java%3A74%29%0A%09at%20org.junit.rules.RunRules.evaluate%28RunRules.java%3A20%29%0A%09at%20org.junit.runners.ParentRunner.runLeaf%28ParentRunner.java%3A271%29%0A%09at%20org.junit.runners.BlockJUnit4ClassRunner.runChild%28BlockJUnit4ClassRunner.java%3A70%29%0A%09at%20org.junit.runners.BlockJUnit4ClassRunner.runChild%28BlockJUnit4ClassRunner.java%3A50%29%0A%09at%20org.junit.runners.ParentRunner%243.run%28ParentRunner.java%3A238%29%0A%09at%20org.junit.runners.ParentRunner%241.schedule%28ParentRunner.java%3A63%29%0A%09at%20org.junit.runners.ParentRunner.runChildren%28ParentRunner.java%3A236%29%0A%09at%20org.junit.runners.ParentRunner.access%24000%28ParentRunner.java%3A53%29%0A%09at%20org.junit.runners.ParentRunner%242.evaluate%28ParentRunner.java%3A229%29%0A%09at%20org.junit.runners.ParentRunner.run%28ParentRunner.java%3A309%29%0A%09at%20org.junit.runner.JUnitCore.run%28JUnitCore.java%3A160%29%0A%09at%20com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs%28JUnit4IdeaTestRunner.java%3A68%29%0A%09at%20com.intellij.rt.execution.junit.IdeaTestRunner%24Repeater.startRunnerWithArgs%28IdeaTestRunner.java%3A47%29%0A%09at%20com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart%28JUnitStarter.java%3A242%29%0A%09at%20com.intellij.rt.execution.junit.JUnitStarter.main%28JUnitStarter.java%3A70%29%0A&java_version=1.8.0_131&operating_system=Linux%204.8.0-59-generic%20amd64%20gnome&throwable_message=throwable&app_id=app_id&user=%7B%22id%22%3A0%7D")));
        crashClient.sendCrash(crash);
    }
}
