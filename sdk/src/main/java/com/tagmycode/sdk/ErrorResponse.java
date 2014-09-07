package com.tagmycode.sdk;


import com.tagmycode.sdk.exception.TagMyCodeException;
import org.json.JSONException;
import org.json.JSONObject;

public class ErrorResponse {
    private Integer code;
    private String message;

    public ErrorResponse(String body) {
        JSONObject json;
        try {
            json = new JSONObject(body);
            setCode(json.getInt("code"));
            setMessage(json.getString("message"));
        } catch (JSONException e) {
            setCode(0);
            setMessage(new TagMyCodeException().getMessage());
        }
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
