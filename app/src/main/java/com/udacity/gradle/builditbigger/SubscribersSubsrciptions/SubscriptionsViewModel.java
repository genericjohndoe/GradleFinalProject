package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.ViewModel;

/**
 * Class provides list of user subscriptions to fragment
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
