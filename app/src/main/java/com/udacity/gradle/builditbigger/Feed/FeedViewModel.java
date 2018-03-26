package com.udacity.gradle.builditbigger.Feed;

import android.arch.lifecycle.ViewModel;

/**
 * class provides fragment with posts for feed
 */

public class FeedViewModel extends ViewModel {

    private FeedLiveData feedLiveData;

    public FeedViewModel(String uid){
        feedLiveData = new FeedLiveData(uid);
    }

    public FeedLiveData getFeedLiveData() {
        return feedLiveData;
    }
}