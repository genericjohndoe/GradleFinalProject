package com.udacity.gradle.builditbigger.forums.replies;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
