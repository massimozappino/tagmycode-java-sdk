package com.tagmycode.sdk.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

@DatabaseTable(tableName = "snippets")
public class Snippet extends ModelAbstract {
    @DatabaseField(generatedId = true)
    private int localId;
    @DatabaseField
    private int id;
    @DatabaseField
    private String title;
    @DatabaseField(dataType = DataType.LONG_STRING)
    private String code;
    @DatabaseField(dataType = DataType.LONG_STRING)
    private String description;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Language language;
    @DatabaseField
    private Date creationDate;
    @DatabaseField
    private Date updateDate;
    @DatabaseField
    private String tags;
    @DatabaseField
    private boolean isPrivate;
    @DatabaseField
    private String url;
    @DatabaseField
    private boolean dirty;
    @DatabaseField
    private boolean deleted;

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
            title = jsonToString("title");
            code = jsonToString("code");
            description = jsonToString("description");
            language = new Language(getJsonObject().getJSONObject("language"));
            tags = jsonToString("tags");
            isPrivate = getJsonObject().getBoolean("is_private");
            url = jsonToString("url");
            creationDate = jsonToDate("created_at");
            updateDate = jsonToDate("updated_at");
        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        } catch (ParseException e) {
            throw new TagMyCodeJsonException(e);
        }
    }

    @Override
    public String toJson() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", fieldToJson(getId()));
        jo.put("title", fieldToJson(getTitle()));
        jo.put("code", fieldToJson(getCode()));
        jo.put("description", fieldToJson(getDescription()));
        jo.put("language", new JSONObject(getLanguage().toJson()));
        jo.put("tags", fieldToJson(getTags()));
        jo.put("is_private", isPrivate());
        jo.put("url", fieldToJson(getUrl()));
        jo.put("created_at", fieldToJson(getCreationDate()));
        jo.put("updated_at", fieldToJson(getUpdateDate()));

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
        return null == language ? new DefaultLanguage() : language;
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

    public int getLocalId() {
        return localId;
    }

    public Snippet setLocalId(int localId) {
        this.localId = localId;
        return this;
    }

    public boolean isDirty() {
        return dirty;
    }

    public Snippet setDirty(boolean dirty) {
        this.dirty = dirty;
        return this;
    }

    public Snippet setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public boolean isDeleted() {
        return this.deleted;
    }
}
