package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.ViewModel;

import java.util.List;

/**
 * Created by joeljohnson on 1/21/18.
 */

public class SearchUserViewModel extends ViewModel {


    SearchUserPostsLiveData searchUserPostsLiveData;

    public SearchUserViewModel(String uid, String[] tags) {
        searchUserPostsLiveData = new SearchUserPostsLiveData(uid, tags);
    }

    public SearchUserPostsLiveData getSearchUserPostsLiveData() {
        return searchUserPostsLiveData;
    }
}
