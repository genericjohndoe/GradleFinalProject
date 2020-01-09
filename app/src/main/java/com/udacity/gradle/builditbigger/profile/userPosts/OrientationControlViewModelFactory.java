package com.udacity.gradle.builditbigger.profile.userPosts;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by joeljohnson on 4/12/18.
 */

public class OrientationControlViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) OrientationControlViewModel.getInstance();
    }
}
