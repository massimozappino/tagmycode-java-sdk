package com.tagmycode.sdk.model;


import org.junit.Test;
import support.BaseTest;
import support.ResourceGenerate;

import static org.junit.Assert.assertEquals;

public class SnippetCollectionTest extends BaseTest {

    @Test
    public void testDefaultConstructor() throws Exception {
        SnippetCollection snippetCollection = new SnippetCollection();
        assertEquals(0, snippetCollection.size());
        snippetCollection.add(new ResourceGenerate().aSnippet());
        assertEquals(1, snippetCollection.size());
    }

    @Test
    public void testConstructorWithJson() throws Exception {
        String json = new ResourceGenerate().aSnippetCollection().toJson();
        SnippetCollection snippetCollection = new SnippetCollection(json);
        assertEquals(json, snippetCollection.toJson());
    }

    @Test
    public void testDeleteSnippetById() throws Exception {
        boolean isDeleted;
        SnippetCollection snippetCollection = new SnippetCollection(new ResourceGenerate().aSnippetCollection().toJson());
        assertEquals(2, snippetCollection.size());

        isDeleted = snippetCollection.deleteById(999);
        assertEquals(false, isDeleted);
        assertEquals(2, snippetCollection.size());

        isDeleted = snippetCollection.deleteById(1);
        assertEquals(true, isDeleted);
        assertEquals(1, snippetCollection.size());
    }

}
