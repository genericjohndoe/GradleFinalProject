package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * UserPostViewModelFactory class used to provide ViewModel to fragment
 */

public class UserPostViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    public UserPostViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserPostsViewModel(uid);
    }
}
