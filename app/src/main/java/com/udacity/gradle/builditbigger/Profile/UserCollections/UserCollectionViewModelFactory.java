package com.udacity.gradle.builditbigger.Profile.UserCollections;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * UserGenreViewModelFactory class provides ViewModel object to fragment
 */

public class UserCollectionViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    public UserCollectionViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserCollectionViewModel(uid);
    }
}
