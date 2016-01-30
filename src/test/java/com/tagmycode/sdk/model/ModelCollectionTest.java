package com.tagmycode.sdk.model;


import org.junit.Test;
import support.BaseTest;

import static org.junit.Assert.assertEquals;

public class ModelCollectionTest extends BaseTest {

    @Test
    public void toJson() throws Exception {
        LanguageCollection lc = new LanguageCollection();

        Language language1 = resourceGenerate.aLanguage();
        language1.setId(1);

        Language language2 = resourceGenerate.aLanguage();
        language2.setId(2);

        lc.add(language1);
        lc.add(language2);

        assertEquals("[" + language1.toJson() + ", " + language2.toJson() + "]", lc.toJson());
    }

    @Test
    public void toJsonAllowsDuplicate() throws Exception {
        SnippetCollection snippets = new SnippetCollection();
        snippets.add(resourceGenerate.aSnippet());
        snippets.add(resourceGenerate.aSnippet());

        assertEquals(snippets, new SnippetCollection(snippets.toJson()));
    }

    @Test
    public void avoidNPE() throws Exception {
        SnippetCollection newSnippetCollection = resourceGenerate.aSnippetCollection();
        newSnippetCollection.add(resourceGenerate.anotherSnippet());
        newSnippetCollection.add(new Snippet().setId(5));

        assertEquals(4, newSnippetCollection.size());

        assertEquals(new SnippetCollection(newSnippetCollection.toJson()), newSnippetCollection);
    }
}
