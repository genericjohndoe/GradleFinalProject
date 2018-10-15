package com.udacity.gradle.builditbigger.messaging.sentMessages;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * used in creation of SentMessagesViewModel
 */

public class SentMessagesViewModelFactory implements ViewModelProvider.Factory {

    private String uid;

    public SentMessagesViewModelFactory(String uid){
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SentMessagesViewModel(uid);
    }
}
