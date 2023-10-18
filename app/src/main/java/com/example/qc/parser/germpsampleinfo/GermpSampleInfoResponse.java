package com.example.qc.parser.germpsampleinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GermpSampleInfoResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
