package com.udacity.gradle.builditbigger.profile.userCollections;

import android.arch.lifecycle.ViewModel;

/**
 * UserGenreViewModel class provides fragment with user generated genres
 */

public class UserCollectionViewModel extends ViewModel {
    private UserCollectionLiveData userCollectionLiveData;
    private SearchUserCollectionLiveData searchUserCollectionLiveData;
    private String uid;

    public UserCollectionViewModel(String uid){
        userCollectionLiveData = new UserCollectionLiveData(uid);
        this.uid = uid;
    }

    public UserCollectionLiveData getUserCollectionLiveData() {
        return userCollectionLiveData;
    }

    public SearchUserCollectionLiveData getSearchUserCollectionLiveData(String keyword) {
        searchUserCollectionLiveData = new SearchUserCollectionLiveData(uid,keyword);
        return searchUserCollectionLiveData;
    }
}
