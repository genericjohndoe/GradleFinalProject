package com.udacity.gradle.builditbigger.NewPost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.gradle.builditbigger.R;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        String number = getIntent().getStringExtra("number");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_post_framelayout, NewPostFragment.newInstance(number) , "profile")
                .commit();
    }
}
