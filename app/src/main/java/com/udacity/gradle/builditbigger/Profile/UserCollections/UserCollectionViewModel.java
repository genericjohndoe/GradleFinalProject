package com.udacity.gradle.builditbigger.Profile.UserCollections;

import android.arch.lifecycle.ViewModel;

/**
 * UserGenreViewModel class provides fragment with user generated genres
 */

public class UserCollectionViewModel extends ViewModel {
    UserCollectionLiveData userCollectionLiveData;

    public UserCollectionViewModel(String uid){
        userCollectionLiveData = new UserCollectionLiveData(uid);
    }

    public UserCollectionLiveData getUserCollectionLiveData() {
        return userCollectionLiveData;
    }
}
