package com.udacity.gradle.builditbigger.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * HilarityUser class serves as model for user
 */
@Entity(tableName = "hilarityusers")
public class HilarityUser {
    private String userName;
    private String urlString;
    @PrimaryKey @NonNull
    private String uid;

    public HilarityUser() {}

    public HilarityUser(String userName, String urlString, String uid) {
        this.userName = userName;
        this.urlString = urlString;
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getUrlString() {
        return urlString;
    }

    public String getUid() {return uid;}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public void setUid(String uid) {this.uid = uid;}

    @Override
    public boolean equals(Object obj) {
            return (obj instanceof HilarityUser) && uid.equals(((HilarityUser) obj).getUid());
    }
}

