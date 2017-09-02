package com.tagmycode.sdk.model;

import com.j256.ormlite.dao.Dao;
import org.junit.Before;
import org.junit.Test;
import support.BaseTest;
import support.MemDbService;

import java.sql.SQLException;

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
    public void findVisible() throws Exception {
        snippetDao.create(resourceGenerate.aSnippet().setId(1));
        snippetDao.create(resourceGenerate.aSnippet().setId(2).setDeleted(true));

        SnippetsCollection snippets = snippetsStorage.findVisible();

        assertEquals(1, snippets.size());
        assertEquals(1, snippets.firstElement().getId());
    }

    @Test
    public void findDeleted() throws Exception {
        snippetDao.create(resourceGenerate.aSnippet().setId(1));
        snippetDao.create(resourceGenerate.aSnippet().setId(2).setDeleted(true));

        SnippetsCollection snippets = snippetsStorage.findDeleted();

        assertEquals(1, snippets.size());
        assertEquals(2, snippets.firstElement().getId());
    }

    @Test
    public void findDeletedIds() throws Exception {
        snippetDao.create(resourceGenerate.aSnippet().setId(1));
        snippetDao.create(resourceGenerate.aSnippet().setId(2).setDeleted(true));

        SnippetsDeletions snippetsDeletions = snippetsStorage.findDeletedIds();

        assertEquals(1, snippetsDeletions.size());
        assertEquals(2, (int) snippetsDeletions.get(0));
    }

    @Test
    public void findByLocalId() throws Exception {
        snippetDao.create(resourceGenerate.aSnippet().setId(2).setLocalId(1));
        snippetDao.create(resourceGenerate.aSnippet().setId(1).setLocalId(2));

        Snippet snippet = snippetsStorage.findByLocalId(1);

        assertEquals(1, snippet.getLocalId());
        assertEquals(2, snippet.getId());
    }

    @Test
    public void findByLocalIdWithZeroReturnAlwaysNull() throws Exception {
        snippetDao.create(resourceGenerate.aSnippet().setLocalId(0));
        assertEquals(1, snippetDao.queryForAll().get(0).getLocalId());
        assertNull(snippetsStorage.findByLocalId(0));
    }

    @Test
    public void findBySnippetIdOnEmptyTable() throws Exception {
        assertNull(snippetsStorage.findBySnippetId(1));
    }

    @Test
    public void findDirtySnippetsNotDeleted() throws Exception {
        snippetDao.create(resourceGenerate.aSnippet().setDirty(true).setTitle("dirty"));
        snippetDao.create(resourceGenerate.aSnippet().setDirty(false).setTitle("non dirty"));
        snippetDao.create(resourceGenerate.aSnippet().setDirty(true).setTitle("dirty not deleted").setDeleted(true));

        SnippetsCollection dirtySnippets = snippetsStorage.findDirtyNotDeleted();

        assertEquals(1, dirtySnippets.size());
        assertEquals("dirty", dirtySnippets.firstElement().getTitle());
    }

    @Test
    public void deleteNonDirty() throws Exception {
        snippetDao.create(resourceGenerate.aSnippet().setDirty(true).setTitle("dirty"));
        snippetDao.create(resourceGenerate.aSnippet().setDirty(false).setTitle("non dirty"));
        snippetDao.create(resourceGenerate.aSnippet().setDirty(true).setTitle("dirty not deleted").setDeleted(true));


        assertEquals(2, snippetsStorage.findVisible().size());
        assertEquals(1, snippetsStorage.findDirtyNotDeleted().size());
        assertEquals(1, snippetsStorage.findDeleted().size());

        snippetsStorage.deleteNonDirty();

        assertEquals(1, snippetsStorage.findVisible().size());
        assertEquals(1, snippetsStorage.findDirtyNotDeleted().size());
        assertEquals(1, snippetsStorage.findDeleted().size());
    }
}
