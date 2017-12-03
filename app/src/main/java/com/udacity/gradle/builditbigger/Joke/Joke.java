package com.udacity.gradle.builditbigger.Joke;

/**
 * Created by joeljohnson on 7/17/17.
 */

public class Joke {
    private String jokeTitle;
    private String user;
    private String jokeBody;
    private String timeStamp;
    private String genre;
    private String uid;
    private String mediaURL;
    private String pushId;
    private String tagline;


    public Joke(){}

    //TODO modify constructor to include image/video url
    public Joke(String jokeTitle, String user, String body, String time, String genre,
                String url, String uid, String pushId, String tagline){
        this.jokeTitle = jokeTitle;
        this.user = user;
        jokeBody = body;
        timeStamp = time;
        this.genre = genre;
        this.uid = uid;
        mediaURL = url;
        this.pushId = pushId;
        this.tagline = tagline;
    }

    public String getJokeTitle() {
        return jokeTitle;
    }

    public void setJokeTitle(String text) {
        jokeTitle = text;
    }

    public String getUser() {
        return user;
    }

    public String getJokeBody(){
        return jokeBody;
    }

    public void setJokeBody(String body){jokeBody = body;}

    public String getTimeStamp(){return timeStamp;}

    public String getGenre(){return genre;}

    public String getUID(){return uid;}

    public String getMediaURL(){return mediaURL;}

    public String getPushId(){return pushId;}

    public String getTagline(){return tagline;}

}
