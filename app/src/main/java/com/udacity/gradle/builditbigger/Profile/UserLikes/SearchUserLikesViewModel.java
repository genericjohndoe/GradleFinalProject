package com.udacity.gradle.builditbigger.Profile.UserLikes;

import android.arch.lifecycle.ViewModel;

/**
 * SearchUserLikesViewModel class DEPRECATED
 */

public class SearchUserLikesViewModel extends ViewModel {

    private SearchUserLikesLiveData searchUserLikesLiveData;

    public SearchUserLikesViewModel(String uid, String[] tags){
        searchUserLikesLiveData = new SearchUserLikesLiveData(uid, tags);
    }

    public SearchUserLikesLiveData getSearchUserLikesLiveData() {
        return searchUserLikesLiveData;
    }
}
