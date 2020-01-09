package com.udacity.gradle.builditbigger.newPost.audioMediaPost;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.models.Post;



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
