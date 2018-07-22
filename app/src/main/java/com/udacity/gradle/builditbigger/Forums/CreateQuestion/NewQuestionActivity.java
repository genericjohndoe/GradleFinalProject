package com.udacity.gradle.builditbigger.Forums.CreateQuestion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.R;

public class NewQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_question_framelayout, NewQuestionFragment.newInstance())
                .commit();
    }
}
