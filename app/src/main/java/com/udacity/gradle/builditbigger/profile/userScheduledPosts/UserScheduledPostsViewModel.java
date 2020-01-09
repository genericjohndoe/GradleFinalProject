package com.udacity.gradle.builditbigger.profile.userScheduledPosts;

import androidx.lifecycle.ViewModel;

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
