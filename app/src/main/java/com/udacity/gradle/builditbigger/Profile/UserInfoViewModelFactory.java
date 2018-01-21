package com.udacity.gradle.builditbigger.Profile;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.gradle.builditbigger.Profile.UserInfoViewModel;

/**
 * Created by joeljohnson on 1/20/18.
 */

public class UserInfoViewModelFactory implements ViewModelProvider.Factory {

    private String uid;

    public UserInfoViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserInfoViewModel(uid);
    }
}
