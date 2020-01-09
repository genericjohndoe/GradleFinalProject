package com.udacity.gradle.builditbigger.profile.userCollections;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
