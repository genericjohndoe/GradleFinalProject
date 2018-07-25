package com.udacity.gradle.builditbigger.Jokes;

import android.arch.paging.DataSource;

import com.udacity.gradle.builditbigger.Models.Post;

public class PostDataSourceFactory extends DataSource.Factory<String, Post> {

    String path;

    public PostDataSourceFactory(String path){
        this.path = path;
    }

    @Override
    public DataSource<String, Post> create() {
        return new PostDataSource(path);
    }
}
