package com.udacity.gradle.builditbigger.forums.replies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ForumQuestionViewModelFactory implements ViewModelProvider.Factory {

    private String forumKey;

    public ForumQuestionViewModelFactory(String forumKey){
        this.forumKey = forumKey;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ForumQuestionViewModel(forumKey);
    }
}
