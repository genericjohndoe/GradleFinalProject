package com.udacity.gradle.builditbigger.Forums.Replies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.gradle.builditbigger.Models.ForumQuestion;
import com.udacity.gradle.builditbigger.R;

public class ForumQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_question);
        ForumQuestion forumQuestion = getIntent().getParcelableExtra("question");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.forum_question_framelayout, ForumQuestionFragment.newInstance(forumQuestion))
                .commit();
    }
}
