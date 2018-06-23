package com.udacity.gradle.builditbigger.NewPost.AudioMediaPost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.gradle.builditbigger.NewPost.TextPost.NewTextPostSubmissionFragment;
import com.udacity.gradle.builditbigger.R;

import java.io.File;

public class AudioMediaPostSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_media_post_submission);
        String number = getIntent().getStringExtra("number");
        String path = getIntent().getStringExtra("path");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.audio_post_submission_framelayout,
                        AudioMediaPostSubmissionFragment.newInstance(number, path))
                .commit();
    }
}
