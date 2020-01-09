package com.udacity.gradle.builditbigger.messaging.sentMessages;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
