package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/20/18.
 */

public class UserInfoViewModel extends ViewModel {

    private UserNameLiveData userNameLiveData;
    private UserProfileImgLiveData userProfileImgLiveData;
    private NumPostLiveData numPostLiveData;
    private NumFollowingLiveData numFollowingLiveData;
    private NumFollowersLiveData numFollowersLiveData;

    UserInfoViewModel(String uid){
        userNameLiveData = new UserNameLiveData(uid);
        userProfileImgLiveData = new UserProfileImgLiveData(uid);
        numPostLiveData = new NumPostLiveData(uid);
        numFollowingLiveData = new NumFollowingLiveData(uid);
        numFollowersLiveData = new NumFollowersLiveData(uid);
    }

    public UserNameLiveData getUserName(){
        return userNameLiveData;
    }

    public UserProfileImgLiveData getUserProfileImg(){ return userProfileImgLiveData;}

    public NumPostLiveData getNumPostLiveData() {
        return numPostLiveData;
    }

    public NumFollowingLiveData getNumFollowingLiveData() {
        return numFollowingLiveData;
    }

    public NumFollowersLiveData getNumFollowersLiveData() {
        return numFollowersLiveData;
    }
}
