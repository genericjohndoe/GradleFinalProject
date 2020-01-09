package com.udacity.gradle.builditbigger.messaging.transcripts;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * MessagesViewModelFactory class aids in creation of MessagesViewModel
 */

public class MessagesViewModelFactory implements ViewModelProvider.Factory {
    private String uid;
    private String path;

    public MessagesViewModelFactory(String uid, String path){
        this.uid = uid;
        this.path = path;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MessagesViewModel(uid, path);
    }
}
