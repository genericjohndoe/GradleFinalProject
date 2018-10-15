package com.udacity.gradle.builditbigger.models;

public class PostWrapper {

    private Post post;
    private int state;

    public PostWrapper(Post post, int state){
        this.post = post;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public Post getPost() {
        return post;
    }
}
