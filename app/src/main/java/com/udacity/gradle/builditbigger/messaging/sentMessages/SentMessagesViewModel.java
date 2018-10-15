package com.udacity.gradle.builditbigger.messaging.sentMessages;

import android.arch.lifecycle.ViewModel;

/**
 * SentMessagesViewModel class provides transcript previews to fragments
 */

public class SentMessagesViewModel extends ViewModel {

    private SentMessagesLiveData sentMessagesLiveData;

    public SentMessagesViewModel(String uid){
        sentMessagesLiveData = new SentMessagesLiveData(uid);
    }

    public SentMessagesLiveData getSentMessagesLiveData() {
        return sentMessagesLiveData;
    }
}
