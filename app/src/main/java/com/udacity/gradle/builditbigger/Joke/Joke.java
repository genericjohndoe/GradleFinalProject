package com.udacity.gradle.builditbigger.Joke;

import com.udacity.gradle.builditbigger.Constants.Constants;

/**
 * Created by joeljohnson on 7/17/17.
 */

public class Joke {
    private String jokeTitle;
    private String author;
    private String jokeBody;
    private Object timeStamp;
    private String genre;
    private String uid;


    public Joke(){}

    //TODO modify constructor to include image/video url
    public Joke(String joke, String user, String body, Object time, String genre){
        jokeTitle = joke;
        author = user;
        jokeBody = body;
        timeStamp = time;
        this.genre = genre;
        uid = Constants.UID;
    }

    public String getJokeTitle() {
        return jokeTitle;
    }

    public void setJokeTitle(String text) {
        jokeTitle = text;
    }

    public String getAuthor() {
        return author;
    }

    public String getJokeBody(){
        return jokeBody;
    }

    public void setJokeBody(String body){jokeBody = body;}

    public Object getTimeStamp(){return timeStamp;}

    public String getGenre(){return genre;}

    public String getUID(){return uid;}




}
