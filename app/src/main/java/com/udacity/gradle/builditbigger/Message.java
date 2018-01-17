package com.udacity.gradle.builditbigger;

/**
 * Created by joeljohnson on 1/16/18.
 */

public class Message {

    private String uid;
    private String contents;
    private String userProfileImagePath;
    private String timeDateString;
    private String userName;

    public Message(String uid, String contents, String userProfileImagePath, String timeDateString, String userName){
        this.uid = uid;
        this.contents = contents;
        this.userProfileImagePath = userProfileImagePath;
        this.timeDateString = timeDateString;
        this.userName = userName;
    }

    public String getContents() {
        return contents;
    }

    public String getUid() {
        return uid;
    }

    public String getUserProfileImagePath(){
        return userProfileImagePath;
    }

    public String getTimeDateString() {
        return timeDateString;
    }

    public String getUserName() {
        return userName;
    }
}
