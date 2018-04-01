package com.udacity.gradle.builditbigger.Models;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumReply {

    private HilarityUser hilarityUser;
    private String content;
    private Long timeStamp;
    private String key;

    public ForumReply(){}

    public ForumReply(HilarityUser hilarityUser, String content, Long timeStamp, String key){
        this.hilarityUser = hilarityUser;
        this.content = content;
        this.timeStamp = timeStamp;
        this.key = key;
    }

    public HilarityUser getHilarityUser() {
        return hilarityUser;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getContent() {
        return content;
    }

    public String getKey() {
        return key;
    }
}