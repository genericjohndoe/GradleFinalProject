package com.udacity.gradle.builditbigger.profile.userLikes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * UserLikesViewModelFactory class used to provide ViewModel object to fragment
 */

public class UserLikesViewModelFactory implements ViewModelProvider.Factory {

    private String uid;

    public UserLikesViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserLikesViewModel(uid);
    }
}
