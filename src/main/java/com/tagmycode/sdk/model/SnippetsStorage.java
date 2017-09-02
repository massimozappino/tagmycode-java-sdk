package com.tagmycode.sdk.model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.tagmycode.sdk.DbService;

import java.sql.SQLException;
import java.util.List;

public class SnippetsStorage {
    public static final String COLUMN_DIRTY = "dirty";
    public static final String COLUMN_DELETED = "deleted";
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

    public SnippetsCollection findDirtyNotDeleted() throws SQLException {
        return new SnippetsCollection(snippetDao.queryBuilder().where()
                .eq(COLUMN_DIRTY, true)
                .and()
                .eq(COLUMN_DELETED, false).query());
    }

    public SnippetsCollection findVisible() throws SQLException {
        return new SnippetsCollection(snippetDao.queryForEq(COLUMN_DELETED, false));
    }


    public SnippetsCollection findDeleted() throws SQLException {
        return new SnippetsCollection(snippetDao.queryForEq(COLUMN_DELETED, true));
    }

    public SnippetsDeletions findDeletedIds() throws SQLException {
        SnippetsDeletions snippetsDeletions = new SnippetsDeletions();
        SnippetsCollection deletedSnippets = findDeleted();
        for (Snippet snippet : deletedSnippets) {
            snippetsDeletions.add(snippet.getId());
        }
        return snippetsDeletions;
    }

    public void deleteNonDirty() throws SQLException {
        DeleteBuilder<Snippet, String> deleteBuilder = snippetDao.deleteBuilder();
        deleteBuilder.where().eq(COLUMN_DIRTY, false);
        deleteBuilder.delete();
    }

    public Dao<Snippet, String> getSnippetDao() {
        return snippetDao;
    }
}
