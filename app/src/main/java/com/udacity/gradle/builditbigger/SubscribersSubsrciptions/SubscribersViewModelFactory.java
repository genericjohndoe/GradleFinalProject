package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.gradle.builditbigger.Profile.UserPosts.UserPostsViewModel;

/**
 * Created by joeljohnson on 1/23/18.
 */

public class SubscribersViewModelFactory implements ViewModelProvider.Factory {

    private String uid;

    public SubscribersViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SubscribersViewModel(uid);
    }
}
