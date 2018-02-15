package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by joeljohnson on 2/15/18.
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
