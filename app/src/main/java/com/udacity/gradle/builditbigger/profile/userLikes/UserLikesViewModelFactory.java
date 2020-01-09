package com.udacity.gradle.builditbigger.profile.userLikes;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
