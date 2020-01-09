package com.udacity.gradle.builditbigger.messaging.composeMessage;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * DEPRECATED
 */

public class NetworkViewModelFactory implements ViewModelProvider.Factory {

    private String uid;

    public NetworkViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NetworkViewModel(uid);
    }
}
