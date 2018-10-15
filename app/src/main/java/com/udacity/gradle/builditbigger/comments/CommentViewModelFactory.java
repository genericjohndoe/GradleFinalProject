package com.udacity.gradle.builditbigger.comments;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * aids in commentviewmodel creation
 */

public class CommentViewModelFactory implements ViewModelProvider.Factory {

    private String uid;
    private String postId;

    public CommentViewModelFactory(String uid, String postId){
        this.uid = uid;
        this.postId = postId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CommentViewModel(uid,postId);
    }
}
