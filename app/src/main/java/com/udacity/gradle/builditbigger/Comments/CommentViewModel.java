package com.udacity.gradle.builditbigger.Comments;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 3/1/18.
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
