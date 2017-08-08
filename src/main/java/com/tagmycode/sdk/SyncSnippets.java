package com.tagmycode.sdk;

import com.tagmycode.sdk.model.SnippetsCollection;
import com.tagmycode.sdk.model.SnippetsDeletions;

public class SyncSnippets {
    private SnippetsDeletions deletedSnippets;
    private SnippetsCollection changedSnippets;

    public SyncSnippets(SnippetsCollection changedSnippets, SnippetsDeletions deletedSnippets) {
        this.changedSnippets = changedSnippets;
        this.deletedSnippets = deletedSnippets;
    }

    public SnippetsDeletions getDeletedSnippets() {
        return deletedSnippets;
    }

    public SnippetsCollection getChangedSnippets() {
        return changedSnippets;
    }
}
