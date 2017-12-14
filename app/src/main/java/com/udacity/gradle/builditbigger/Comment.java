package com.udacity.gradle.builditbigger;

/**
 * Created by joeljohnson on 12/13/17.
 */

public class Comment {

    private String uid;
    private String timeDate;
    private String userName;
    private String profilePictureURL;
    private String commentContent;
    //private String likes;

    public Comment(){

    }

    public Comment(String uid, String timeDate, String userName, String profilePictureURL, String commentContent){
        this.timeDate = timeDate;
        this.userName = userName;
        this.profilePictureURL = profilePictureURL;
        this.commentContent = commentContent;
        this.uid = uid;
    }

    public String getUid(){return uid;}

    public String getTimeDate(){return timeDate;}

    public String getUserName(){return userName;}

    public String getProfilePictureURL(){return profilePictureURL;}

    public String getCommentContent(){return commentContent;}
}
