package com.udacity.gradle.builditbigger.Profile.UserCollections;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * DEPRECATED
 */

public class SearchUserCollectionViewModelFactory implements ViewModelProvider.Factory {
    private String uid;
    private String[] tags;

    public SearchUserCollectionViewModelFactory(String uid, String[] tags){
        this.uid = uid;
        this.tags = tags;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchUserCollectionViewModel(uid, tags);
    }
}
