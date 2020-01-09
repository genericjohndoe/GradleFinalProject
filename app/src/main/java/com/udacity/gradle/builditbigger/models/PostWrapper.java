package com.udacity.gradle.builditbigger.models;

public class PostWrapper {

    private Post post;
    private int state;
    public static final int NEW = 1;
    public static final int EDITTED = 2;
    public static final int REMOVED = 3;

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
