package com.udacity.gradle.builditbigger.profile.userScheduledPosts;

import android.arch.lifecycle.ViewModel;

import com.udacity.gradle.builditbigger.profile.userPosts.SearchUserPostsLiveData;
import com.udacity.gradle.builditbigger.profile.userPosts.UserPostsLiveData;

public class UserScheduledPostsViewModel extends ViewModel {

    private UserScheduledPostsLiveData userScheduledPostsLiveData;
    private SearchUserScheduledPostsLiveData searchUserScheduledPostsLiveData;
    private String uid;

    public UserScheduledPostsViewModel(String uid){
        userScheduledPostsLiveData = new UserScheduledPostsLiveData(uid);
        this.uid = uid;
    }

    public UserScheduledPostsLiveData getUserScheduledPostsLiveData() {
        return userScheduledPostsLiveData;
    }

    public SearchUserScheduledPostsLiveData getSearchUserScheduledPostsLiveData(String tag) {
        searchUserScheduledPostsLiveData = new SearchUserScheduledPostsLiveData(tag);
        return searchUserScheduledPostsLiveData;
    }
}
