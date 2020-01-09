package com.udacity.gradle.builditbigger.profile.userPosts;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
