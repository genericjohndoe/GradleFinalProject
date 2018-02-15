package com.udacity.gradle.builditbigger.Messaging.ComposeMessage;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 2/5/18.
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
