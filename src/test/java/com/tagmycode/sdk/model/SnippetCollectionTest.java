package com.tagmycode.sdk.model;


import org.junit.Test;
import support.BaseTest;
import support.ResourceGenerate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

    @Test
    public void testGetSnippetById() throws Exception {
        SnippetCollection snippets = new SnippetCollection();
        Snippet snippet1 = resourceGenerate.aSnippet();
        Snippet snippet2 = resourceGenerate.anotherSnippet();
        snippets.add(snippet1);
        snippets.add(snippet2);
        snippets.add(new Snippet().setId(0));

        assertEquals(snippet1, snippets.getById(snippet1.getId()));
        assertEquals(snippet2, snippets.getById(snippet2.getId()));
        assertNull(snippets.getById(99));
        assertNull(snippets.getById(0));
    }

    @Test
    public void testUpdateSnippet() throws Exception {
        SnippetCollection snippets = new SnippetCollection();
        Snippet snippet1 = resourceGenerate.aSnippet();
        Snippet snippet2 = resourceGenerate.anotherSnippet();
        snippets.add(snippet1);
        snippets.add(snippet2);
        snippets.add(new Snippet().setId(0));

        Snippet updatedSnippet1 = snippets.updateSnippet(snippet1);
        assertEquals(snippet1, updatedSnippet1);
        assertEquals(snippet1, snippets.getById(updatedSnippet1.getId()));

        assertNull(snippets.updateSnippet(new Snippet()));

        Snippet updatedSnippet2 = new Snippet().setId(snippet2.getId()).setTitle("updated title");
        assertEquals(snippet2, snippets.getById(updatedSnippet2.getId()));

        Snippet nonPresentSnippet = new Snippet().setId(555);
        assertNull(snippets.getById(nonPresentSnippet.getId()));
    }
}
