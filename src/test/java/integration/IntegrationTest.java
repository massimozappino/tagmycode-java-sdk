package integration;


import com.tagmycode.sdk.Client;
import com.tagmycode.sdk.TagMyCode;
import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.authentication.TagMyCodeApiProduction;
import com.tagmycode.sdk.model.LanguagesCollection;
import org.junit.Test;
import support.VoidOauthWallet;

import static org.junit.Assert.assertTrue;

public class IntegrationTest {

    @Test
    public void fetchLanguages() throws Exception {
        Client client = new Client(new TagMyCodeApiProduction(), "123", "456", new VoidOauthWallet());
        client.setOauthToken(new OauthToken("123", "456"));

        LanguagesCollection languages = new TagMyCode(client).fetchLanguages();
        assertTrue(languages.size() > 0);
    }

    @Test
    public void isServiceAvailable() throws Exception {
        TagMyCode tagMyCode = new TagMyCode(createProductionClient());
        assertTrue(tagMyCode.isServiceAvailable());
    }

    private Client createProductionClient() {
        return new Client(new TagMyCodeApiProduction(), "key", "secret", new VoidOauthWallet());
    }
}
