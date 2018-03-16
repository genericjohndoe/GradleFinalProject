package com.udacity.gradle.builditbigger.Models;

/**
 * Message class serves as model for direct messages sent among users
 */

public class Message {

    private String contents;
    private Long timeStamp;
    private HilarityUser hilarityUser;

    public Message(){}

    public Message(HilarityUser user, String contents, long timeStamp){
        hilarityUser = user;
        this.contents = contents;
        this.timeStamp = timeStamp;
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
}
