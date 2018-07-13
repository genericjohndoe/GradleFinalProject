package com.udacity.gradle.builditbigger.Explore;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * aids in creation of ExploreViewModel
 */

public class ExploreViewModelFactory implements ViewModelProvider.Factory {

    private String uid;
    private Context context;

    public ExploreViewModelFactory(String uid, Context context){
        this.context = context;
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ExploreViewModel(context);
    }
}
