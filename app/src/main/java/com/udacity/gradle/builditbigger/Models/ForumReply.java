package com.udacity.gradle.builditbigger.Models;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumReply {

    private String hilarityUserUID;
    private String content;
    private Long timeStamp;
    private String key;

    public ForumReply(){}

    public ForumReply(String hilarityUserUID, String content, Long timeStamp, String key){
        this.hilarityUserUID = hilarityUserUID;
        this.content = content;
        this.timeStamp = timeStamp;
        this.key = key;
    }

    public String getHilarityUserUID() {
        return hilarityUserUID;
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

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof ForumReply) && ((ForumReply) obj).getKey().equals(key));
    }
}
