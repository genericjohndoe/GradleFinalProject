package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.ViewModel;

/**
 * UserPostsViewModel class used to pass user generated posts to fragment
 */

public class UserPostsViewModel extends ViewModel {
    UserPostsLiveData userPostsLiveData;

    public UserPostsViewModel(String uid){
        userPostsLiveData = new UserPostsLiveData(uid);
    }

    public UserPostsLiveData getUserPostsLiveData() {
        return userPostsLiveData;
    }
}
