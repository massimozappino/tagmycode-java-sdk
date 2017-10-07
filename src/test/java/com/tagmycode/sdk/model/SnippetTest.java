package com.tagmycode.sdk.model;

import com.j256.ormlite.dao.Dao;
import com.tagmycode.sdk.DateParser;
import com.tagmycode.sdk.DbService;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import support.MemDbService;
import support.ModelAbstractBaseTest;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.*;

public class SnippetTest extends ModelAbstractBaseTest {

    @Test
    public void newSnippet() {
        assertEquals(0, new Snippet().getId());
    }

    @Test
    public void newModelWithJsonObject() throws Exception {
        String jsonString = resourceGenerate.aLanguage().toJson();
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
        assertSnippetValues(new Snippet(jsonObject), 1);
    }

    @Test
    public void urlCanBeNull() throws Exception {
        Snippet snippet = resourceGenerate.aSnippet();
        snippet.setUrl(null);

        assertEquals(snippet.toJson(), new Snippet(snippet.toJson()).toJson());
        assertNull(new Snippet(snippet.toJson()).getUrl());
    }

    @Test
    public void newModelWithJsonString() throws Exception {
        String jsonString = resourceGenerate.getResourceReader().readFile("snippet.json");
        assertSnippetValues(new Snippet(jsonString), 1);
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
        s.setDirty(true);
        s.setDeleted(true);
    }

    @Test
    public void canSaveWithoutLocalId() throws Exception {
        DbService memDbService = new MemDbService().initialize();
        Dao<Snippet, String> snippetDao = memDbService.snippetDao();
        snippetDao.create(new Snippet());
        assertEquals(1, snippetDao.queryForAll().get(0).getLocalId());
        assertEquals(0, snippetDao.queryForAll().get(0).getId());
    }

    private void assertSnippetValues(Snippet s, int id) throws ParseException {
        assertEquals(id, s.getId());
        assertEquals("My title", s.getTitle());
        assertEquals("code\r\nsecond line", s.getCode());
        assertEquals("A simple description", s.getDescription());
        assertEquals("Java", s.getLanguage().getName());
        assertEquals("tag1 tag2 tag3", s.getTags());
        Assert.assertEquals(DateParser.parseDate("2010-11-22T13:11:25+00:00"), s.getCreationDate());
        assertEquals(DateParser.parseDate("2010-11-22T13:11:25+00:00"), s.getUpdateDate());
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
        String expectedJson = "{\"tags\":null,\"id\":0,\"title\":null,\"updated_at\":null,\"description\":null,\"created_at\":null,\"is_private\":false,\"language\":{\"id\":1,\"name\":\"Text\",\"code\":\"text\"},\"code\":null,\"url\":null}";
        JSONAssert.assertEquals(expectedJson, snippet.toJson(), false);

        snippet.setId(1)
                .setTitle("My title")
                .setCode("code\r\nsecond line")
                .setTags("tag1 tag2 tag3")
                .setDescription("A simple description")
                .setCreationDate(DateParser.parseDate("2010-11-22T13:11:25Z"))
                .setUpdateDate(DateParser.parseDate("2010-11-22T13:11:25Z"))
                .setLanguage(resourceGenerate.aLanguage())
                .setUrl("https://tagmycode.com/snippet/1");

        assertEquals(resourceGenerate.aSnippet().toJson(), snippet.toJson());
        assertEquals(resourceGenerate.aLanguage(), snippet.getLanguage());
        assertTrue(snippet.equals(resourceGenerate.aSnippet()));
    }

    @Test
    public void dirtyIsFalseOnNewSnippet() {
        Snippet snippet = new Snippet();
        assertFalse(snippet.isDirty());
        snippet.setDirty(true);
        assertTrue(snippet.isDirty());
        snippet.setDirty(false);
        assertFalse(snippet.isDirty());
    }

    @Test
    public void toJsonShouldNotThrowsNPE() throws Exception {
        Snippet snippet = new Snippet();
        assertEquals(new Snippet(), new Snippet());
        assertEquals(new Snippet().toJson(), new Snippet(snippet.toJson()).toJson());
    }

    @Test
    public void serializeAndDeserialize() throws Exception {
        Snippet model = resourceGenerate.aSnippet();
        Snippet restored = new Snippet(model.toJson());
        assertTrue(model.equals(restored));
    }

}
