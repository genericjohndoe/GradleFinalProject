package com.udacity.gradle.builditbigger.comments;

import androidx.lifecycle.ViewModel;

/**
 * provides comment objects to fragment
 */

public class CommentViewModel extends ViewModel {

    private CommentLiveData commentLiveData;

    public CommentViewModel(String uid, String postId){
        commentLiveData = new CommentLiveData(uid, postId);
    }

    public CommentLiveData getCommentLiveData() {
        return commentLiveData;
    }
}
