package com.tagmycode.sdk;

import com.tagmycode.sdk.model.Language;
import com.tagmycode.sdk.model.Snippet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import support.BaseTest;
import support.MemDbService;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class DbServiceTest extends BaseTest {
    private DbService dbServiceSpy;

    @Before
    public void initSpyDbService() throws SQLException {
        dbServiceSpy = spy(new MemDbService());
        dbServiceSpy.initialize();
    }

    @Test
    public void closeWorksAlsoIfIsConnectionNotInitialized() throws Exception {
        new MemDbService("not_initialized_db").close();
    }

    @Test
    public void tableClasses() throws Exception {
        Class[] classes = {Language.class, Snippet.class};
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

    private void assertLanguagesCount(DbService dbService, long languagesCount) throws SQLException {
        assertEquals(languagesCount, dbService.languageDao().countOf());
    }

}


