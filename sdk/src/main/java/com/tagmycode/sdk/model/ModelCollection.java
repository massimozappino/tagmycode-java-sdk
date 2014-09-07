package com.tagmycode.sdk.model;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ModelCollection<T extends ModelAbstract> extends ArrayList<T> {

    public String toJson() throws JSONException {
        Set<String> jsonStringSet = new LinkedHashSet<String>();
        for (ModelAbstract model : this) {
            jsonStringSet.add(model.toJson());
        }

        return "[" + StringUtils.join(jsonStringSet, ", ") + "]";
    }
}
