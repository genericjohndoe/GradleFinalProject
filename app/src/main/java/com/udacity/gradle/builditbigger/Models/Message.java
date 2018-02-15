package com.udacity.gradle.builditbigger.Models;

/**
 * Created by joeljohnson on 1/16/18.
 */

public class Message {

    private String contents;
    private String timeDateString;
    private HilarityUser hilarityUser;

    public Message(HilarityUser user, String contents, String timeDateString){
        hilarityUser = user;
        this.contents = contents;
        this.timeDateString = timeDateString;
    }

    public HilarityUser getHilarityUser() {
        return hilarityUser;
    }

    public String getContents() {
        return contents;
    }

    public String getTimeDateString() {
        return timeDateString;
    }
}
