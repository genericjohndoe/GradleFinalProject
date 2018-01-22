package com.udacity.gradle.builditbigger.Profile.UserGenres;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.gradle.builditbigger.Profile.UserPosts.UserPostsViewModel;

/**
 * Created by joeljohnson on 1/21/18.
 */

public class UserGenreViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    public UserGenreViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserGenreViewModel(uid);
    }
}
