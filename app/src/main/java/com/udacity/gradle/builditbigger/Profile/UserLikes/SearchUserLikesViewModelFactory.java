package com.udacity.gradle.builditbigger.Profile.UserLikes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.gradle.builditbigger.Profile.UserPosts.SearchUserViewModel;

/**
 * Created by joeljohnson on 1/22/18.
 */

public class SearchUserLikesViewModelFactory implements ViewModelProvider.Factory {
    private String uid;
    private String[] tags;

    public SearchUserLikesViewModelFactory(String uid, String[] tags){
        this.uid = uid;
        this.tags = tags;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchUserLikesViewModel(uid, tags);
    }
}
