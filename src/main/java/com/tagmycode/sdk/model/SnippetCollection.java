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
                Snippet snippet = new Snippet(ja.getJSONObject(i));
                add(snippet);
            }
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        }
    }

    public SnippetCollection() {

    }
}
