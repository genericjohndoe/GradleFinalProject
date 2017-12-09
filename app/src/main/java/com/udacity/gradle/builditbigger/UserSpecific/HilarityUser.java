package com.udacity.gradle.builditbigger.UserSpecific;

import com.udacity.gradle.builditbigger.Constants.Constants;

import java.util.List;

/**
 * Created by joeljohnson on 10/2/17.
 */

public class HilarityUser {
    private String userName;
    private String urlString;
    private List<String> languages;
    private String uid;

    public HilarityUser() {
    }

    public HilarityUser(String userName, String urlString, List<String> languages) {
        this.userName = userName;
        this.urlString = urlString;
        this.languages = languages;
        uid = Constants.UID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUrlString() {
        return urlString;
    }

    public List<String> getLanguages() {
        return languages;
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

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }


}

