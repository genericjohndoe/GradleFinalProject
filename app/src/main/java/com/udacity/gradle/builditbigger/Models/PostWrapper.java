package com.udacity.gradle.builditbigger.Models;

public class PostWrapper {

    Post post;
    int state;

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
