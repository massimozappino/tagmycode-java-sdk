package com.tagmycode.sdk.model;

import com.tagmycode.sdk.DateParser;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

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

    protected Object fieldToJson(String field) {
        return field != null ? field : JSONObject.NULL;
    }

    protected int fieldToJson(int field) {
        return field;
    }

    protected Object fieldToJson(Date date) {
        Object jsonDate = JSONObject.NULL;
        if (date != null) {
            jsonDate = new DateParser().toISO8601(date);
        }
        return jsonDate;
    }


    protected String jsonToString(String fieldName) throws JSONException {
        if (getJsonObject().isNull(fieldName)) {
            return null;
        }
        return getJsonObject().getString(fieldName);
    }

    protected Date jsonToDate(String fieldName) throws ParseException, JSONException {
        if (getJsonObject().isNull(fieldName)) {
            return null;
        } else {
            return new DateParser().parseDate(getJsonObject().getString(fieldName));
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

