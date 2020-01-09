package com.udacity.gradle.builditbigger.forums.replies;

import androidx.lifecycle.ViewModel;

public class ForumQuestionViewModel extends ViewModel {
    private ForumQuestionLiveData forumQuestionLiveData;

    public ForumQuestionViewModel(String forumKey){
        forumQuestionLiveData = new ForumQuestionLiveData(forumKey);
    }

    public ForumQuestionLiveData getForumQuestionLiveData() {
        return forumQuestionLiveData;
    }
}
