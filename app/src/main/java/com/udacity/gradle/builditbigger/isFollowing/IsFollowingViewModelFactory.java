package com.udacity.gradle.builditbigger.isFollowing;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

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
