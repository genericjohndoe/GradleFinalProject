package com.udacity.gradle.builditbigger.Database;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

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
