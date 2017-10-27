package com.udacity.gradle.builditbigger.Genres;

import com.udacity.gradle.builditbigger.Constants.Constants;

/**
 * Created by joeljohnson on 10/17/17.
 */

public class Genre {
    private String title;
    private String author;
    private Boolean restricted;
    private Object timeStamp;
    private String uid;

    public Genre(){}

    public Genre(String title, String userName,Boolean restricted, Object timeStamp){
        this.title = title;
        this.author = userName;
        this.restricted = restricted;
        this.timeStamp = timeStamp;
        uid = Constants.UID;
    }

    public String getTitle(){ return title;}

    public String getAuthor(){return author;}

    public Boolean getRestricted(){return restricted;}

    public void setRestricted(Boolean restricted){ this.restricted = restricted;}

    public Object getTimeStamp(){return timeStamp;}

    public String getUID(){return uid;}
}
