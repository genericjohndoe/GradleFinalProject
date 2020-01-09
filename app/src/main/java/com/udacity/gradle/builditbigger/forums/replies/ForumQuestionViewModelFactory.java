package com.udacity.gradle.builditbigger.forums.replies;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
