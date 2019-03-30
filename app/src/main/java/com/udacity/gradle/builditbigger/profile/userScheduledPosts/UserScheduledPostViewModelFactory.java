package com.udacity.gradle.builditbigger.profile.userScheduledPosts;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.gradle.builditbigger.profile.userPosts.UserPostsViewModel;

public class UserScheduledPostViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    public UserScheduledPostViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserScheduledPostsViewModel(uid);
    }
}
