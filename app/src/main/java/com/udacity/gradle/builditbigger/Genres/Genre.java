package com.udacity.gradle.builditbigger.Genres;

import java.util.Date;

/**
 * Created by joeljohnson on 10/17/17.
 */

public class Genre {
    private String title;
    private String author;
    private Boolean restricted;
    private Date timeStamp;
    private String uid;
    private String language;
    private String genreId;

    public Genre() {
    }

    public Genre(String title, String userName, Boolean restricted,
                 String language, Date time, String uid, String genreId) {
        this.title = title;
        this.author = userName;
        this.restricted = restricted;
        this.timeStamp = time;
        this.uid = uid;
        this.language = language;
        this.genreId = genreId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getUID() {
        return uid;
    }

    public String getLanguage() {
        return language;
    }

    public String getGenreId() {
        return genreId;
    }
}
