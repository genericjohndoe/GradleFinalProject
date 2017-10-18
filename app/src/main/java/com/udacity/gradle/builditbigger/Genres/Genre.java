package com.udacity.gradle.builditbigger.Genres;

/**
 * Created by joeljohnson on 10/17/17.
 */

public class Genre {
    String title;
    String userName;
    Boolean restricted;
    Object timeStamp;

    public Genre(){}

    public Genre(String title, String userName,Boolean restricted, Object timeStamp){
        this.title = title;
        this.userName = userName;
        this.restricted = restricted;
        this.timeStamp = timeStamp;
    }

    public String getTitle(){ return title;}

    public void setTitle(String title){ this.title = title;}

    public String getUserName(){return userName;}

    public Boolean getRestricted(){return restricted;}

    public void setRestricted(Boolean restricted){ this.restricted = restricted;}

    public Object getTimeStamp(){return timeStamp;}
}
