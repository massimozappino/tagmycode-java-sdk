package com.tagmycode.sdk.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONException;
import org.json.JSONObject;

@DatabaseTable(tableName = "languages")
public class Language extends ModelAbstract {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField()
    private String code;

    public Language(JSONObject jsonObject) throws TagMyCodeJsonException {
        super(jsonObject);
    }

    public Language(String jsonString) throws TagMyCodeJsonException {
        super(jsonString);
    }

    public Language() {

    }

    @Override
    protected void extractFields() throws TagMyCodeJsonException {
        try {
            id = getJsonObject().getInt("id");
            name = getJsonObject().getString("name");
            code = getJsonObject().getString("code");
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        }
    }

    public int getId() {
        return id;
    }

    public Language setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Language setName(String name) {
        this.name = name;
        return this;
    }

    public Language setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String toJson() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", getId());
        jo.put("name", getName() + "");
        jo.put("code", getCode() + "");

        return jo.toString();
    }
}
