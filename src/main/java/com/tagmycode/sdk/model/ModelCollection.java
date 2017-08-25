package com.tagmycode.sdk.model;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class ModelCollection<T extends ModelAbstract> extends Vector<T> {

    public ModelCollection() {

    }

    public ModelCollection(List<T> list) {
        super(list);
    }

    public ModelCollection(T[] array) {
        Collections.addAll(this, array);
    }

    public String toJson() throws JSONException {
        ArrayList<String> jsonStringArray = new ArrayList<String>();
        for (ModelAbstract model : this) {
            jsonStringArray.add(model.toJson());
        }

        return "[" + StringUtils.join(jsonStringArray, ", ") + "]";
    }
}
