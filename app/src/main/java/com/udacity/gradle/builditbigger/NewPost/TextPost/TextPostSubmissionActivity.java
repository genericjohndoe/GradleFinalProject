package com.udacity.gradle.builditbigger.NewPost.TextPost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.R;

public class TextPostSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post_submission);
        String title = getIntent().getStringExtra("title");
        String body = getIntent().getStringExtra("body");
        String tagline = getIntent().getStringExtra("tagline");
        String number = getIntent().getStringExtra("number");
        Post post = getIntent().getParcelableExtra("post");
        Fragment fragment = (post == null) ? NewTextPostSubmissionFragment.newInstance(title,body,tagline,number) :
                NewTextPostSubmissionFragment.newInstance(post);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.new_text_submission_framelayout, fragment)
                .commit();
    }
}
