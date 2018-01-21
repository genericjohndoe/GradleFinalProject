package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/21/18.
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
