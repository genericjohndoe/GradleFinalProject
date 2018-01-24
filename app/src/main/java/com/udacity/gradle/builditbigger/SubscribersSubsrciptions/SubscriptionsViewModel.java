package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/23/18.
 */

public class SubscriptionsViewModel extends ViewModel {

    private SubscriptionsLiveData subscriptionsLiveData;

    public SubscriptionsViewModel(String uid){
        subscriptionsLiveData = new SubscriptionsLiveData(uid);
    }

    public SubscriptionsLiveData getSubscriptionsLiveData() {
        return subscriptionsLiveData;
    }
}
