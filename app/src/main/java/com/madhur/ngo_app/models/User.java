package com.madhur.ngo_app.models;

import android.util.Log;

public class User {
    private String name;
    private String uid;
    private String imageUrl;

    public User() {
    }

    public User(String name, String uid, String imageUrl) {
        this.name = name;
        this.uid = uid;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        Log.d("Checking Reached", "setImageUrl: Reached here" + this.getImageUrl());

    }
}
