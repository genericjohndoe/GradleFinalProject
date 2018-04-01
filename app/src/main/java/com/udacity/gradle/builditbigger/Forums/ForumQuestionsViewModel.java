package com.udacity.gradle.builditbigger.Forums;

import android.arch.lifecycle.ViewModel;

import com.udacity.gradle.builditbigger.Forums.Questions.ForumQuestionsLiveData;
import com.udacity.gradle.builditbigger.Forums.Replies.ForumQuestionReplyLiveData;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumQuestionsViewModel extends ViewModel {

    private ForumQuestionsLiveData forumQuestionsLiveData = new ForumQuestionsLiveData();
    private ForumQuestionReplyLiveData forumQuestionReplyLiveData;

    public ForumQuestionsLiveData getForumQuestionsLiveData() {
        return forumQuestionsLiveData;
    }

    public ForumQuestionReplyLiveData getForumQuestionReplyLiveData(String key) {
        forumQuestionReplyLiveData = new ForumQuestionReplyLiveData(key);
        return forumQuestionReplyLiveData;
    }
}