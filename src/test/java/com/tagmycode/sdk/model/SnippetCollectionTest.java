package com.tagmycode.sdk.model;


import org.junit.Test;
import support.BaseTest;
import support.ResourceGenerate;

import static org.junit.Assert.*;

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
        SnippetCollection snippetCollection = new SnippetCollection(resourceGenerate.aSnippetCollection().toJson());
        assertEquals(2, snippetCollection.size());

        isDeleted = snippetCollection.deleteById(999);
        assertEquals(false, isDeleted);
        assertEquals(2, snippetCollection.size());

        isDeleted = snippetCollection.deleteById(1);
        assertEquals(true, isDeleted);
        assertEquals(1, snippetCollection.size());
    }

    @Test
    public void testDeleteSnippetsBySnippetsDeletions() throws Exception {
        SnippetCollection snippetCollection = new SnippetCollection();
        snippetCollection.add(new Snippet().setId(1));
        snippetCollection.add(new Snippet().setId(2));
        snippetCollection.add(new Snippet().setId(3));
        snippetCollection.add(new Snippet().setId(4));
        snippetCollection.add(new Snippet().setId(5));

        SnippetsDeletions deletions = new SnippetsDeletions();
        deletions.add(2);
        deletions.add(4);

        snippetCollection.deleteByDeletions(deletions);
        assertEquals(3, snippetCollection.size());
        assertNotNull(snippetCollection.getById(1));
        assertNull(snippetCollection.getById(2));
        assertNotNull(snippetCollection.getById(3));
        assertNull(snippetCollection.getById(4));
        assertNotNull(snippetCollection.getById(5));
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

    @Test
    public void testMergeWithNewSnippetCollection() throws Exception {
        SnippetCollection snippetsBase = new SnippetCollection();
        snippetsBase.add(resourceGenerate.aSnippet());
        SnippetCollection newSnippetCollection = new SnippetCollection();
        newSnippetCollection.add(resourceGenerate.anotherSnippet());
        snippetsBase.merge(newSnippetCollection);

        assertEquals(2, snippetsBase.size());
    }

    @Test
    public void testMergeWithNewSnippetCollectionWithSameId() throws Exception {
        SnippetCollection snippetsBase = new SnippetCollection();
        snippetsBase.add(resourceGenerate.aSnippet());
        SnippetCollection newSnippetCollection = new SnippetCollection();
        newSnippetCollection.add(resourceGenerate.aSnippet().setTitle("Changed title"));

        snippetsBase.merge(newSnippetCollection);

        assertEquals(1, snippetsBase.size());
        assertEquals("Changed title", snippetsBase.getById(resourceGenerate.aSnippet().getId()).getTitle());
    }

    @Test
    public void testMergeNoIdWithNewSnippetCollection() throws Exception {
        SnippetCollection snippetsBase = new SnippetCollection();
        snippetsBase.add(new Snippet());
        SnippetCollection newSnippetCollection = new SnippetCollection();
        newSnippetCollection.add(resourceGenerate.aSnippet());

        snippetsBase.merge(newSnippetCollection);

        assertEquals(2, snippetsBase.size());
        assertEquals(0, snippetsBase.get(0).getId());
    }
}