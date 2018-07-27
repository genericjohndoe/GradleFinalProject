package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class SubsViewModelFactory implements ViewModelProvider.Factory {

    private String uid;
    private boolean getFollowers;

    public SubsViewModelFactory(String uid, boolean getFollowers){
        this.uid = uid;
        this.getFollowers = getFollowers;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SubsViewModel(uid, getFollowers);
    }
}
