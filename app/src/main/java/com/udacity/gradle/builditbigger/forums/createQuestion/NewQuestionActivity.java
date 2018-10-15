package com.udacity.gradle.builditbigger.forums.createQuestion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;

public class NewQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        String content = getIntent().getStringExtra("contents");
        String key = getIntent().getStringExtra("key");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_question_framelayout, NewQuestionFragment.newInstance(content, key))
                .commit();
    }
}
