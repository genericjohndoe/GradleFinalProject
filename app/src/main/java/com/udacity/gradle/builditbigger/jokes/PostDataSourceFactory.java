package com.udacity.gradle.builditbigger.jokes;

import android.arch.paging.DataSource;

import com.udacity.gradle.builditbigger.models.Post;

public class PostDataSourceFactory extends DataSource.Factory<String, Post> {

    String path;
    String searchTag;

    public PostDataSourceFactory(String path, String searchTag){
        this.path = path;
        this.searchTag = searchTag;
    }

    @Override
    public DataSource<String, Post> create() {
        return new PostDataSource(path, searchTag);
    }
}
