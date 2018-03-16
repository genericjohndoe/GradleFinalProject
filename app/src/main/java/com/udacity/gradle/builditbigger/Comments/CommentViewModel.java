package com.udacity.gradle.builditbigger.Comments;

import android.arch.lifecycle.ViewModel;

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
