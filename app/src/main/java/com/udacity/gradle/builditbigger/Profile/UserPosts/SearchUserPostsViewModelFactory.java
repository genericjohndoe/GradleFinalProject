package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * SearchUserPostsViewModelFactory DEPRECATED
 */

public class SearchUserPostsViewModelFactory implements ViewModelProvider.Factory {
    private String uid;
    private String[] tags;

    public SearchUserPostsViewModelFactory(String uid, String[] tags){
        this.uid = uid;
        this.tags = tags;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchUserViewModel(uid, tags);
    }
}
