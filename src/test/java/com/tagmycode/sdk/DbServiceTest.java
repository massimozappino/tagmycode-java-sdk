package com.tagmycode.sdk;

import com.tagmycode.sdk.model.Language;
import com.tagmycode.sdk.model.Property;
import com.tagmycode.sdk.model.Snippet;
import org.h2.jdbc.JdbcSQLException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import support.BaseTest;
import support.MemDbService;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class DbServiceTest extends BaseTest {
    private DbService dbServiceSpy;

    @Before
    public void initSpyDbService() throws SQLException {
        dbServiceSpy = spy(new MemDbService()).initialize();
    }

    @After
    public void shutDownDb() throws IOException {
        dbServiceSpy.close();
    }

    @Test
    public void newInstanceOfDbHasCorrectSchemaVersion() throws SQLException {
        MemDbService memDbService = new MemDbService("another_db");
        assertFalse(memDbService.isCurrentSchemaVersion());

        memDbService.initialize();

        assertTrue(memDbService.isCurrentSchemaVersion());
    }

    @Test
    public void setSchemaVersion() throws SQLException {
        dbServiceSpy.propertyDao().deleteBuilder().delete();

        dbServiceSpy.setCurrentSchemaVersion();

        assertTrue(dbServiceSpy.isCurrentSchemaVersion());
    }

    @Test
    public void constructWithSaveFilePath() throws IOException {
        SaveFilePath saveFilePathMock = mock(SaveFilePath.class);
        when(saveFilePathMock.getPath()).thenReturn("test");

        DbService dbService = new DbService(saveFilePathMock);
        String dbPath = dbService.getDbPath();

        verify(saveFilePathMock, times(1)).getPath();
        assertEquals("test/db/data", dbPath);
    }

    @Test
    public void initializeIsCalledOnlyIfThereIsNotAConnection() throws Exception {
        MemDbService memDbService = spy(new MemDbService("not_initialized_db"));
        verify(memDbService, times(0)).createAllTables();

        memDbService.initialize();
        verify(memDbService, times(1)).createAllTables();

        memDbService.initialize();
        verify(memDbService, times(1)).createAllTables();
    }

    @Test
    public void closeWorksAlsoIfIsConnectionNotInitialized() throws Exception {
        new MemDbService("not_initialized_db").close();
    }

    @Test
    public void tableClasses() throws Exception {
        Class[] classes = {Language.class, Snippet.class, Property.class};
        Assert.assertArrayEquals(classes, dbServiceSpy.getTableClasses());
    }

    @Test
    public void createAllTables() throws Exception {
        for (Class aClass : dbServiceSpy.getTableClasses()) {
            verify(dbServiceSpy, times(1)).createTableIfNotExists(aClass);
        }
    }

    @Test
    public void clearAllTables() throws Exception {
        Language language = resourceGenerate.aLanguage();
        dbServiceSpy.languageDao().createOrUpdate(language);
        assertLanguagesCount(dbServiceSpy, 1L);

        dbServiceSpy.clearAllTables();

        assertLanguagesCount(dbServiceSpy, 0L);
        for (Class aClass : dbServiceSpy.getTableClasses()) {
            verify(dbServiceSpy, times(1)).clearTable(aClass);
        }
    }

    @Test
    public void dropAllTables() throws Exception {
        Language language = resourceGenerate.aLanguage();
        dbServiceSpy.languageDao().createOrUpdate(language);
        assertLanguagesCount(dbServiceSpy, 1L);

        dbServiceSpy.dropAllTables();

        try {
            dbServiceSpy.languageDao().countOf();
            fail("Expected exception");
        } catch (JdbcSQLException ignore) {
        }
        for (Class aClass : dbServiceSpy.getTableClasses()) {
            verify(dbServiceSpy, times(2)).dropTable(aClass);
        }
    }

    @Test
    public void createAndReadAllModelTypes() throws Exception {
        for (Class aClass : dbServiceSpy.getTableClasses()) {
            verify(dbServiceSpy, times(1)).createTableIfNotExists(aClass);
        }

        Language language = resourceGenerate.aLanguage();
        dbServiceSpy.languageDao().createOrUpdate(language);
        Language readLanguage = dbServiceSpy.languageDao().queryBuilder().queryForFirst();
        assertEquals(readLanguage, language);

        Snippet snippet = resourceGenerate.aSnippet();
        dbServiceSpy.snippetDao().createOrUpdate(snippet);
        Snippet readSnippet = dbServiceSpy.snippetDao().queryBuilder().queryForFirst();
        assertEquals(readSnippet, snippet);
    }

    @Test
    public void testPropertiesTable() throws SQLException {
        dbServiceSpy.clearAllTables();
        dbServiceSpy.propertyDao().createOrUpdate(new Property("key", "value"));
        Property readProperty = dbServiceSpy.propertyDao().queryForId("key");
        assertEquals("value", readProperty.getValue());
        assertEquals(1, dbServiceSpy.propertyDao().countOf());

        dbServiceSpy.propertyDao().createOrUpdate(new Property("key", "value"));

        assertEquals(1, dbServiceSpy.propertyDao().countOf());
    }

    private void assertLanguagesCount(DbService dbService, long languagesCount) throws SQLException {
        assertEquals(languagesCount, dbService.languageDao().countOf());
    }

}


