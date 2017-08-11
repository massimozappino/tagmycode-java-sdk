package com.tagmycode.sdk.model;

import com.j256.ormlite.dao.Dao;
import org.junit.Before;
import org.junit.Test;
import support.BaseTest;
import support.MemDbService;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SnippetsStorageTest extends BaseTest {
    SnippetsStorage snippetsStorage;
    Dao<Snippet, String> snippetDao;

    @Before
    public void initSnippetsStorage() throws SQLException {
        MemDbService dbService = new MemDbService();
        dbService.initialize();
        snippetsStorage = new SnippetsStorage(dbService);
        snippetDao = dbService.snippetDao();
    }

    @Test
    public void findBySnippetId() throws Exception {
        snippetDao.create(resourceGenerate.aSnippet().setId(1));
        snippetDao.create(resourceGenerate.aSnippet().setId(2));

        Snippet snippet = snippetsStorage.findBySnippetId(1);

        assertEquals(1, snippet.getId());
    }

    @Test
    public void findBySnippetIdOnEmptyTable() throws Exception {
        assertNull(snippetsStorage.findBySnippetId(1));
    }

    @Test
    public void findDirtySnippets() throws Exception {
        snippetDao.create(resourceGenerate.aSnippet().setDirty(true).setTitle("dirty"));
        snippetDao.create(resourceGenerate.aSnippet().setDirty(false).setTitle("non dirty"));

        List<Snippet> dirtySnippets = snippetsStorage.findDirty();

        assertEquals(1, dirtySnippets.size());
        assertEquals("dirty", dirtySnippets.get(0).getTitle());
    }
}
