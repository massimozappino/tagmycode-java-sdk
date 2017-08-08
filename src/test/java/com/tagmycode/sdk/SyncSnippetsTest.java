package com.tagmycode.sdk;

import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import com.tagmycode.sdk.model.SnippetsCollection;
import com.tagmycode.sdk.model.SnippetsDeletions;
import org.junit.Test;
import support.BaseTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SyncSnippetsTest extends BaseTest {
    @Test
    public void constructor() throws IOException, TagMyCodeJsonException {
        SnippetsCollection changedSnippets = new SnippetsCollection();
        changedSnippets.add(resourceGenerate.aSnippet());
        SnippetsDeletions deletedSnippets = new SnippetsDeletions();
        deletedSnippets.add(1);

        SyncSnippets syncSnippets = new SyncSnippets(changedSnippets, deletedSnippets);

        assertEquals(1, syncSnippets.getChangedSnippets().size());
        assertEquals(1, syncSnippets.getDeletedSnippets().size());
    }

}
