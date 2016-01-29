package com.tagmycode.sdk.model;

import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONArray;
import org.json.JSONException;

public class SnippetCollection extends ModelCollection<Snippet> {
    public SnippetCollection(String json) throws TagMyCodeJsonException {
        JSONArray ja;
        try {
            ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                add(new Snippet(ja.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        }
    }

    public SnippetCollection() {

    }

    public boolean deleteById(int id) {
        for (Snippet snippet : this) {
            if (snippet.getId() == id) {
                remove(snippet);
                return true;
            }
        }
        return false;
    }

    public Snippet getById(int id) {
        if (id == 0) {
            return null;
        }
        for (Snippet snippet : this) {
            if (snippet.getId() == id) {
                return snippet;
            }
        }
        return null;
    }

    public Snippet updateSnippet(Snippet snippet) {
        Snippet actualSnippet = getById(snippet.getId());
        if (actualSnippet == null) {
            return null;
        }
        int i = indexOf(actualSnippet);
        set(i, snippet);
        return snippet;
    }

    public void merge(SnippetCollection newSnippetCollection) {
        for (Snippet snippet : newSnippetCollection) {
            Snippet existentSnippet = getById(snippet.getId());
            if (existentSnippet != null) {
                remove(existentSnippet);
            }
            add(snippet);
        }
    }
}
