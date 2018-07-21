package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.ViewModel;

/**
 * UserPostsViewModel class used to pass user generated posts to fragment
 */

public class UserPostsViewModel extends ViewModel {
    UserPostsLiveData userPostsLiveData;
    SearchUserPostsLiveData searchUserPostsLiveData;
    String uid;

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
}
