package com.udacity.gradle.builditbigger.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by joeljohnson on 3/11/18.
 */

public class SearchViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private Application application;

    public SearchViewModelFactory(Application application){
        super(application);
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(application);
    }
}
