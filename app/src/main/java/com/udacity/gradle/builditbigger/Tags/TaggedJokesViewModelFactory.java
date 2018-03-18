package com.udacity.gradle.builditbigger.Tags;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by joeljohnson on 3/18/18.
 */

public class TaggedJokesViewModelFactory implements ViewModelProvider.Factory {
    private String tag;

    public TaggedJokesViewModelFactory(String tag){
        this.tag = tag;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TaggedJokesViewModel(tag);
    }
}
