package com.udacity.gradle.builditbigger.MainUI;

import android.arch.lifecycle.ViewModel;

public class UnreadMessagesViewModel extends ViewModel {

    private UnreadMessagesLiveData unreadMessagesLiveData;

    public UnreadMessagesLiveData getUnreadMessagesLiveData() {
        if (unreadMessagesLiveData == null) unreadMessagesLiveData = new UnreadMessagesLiveData();
        return unreadMessagesLiveData;
    }
}
