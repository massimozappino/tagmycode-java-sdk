package com.tagmycode.sdk;

import com.tagmycode.sdk.model.Language;
import com.tagmycode.sdk.model.Snippet;
import org.junit.Test;
import support.BaseTest;

import static org.junit.Assert.assertEquals;


public class StorageServiceTest extends BaseTest {

    @Test
    public void closeWorksAlsoIfIsConnectionNotInitialized() throws Exception {
        StorageService storageService = new StorageService();
        storageService.close();
    }

    @Test
    public void createAndReadAllModelTypes() throws Exception {
        StorageService storageService = initializeInMemoryStorage();

        Language language = resourceGenerate.aLanguage();
        storageService.languageDao().createOrUpdate(language);
        Language readLanguage = storageService.languageDao().queryBuilder().queryForFirst();
        assertEquals(readLanguage, language);

        Snippet snippet = resourceGenerate.aSnippet();
        storageService.snippetDao().createOrUpdate(snippet);
        Snippet readSnippet = storageService.snippetDao().queryBuilder().queryForFirst();
        assertEquals(readSnippet, snippet);
    }

}


