package com.tagmycode.sdk.model;


import com.tagmycode.sdk.exception.TagMyCodeJsonException;
import org.json.JSONException;
import org.json.JSONObject;

public class User extends ModelAbstract {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;

    public User(JSONObject jsonObject) throws TagMyCodeJsonException {
        super(jsonObject);
    }

    public User() {

    }

    public User(String jsonString) throws TagMyCodeJsonException {
        super(jsonString);
    }

    @Override
    protected void extractFields() throws TagMyCodeJsonException {
        try {
            id = getJsonObject().getInt("id");
            username = getJsonObject().getString("username");
            firstname = getJsonObject().getString("firstname");
            lastname = getJsonObject().getString("lastname");
            email = getJsonObject().getString("email");

        } catch (JSONException e) {
            throw new TagMyCodeJsonException(e);
        }
    }

    @Override
    public String toJson() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", getId());
        jo.put("username", getUsername());
        jo.put("firstname", getFirstname());
        jo.put("lastname", getLastname());
        jo.put("email", getEmail());

        return jo.toString();
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public User setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getLastname() {
        return lastname;
    }

    public User setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }
}
