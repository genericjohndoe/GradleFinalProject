package com.udacity.gradle.builditbigger.UserSpecific;

import java.util.List;

/**
 * Created by joeljohnson on 10/2/17.
 */

public class HilarityUser {
    String userName;
    String urlString;
    List<HilarityUser> followers;
    List<HilarityUser> following;
    List<String> languages;

    public HilarityUser(){}

    public HilarityUser(String userName, String urlString, List<HilarityUser> followers, List<HilarityUser> following, List<String> languages){
        this.userName = userName;
        this.urlString = urlString;
        this.followers = followers;
        this.following = following;
        this.languages = languages;
    }

    public String getUserName(){
        return userName;
    }

    public String getUrlString(){
        return urlString;
    }

    public List<HilarityUser>  getFollowers(){
        return followers;
    }

    public List<HilarityUser> getFollowing(){
        return following;
    }

    public List<String> getLanguages() { return languages;}

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setUrlString(String urlString){
        this.urlString = urlString;
    }

    public void setFollowers(List<HilarityUser> followers){
        this.followers = followers;
    }

    public void setFollowing(List<HilarityUser> following){
        this.following = following;
    }

    public void setLanguages(List<String> languages){
        this.languages = languages;
    }


}

