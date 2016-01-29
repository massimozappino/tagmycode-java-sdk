package com.tagmycode.sdk;


import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import com.tagmycode.sdk.model.LanguageCollection;
import com.tagmycode.sdk.model.Snippet;
import com.tagmycode.sdk.model.SnippetCollection;
import com.tagmycode.sdk.model.User;
import org.scribe.model.Verb;

public class TagMyCode {
    private final Client client;
    private String lastSnippetUpdate;

    public TagMyCode(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public User fetchAccount() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("account", Verb.GET);
        return new User(cr.getBody());
    }

    public LanguageCollection fetchLanguages() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("languages", Verb.GET);
        return createLanguageCollection(cr);
    }

    public SnippetCollection searchSnippets(String query) throws TagMyCodeException {
        ParamList paramList = new ParamList()
                .add("q", query);

        ClientResponse cr = client.sendRequest("search/", Verb.GET, paramList);

        return createSnippetsCollection(cr);
    }

    public Snippet createSnippet(Snippet inputSnippet) throws TagMyCodeException {
        ParamList paramList = prepareSnippetParamList(inputSnippet);
        ClientResponse cr = client.sendRequest("snippets/", Verb.POST, paramList);

        return new Snippet(cr.getBody());
    }

    public Snippet updateSnippet(Snippet inputSnippet) throws TagMyCodeException {
        ParamList paramList = prepareSnippetParamList(inputSnippet);
        ClientResponse cr = client.sendRequest("snippets/" + inputSnippet.getId(), Verb.PUT, paramList);

        return new Snippet(cr.getBody());
    }

    public Snippet fetchSnippet(int snippetId) throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("snippets/" + snippetId, Verb.GET);

        return new Snippet(cr.getBody());
    }

    public SnippetCollection fetchSnippetsCollection() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("snippets", Verb.GET);

        return createSnippetsCollection(cr);
    }

    public SnippetCollection fetchSnippetsChanges(String gmtDate) throws TagMyCodeException {
        ParamList headers = new ParamList();
        headers.add("Snippets-Changes-Since", gmtDate);
        ClientResponse cr = client.sendRequest("snippets", Verb.GET, new ParamList(), headers);

        return createSnippetsCollection(cr);
    }

    public void deleteSnippet(int snippetId) throws TagMyCodeException {
        client.sendRequest("snippets/" + snippetId, Verb.DELETE);
    }

    protected ParamList prepareSnippetParamList(Snippet snippet) {
        return new ParamList()
                .add("language_id", snippet.getLanguage().getId())
                .add("title", snippet.getTitle())
                .add("code", snippet.getCode())
                .add("description", snippet.getDescription())
                .add("tags", snippet.getTags())
                .add("is_private", snippet.isPrivate());
    }

    private LanguageCollection createLanguageCollection(ClientResponse cr) throws TagMyCodeJsonException {
        return new LanguageCollection(cr.getBody());
    }

    protected SnippetCollection createSnippetsCollection(ClientResponse cr) throws TagMyCodeJsonException {
        lastSnippetUpdate = cr.extractLastResourceUpdate();
        return new SnippetCollection(cr.getBody());
    }

    public String getLastSnippetUpdate() {
        return lastSnippetUpdate;
    }
}
