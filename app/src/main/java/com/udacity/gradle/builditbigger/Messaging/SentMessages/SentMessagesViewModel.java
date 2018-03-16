package com.udacity.gradle.builditbigger.Messaging.SentMessages;

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
