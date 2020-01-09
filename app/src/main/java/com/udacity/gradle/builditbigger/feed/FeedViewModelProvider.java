package com.udacity.gradle.builditbigger.feed;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Class aids in FeedViewModel creation
 */

public class FeedViewModelProvider implements ViewModelProvider.Factory {

    private String uid;

    public FeedViewModelProvider(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FeedViewModel(uid);
    }
}
