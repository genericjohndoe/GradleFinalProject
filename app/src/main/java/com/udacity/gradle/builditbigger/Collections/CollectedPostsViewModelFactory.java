package com.udacity.gradle.builditbigger.Collections;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by joeljohnson on 4/7/18.
 */

public class CollectedPostsViewModelFactory implements ViewModelProvider.Factory {
    private String collectionId;

    public CollectedPostsViewModelFactory(String collectionId){
        this.collectionId = collectionId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CollectedPostsViewModel(collectionId);
    }
}
