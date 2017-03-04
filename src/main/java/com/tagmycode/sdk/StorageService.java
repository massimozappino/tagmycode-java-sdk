package com.tagmycode.sdk;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tagmycode.sdk.model.Language;
import com.tagmycode.sdk.model.Snippet;

import java.io.IOException;
import java.sql.SQLException;

public class StorageService {

    private JdbcConnectionSource jdbcConnectionSource;
    private Dao<Language, String> languageDao;
    private Dao<Snippet, String> snippetDao;

    public void initialize(String dbPath) throws SQLException {
        String databaseUrl = "jdbc:h2:" + dbPath;
        jdbcConnectionSource = new JdbcConnectionSource(databaseUrl);
        languageDao = createDao(Language.class);
        snippetDao = createDao(Snippet.class);
        createTableIfNotExists(Language.class);
        createTableIfNotExists(Snippet.class);
    }

    public void createTableIfNotExists(Class aClass) throws SQLException {
        TableUtils.createTableIfNotExists(jdbcConnectionSource, aClass);
    }

    public Dao<Language, String> languageDao() {
        return languageDao;
    }

    public Dao<Snippet, String> snippetDao() {
        return snippetDao;
    }

    private <D extends Dao<T, ?>, T> D createDao(Class<T> aClass) throws SQLException {
        return DaoManager.createDao(jdbcConnectionSource, aClass);
    }

    public void close() throws IOException {
        if (jdbcConnectionSource != null) {
            jdbcConnectionSource.close();
        }
    }
}
