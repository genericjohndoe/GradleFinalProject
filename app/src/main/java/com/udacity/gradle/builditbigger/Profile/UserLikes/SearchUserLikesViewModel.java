package com.udacity.gradle.builditbigger.Profile.UserLikes;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/22/18.
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
