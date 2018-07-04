package com.tagmycode.sdk;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tagmycode.sdk.model.Language;
import com.tagmycode.sdk.model.Property;
import com.tagmycode.sdk.model.Snippet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class DbService {

    public static final String SCHEMA_VERSION_PROPERTY_KEY = "_schemaVersion";
    private final int currentSchemaVersion = 2;
    private JdbcConnectionSource jdbcConnectionSource;
    private Dao<Language, String> languageDao;
    private Dao<Snippet, String> snippetDao;
    private String dbPath;
    private Class[] tableClasses = {Language.class, Snippet.class, Property.class};
    private Dao<Property, String> propertyDao;

    public DbService(SaveFilePath saveFilePath) {
        this(saveFilePath.getPath());
    }

    public DbService(String dbPath) {
        this.dbPath = dbPath + File.separator + "db/data";
    }

    public DbService initialize() throws SQLException {
        if (jdbcConnectionSource == null) {
            String databaseUrl = "jdbc:h2:" + dbPath;
            jdbcConnectionSource = new JdbcConnectionSource(databaseUrl);
            languageDao = createDao(Language.class);
            snippetDao = createDao(Snippet.class);
            propertyDao = createDao(Property.class);

            if (!isCurrentSchemaVersion()) {
                dropAllTables();
                createAllTables();
                setCurrentSchemaVersion();
            }
        }
        return this;
    }

    protected boolean isCurrentSchemaVersion() {
        Property schemaVersionProperty = null;
        try {
            schemaVersionProperty = propertyDao.queryForId(SCHEMA_VERSION_PROPERTY_KEY);
        } catch (Throwable ignored) {
        }
        return schemaVersionProperty != null && Integer.parseInt(schemaVersionProperty.getValue()) == this.currentSchemaVersion;
    }

    public Class[] getTableClasses() {
        return tableClasses;
    }

    public void createAllTables() throws SQLException {
        for (Class aClass : tableClasses) {
            createTableIfNotExists(aClass);
        }
    }

    public void dropAllTables() throws SQLException {
        for (Class aClass : tableClasses) {
            dropTable(aClass);
        }
    }

    public void clearAllTables() throws SQLException {
        for (Class aClass : tableClasses) {
            clearTable(aClass);
        }
    }

    protected void createTableIfNotExists(Class dataClass) throws SQLException {
        TableUtils.createTableIfNotExists(jdbcConnectionSource, dataClass);
    }

    protected void dropTable(Class dataClass) throws SQLException {
        TableUtils.dropTable(jdbcConnectionSource, dataClass, true);
    }

    protected void clearTable(Class dataClass) throws SQLException {
        TableUtils.clearTable(jdbcConnectionSource, dataClass);
    }

    public Dao<Language, String> languageDao() {
        return languageDao;
    }

    public Dao<Snippet, String> snippetDao() {
        return snippetDao;
    }

    public Dao<Property, String> propertyDao() {
        return propertyDao;
    }

    private <D extends Dao<T, ?>, T> D createDao(Class<T> aClass) throws SQLException {
        return DaoManager.createDao(jdbcConnectionSource, aClass);
    }

    public void close() throws IOException {
        if (jdbcConnectionSource != null) {
            jdbcConnectionSource.close();
        }
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setCurrentSchemaVersion() throws SQLException {
        propertyDao.create(new Property(SCHEMA_VERSION_PROPERTY_KEY, String.valueOf(currentSchemaVersion)));
    }
}
