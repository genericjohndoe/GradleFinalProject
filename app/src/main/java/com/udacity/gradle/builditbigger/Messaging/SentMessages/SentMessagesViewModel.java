package com.udacity.gradle.builditbigger.Messaging.SentMessages;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/31/18.
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
