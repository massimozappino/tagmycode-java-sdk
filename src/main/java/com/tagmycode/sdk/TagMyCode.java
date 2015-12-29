package com.tagmycode.sdk;


import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import com.tagmycode.sdk.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.scribe.model.Verb;

public class TagMyCode {
    private final Client client;

    public TagMyCode(Client client) {
        this.client = client;
    }

    public Client getClient()
    {
        return client;
    }

    public User fetchAccount() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("account", Verb.GET);
        return new User(cr.getBody());
    }

    public LanguageCollection fetchLanguages() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("languages", Verb.GET);
        LanguageCollection languages = new LanguageCollection();

        try {
            JSONArray jsonArray = new JSONArray(cr.getBody());
            for (int i = 0; i < jsonArray.length(); i++) {
                languages.add(new Language(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        }
        return languages;
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

    public Snippet fetchSnippet(int snippetId) throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("snippets/" + snippetId, Verb.GET);
        return new Snippet(cr.getBody());
    }

    public SnippetCollection fetchSnippets() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("snippets", Verb.GET);

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

    protected SnippetCollection createSnippetsCollection(ClientResponse cr) throws TagMyCodeJsonException {
        SnippetCollection collection = new SnippetCollection();

        try {
            JSONArray jsonArray = new JSONArray(cr.getBody());
            for (int i = 0; i < jsonArray.length(); i++) {
                collection.add(new Snippet(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        }
        return collection;
    }
}
