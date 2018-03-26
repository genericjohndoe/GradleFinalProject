package com.udacity.gradle.builditbigger.Profile.UserGenres;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * UserGenreViewModelFactory class provides ViewModel object to fragment
 */

public class UserGenreViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    public UserGenreViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserGenreViewModel(uid);
    }
}
