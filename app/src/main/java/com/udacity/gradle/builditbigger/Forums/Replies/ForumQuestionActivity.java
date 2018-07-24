package com.udacity.gradle.builditbigger.Forums.Replies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.udacity.gradle.builditbigger.Models.ForumQuestion;
import com.udacity.gradle.builditbigger.R;

public class ForumQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_question);
        String forumKey = getIntent().getStringExtra("key");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.forum_question_framelayout, ForumQuestionFragment.newInstance(forumKey))
                .commit();
    }
}
