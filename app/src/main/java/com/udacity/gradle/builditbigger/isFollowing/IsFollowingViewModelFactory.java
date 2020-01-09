package com.udacity.gradle.builditbigger.isFollowing;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * DEPRECATED
 */

public class IsFollowingViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    public IsFollowingViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new IsFollowingViewHolder(uid);
    }
}
