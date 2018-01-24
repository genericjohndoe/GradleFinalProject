package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/23/18.
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
