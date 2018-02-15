package com.udacity.gradle.builditbigger.Messaging.SentMessages;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by joeljohnson on 1/31/18.
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
