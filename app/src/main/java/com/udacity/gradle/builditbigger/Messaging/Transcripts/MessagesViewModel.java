package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 2/15/18.
 */

public class MessagesViewModel extends ViewModel {

    private MessagesLiveData messagesLiveData;

    public MessagesViewModel(String uid, String path){
        messagesLiveData = new MessagesLiveData(uid, path);
    }

    public MessagesLiveData getMessagesLiveData() {
        return messagesLiveData;
    }
}
