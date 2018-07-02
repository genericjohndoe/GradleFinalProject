package com.udacity.gradle.builditbigger.NewPost.VisualMediaPost;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.R;

public class VisualMediaPostSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_media_post_submission);
        String file = getIntent().getStringExtra("filepath");
        String number = getIntent().getStringExtra("number");
        Post post = getIntent().getParcelableExtra("post");
        Fragment fragment = (post == null) ? VisualMediaPostSubmissionFragment.newInstance(file,number) :
                VisualMediaPostSubmissionFragment.newInstance(post);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.visual_media_post_submission_framelayout,
                        fragment)
                .commit();
    }
}
