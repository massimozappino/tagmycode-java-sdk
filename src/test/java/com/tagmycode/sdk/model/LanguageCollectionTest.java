package com.tagmycode.sdk.model;


import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONException;
import org.junit.Test;
import support.BaseTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void testFindByCode() throws Exception {
        LanguagesCollection languageCollection = resourceGenerate.aLanguageCollection();
        assertEquals("Java", languageCollection.findByCode("java").getName());
        assertEquals("Xxx", languageCollection.findByCode("xxx").getName());
    }

    @Test
    public void testFindByFileName() throws Exception {
        LanguagesCollection languageCollection = resourceGenerate.aLanguageCollection();
        Language xml = resourceGenerate.aLanguage();
        xml.setCode("xml");
        xml.setName("XML");
        languageCollection.add(xml);

        assertEquals("Java", languageCollection.findByFileName("file.java").getName());
        assertEquals("XML", languageCollection.findByFileName("pom.xml").getName());
        assertNull(languageCollection.findByFileName("file"));
    }

    @Test
    public void testConstructorWithList() throws Exception {
        List<Language> arrayListOfLanguages = new ArrayList<Language>();
        arrayListOfLanguages.add(resourceGenerate.aLanguage());
        arrayListOfLanguages.add(resourceGenerate.anotherLanguage());

        LanguagesCollection languagesCollection = new LanguagesCollection(arrayListOfLanguages);
        assertEquals(2, languagesCollection.size());
    }

    @Test
    public void testConstructorWithMultipleElements() throws Exception {
        LanguagesCollection languagesCollection = new LanguagesCollection(resourceGenerate.aLanguage(), resourceGenerate.anotherLanguage());
        assertEquals(2, languagesCollection.size());
    }

}
