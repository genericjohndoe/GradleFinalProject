package com.udacity.gradle.builditbigger.UserSpecific;

import java.util.List;

/**
 * Created by joeljohnson on 10/2/17.
 */

public class User {
    String userName;
    String urlString;
    List<User> followers;
    List<User> following;

    public User(){}

    public User(String userName, String urlString, List<User> followers, List<User> following){
        this.userName = userName;
        this.urlString = urlString;
        this.followers = followers;
        this.following = following;
    }

    public String getUserName(){
        return userName;
    }

    public String getUrlString(){
        return urlString;
    }

    public List<User>  getFollowers(){
        return followers;
    }

    public List<User> getFollowing(){
        return following;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setUrlString(String urlString){
        this.urlString = urlString;
    }

    public void setFollowers(List<User> followers){
        this.followers = followers;
    }

    public void setFollowing(List<User> following){
        this.following = following;
    }
}

