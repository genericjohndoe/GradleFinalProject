package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.udacity.gradle.builditbigger.Jokes.PostDataSourceFactory;
import com.udacity.gradle.builditbigger.Models.Post;

/**
 * UserPostsViewModel class used to pass user generated posts to fragment
 */

public class UserPostsViewModel extends ViewModel {
    private UserPostsLiveData userPostsLiveData;
    private SearchUserPostsLiveData searchUserPostsLiveData;
    private String uid;

    public UserPostsViewModel(String uid){
        userPostsLiveData = new UserPostsLiveData(uid);
        this.uid = uid;
    }

    public UserPostsLiveData getUserPostsLiveData() {
        return userPostsLiveData;
    }

    public SearchUserPostsLiveData getSearchUserPostsLiveData(String tag) {
        searchUserPostsLiveData = new SearchUserPostsLiveData(uid,tag);
        return searchUserPostsLiveData;
    }

    /*public LiveData<PagedList<Post>> getPosts() {
        return posts;
    }

    public LiveData<PagedList<Post>> getSearchPosts(String tag) {
        searchPosts = new LivePagedListBuilder<>(
                new PostDataSourceFactory("userposts/" + uid + "/posts",tag),
                config).build();
        return searchPosts;
    }*/
}
