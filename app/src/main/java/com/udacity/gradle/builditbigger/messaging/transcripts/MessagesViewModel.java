package com.udacity.gradle.builditbigger.messaging.transcripts;

import androidx.lifecycle.ViewModel;

/**
 * MessagesViewModel class used to provide fragment with message objects
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
