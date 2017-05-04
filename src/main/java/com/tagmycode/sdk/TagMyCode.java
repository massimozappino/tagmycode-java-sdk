package com.tagmycode.sdk;


import com.tagmycode.sdk.authentication.OauthToken;
import com.tagmycode.sdk.exception.TagMyCodeException;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import com.tagmycode.sdk.exception.TagMyCodeUnauthorizedException;
import com.tagmycode.sdk.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.scribe.model.Verb;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class TagMyCode {
    private final Client client;
    private String lastSnippetsUpdate;

    public TagMyCode(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public boolean isServiceAvailable() {
        String domain = client.getTagmycodeApi().getDomain();

        try {
            Socket socket = new Socket(domain, 80);
            boolean isConnected = socket.isConnected();
            socket.close();
            return isConnected;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User fetchAccount() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("account", Verb.GET);
        return new User(cr.getBody());
    }

    public LanguagesCollection fetchLanguages() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("languages", Verb.GET);
        return createLanguageCollection(cr);
    }

    public SnippetsCollection searchSnippets(String query) throws TagMyCodeException {
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

    public SnippetsCollection fetchSnippetsCollection() throws TagMyCodeException {
        ClientResponse cr = client.sendRequest("snippets", Verb.GET);

        return createSnippetsCollection(cr);
    }

    public SnippetsCollection fetchSnippetsChanges(String gmtDate) throws TagMyCodeException {
        ParamList headers = new ParamList();
        headers.add("Snippets-Changes-Since", gmtDate);
        ClientResponse cr = client.sendRequest("snippets", Verb.GET, new ParamList(), headers);

        return createSnippetsCollection(cr);
    }

    public SnippetsDeletions fetchDeletions(String gmtDate) throws TagMyCodeException {
        ParamList headers = new ParamList();
        headers.add("Snippets-Changes-Since", gmtDate);
        ParamList params = new ParamList();
        params.add("deletions", "true");
        ClientResponse cr = client.sendRequest("snippets", Verb.GET, params, headers);

        SnippetsDeletions deletions = new SnippetsDeletions();
        try {
            JSONArray jsonArray = new JSONArray(cr.getBody());
            for (int i = 0; i < jsonArray.length(); i++) {
                deletions.add(jsonArray.getInt(i));
            }
        } catch (JSONException e) {
            throw new TagMyCodeException(e);
        }

        return deletions;
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

    private LanguagesCollection createLanguageCollection(ClientResponse cr) throws TagMyCodeJsonException {
        return new LanguagesCollection(cr.getBody());
    }

    protected SnippetsCollection createSnippetsCollection(ClientResponse cr) throws TagMyCodeJsonException {
        lastSnippetsUpdate = cr.extractLastResourceUpdate();
        return new SnippetsCollection(cr.getBody());
    }

    public String getLastSnippetsUpdate() {
        return lastSnippetsUpdate;
    }

    public void setLastSnippetsUpdate(String lastSnippetsUpdate) {
        this.lastSnippetsUpdate = lastSnippetsUpdate;
    }

    public void syncSnippets(SnippetsCollection localSnippets, SnippetsDeletions localDeletions) throws TagMyCodeException {
        // TODO sync should return snippets tu add, snippets to update and snippets to delete

        SnippetsCollection remoteSnippets = fetchSnippetsChanges(lastSnippetsUpdate);
        SnippetsDeletions remoteDeletions = fetchDeletions(lastSnippetsUpdate);

        localSnippets.merge(remoteSnippets);
        localSnippets.deleteByDeletions(remoteDeletions);
        localSnippets.deleteByDeletions(localDeletions);

        ArrayList<Snippet> snippetsToAdd = new ArrayList<Snippet>();
        ArrayList<Snippet> snippetsToDelete = new ArrayList<Snippet>();

        for (Snippet snippet : localSnippets) {
            if (snippet.getId() == 0) {
                snippetsToAdd.add(createSnippet(snippet));
                snippetsToDelete.add(snippet);
            }
        }

        for (Snippet snippet : snippetsToAdd) {
            localSnippets.add(snippet);
        }

        for (Snippet snippet : snippetsToDelete) {
            localSnippets.remove(snippet);
        }
    }

    public OauthToken loadOauthToken() throws TagMyCodeException {
        return client.loadOauthToken();
    }

    public void revokeAccessToken() throws TagMyCodeException {
        client.revokeAccess();
    }

    public boolean isAuthenticated() {
        return client.isAuthenticated();
    }

    public void authenticate(String verificationCode) throws TagMyCodeException {
        client.fetchOauthToken(verificationCode);
        if (!client.isAuthenticated()) {
            throw new TagMyCodeUnauthorizedException();
        }
    }

    public String getAuthorizationUrl() {
        return client.getAuthorizationUrl();
    }

}
