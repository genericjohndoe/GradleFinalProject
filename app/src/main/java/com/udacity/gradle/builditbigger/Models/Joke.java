package com.udacity.gradle.builditbigger.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Joke class serves as model for user generated content
 */

public class Joke {
    private String jokeTitle;
    private String jokeBody;
    private Long timeStamp;
    private String genre;
    private String uid;
    private String mediaURL;
    private String pushId;
    private String tagline;
    private Integer type;
    private List<String> taglist;
    private MetaData metaData;


    public Joke() {
    }

    //TODO modify constructor to include image/video url
    public Joke(String jokeTitle, String body, Long time, String genre,
                String url, String uid, String pushId, String tagline, Integer type, MetaData metaData) {
        this.jokeTitle = jokeTitle;
        jokeBody = body;
        timeStamp = time;
        this.genre = genre;
        this.uid = uid;
        mediaURL = url;
        this.pushId = pushId;
        this.tagline = tagline;
        this.type = type;
        taglist = new ArrayList<>();
        this.metaData = metaData;
    }

    public String getJokeTitle() {
        return jokeTitle;
    }

    public void setJokeTitle(String text) {
        jokeTitle = text;
    }

    public String getJokeBody() {
        return jokeBody;
    }

    public void setJokeBody(String body) {
        jokeBody = body;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getGenre() {
        return genre;
    }

    public String getUID() {
        return uid;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public String getPushId() {
        return pushId;
    }

    public String getTagline() {
        return tagline;
    }

    public Integer getType(){ return type;}

    public List<String> getTaglist(){
        return taglist;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData){
        this.metaData = metaData;
    }

    @Override
    public boolean equals(Object object){
        return pushId.equals(((Joke) object).getPushId());
    }
}
