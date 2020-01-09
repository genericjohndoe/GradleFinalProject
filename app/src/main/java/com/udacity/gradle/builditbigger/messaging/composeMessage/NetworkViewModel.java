package com.udacity.gradle.builditbigger.messaging.composeMessage;

import androidx.lifecycle.ViewModel;

/**
 * DEPRECATED
 */

public class NetworkViewModel extends ViewModel {

    private NetworkLiveData networkLiveData;

    public NetworkViewModel(String uid){
        networkLiveData = new NetworkLiveData(uid);
    }

    public NetworkLiveData getNetworkLiveData() {
        return networkLiveData;
    }
}
