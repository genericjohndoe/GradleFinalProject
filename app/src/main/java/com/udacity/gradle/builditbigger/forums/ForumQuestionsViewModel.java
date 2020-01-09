package com.udacity.gradle.builditbigger.forums;

import androidx.lifecycle.ViewModel;

import com.udacity.gradle.builditbigger.forums.questions.ForumQuestionsLiveData;
import com.udacity.gradle.builditbigger.forums.replies.ForumQuestionLiveData;
import com.udacity.gradle.builditbigger.forums.replies.ForumQuestionReplyLiveData;
import com.udacity.gradle.builditbigger.profile.UserNameLiveData;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumQuestionsViewModel extends ViewModel {

    private ForumQuestionsLiveData forumQuestionsLiveData = new ForumQuestionsLiveData();
    private ForumQuestionReplyLiveData forumQuestionReplyLiveData;
    private ForumQuestionLiveData forumQuestionLiveData;
    private UserNameLiveData userNameLiveData;

    public ForumQuestionsLiveData getForumQuestionsLiveData() {
        return forumQuestionsLiveData;
    }

    public ForumQuestionReplyLiveData getForumQuestionReplyLiveData(String key) {
        forumQuestionReplyLiveData = new ForumQuestionReplyLiveData(key);
        return forumQuestionReplyLiveData;
    }

    public ForumQuestionLiveData getForumQuestionLiveData(String key) {
        forumQuestionLiveData = new ForumQuestionLiveData(key);
        return forumQuestionLiveData;
    }

    public UserNameLiveData getUserNameLiveData(String uid) {
        userNameLiveData = new UserNameLiveData(uid);
        return userNameLiveData;
    }
}
