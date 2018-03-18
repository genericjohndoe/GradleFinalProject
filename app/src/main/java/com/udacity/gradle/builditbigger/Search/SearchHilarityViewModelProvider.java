package com.udacity.gradle.builditbigger.Search;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by joeljohnson on 3/17/18.
 */

public class SearchHilarityViewModelProvider implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) SearchHilarityViewModel.getInstance();
    }
}
