package com.tagmycode.sdk;

import java.util.HashMap;
import java.util.Map;

public class ParamList extends HashMap<String, String> {

    public ParamList add(String key, Object value) {
        if (value != null) {
            put(key, value.toString());
        }
        return this;
    }

    public ParamList addAll(Map<String, String> map) {
        super.putAll(map);
        return this;
    }

}
