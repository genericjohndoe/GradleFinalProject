package com.udacity.gradle.builditbigger.Profile.UserLikes;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/22/18.
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
