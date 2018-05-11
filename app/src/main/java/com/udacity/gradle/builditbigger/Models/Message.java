package com.udacity.gradle.builditbigger.Models;

/**
 * Message class serves as model for direct messages sent among users
 */

public class Message {

    private String contents;
    private Long timeStamp;
    private HilarityUser hilarityUser;
    private String pushId;

    public Message(){}

    public Message(HilarityUser user, String contents, long timeStamp, String pushId){
        hilarityUser = user;
        this.contents = contents;
        this.timeStamp = timeStamp;
        this.pushId = pushId;
    }

    public HilarityUser getHilarityUser() {
        return hilarityUser;
    }

    public String getContents() {
        return contents;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getPushId() {
        return pushId;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Message) && ((Message) obj).getPushId().equals(pushId);
    }
}
