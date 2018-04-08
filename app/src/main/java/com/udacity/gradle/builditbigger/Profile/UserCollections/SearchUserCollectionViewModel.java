package com.udacity.gradle.builditbigger.Profile.UserCollections;

import android.arch.lifecycle.ViewModel;

/**
 * DEPRECATED
 */

public class SearchUserCollectionViewModel extends ViewModel {
    private SearchUserCollectionLiveData searchUserCollectionLiveData;
    public SearchUserCollectionViewModel(String uid, String[] tags){
        searchUserCollectionLiveData = new SearchUserCollectionLiveData(uid, tags);
    }

    public SearchUserCollectionLiveData getSearchUserCollectionLiveData() {
        return searchUserCollectionLiveData;
    }
}
