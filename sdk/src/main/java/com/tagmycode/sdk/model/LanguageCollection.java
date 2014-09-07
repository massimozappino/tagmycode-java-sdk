package com.tagmycode.sdk.model;

import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONArray;
import org.json.JSONException;

public class LanguageCollection extends ModelCollection<Language> {
    public LanguageCollection(String json) throws TagMyCodeJsonException {
        JSONArray ja;
        try {
            ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                Language language = new Language(ja.getJSONObject(i));
                add(language);
            }
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        }
    }

    public LanguageCollection() {
        super();
    }
}
