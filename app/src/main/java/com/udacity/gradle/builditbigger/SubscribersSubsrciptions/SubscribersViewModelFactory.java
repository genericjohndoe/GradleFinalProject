package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


/**
 * SubscribersViewModelFactory aids in creation of SubscribersViewModel
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
