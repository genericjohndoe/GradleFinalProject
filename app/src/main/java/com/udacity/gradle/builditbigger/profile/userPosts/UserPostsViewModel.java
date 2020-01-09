package com.udacity.gradle.builditbigger.profile.userPosts;

import androidx.lifecycle.ViewModel;

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
