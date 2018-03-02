package com.udacity.gradle.builditbigger.Models;

/**
 * Created by joeljohnson on 12/13/17.
 */

public class Comment {

    private String uid;
    private String timeDate;
    private String userName;
    private String profilePictureURL;
    private String commentContent;
    private String postUid;
    private String postPushId;
    private String commentId;

    public Comment(){

    }

    public Comment(String uid, String timeDate, String userName, String profilePictureURL, String commentContent,
                   String postUid, String postPushId, String commentId){
        this.timeDate = timeDate;
        this.userName = userName;
        this.profilePictureURL = profilePictureURL;
        this.commentContent = commentContent;
        this.uid = uid;
        this.postUid = postUid;
        this.postPushId = postPushId;
        this.commentId = commentId;
    }

    public String getUid(){return uid;}

    public String getTimeDate(){return timeDate;}

    public String getUserName(){return userName;}

    public String getProfilePictureURL(){return profilePictureURL;}

    public String getCommentContent(){return commentContent;}

    public String getPostUid(){return postUid;}

    public String getPostPushId(){return postPushId;}

    public String getCommentId(){return commentId;}
}
