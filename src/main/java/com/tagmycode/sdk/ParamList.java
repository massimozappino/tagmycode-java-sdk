package com.tagmycode.sdk;

import java.util.HashMap;

public class ParamList extends HashMap<String, String> {

    public ParamList add(String key, Object value) {
        if (value != null) {
            put(key, value.toString());
        }
        return this;
    }

}
