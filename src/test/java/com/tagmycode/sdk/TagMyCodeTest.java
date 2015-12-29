package com.tagmycode.sdk;

import com.tagmycode.sdk.exception.TagMyCodeApiException;
import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.model.*;
import org.junit.Before;
import org.junit.Test;
import support.ClientBaseTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class TagMyCodeTest extends ClientBaseTest {
    TagMyCode tagMyCode;

    @Before
    public void initTagMyCodeObject() {
        tagMyCode = new TagMyCode(client);
    }

    @Test
    public void getClient() throws Exception {
        assertEquals(client, tagMyCode.getClient());
    }

    @Test
    public void fetchAccount() throws Exception {
        stubFor(get(urlMatching("/account.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceGenerate.anUser().toJson())));

        User user = tagMyCode.fetchAccount();
        assertEquals(resourceGenerate.anUser(), user);
    }

    @Test
    public void fetchLanguages() throws Exception {
        stubFor(get(urlMatching("/languages.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[" + resourceGenerate.aLanguage().toJson() + "]"
                        )));


        LanguageCollection languages = tagMyCode.fetchLanguages();
        Language language = languages.get(0);
        assertEquals(resourceGenerate.aLanguage(), language);
    }

    @Test
    public void searchSnippets() throws Exception {
        stubFor(get(urlMatching("/search.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[" + resourceGenerate.aSnippet().toJson() + "]"
                        )));

        ModelCollection<Snippet> searchCollection = tagMyCode.searchSnippets("java");

        Snippet snippet = searchCollection.get(0);
        assertEquals(resourceGenerate.aSnippet(), snippet);
    }

    @Test
    public void fetchSnippet() throws Exception {
        stubFor(get(urlMatching("/snippets.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceGenerate.aSnippet().toJson()
                        )));
        Snippet snippet = tagMyCode.fetchSnippet(1);

        assertEquals(resourceGenerate.aSnippet(), snippet);
    }

    @Test
    public void fetchSnippets() throws Exception {
        stubFor(get(urlMatching("/snippets.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceGenerate.aSnippetCollection().toJson()
                        )));

        SnippetCollection snippets = tagMyCode.fetchSnippets();

        assertEquals(resourceGenerate.aSnippetCollection(), snippets);
    }

    @Test
    public void notFoundSnippetCatch404Error() throws TagMyCodeException {
        try {
            tagMyCode.fetchSnippet(8888888);
            fail("Expected exception");
        } catch (TagMyCodeApiException e) {
            assertEquals(404, e.getHttpStatusCode());
        }
    }

    @Test
    public void snippetParamListShouldContainsAllRequiredFields() throws Exception {
        ParamList paramList = tagMyCode.prepareSnippetParamList(resourceGenerate.aSnippet());
        assertTrue(paramList.containsKey("title"));
        assertTrue(paramList.containsKey("language_id"));
        assertTrue(paramList.containsKey("code"));
        assertTrue(paramList.containsKey("description"));
        assertTrue(paramList.containsKey("tags"));
        assertTrue(paramList.containsKey("is_private"));
    }

    @Test
    public void createSnippet() throws Exception {
        stubFor(post(urlMatching("/snippets.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceGenerate.aSnippet().toJson()
                        )));

        Snippet snippetInput = new Snippet();
        snippetInput.setCode("code\r\nsecond line");
        snippetInput.setLanguage(resourceGenerate.aLanguage());
        Snippet snippet = tagMyCode.createSnippet(snippetInput);

        assertEquals(resourceGenerate.aSnippet(), snippet);
    }

    @Test
    public void deleteSnippetWithSuccess() throws Exception {
        stubFor(delete(urlMatching("/snippets.*"))
                .willReturn(aResponse()
                                .withStatus(204)
                                .withHeader("Content-Type", "application/json")
                ));


        tagMyCode.deleteSnippet(1);

        verify(deleteRequestedFor(urlMatching("/snippets/1?.*")));
    }

    @Test
    public void deleteSnippetWithNoSuccess() throws Exception {
        stubFor(delete(urlMatching("/snippets.*"))
                .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader("Content-Type", "application/json")
                ));

        try {
            tagMyCode.deleteSnippet(1);
            fail("Expected exception");
        } catch (TagMyCodeApiException ignore) {
            verify(deleteRequestedFor(urlMatching("/snippets/1?.*")));
        }
    }
}
