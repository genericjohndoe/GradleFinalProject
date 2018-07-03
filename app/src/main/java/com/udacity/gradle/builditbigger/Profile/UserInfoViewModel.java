package com.udacity.gradle.builditbigger.Profile;

import android.arch.lifecycle.ViewModel;

/**
 * Class responsible for populating views in profile fragment with user data
 * data provided: user name, profile image, number of posts, number of followers
 * number of following
 */

public class UserInfoViewModel extends ViewModel {

    private UserNameLiveData userNameLiveData;
    private UserProfileImgLiveData userProfileImgLiveData;
    private NumPostLiveData numPostLiveData;
    private NumFollowingLiveData numFollowingLiveData;
    private NumFollowersLiveData numFollowersLiveData;
    private TaglineLiveData taglineLiveData;

    UserInfoViewModel(String uid){
        userNameLiveData = new UserNameLiveData(uid);
        userProfileImgLiveData = new UserProfileImgLiveData(uid);
        numPostLiveData = new NumPostLiveData(uid);
        numFollowingLiveData = new NumFollowingLiveData(uid);
        numFollowersLiveData = new NumFollowersLiveData(uid);
        taglineLiveData = new TaglineLiveData(uid);
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

    public TaglineLiveData getTaglineLiveData() {return taglineLiveData;}
}
