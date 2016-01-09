package com.tagmycode.sdk.model;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.util.ArrayList;

public class ModelCollection<T extends ModelAbstract> extends ArrayList<T> {

    public String toJson() throws JSONException {
        ArrayList<String> jsonStringArray = new ArrayList<String>();
        for (ModelAbstract model : this) {
            jsonStringArray.add(model.toJson());
        }

        return "[" + StringUtils.join(jsonStringArray, ", ") + "]";
    }
}
