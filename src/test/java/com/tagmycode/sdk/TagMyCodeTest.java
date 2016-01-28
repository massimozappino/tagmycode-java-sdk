package com.tagmycode.sdk;

import com.tagmycode.sdk.exception.TagMyCodeApiException;
import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.model.*;
import org.junit.Before;
import org.junit.Test;
import support.ClientBaseTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
                        .withBody(resourceGenerate.aUser().toJson())));

        User user = tagMyCode.fetchAccount();
        assertEquals(resourceGenerate.aUser(), user);
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
    public void fetchSnippetsCollection() throws Exception {
        stubFor(get(urlMatching("/snippets.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceGenerate.aSnippetCollection().toJson()
                        )));

        SnippetCollection snippets = tagMyCode.fetchSnippetsCollection();

        assertEquals(resourceGenerate.aSnippetCollection(), snippets);
    }

    @Test
    public void fetchSnippetsChanges() throws Exception {
        stubFor(get(urlMatching("/snippets.*"))
                .withHeader("Snippets-Changes-Since", equalTo(resourceGenerate.aSnippetsLastUpdate()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(resourceGenerate.aSnippetCollection().toJson()
                        )));

        SnippetCollection snippets = tagMyCode.fetchSnippetsChanges(resourceGenerate.aSnippetsLastUpdate());

        assertEquals(resourceGenerate.aSnippetCollection(), snippets);
        assertEquals("Sun, 24 Jan 2016 20:00:00 GMT", tagMyCode.getLastSnippetUpdate());
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
    public void createSnippetsCollection() throws Exception {
        assertEquals(null, tagMyCode.getLastSnippetUpdate());
        ClientResponse mock = mock(ClientResponse.class);
        when(mock.getBody()).thenReturn(resourceGenerate.aSnippetCollection().toJson());
        when(mock.getLastUpdate()).thenReturn("xxx");

        tagMyCode.createSnippetsCollection(mock);

        assertEquals("xxx", tagMyCode.getLastSnippetUpdate());
    }


    @Test
    public void updateSnippetWithSuccess() throws Exception {
        Snippet snippet = resourceGenerate.aSnippet();
        stubFor(put(urlMatching("/snippets/1.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(snippet.toJson()
                        )));

        assertEquals(snippet, tagMyCode.updateSnippet(resourceGenerate.aSnippet()));
        verify(putRequestedFor(urlMatching("/snippets/1.*")));
    }

    @Test
    public void updateSnippetWithoutSuccess() throws Exception {
        stubFor(put(urlMatching("/snippets/1.*"))
                .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader("Content-Type", "application/json")
                ));

        try {
            tagMyCode.updateSnippet(resourceGenerate.anotherSnippet());
            fail("Expected exception");
        } catch (TagMyCodeApiException ignore) {
            verify(putRequestedFor(urlMatching("/snippets/9?.*")));
        }
    }

    @Test
    public void deleteSnippetWithSuccess() throws Exception {
        stubFor(delete(urlMatching("/snippets/1.*"))
                .willReturn(aResponse()
                                .withStatus(204)
                                .withHeader("Content-Type", "application/json")
                ));


        tagMyCode.deleteSnippet(1);

        verify(deleteRequestedFor(urlMatching("/snippets/1.*")));
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
