package com.tagmycode.sdk;

import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.authentication.VoidOauthToken;
import com.tagmycode.sdk.exception.TagMyCodeApiException;
import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import support.ClientBaseTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class TagMyCodeTest extends ClientBaseTest {
    private TagMyCode tagMyCode;

    @Before
    public void initTagMyCodeObject() {
        tagMyCode = new TagMyCode(client);
    }

    @Test
    public void isServiceAvailable() throws Exception {
        tagMyCode = new TagMyCode(createProductionClient());
        assertTrue(tagMyCode.isServiceAvailable());
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

        SnippetCollection searchCollection = tagMyCode.searchSnippets("java");

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
                        .withHeader("Last-Resource-Update", resourceGenerate.aSnippetsLastUpdate())
                        .withBody(resourceGenerate.aSnippetCollection().toJson()
                        )));

        SnippetCollection snippets = tagMyCode.fetchSnippetsCollection();

        assertEquals(resourceGenerate.aSnippetCollection(), snippets);
        assertEquals("Sun, 24 Jan 2016 20:00:00 GMT", tagMyCode.getLastSnippetsUpdate());
    }

    @Test
    public void fetchSnippetsChanges() throws Exception {
        stubFor(get(urlMatching("/snippets.*"))
                .withHeader("Snippets-Changes-Since", equalTo(resourceGenerate.aSnippetsLastUpdate()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Last-Resource-Update", resourceGenerate.aSnippetsLastUpdate())
                        .withBody(resourceGenerate.aSnippetCollection().toJson())
                ));

        SnippetCollection snippets = tagMyCode.fetchSnippetsChanges(resourceGenerate.aSnippetsLastUpdate());

        assertEquals(resourceGenerate.aSnippetCollection(), snippets);
        assertEquals("Sun, 24 Jan 2016 20:00:00 GMT", tagMyCode.getLastSnippetsUpdate());
    }


    @Test
    public void fetchDeletions() throws Exception {
        stubFor(get(urlMatching("/snippets.*deletions=true.*"))
                .withHeader("Snippets-Changes-Since", equalTo(resourceGenerate.aSnippetsLastUpdate()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Last-Resource-Update", resourceGenerate.aSnippetsLastUpdate())
                        .withBody("[\"2\",\"5\",\"9\"]")
                ));

        SnippetsDeletions deletions = tagMyCode.fetchDeletions(resourceGenerate.aSnippetsLastUpdate());

        SnippetsDeletions expected = new SnippetsDeletions();
        expected.add(2);
        expected.add(5);
        expected.add(9);

        assertEquals(expected, deletions);
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
        assertEquals(null, tagMyCode.getLastSnippetsUpdate());
        ClientResponse mock = mock(ClientResponse.class);
        when(mock.getBody()).thenReturn(resourceGenerate.aSnippetCollection().toJson());
        when(mock.extractLastResourceUpdate()).thenReturn("xxx");

        tagMyCode.createSnippetsCollection(mock);

        assertEquals("xxx", tagMyCode.getLastSnippetsUpdate());
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

    @Test
    public void testSync() throws Exception {
        final SnippetCollection changedSnippets = resourceGenerate.aSnippetCollection();
        changedSnippets.add(resourceGenerate.aSnippet().setId(5).setTitle("changed title"));
        changedSnippets.add(resourceGenerate.aSnippet().setId(6));

        final SnippetsDeletions remoteDeletions = new SnippetsDeletions();
        remoteDeletions.add(3);

        SnippetCollection localSnippets = new SnippetCollection();
        localSnippets.add(resourceGenerate.aSnippet().setId(1));
        localSnippets.add(resourceGenerate.aSnippet().setId(3));
        localSnippets.add(resourceGenerate.aSnippet().setId(4));
        localSnippets.add(resourceGenerate.aSnippet().setId(5));
        localSnippets.add(resourceGenerate.aSnippet().setId(0));

        SnippetsDeletions localDeletions = new SnippetsDeletions();
        localDeletions.add(2);

        tagMyCode = new TagMyCode(client) {
            public SnippetCollection fetchSnippetsChanges(String gmtDate) throws TagMyCodeException {
                return changedSnippets;
            }

            public SnippetsDeletions fetchDeletions(String gmtDate) throws TagMyCodeException {
                return remoteDeletions;
            }

            public Snippet createSnippet(Snippet inputSnippet) throws TagMyCodeException {
                Snippet snippet = null;
                try {
                    snippet = resourceGenerate.aSnippet().setId(7);
                } catch (Exception ignored) {
                }
                return snippet;
            }
        };

        tagMyCode.syncSnippets(localSnippets, localDeletions);

        assertNotNull(localSnippets.getById(1));
        assertNull(localSnippets.getById(2));
        assertNull(localSnippets.getById(3));
        assertNotNull(localSnippets.getById(4));
        assertNotNull(localSnippets.getById(5));
        assertEquals("changed title", localSnippets.getById(5).getTitle());
        assertNotNull(localSnippets.getById(6));
        assertNotNull(localSnippets.getById(7));
        assertEquals(5, localSnippets.size());
    }

    @Test
    public void loadAccessToken() throws Exception {
        Client clientMock = mock(Client.class);
        tagMyCode = new TagMyCode(clientMock);

        OauthToken dummyOauthToken = new VoidOauthToken();
        when(clientMock.loadOauthToken()).thenReturn(dummyOauthToken);

        assertEquals(dummyOauthToken, tagMyCode.loadOauthToken());
        Mockito.verify(clientMock, times(1)).loadOauthToken();
    }

    @Test
    public void revokeAccessToken() throws Exception {
        Client clientMock = mock(Client.class);
        tagMyCode = new TagMyCode(clientMock);

        tagMyCode.revokeAccessToken();

        Mockito.verify(clientMock, times(1)).revokeAccess();
    }

    @Test
    public void isAuthenticated() throws Exception {
        Client clientMock = mock(Client.class);
        when(clientMock.isAuthenticated()).thenReturn(true);

        tagMyCode = new TagMyCode(clientMock);

        boolean authenticated = tagMyCode.isAuthenticated();

        Mockito.verify(clientMock, times(1)).isAuthenticated();
        assertTrue(authenticated);
    }
    @Test
    public void authenticate() throws Exception {
        Client clientMock = mock(Client.class);
        when(clientMock.isAuthenticated()).thenReturn(true);
        String verificationCode = "123";

        tagMyCode = new TagMyCode(clientMock);

        tagMyCode.authenticate(verificationCode);

        Mockito.verify(clientMock, times(1)).fetchOauthToken(verificationCode);
    }

    @Test
    public void getAuthorizationUrl() throws Exception {
        Client clientMock = mock(Client.class);
        String actualUrl = "http://ok";
        when(clientMock.getAuthorizationUrl()).thenReturn(actualUrl);
        tagMyCode = new TagMyCode(clientMock);

        String givenUrl = tagMyCode.getAuthorizationUrl();

        Mockito.verify(clientMock, times(1)).getAuthorizationUrl();
        assertEquals(givenUrl, actualUrl);
    }

}
