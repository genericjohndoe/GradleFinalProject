package com.udacity.gradle.builditbigger.isFollowing;

import android.arch.lifecycle.ViewModel;

/**
 * DEPRECATED
 */

public class IsFollowingViewHolder extends ViewModel {
    private IsFollowingLiveData isFollowingLiveData;

    public IsFollowingViewHolder(String uid){
        isFollowingLiveData = new IsFollowingLiveData(uid);
    }

    public IsFollowingLiveData getIsFollowingLiveData() {
        return isFollowingLiveData;
    }
}
