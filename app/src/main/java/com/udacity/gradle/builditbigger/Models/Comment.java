package com.udacity.gradle.builditbigger.Models;

/**
 * Comment class serves as model for comments under posts
 */

public class Comment {

    private Long timeDate;
    private String commentContent;
    private String postUid;
    private String postPushId;
    private String commentId;
    HilarityUser hilarityUser;

    public Comment(){}

    public Comment(HilarityUser hilarityUser, Long timeDate, String commentContent,
                   String postUid, String postPushId, String commentId){
        this.timeDate = timeDate;
        this.hilarityUser = hilarityUser;
        this.commentContent = commentContent;
        this.postUid = postUid;
        this.postPushId = postPushId;
        this.commentId = commentId;
    }

    public HilarityUser getHilarityUser() {
        return hilarityUser;
    }

    public Long getTimeDate(){return timeDate;}

    public String getCommentContent(){return commentContent;}

    public String getPostUid(){return postUid;}

    public String getPostPushId(){return postPushId;}

    public String getCommentId(){return commentId;}
}
