package com.udacity.gradle.builditbigger.profile.userPosts;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

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
