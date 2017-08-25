package com.tagmycode.sdk.model;

import com.j256.ormlite.dao.Dao;
import com.tagmycode.sdk.DbService;

import java.sql.SQLException;
import java.util.List;

public class SnippetsStorage {
    private Dao<Snippet, String> snippetDao;

    public SnippetsStorage(DbService dbService) {
        snippetDao = dbService.snippetDao();
    }

    public Snippet findBySnippetId(int snippetId) throws SQLException {
        List<Snippet> snippets = snippetDao.queryForEq("id", snippetId);
        if (snippets.size() == 0) {
            return null;
        }
        return snippets.get(0);
    }

    public Snippet findByLocalId(int localId) throws SQLException {
        return snippetDao.queryForId(String.valueOf(localId));
    }

    public SnippetsCollection findDirty() throws SQLException {
        return new SnippetsCollection(snippetDao.queryForEq("dirty", true));
    }

    public SnippetsCollection findVisible() throws SQLException {
        return new SnippetsCollection(snippetDao.queryForEq("deleted", false));
    }


    public SnippetsCollection findDeleted() throws SQLException {
        return new SnippetsCollection(snippetDao.queryForEq("deleted", true));
    }
}
