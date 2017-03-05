package com.tagmycode.sdk.model;


import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONObject;
import org.junit.Test;
import support.ModelAbstractBaseTest;

import java.io.IOException;

import static org.junit.Assert.*;


public class LanguageTest extends ModelAbstractBaseTest {

    @Test
    public void newLanguage() throws IOException {
        Language l = new Language();
        assertNull(l.toString());
    }

    @Test
    public void newModelWithJsonObject() throws Exception {
        JSONObject jsonObject = new JSONObject()
                .put("id", 1)
                .put("name", "Java")
                .put("code", "java");
        Language l = new Language(jsonObject);
        assertLanguageValues(l, 1);
    }

    @Override
    @Test
    public void newModelWithJsonString() throws IOException, TagMyCodeJsonException {
        String jsonString = resourceGenerate.getResourceReader().readFile("language.json");
        Language l = new Language(jsonString);
        assertLanguageValues(l, 2);
    }

    @Test
    public void twoObjectsAreEquals() {
        assertEquals(new Language(), new Language());
    }

    @Test
    public void settingFields() {
        Language l = new Language();
        l.setName("Java");
        l.setCode("java");
        assertLanguageValues(l, 0);
    }

    @Test
    public void toStringMethodReturnName() {
        Language l = new Language();
        assertNull(l.toString());
        l.setName("Java");
        assertEquals("Java", l.toString());
    }

    @Test
    public void collectionOfLanguages() {
        LanguageCollection languages = new LanguageCollection();
        languages.add(new Language());
        assertEquals(1, languages.size());
    }

    @Test
    public void toJson() throws Exception {
        Language language = new Language();
        language.setId(2);
        language.setName("Java");
        language.setCode("java");
        assertTrue(language.equals(resourceGenerate.aLanguage()));
    }

    @Test
    public void compareModelObject() throws IOException, TagMyCodeJsonException {
        Language model1 = resourceGenerate.aLanguage();
        Language model2 = resourceGenerate.aLanguage();
        assertTrue(model1.equals(model2));

        model2.setName("false");
        assertFalse(model1.equals(model2));
    }

    @Test
    public void serializeAndDeserialize() throws Exception {
        Language model = resourceGenerate.aLanguage();
        Language restored = new Language(model.toJson());
        assertTrue(model.equals(restored));
    }

    private void assertLanguageValues(Language l, int id) {
        assertEquals(id, l.getId());
        assertEquals("Java", l.getName());
        assertEquals("java", l.getCode());
    }
}
