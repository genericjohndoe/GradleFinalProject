package com.udacity.gradle.builditbigger.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.udacity.gradle.builditbigger.Constants.Constants;

import java.util.List;

/**
 * Created by joeljohnson on 10/2/17.
 */
@Entity(tableName = "hilarityusers")
public class HilarityUser {
    private String userName;
    private String urlString;
    @PrimaryKey
    private String uid;

    public HilarityUser() {}

    public HilarityUser(String userName, String urlString) {
        this.userName = userName;
        this.urlString = urlString;
        uid = Constants.UID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUrlString() {
        return urlString;
    }


    public String getUID() {
        return uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }
}

