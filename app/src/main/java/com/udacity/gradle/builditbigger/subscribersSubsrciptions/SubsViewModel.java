package com.udacity.gradle.builditbigger.subscribersSubsrciptions;

import androidx.lifecycle.ViewModel;

public class SubsViewModel extends ViewModel {

    private SubsLiveData subsLiveData;

    public SubsViewModel(String uid, boolean getFollowers){
        subsLiveData = new SubsLiveData(uid, getFollowers);
    }

    public SubsLiveData getSubsLiveData() {
        return subsLiveData;
    }
}
