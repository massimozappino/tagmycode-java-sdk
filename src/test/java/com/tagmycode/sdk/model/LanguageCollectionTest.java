package com.tagmycode.sdk.model;


import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import support.BaseTest;
import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class LanguageCollectionTest extends BaseTest {

    @Test
    public void restoreFromJson() throws IOException, JSONException, TagMyCodeJsonException {
        LanguagesCollection lc = new LanguagesCollection();

        Language language1 = resourceGenerate.aLanguage();
        language1.setId(1);

        Language language2 = resourceGenerate.aLanguage();
        language2.setId(2);

        lc.add(language1);
        lc.add(language2);

        String json = lc.toJson();
        assertEquals(json, new LanguagesCollection(json).toJson());
    }

    @Test
    public void testGetByCode() throws Exception {
        LanguagesCollection languageCollection = resourceGenerate.aLanguageCollection();
        assertEquals("Java", languageCollection.getByCode("java").getName());
        assertEquals("Xxx", languageCollection.getByCode("xxx").getName());
    }
}
