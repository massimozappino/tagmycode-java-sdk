package com.tagmycode.sdk.model;

import com.tagmycode.sdk.FileNameToLanguage;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONArray;
import org.json.JSONException;

public class LanguagesCollection extends ModelCollection<Language> {
    public LanguagesCollection(String json) throws TagMyCodeJsonException {
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

    public LanguagesCollection() {
    }

    public Language findByCode(String code) {
        for (Language language : this) {
            if (language.getCode().equals(code)) {
                return language;
            }
        }
        return null;
    }

    public Language findByFileName(String fileName) {
        return findByCode(FileNameToLanguage.getCode(fileName));
    }
}
