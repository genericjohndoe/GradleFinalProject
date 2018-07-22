package com.udacity.gradle.builditbigger.Explore;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

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
