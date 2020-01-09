package com.udacity.gradle.builditbigger.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
