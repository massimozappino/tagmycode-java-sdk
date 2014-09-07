package com.tagmycode.sdk;


import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import com.tagmycode.sdk.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.scribe.model.Verb;

public class TagMyCode {
    private Client client;

    public TagMyCode(Client client) {
        this.client = client;
    }

    public User getAccount() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("account", Verb.GET);
        return new User(cr.getBody());
    }

    public LanguageCollection getLanguages() throws TagMyCodeException {
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

    public ModelCollection<Snippet> searchSnippets(String query) throws TagMyCodeException {
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


    public ParamList prepareSnippetParamList(Snippet snippet) {
        return new ParamList()
                .add("language_id", snippet.getLanguage().getId())
                .add("title", snippet.getTitle())
                .add("code", snippet.getCode())
                .add("description", snippet.getDescription())
                .add("tags", snippet.getTags())
                .add("is_private", snippet.isPrivate());
    }


    public ModelCollection<Snippet> createSnippetsCollection(ClientResponse cr) throws TagMyCodeJsonException {
        ModelCollection<Snippet> collection;

        collection = new ModelCollection<Snippet>();

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

    public Snippet getSnippet(int snippetId) throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("snippets/" + snippetId, Verb.GET);
        return new Snippet(cr.getBody());
    }

    public void deleteSnippet(int snippetId) throws TagMyCodeException {
        client.sendRequest("snippets/" + snippetId, Verb.DELETE);
    }
}
