package com.tagmycode.sdk.model;

import com.tagmycode.sdk.DateParser;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import support.ModelAbstractBaseTest;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.*;

public class SnippetTest extends ModelAbstractBaseTest {

    @Test
    public void newSnippet() {
        Snippet s = new Snippet();
        assertEquals(0, s.getId());
    }

    @Test
    public void newModelWithJsonObject() throws Exception {

        String jsonString = resourceGenerate.getResourceReader().readFile("language.json");
        JSONObject jsonObject = new JSONObject()
                .put("id", 1)
                .put("title", "My title")
                .put("code", "code\r\nsecond line")
                .put("description", "A simple description")
                .put("language", new JSONObject(jsonString))
                .put("tags", "tag1 tag2 tag3")
                .put("is_private", false)
                .put("url", "https://tagmycode.com/snippet/1")
                .put("created_at", "2010-11-22T13:11:25+00:00")
                .put("updated_at", "2010-11-22T13:11:25+00:00");
        Snippet s = new Snippet(jsonObject);
        assertSnippetValues(s, 1);
    }

    @Test
    public void newModelWithJsonString() throws Exception {
        String jsonString = resourceGenerate.getResourceReader().readFile("snippet.json");
        Snippet s = new Snippet(jsonString);
        assertSnippetValues(s, 1);
    }

    @Test
    public void settingFields() throws IOException, TagMyCodeJsonException {
        Snippet s = new Snippet();
        s.setId(1);
        s.setTitle("My title");
        s.setLanguage(new Language(resourceGenerate.getResourceReader().readFile("language.json")));
        s.setCode("code");
        s.setDescription("A simple description");
        s.setTags("tag1 tag2 tag3");
        s.setPrivate(true);
    }


    private void assertSnippetValues(Snippet s, int id) throws ParseException {
        assertEquals(id, s.getId());
        assertEquals("My title", s.getTitle());
        assertEquals("code\r\nsecond line", s.getCode());
        assertEquals("A simple description", s.getDescription());
        assertEquals("Java", s.getLanguage().getName());
        assertEquals("tag1 tag2 tag3", s.getTags());
        Assert.assertEquals(new DateParser().parseDate("2010-11-22T13:11:25+00:00"), s.getCreationDate());
        assertEquals(new DateParser().parseDate("2010-11-22T13:11:25+00:00"), s.getUpdateDate());
        assertEquals("https://tagmycode.com/snippet/1", s.getUrl());
    }


    @Test
    public void compareModelObject() throws IOException, TagMyCodeJsonException {
        Snippet snippet1 = resourceGenerate.aSnippet();
        Snippet snippet2 = resourceGenerate.aSnippet();
        assertTrue(snippet1.equals(snippet2));

        snippet2.setTitle("false");
        assertFalse(snippet1.equals(snippet2));
    }


    @Test
    public void newSnippetWithNoLanguage() throws IOException, TagMyCodeJsonException {
        Snippet snippet = new Snippet();

        assertTrue(snippet.getLanguage() instanceof DefaultLanguage);
    }

    @Test
    public void toJson() throws Exception {
        Snippet snippet = new Snippet();
        snippet.setId(1)
                .setTitle("My title")
                .setCode("code\r\nsecond line")
                .setTags("tag1 tag2 tag3")
                .setDescription("A simple description")
                .setCreationDate(new DateParser().parseDate("2010-11-22T13:11:25Z"))
                .setUpdateDate(new DateParser().parseDate("2010-11-22T13:11:25Z"))
                .setLanguage(resourceGenerate.aLanguage())
                .setUrl("https://tagmycode.com/snippet/1");

        assertEquals(resourceGenerate.aSnippet().toJson(), snippet.toJson());
        assertEquals(resourceGenerate.aLanguage(), snippet.getLanguage());
        assertTrue(snippet.equals(resourceGenerate.aSnippet()));
    }

    @Test
    public void serializeAndDeserialize() throws Exception {
        Snippet model = resourceGenerate.aSnippet();
        Snippet restored = new Snippet(model.toJson());
        assertTrue(model.equals(restored));
    }

}
