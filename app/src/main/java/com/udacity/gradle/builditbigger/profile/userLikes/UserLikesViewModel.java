package com.udacity.gradle.builditbigger.profile.userLikes;

import android.arch.lifecycle.ViewModel;

/**
 * UserLikesViewModel class used to provide liked post data to fragment
 */

public class UserLikesViewModel extends ViewModel {

    private UserLikesLiveData userLikesLiveData;
    private SearchUserLikesLiveData searchUserLikesLiveData;
    private String uid;

    public UserLikesViewModel(String uid){
        userLikesLiveData = new UserLikesLiveData(uid);
        this.uid = uid;
    }

    public UserLikesLiveData getUserLikesLiveData() {
        return userLikesLiveData;
    }

    public SearchUserLikesLiveData getSearchUserLikesLiveData(String keyword) {
        searchUserLikesLiveData = new SearchUserLikesLiveData(uid, keyword);
        return searchUserLikesLiveData;
    }
}
