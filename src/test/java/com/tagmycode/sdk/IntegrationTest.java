package com.tagmycode.sdk;


import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.authentication.TagMyCodeApiProduction;
import com.tagmycode.sdk.model.LanguageCollection;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IntegrationTest {

    @Test
    public void fetchLanguages() throws Exception {
        Client client = new Client(new TagMyCodeApiProduction(), "123", "456");
        client.setOauthToken(new OauthToken("123", "456"));

        LanguageCollection languages = new TagMyCode(client).fetchLanguages();
        assertTrue(languages.size() > 0);
    }

}
