package com.udacity.gradle.builditbigger.newPost.audioMediaPost;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.R;



public class AudioMediaPostSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_media_post_submission);
        String number = getIntent().getStringExtra("number");
        String path = getIntent().getStringExtra("path");
        Post post = getIntent().getParcelableExtra("post");
        Fragment fragment = (post == null) ? AudioMediaPostSubmissionFragment.newInstance(number, path) :
            AudioMediaPostSubmissionFragment.newInstance(post);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.audio_post_submission_framelayout,
                        fragment)
                .commit();
    }
}
