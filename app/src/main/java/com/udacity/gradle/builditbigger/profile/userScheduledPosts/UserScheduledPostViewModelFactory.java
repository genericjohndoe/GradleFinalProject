package com.udacity.gradle.builditbigger.profile.userScheduledPosts;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
