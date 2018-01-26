package com.udacity.gradle.builditbigger.isFollowing;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/23/18.
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
