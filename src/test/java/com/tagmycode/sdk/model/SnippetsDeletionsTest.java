package com.tagmycode.sdk.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class SnippetsDeletionsTest {
    @Test
    public void toJson() {
        SnippetsDeletions snippetsDeletions = new SnippetsDeletions();
        snippetsDeletions.add(2);
        snippetsDeletions.add(3);
        snippetsDeletions.add(4);

        assertEquals("[2, 3, 4]", snippetsDeletions.toJson());
    }

    @Test
    public void constructorWithArrayOfInt() {
        assertEquals("[1, 2, 3, 99]", new SnippetsDeletions(1, 2, 3, 99).toJson());
    }
}