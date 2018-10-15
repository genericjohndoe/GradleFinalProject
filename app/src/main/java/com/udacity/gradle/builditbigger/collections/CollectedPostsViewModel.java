package com.udacity.gradle.builditbigger.collections;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 4/7/18.
 */

public class CollectedPostsViewModel extends ViewModel {

    private CollectedPostsLiveData collectedPostsLiveData;

    public CollectedPostsViewModel(String collectionId){
        collectedPostsLiveData = new CollectedPostsLiveData(collectionId);
    }

    public CollectedPostsLiveData getCollectedPostsLiveData() {
        return collectedPostsLiveData;
    }
}
