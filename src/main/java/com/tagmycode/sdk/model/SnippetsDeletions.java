package com.tagmycode.sdk.model;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class SnippetsDeletions extends ArrayList<Integer> {

    public SnippetsDeletions(int... idsToDelete) {
        for (int id : idsToDelete) {
            add(id);
        }
    }

    public String toJson() {
        return "[" + StringUtils.join(this, ", ") + "]";
    }
}
