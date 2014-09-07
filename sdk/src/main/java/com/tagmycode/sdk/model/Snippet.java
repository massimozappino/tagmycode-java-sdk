package com.tagmycode.sdk.model;

import com.tagmycode.sdk.DateParser;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class Snippet extends ModelAbstract {
    private String title;
    private String code;
    private String description;
    private int id;
    private Language language;
    private Date creationDate;
    private Date updateDate;
    private String tags;
    private boolean isPrivate;
    private String url;

    public Snippet() {
    }

    public Snippet(String jsonString) throws TagMyCodeJsonException {
        super(jsonString);
    }

    public Snippet(JSONObject jsonObject) throws TagMyCodeJsonException {
        super(jsonObject);
    }

    @Override
    protected void extractFields() throws TagMyCodeJsonException {
        try {
            id = getJsonObject().getInt("id");
            title = getJsonObject().getString("title");
            code = getJsonObject().getString("code");
            description = getJsonObject().getString("description");
            language = new Language(getJsonObject().getJSONObject("language"));
            tags = getJsonObject().getString("tags");
            isPrivate = getJsonObject().getBoolean("is_private");
            url = getJsonObject().getString("url");
            creationDate = new DateParser().parseDate(getJsonObject().getString("created_at"));
            updateDate = new DateParser().parseDate(getJsonObject().getString("updated_at"));
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        } catch (ParseException e) {
            throw new TagMyCodeJsonException(e);
        }
    }

    @Override
    public String toJson() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", getId());
        jo.put("title", getTitle());
        jo.put("code", getCode());
        jo.put("description", getDescription());
        jo.put("language", getLanguage() != null ? new JSONObject(getLanguage().toJson()) : "");
        jo.put("tags", getTags());
        jo.put("is_private", isPrivate());
        jo.put("url", getUrl());
        jo.put("created_at", new DateParser().toISO8601(getCreationDate()));
        jo.put("updated_at", new DateParser().toISO8601(getUpdateDate()));

        return jo.toString();
    }

    public int getId() {
        return id;
    }

    public Snippet setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Snippet setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Snippet setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Snippet setDescription(String description) {
        this.description = description;
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public Snippet setLanguage(Language language) {
        this.language = language;
        return this;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Snippet setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public Snippet setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public Snippet setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public Snippet setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Snippet setUrl(String url) {
        this.url = url;
        return this;
    }
}
