package com.udacity.gradle.builditbigger.Profile.UserLikes;

import android.arch.lifecycle.ViewModel;

/**
 * UserLikesViewModel class used to provide liked post data to fragment
 */

public class UserLikesViewModel extends ViewModel {

    private UserLikesLiveData userLikesLiveData;

    public UserLikesViewModel(String uid){
        userLikesLiveData = new UserLikesLiveData(uid);
    }

    public UserLikesLiveData getUserLikesLiveData() {
        return userLikesLiveData;
    }
}
