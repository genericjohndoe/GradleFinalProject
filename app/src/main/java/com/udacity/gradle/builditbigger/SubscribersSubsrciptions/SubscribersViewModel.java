package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.ViewModel;

/**
 * provides list of subscribers to fragment
 */

public class SubscribersViewModel extends ViewModel {

    private SubscribersLiveData subscribersLiveData;

    public SubscribersViewModel(String uid){
        subscribersLiveData = new SubscribersLiveData(uid);
    }

    public SubscribersLiveData getSubscribersLiveData() {
        return subscribersLiveData;
    }
}
