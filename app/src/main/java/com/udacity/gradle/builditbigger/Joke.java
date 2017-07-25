package com.udacity.gradle.builditbigger;

/**
 * Created by joeljohnson on 7/17/17.
 */

public class Joke {
    String jokeTitle;
    String userName;
    String jokeBody;

    public Joke(){

    }
    public Joke(String joke, String user, String body){
        jokeTitle = joke;
        userName = user;
        jokeBody = body;
    }

    public String getJokeTitle() {
        return jokeTitle;
    }

    public void setJokeTitle(String text) {
        jokeTitle = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public String getJokeBody(){
        return jokeBody;
    }

    public void setJokeBody(String body){
        jokeBody = body;
    }
}
