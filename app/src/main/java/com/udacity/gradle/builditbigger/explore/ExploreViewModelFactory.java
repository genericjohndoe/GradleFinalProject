package com.udacity.gradle.builditbigger.explore;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * aids in creation of ExploreViewModel
 */

public class ExploreViewModelFactory implements ViewModelProvider.Factory {

    private String uid;

    public ExploreViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ExploreViewModel();
    }
}
