package com.tagmycode.sdk.model;

import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONException;
import org.json.JSONObject;

abstract public class ModelAbstract implements Comparable<ModelAbstract> {
    private JSONObject jsonObject;

    public ModelAbstract() {
    }

    public ModelAbstract(JSONObject jsonObject) throws TagMyCodeJsonException {
        setJsonObject(jsonObject);
        extractFields();
    }

    public ModelAbstract(String jsonString) throws TagMyCodeJsonException {
        convertToJsonObject(jsonString);
        extractFields();
    }

    protected abstract void extractFields() throws TagMyCodeJsonException;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    private void convertToJsonObject(String jsonString) throws TagMyCodeJsonException {
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        }
    }

    public abstract String toJson() throws JSONException;

    @Override
    public int compareTo(ModelAbstract model) {
        try {
            return toJson().compareTo(model.toJson());
        } catch (JSONException e) {
            return -1;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }

        if (!(object instanceof ModelAbstract)) {
            return false;
        }

        return compareTo((ModelAbstract) object) == 0;
    }
}

