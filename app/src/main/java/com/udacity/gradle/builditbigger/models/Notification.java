package com.udacity.gradle.builditbigger.models;

public class Notification {
    private Boolean followerNotification;
    private Boolean forumReplyNotification;
    private Boolean mentionsNotification;
    private Boolean messagesNotification;

    public Notification(){}

    public Boolean getFollowerNotification() {
        return followerNotification;
    }

    public Boolean getForumReplyNotification() {
        return forumReplyNotification;
    }

    public Boolean getMentionsNotification() {
        return mentionsNotification;
    }

    public Boolean getMessagesNotification() {
        return messagesNotification;
    }
}
