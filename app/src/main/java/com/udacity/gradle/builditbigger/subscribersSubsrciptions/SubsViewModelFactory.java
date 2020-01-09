package com.udacity.gradle.builditbigger.subscribersSubsrciptions;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
